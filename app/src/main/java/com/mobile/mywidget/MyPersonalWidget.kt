package com.mobile.mywidget

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import java.util.*

const val WIDGET_SYNC = ""

class MyPersonalWidget : AppWidgetProvider() {

    lateinit var preference: MyPreference

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        if (!::preference.isInitialized) {
            preference = MyPreference(context)

            val ids = preference.getWidgetId()

            for (appWidgetId in appWidgetIds) {
                ids.add(appWidgetId.toString())
                updateAppWidget(context, appWidgetManager, appWidgetId)
            }

            preference.updateWidgetId(ids)
        }
    }

    override fun onEnabled(context: Context) {
        val intent = Intent(context, MyPersonalWidget::class.java)
        intent.action = WIDGET_SYNC

        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MINUTE, 1)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setInexactRepeating(
            AlarmManager.RTC,
            calendar.timeInMillis,
            60000,
            pendingIntent
        )
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context, intent: Intent?) {

        if (WIDGET_SYNC == intent?.action)
            if (!::preference.isInitialized) {
                preference = MyPreference(context)
                val ids = preference.getWidgetId()

                for (id in ids) {
                    updateAppWidget(context, AppWidgetManager.getInstance(context), id.toInt())
                }
            }
        super.onReceive(context, intent)
    }

    companion object {
        private const val TAG = "MyPersonalWidget"
        internal fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {

            val intent = Intent(context, MyPersonalWidget::class.java)
            intent.action = WIDGET_SYNC
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

            val random = Random().nextInt().toString()
            val views = RemoteViews(context.packageName, R.layout.my_personal_widget)
            views.setTextViewText(R.id.appwidget_text, random)

            Log.d(TAG, "updateAppWidget: " + random)
            views.setOnClickPendingIntent(R.id.sync, pendingIntent)
            appWidgetManager.updateAppWidget(appWidgetId, views)

        }
    }
}