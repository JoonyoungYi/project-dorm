package kr.clude.yearnning.smartdorm;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by yearnning on 15. 1. 20..
 */
public class PreferenceManager {

    /**
     * @param context
     * @param key
     * @param value
     */
    public static void put(Context context, String key, String value) {

        SharedPreferences prefs = context.getSharedPreferences(Argument.PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefs_editor;

        prefs_editor = prefs.edit();
        prefs_editor.putString(key, value);
        prefs_editor.commit();
    }

    /**
     * @param context
     * @param key
     * @param value_init
     * @return
     */
    public static String get(Context context, String key, String value_init) {
        SharedPreferences prefs = context.getSharedPreferences(Argument.PREFS, Context.MODE_PRIVATE);
        return prefs.getString(key, value_init);
    }

}
