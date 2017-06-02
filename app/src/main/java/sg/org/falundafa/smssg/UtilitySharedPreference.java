package sg.org.falundafa.smssg;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by ok on 2/6/17.
 */
public class UtilitySharedPreference {

    private static SharedPreferences sharedPreferences = null;

    private static UtilitySharedPreference utilitySharedPreference = null;

    Context context = null;
    private UtilitySharedPreference(Context context) {
        this.context = context;
    }

    public static UtilitySharedPreference getInstance( Context context ) {
        if( utilitySharedPreference ==  null ) {
            utilitySharedPreference = new UtilitySharedPreference( context );
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences( context );
        }
        return utilitySharedPreference;
    }


    private String PREFERENCE_DEFAULT_SMS_BODY = "DEFAULT_SMS_BODY";
    public String getDefaultSmsbody( ) {
        return sharedPreferences.getString(PREFERENCE_DEFAULT_SMS_BODY, "");
    }

    public void setDefaultSmsbody( String smsbody) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString( PREFERENCE_DEFAULT_SMS_BODY,smsbody );
        editor.commit();
    }

    private String PREFERENCE_DEFAULT_SMS_PREFIX = "DEFAULT_SMS_PREFIX";
    public String getDefaultSmsPrefix( ) {
        return sharedPreferences.getString(PREFERENCE_DEFAULT_SMS_PREFIX, "+86");
    }

    public void setDefaultSmsPrefix( String smsPrefix) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString( PREFERENCE_DEFAULT_SMS_PREFIX, smsPrefix );
        editor.commit();
    }

}
