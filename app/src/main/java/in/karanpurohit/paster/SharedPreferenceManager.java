package in.karanpurohit.paster;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by karan on 3/11/16.
 */
public class SharedPreferenceManager {
    static String setValue(Context context, String key, String value){
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.apply();
        return key;
    }

    static String getValue(Context context,String key){
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key,null);
    }
}
