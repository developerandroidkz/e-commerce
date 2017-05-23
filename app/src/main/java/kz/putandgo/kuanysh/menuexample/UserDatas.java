package kz.putandgo.kuanysh.menuexample;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by Kuanysh on 18.03.2017.
 */

public class UserDatas {
    private final String KEY_PREFS = "prefs_user";

    private final String KEY_ID = "id";
    private final String KEY_USERNAME = "username";

    private final String KEY_TELL_NUMBER = "tellNumber";
    private final String KEY_EMAIL = "email";
    private final String KEY_DATE_REGISTRATED = "date_registrated";
    private final String KEY_IS_ACTIVE = "is_active";
    private final String KEY_IS_MARKET_ID = "Market_id";
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;
    public UserDatas() {

    }

    public UserDatas(Context context) {
        mPrefs = context.getSharedPreferences(KEY_PREFS, Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();
    }
    public void deleteUser(){
        mPrefs.edit().clear().commit();
    }
    public boolean checkLoginValidate() {
        String realUsername = mPrefs.getString(KEY_TELL_NUMBER, "");
        if ((!TextUtils.isEmpty(realUsername))){
            return true;
        }
        return false;
    }
    public String getName(){
        String realName = mPrefs.getString(KEY_USERNAME, "");
        return  realName;
    }
    public String getTellNumber(){
        String realUsername = mPrefs.getString(KEY_TELL_NUMBER, "");
        return  realUsername;
    }
    public int getId(){
        String ID=mPrefs.getString(KEY_ID, "");
        Integer id=Integer.valueOf(ID);
        return id;
    }
    public String getMatketId(){
        String ID=mPrefs.getString(KEY_IS_MARKET_ID, "");
        return ID;
    }
    public boolean registerUser(Integer id,String username,String tellnumber, String email, String is_active,String date_registrated) {
        mEditor.putString(KEY_ID, id.toString());
        mEditor.putString(KEY_USERNAME, username);
        mEditor.putString(KEY_TELL_NUMBER, tellnumber);
        mEditor.putString(KEY_EMAIL, email);
        mEditor.putString(KEY_IS_ACTIVE, is_active);
        mEditor.putString(KEY_DATE_REGISTRATED, date_registrated);
        return mEditor.commit();
    }
    public boolean markedID(String id) {
        mEditor.putString(KEY_IS_MARKET_ID, id);
        return mEditor.commit();
    }

    }
