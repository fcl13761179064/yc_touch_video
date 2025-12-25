package com.common.framework.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.KeyguardManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.EditText;
import android.widget.TextView;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class CommonUtils {

	private static final String TAG = CommonUtils.class.getSimpleName();

	/**
	 * 下载app
	 */
	public static void downapp(Context context, String downURL) {
		try {
			Intent intent = null;
			intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(downURL));
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 用户名字符长度为4-12个英文字母、数字或汉字;
	 *
	 * @param name
	 * @return true 昵称不符合规则
	 */
	public static boolean isNameInvalid(String name) {
		return (name == null) || (name.length() < 4) || (name.length() > 12);
	}

	/**
	 * 动态配置图片资源
	 *
	 * @param mContext
	 * @param pic_name
	 *            图片名字
	 * @return
	 */
	public static int getDrawbleResource(Context mContext, String pic_name) {
		ApplicationInfo appInfo = mContext.getApplicationInfo();
		// 得到该图片的id(name 是该图片的名字，"drawable"
		// 是该图片存放的目录，appInfo.packageName是应用程序的包)
		int resID = mContext.getResources().getIdentifier(pic_name, "drawable", appInfo.packageName);
		return resID;
	}

    /**
     * 获取IMEI
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
	public static String getImei(Context context) {
        String result = null;
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (null != tm) {
                result = tm.getDeviceId();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取android id
     * @param context
     * @return
     */
    public static String getAndroidId(Context context) {
        String result = null;
        try {
            result = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取build serial
     * @param context
     * @return
     */
    public static String getBuildSerial(Context context) {
        String result = null;
        try {
            result = Build.SERIAL;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return result;
    }

	/**
	 * server IP 地址
	 *
	 * @param host
	 * @return
	 */
	public static String getHostIPAddress(String host) {
		try {
			return InetAddress.getByName(host).getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * client IP 地址
	 *
	 * @return
	 */
	public static String getLocalIPAddress() {
		try{
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()){
						return inetAddress.getHostAddress();
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}


		/**
         * provide default 0 to avoid NumberFormatException
         *
         * @param value integer in format of string
         * @return 0 or value as int
         */
	public static int parseInt(String value) {
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			return 0;
		}
	}

	public static long parseLong(String value) {
		try {
			return Long.parseLong(value);
		} catch (Exception e) {
			return 0;
		}
	}

	public static double parseDouble(String value) {
		try {
			return Double.parseDouble(value);
		} catch (Exception e) {
			return 0;
		}
	}
	public static float parseFloat(String value) {
		try {
			return Float.parseFloat(value);
		} catch (Exception e) {
			return 0;
		}
	}

	public static float parseFloat(String value, float defValue) {
		try {
			return Float.parseFloat(value);
		} catch (Exception e) {
			return defValue;
		}
	}



    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;

        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = CommonUtils.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sbar;
    }

    /**
     * 判断class对应的Service是否开启
     * @param context
     * @param className service 全称
     * eg： com.babytree.apps.pregnancy.ic_scene_9.MusicService
     *
     *                   获取当前手机运行的前200个服务
     * @return
     */
    public static boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;
        if (!TextUtils.isEmpty(className)) {
            try {
                ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(200);
                if (serviceList == null || !(serviceList.size() > 0)) {
                    return false;
                }
                for (int i = 0; i < serviceList.size(); i++) {
                    ComponentName service = serviceList.get(i).service;
                    if (service != null && className.equals(service.getClassName())) {
                        isRunning = true;
                        break;
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return isRunning;
    }

	/**
	 * 获取View Tag里面的值
	 *
	 * @param view        数据源
	 * @param defaultData 默认返回数据
	 * @return Object 数据
	 */
	public static Object getDataByViewTag(View view, Object defaultData) {
		try {
			if (view != null) {
				Object tag = view.getTag();
				if (tag != null) {
					return tag;
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return defaultData;
	}

	/**
	 * 获取Asset目录下的Json文件
	 *
	 * @param context
	 * @param fileName 	文件名
	 * @return
	 */
	public static String getAssetJsonByName(Context context, String fileName) {
		StringBuilder sb = new StringBuilder();
		BufferedReader bf = null;
		try {
			AssetManager am = context.getAssets();
			bf = new BufferedReader(new InputStreamReader(am.open(fileName)));
			String line;
			while ((line = bf.readLine()) != null) {
				sb.append(line);
			}
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (bf != null) {
					bf.close();
					bf = null;
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return sb.toString().length() > 0 ? sb.toString() : null;
	}

	/**
	 * 在指定 count范围内，获取一个随机数，不包含之前的 preIndex
	 *
	 * @param count 随机范围
	 * @param preIndex 之前的值
	 * @return
	 */
	public static int getNewRandomInt(int count, int preIndex) {
		try {
			Random random = new Random();
			int nextInt = random.nextInt(count);
			if (preIndex != nextInt) {
				return nextInt;
			}
			return getNewRandomInt(count, preIndex);
		} catch (Throwable e) {
			e.printStackTrace();
			return preIndex;
		}
	}

	/**
	 * 传入的字符串，是否等于1
	 *
	 * @param value 值
	 * @return
	 */
	public static boolean isEqualTo1(String value) {
		return "1".equals(value);
	}

	/**
	 * 把资源文件，转换成Drawable
	 *
	 * @param context
	 * @param
	 * @return
	 */
	public static Drawable getDrawableById(Context context, int id) {
		if (id <= 0) return null;
		try {
			Drawable drawable = context.getResources().getDrawable(id);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			return drawable;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 测量文字在TextView上面的宽度
	 *
	 * @param textView
	 * @param text
	 * @return
	 */
	public static int getTextViewLength(TextView textView, String text) {
		try {
			TextPaint paint = textView.getPaint();
			return (int) paint.measureText(text);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	static final Pattern reUnicode = Pattern.compile("0x([0-9a-zA-z]{4,5})");

	public static String decodeUnicode(String theString) {

		Matcher m = reUnicode.matcher(theString);
		StringBuffer sb = new StringBuffer(theString.length());
		while (m.find()) {
			m.appendReplacement(sb, newString(Integer.parseInt(m.group(1), 16)));
		}
		m.appendTail(sb);
		return sb.toString();
	}

	public static String newString(int codePoint) {
		return new String(Character.toChars(codePoint));
	}

	// meitun应用是否在前台
	private static boolean mIsMTActive = false;
	public static boolean isOnMT = false;



	/**
	 * 启动原生页面
	 * @param context
	 * @param schemeName
	 */
	public static void openScheme(Context context, String schemeName) {
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(schemeName));
			context.startActivity(intent);
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 获取指定app VersionCode
	 */
	public static int getAppVersionCode(Context context, String packageName) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_CONFIGURATIONS);
			return info.versionCode;
		} catch (Throwable e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * 指定Apk是否安装
	 *
	 * @param context
	 * @param packageName
     * @return
     */
	public static boolean isAppInstall(Context context, String packageName) {
		Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
		return intent != null;
	}


	/**
	 * findViewById 工具方法
	 *
	 * @param view
	 * @param id
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends View> T $(View view, int id) {
		return (T) view.findViewById(id);
	}

	/**
	 * 根据要求的最大行数，在末尾添加...
	 * @param tv
	 * @param maxlines
     */
	public static void setEllipsizeForText(final TextView tv, final int maxlines) {
		ViewTreeObserver vto = tv.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				try {
					ViewTreeObserver obs = tv.getViewTreeObserver();
					obs.removeGlobalOnLayoutListener(this);
					if (tv.getLineCount() > maxlines) {
						int lineEndIndex = tv.getLayout().getLineEnd(maxlines - 1);
						CharSequence text = tv.getText().subSequence(0, lineEndIndex - 2);
						tv.setText(text);
						tv.append("...");
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 判断list 是否为空 或者null
	 * @param list
	 * @return
     */
	public static boolean isEmptyList(List list) {
		if (list == null || list.isEmpty()) return true;
		return false;
	}

	/**
	 * 判定输入汉字
	 *
	 * @param c
	 * @return
	 */
	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
			return true;
		}
		return false;
	}

	/**
	 * 检测String是否全是中文
	 *
	 * @param name
	 * @return
	 */
	public static boolean checkNameChese(String name) {
		boolean res = true;
		try {
			char[] cTemp = name.toCharArray();
			for (int i = 0; i < name.length(); i++) {
				if (!isChinese(cTemp[i])) {
					res = false;
					break;
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 根据fraction值来计算当前的颜色。
	 */
	public static int getCurrentColor(int startColor, int endColor, float fraction) {
		int redCurrent;
		int blueCurrent;
		int greenCurrent;
		int alphaCurrent;

		int redStart = Color.red(startColor);
		int blueStart = Color.blue(startColor);
		int greenStart = Color.green(startColor);
		int alphaStart = Color.alpha(startColor);

		int redEnd = Color.red(endColor);
		int blueEnd = Color.blue(endColor);
		int greenEnd = Color.green(endColor);
		int alphaEnd = Color.alpha(endColor);

		int redDifference = redEnd - redStart;
		int blueDifference = blueEnd - blueStart;
		int greenDifference = greenEnd - greenStart;
		int alphaDifference = alphaEnd - alphaStart;

		redCurrent = (int) (redStart + fraction * redDifference);
		blueCurrent = (int) (blueStart + fraction * blueDifference);
		greenCurrent = (int) (greenStart + fraction * greenDifference);
		alphaCurrent = (int) (alphaStart + fraction * alphaDifference);

		return Color.argb(alphaCurrent, redCurrent, greenCurrent, blueCurrent);
	}

	/**
	 * 获取334格式手机号
	 * @param phoneNumber
	 * @return
	 */
	public static String get334FormatString(String phoneNumber){
		if(phoneNumber == null || phoneNumber.length() < 11){
			return phoneNumber;
		}
		StringBuilder sb = new StringBuilder(phoneNumber);
		sb.insert(3," ");
		sb.insert(8," ");
		return sb.toString();
	}

	public static RequestBody json2Body(JsonObject jsonObject){
		return RequestBody.create(
				MediaType.parse("application/json; charset=UTF-8"),
				jsonObject.toString()
		);
	}

	/**
	 * Copy the text to clipboard.
	 * <p>The label equals name of package.</p>
	 *
	 * @param text The text.
	 */
	public static void copyText(Context context,final CharSequence text) {
		ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
		cm.setPrimaryClip(ClipData.newPlainText(context.getPackageName(), text));
	}
}