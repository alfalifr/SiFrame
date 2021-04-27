package sidev.lib.implementation.frag

import android.app.PendingIntent
import android.content.Intent
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.page_alarm.*
import org.jetbrains.anko.backgroundColorResource
import org.jetbrains.anko.textColorResource
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.implementation.R
import sidev.lib.implementation.util.AlarmNotifReceiver
import sidev.lib.implementation.util._Util


class AlarmFrag: Frag() {
    override val layoutId: Int = R.layout.page_alarm
    private var isAlarmUp = false

    override fun _initView(layoutView: View) {
        val alarmIntent = Intent(context!!, AlarmNotifReceiver::class.java)
        isAlarmUp = isAlarmUp(alarmIntent)

        layoutView.apply {
            btn.apply {
                setBtnActive(isAlarmUp)

                setOnClickListener {
                    if(isAlarmUp(alarmIntent)){
                        val pi = getPendingIntent(alarmIntent, PendingIntent.FLAG_NO_CREATE)!!
                        pi.cancel() // It's mandatory to call
                        _Util.stopAlarm(context!!, pi)
                    } else {
                        val hour = et_hour.text.toString().toInt()
                        val minute = et_minute.text.toString().toInt()
                        val second = et_second.text.toString().toInt()

                        val pi = getPendingIntent(alarmIntent)!!
                        _Util.setAlarm(context!!, pi, hour, minute, second)
                    }
                    setBtnActive(!isAlarmUp)
                }
            }
        }
    }

    private fun isAlarmUp(intent: Intent) = getPendingIntent(intent, PendingIntent.FLAG_NO_CREATE) != null

    private fun getPendingIntent(intent: Intent, flag: Int = PendingIntent.FLAG_UPDATE_CURRENT): PendingIntent? =
        PendingIntent.getBroadcast(context!!, 0, intent, flag)

    private fun Button.setBtnActive(active: Boolean = true){
        if(active){
            backgroundColorResource = R.color.colorPrimary
            textColorResource = R.color.putih
            text = "Non-Aktifkan"
        } else {
            backgroundColorResource = R.color.abuLebihTua
            textColorResource = R.color.hitam
            text = "Aktifkan"
        }
        isAlarmUp = active
    }
}