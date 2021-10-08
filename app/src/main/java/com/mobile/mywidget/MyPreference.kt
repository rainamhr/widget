package com.mobile.mywidget

import android.content.Context

const val PREFERENCE_NAME = "preference"
const val PREFERENCE_APP_ID = "preference_app_id"

class MyPreference(context: Context) {
    private val preference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun updateWidgetId(ids: MutableSet<String>) {
        val editor = preference.edit()
        editor.putStringSet(PREFERENCE_APP_ID, ids)
        editor.apply()
    }

    fun getWidgetId():MutableSet<String>{
        return preference.getStringSet(PREFERENCE_APP_ID, hashSetOf())!!
    }
}