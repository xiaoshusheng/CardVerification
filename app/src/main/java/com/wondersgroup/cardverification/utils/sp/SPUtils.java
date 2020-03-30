package com.wondersgroup.cardverification.utils.sp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public final class SPUtils {

    private final static String name = "config";
    private final static int mode = Context.MODE_PRIVATE;

    /**
     * 保存首选项
     *
     * @param context
     * @param key
     * @param value
     */
    public static void saveBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(name, mode);
        Editor edit = sp.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public static void saveInt(Context context, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(name, mode);
        Editor edit = sp.edit();
        edit.putInt(key, value);
        edit.commit();
    }
    public static void saveLong(Context context, String key, long value) {
        SharedPreferences sp = context.getSharedPreferences(name, mode);
        Editor edit = sp.edit();
        edit.putLong(key, value);
        edit.commit();
    }

    public static void saveString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(name, mode);
        Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }
    /**
     * 获取首选项
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static boolean getBoolean(Context context, String key, boolean defValue) {
        SharedPreferences sp = context.getSharedPreferences(name, mode);
        return sp.getBoolean(key, defValue);
    }

    public static int getInt(Context context, String key, int defValue) {
        SharedPreferences sp = context.getSharedPreferences(name, mode);
        return sp.getInt(key, defValue);
    }

    public static long getLone(Context context, String key, long defValue) {
        SharedPreferences sp = context.getSharedPreferences(name, mode);
        return sp.getLong(key, defValue);
    }

    public static String getString(Context context, String key, String defValue) {
        SharedPreferences sp = context.getSharedPreferences(name, mode);
        return sp.getString(key, defValue);
    }

    /**
     * 检测指定的可以是否存在,当key 的值为空是也指定该key 不存在
     *
     * @param context
     * @param key
     * @return true 存在    false 不存在
     */
    public static boolean checkKeyExist(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(name, mode);
        String value = sp.getString(key,"");
        if(value.trim().length() == 0){
            return false;
        }
        return true;
    }

    public static boolean remove(Context context, String key){

        SharedPreferences sp = context.getSharedPreferences(name, mode);
        Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
        return true;
    }

    /**
     * 清空所有内容
     * @param context
     * @return
     */
    public static boolean clear(Context context){

        SharedPreferences sp = context.getSharedPreferences(name, mode);
        Editor editor = sp.edit();
        editor.clear();
        editor.commit();
        return true;

    }
}
