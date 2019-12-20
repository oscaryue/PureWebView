package com.oscaryue.purewebview;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * 存放SharedPreferences的工具类
 */
public final class SettingUtils {
    private static final String SETTINGS_NAME = "com.oscaryue.purewebview.SettingUtils";
    private static volatile SettingUtils mInstance;
    private static byte[] lock = new byte[]{};

    private SharedPreferences mSharedPref;

    private SettingUtils() {

    }

    public void init(Context context) {
        mSharedPref = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_MULTI_PROCESS);
    }

    public static SettingUtils getInstance() {
        if (null == mInstance) {
            synchronized (lock) {
                if (null == mInstance) {
                    mInstance = new SettingUtils();
                }
            }
        }
        return mInstance;
    }

    public void setSetting(String key, boolean value) {
        try {
            SharedPreferences.Editor editor = mSharedPref.edit();
            editor.putBoolean(key, value);
            editor.commit();
        } catch (Exception e) {
        }
    }

    public void setSetting(String key, int value) {
        try {
            SharedPreferences.Editor editor = mSharedPref.edit();
            editor.putInt(key, value);
            editor.commit();
        } catch (Exception e) {
        }
    }

    public void setSetting(String key, float value) {
        try {
            SharedPreferences.Editor editor = mSharedPref.edit();
            editor.putFloat(key, value);
            editor.commit();
        } catch (Exception e) {
        }
    }

    public void setSetting(String key, long value) {
        try {
            SharedPreferences.Editor editor = mSharedPref.edit();
            editor.putLong(key, value);
            editor.commit();
        } catch (Exception e) {
        }
    }

    public void setSetting(String key, String value) {
        if (null != value) {
            //要过滤'\0',否则会使XML读取异常
            value = value.replace("\0", "");
        }
        try {
            SharedPreferences.Editor editor = mSharedPref.edit();
            editor.putString(key, value);
            editor.commit();
        } catch (Exception e) {
        }
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        boolean result = defaultValue;
        try {
            result = mSharedPref.getBoolean(key, result);
        } catch (Exception e) {
        }
        return result;
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        int value = defaultValue;
        try {
            value = mSharedPref.getInt(key, defaultValue);
        } catch (Exception e) {
        }
        return value;
    }

    public float getFloat(String key) {
        return getFloat(key, 0);
    }

    public float getFloat(String key, float defaultValue) {
        float value = defaultValue;
        try {
            value = mSharedPref.getFloat(key, defaultValue);
        } catch (Exception e) {
        }
        return value;
    }

    public long getLong(String key) {
        return getLong(key, 0);
    }

    public long getLong(String key, long defaultValue) {
        long value = defaultValue;
        try {
            value = mSharedPref.getLong(key, defaultValue);
        } catch (Exception e) {
        }
        return value;
    }

    public String getString(String key) {
        return getString(key, null);
    }

    public String getString(String key, String defaultValue) {
        String value = defaultValue;
        try {
            value = mSharedPref.getString(key, defaultValue);
        } catch (Exception e) {
        }
        return value;
    }

    public void removeSetting(String key) {
        try {
            //如果key不为空，把key删掉
            if (!TextUtils.isEmpty(key)) {
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.remove(key);
                editor.commit();
            }
        } catch (Exception e) {
        }
    }
}
