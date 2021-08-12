package com.safidence.safidence.data.prefs

import android.content.Context
import android.content.SharedPreferences
import com.safidence.safidence.data.model.ResponseLogin


class SavePref(context: Context) {
    private val mContext = context
    private var sharedPref: SharedPreferences
    private val PREFS_NAME = "app_prefs"

    private val USER_ID = "user_id"
    private val USER_NAME: String = "user_name"
    private val EMAIL: String = "user_email"
    private val PHONE: String = "user_ph"
    private val CNIC: String = "user_cnic"
    private val FCM_TOKEN: String = "fcm_token"
    private val DEVICE_ID: String = "device_id"

    private val ACCESSTOKEN = "access_token"
    private lateinit var editor: SharedPreferences.Editor

    init {
        sharedPref = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveLogin(responseLogin: ResponseLogin){
        editor = sharedPref.edit()
        editor.putInt(USER_ID, responseLogin.user.id)
        editor.putString(USER_NAME, responseLogin.user.name)
        editor.putString(CNIC, responseLogin.user.nic)
        editor.putString(EMAIL, responseLogin.user.email)
        editor.putString(PHONE, responseLogin.user.phone)
        editor.putString(ACCESSTOKEN, responseLogin.access_token)
        editor.apply()
    }

    fun setUserId(id: Int) {
        editor = sharedPref.edit()
        editor.putInt(USER_ID, id)
        editor.apply()
    }

    fun getUserId(): String {
        return sharedPref.getInt(USER_ID, 0).toString()
    }

    fun setFcmToken(token: String) {
        editor = sharedPref.edit()
        editor.putString(FCM_TOKEN, token)
        editor.apply()
    }

    fun getFcmToken(): String {
        return sharedPref.getString(FCM_TOKEN, "").toString()
    }

    fun setAccessToken() {
        editor = sharedPref.edit()
        editor.putString(ACCESSTOKEN, "")
        editor.apply()
    }

    fun getAccessToken(): String {
        return sharedPref.getString(ACCESSTOKEN, "").toString()
    }

    fun getUserName(): String {
        return sharedPref.getString(USER_NAME, "").toString()
    }

    fun getUserCNIC(): String {
        return sharedPref.getString(CNIC, "").toString()
    }

    fun setDeviceId(deviceId: String) {
        editor = sharedPref.edit()
        editor.putString(DEVICE_ID, deviceId)
        editor.apply()
    }

    fun getDeviceId(): String {
        return sharedPref.getString(DEVICE_ID, "").toString()
    }
}