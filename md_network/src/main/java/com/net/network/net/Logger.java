package com.net.network.net;

import android.text.TextUtils;
import android.util.Log;
import com.common.framework.utils.LogUtil;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import okhttp3.Request;
import okio.Buffer;

class Logger {
    private static final int JSON_INDENT = 3;
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final String DOUBLE_SEPARATOR;
    private static final int MAX_LONG_SIZE = 110;
    private static final String N = "\n";
    private static final String T = "\t";

    protected Logger() {
        throw new UnsupportedOperationException();
    }

    private static boolean isEmpty(String line) {
        return TextUtils.isEmpty(line) || "\n".equals(line) || "\t".equals(line) || TextUtils.isEmpty(line.trim());
    }

    public static void printJsonRequest(Request request) {
        String url = request.url().toString();
        String requestHeaders = request.headers().toString();
        String requestBodyValue = bodyToString(request);
        String requestBody = " requestBody:" + requestBodyValue;
        StringBuffer buffer = new StringBuffer();
//        buffer.append("                                \n");
//        buffer.append("╔══════ Request Log ═══════════════════════════════════════════════════════════════════════");
//        buffer.append("\n║ ");
        buffer.append("\n method: ").append(request.method());
        buffer.append("     URL: ").append(url);
        buffer.append("\n requestHeaders: ").append(isEmpty(requestHeaders) ? "" : dotHeaders(requestHeaders));
        buffer.append("\n ");
        buffer.append("\n ").append(requestBody);

//        buffer.append("\n║ ");
//        buffer.append("\n╚═══════════════════════════════════════════════════════════════════════════════════════");
        LogUtil.i(buffer.toString());
    }

    public  static  void printJsonResponse(HttpLoggingInterceptor2.Builder builder, Request request, long chainMs, boolean isSuccessful, int code, String headers, String bodyString, List<String> segments) {
        String url = request.url().toString();
        String requestHeaders = request.headers().toString();
        String requestBodyValue = bodyToString(request);
        (new StringBuilder()).append(" requestBody:").append(requestBodyValue).toString();
        String responseBody = " responseBoby:" + bodyString;
        if (url.contains(".jpg") || url.contains(".png")) {
            responseBody = "responseBoby: is map";
        }

        String tag = builder.getTag(false);
        StringBuffer buffer = new StringBuffer();
        buffer.append("\n URL: ").append(request.url());
        buffer.append("\n method: ").append(request.method());
        buffer.append("     status code:").append(code);
        buffer.append("     is success:").append(isSuccessful);
        buffer.append("\n ");
        buffer.append("\n responseHeaders: ").append(isEmpty(headers) ? "" : dotHeaders(headers));
        buffer.append("\n\n ").append(responseBody);
        LogUtil.i(buffer.toString());
    }

    private static String[] getRequest(Request request) {
        String header = request.headers().toString();
        String message = isEmpty(header) ? "" : LINE_SEPARATOR + "Headers:" + LINE_SEPARATOR + dotHeaders(header);
        return message.split(LINE_SEPARATOR);
    }

    private static String[] getResponse(String header, long tookMs, int code, boolean isSuccessful, List<String> segments) {
        String message = LINE_SEPARATOR + "is success : " + isSuccessful + " - Received in: " + tookMs + "ms - Status Code: " + code + (isEmpty(header) ? "" : "\nresponseHeaders:" + LINE_SEPARATOR + dotHeaders(header));
        return message.split(LINE_SEPARATOR);
    }

    private static String slashSegments(List<String> segments) {
        StringBuilder segmentString = new StringBuilder();
        Iterator var2 = segments.iterator();

        while(var2.hasNext()) {
            String segment = (String)var2.next();
            segmentString.append("/").append(segment);
        }

        return segmentString.toString();
    }

    private static String dotHeaders(String header) {
        String[] headers = header.split(LINE_SEPARATOR);
        StringBuilder builder = new StringBuilder();
        String[] var3 = headers;
        int var4 = headers.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String item = var3[var5];
            builder.append("\n").append("- ").append(item);
        }

        return builder.toString();
    }

    private static void logLines(int type, String tag, String[] lines) {
        String[] var3 = lines;
        int var4 = lines.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String line = var3[var5];
            int lineLength = line.length();

            for(int i = 0; i <= lineLength / 110; ++i) {
                int start = i * 110;
                int end = (i + 1) * 110;
                end = end > line.length() ? line.length() : end;
                Log.w("http", "║ " + line.substring(start, end));
            }
        }

    }

    private static String bodyToString(Request request) {
        try {
            Request copy = request.newBuilder().build();
            Buffer buffer = new Buffer();
            if (copy.body() == null) {
                return "";
            } else {
                copy.body().writeTo(buffer);
                return buffer.readUtf8();
            }
        } catch (IOException var3) {
            return "{\"err\": \"" + var3.getMessage() + "\"}";
        }
    }

    static {
        DOUBLE_SEPARATOR = LINE_SEPARATOR + LINE_SEPARATOR;
    }
}