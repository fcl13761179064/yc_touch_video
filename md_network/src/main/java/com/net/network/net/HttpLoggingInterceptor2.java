package com.net.network.net;

import static com.net.network.net.Logger.printJsonRequest;
import static com.net.network.net.Logger.printJsonResponse;

import android.text.TextUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public final class HttpLoggingInterceptor2 implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private final boolean isDebug;
    private Builder builder;

    private HttpLoggingInterceptor2(Builder builder) {
        this.builder = builder;
        this.isDebug = builder.isDebug;
    }

    public Response intercept(Chain chain) throws IOException {
        if (!isDebug) {
            return chain.proceed(chain.request());
        }
        Request request = chain.request();
        long st = System.nanoTime();
        long chainMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - st);
        Response response = chain.proceed(request);
        printJsonRequest(response.networkResponse().request());
        String header = response.headers().toString();
        int code = response.code();
        boolean isSuccessful = response.isSuccessful();
        byte[] byteBody = new byte[0];
        if (response.body() != null) {
            byteBody = response.body().bytes();
        }
        printJsonResponse(this.builder, request, chainMs, isSuccessful, code, header, new String(byteBody), /*segmentList*/null);
        Request cloneRequest = chain.request();
        MediaType contentType = null;
        if (cloneRequest.body() != null) {
            contentType = cloneRequest.body().contentType();
        }

        ResponseBody body = ResponseBody.create(contentType, byteBody);
        return response.newBuilder().body(body).build();
    }


    public static class Builder {
        private static String TAG = "LoggingI";
        private boolean isDebug;
        private int type = 3;
        private String requestTag;
        private String responseTag;
        private Headers.Builder builder = new Headers.Builder();

        public Builder() {
        }

        int getType() {
            return this.type;
        }

        Headers getHeaders() {
            return this.builder.build();
        }

        String getTag(boolean isRequest) {
            if (isRequest) {
                return TextUtils.isEmpty(this.requestTag) ? TAG : this.requestTag;
            } else {
                return TextUtils.isEmpty(this.responseTag) ? TAG : this.responseTag;
            }
        }

        public Builder addHeader(String name, String value) {
            this.builder.set(name, value);
            return this;
        }

        public Builder tag(String tag) {
            TAG = tag;
            return this;
        }

        public Builder request(String tag) {
            this.requestTag = tag;
            return this;
        }

        public Builder response(String tag) {
            this.responseTag = tag;
            return this;
        }

        public Builder loggable(boolean isDebug) {
            this.isDebug = isDebug;
            return this;
        }

        public Builder log(int type) {
            this.type = type;
            return this;
        }

        public HttpLoggingInterceptor2 build() {
            return new HttpLoggingInterceptor2(this);
        }
    }
}