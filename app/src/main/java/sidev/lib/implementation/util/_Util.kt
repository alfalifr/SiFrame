package sidev.lib.implementation.util

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import sidev.lib.`val`.SuppressLiteral
import sidev.lib.implementation.R
import java.util.*

object _Util {
    fun setAlarm(
        c: Context, pi: PendingIntent,
        hour: Int = 1, minute: Int = 0, second: Int = 0,
        repeat: Boolean = true
    ){
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        cal.set(Calendar.SECOND, second)
        val manager = c.getSystemService<AlarmManager>()!!

        if(repeat)
            manager.setRepeating(AlarmManager.RTC_WAKEUP, cal.timeInMillis, AlarmManager.INTERVAL_DAY, pi)
        else
            manager.set(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pi)
    }

    fun stopAlarm(c: Context, pi: PendingIntent){
        c.getSystemService<AlarmManager>()!!.cancel(pi)
    }

    @Suppress(SuppressLiteral.NAME_SHADOWING)
    fun showNotif(
        c: Context,
        smIcon: Int = R.drawable.ic_menu_camera,
        title: String = "Ini judulnya",
        desc: String = "Ini desc nya",
        channelId: String = "CHANNEL_ID",
        channelName: String = "CHANNEL_NAME",
        channelDesc: String = "CHANNEL_DESC",
        notifId: Int = 1
    ){
        val channelId = channelId //"CHANNEL_ID"
        val channelName = channelName //"CHANNEL_NAME"
        val channelDesc = channelDesc //"CHANNEL_DESC"
        val notifId = notifId

        val manager = c.getSystemService<NotificationManager>()!!

        val builder = NotificationCompat.Builder(c, channelId)
            .setContentTitle(title)
            .setContentText(desc)
            .setSmallIcon(smIcon)
            //.setLargeIcon(R.drawable.ic_chat)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .setAutoCancel(true)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = channelDesc

            //builder.setChannelId(channelId)
            manager.createNotificationChannel(channel)
        }

        val notif = builder.build()
        manager.notify(notifId, notif)
        //Toast.makeText(context!!, "Notif dikirimkan", Toast.LENGTH_LONG).show()
    }
}