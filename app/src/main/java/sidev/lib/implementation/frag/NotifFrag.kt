package sidev.lib.implementation.frag

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import kotlinx.android.synthetic.main.page_btn.view.*
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.std.tool.util.`fun`.toast
import sidev.lib.implementation.R
import sidev.lib.implementation.util._Util

class NotifFrag: Frag() {
    override val layoutId: Int = R.layout.page_btn

    override fun _initView(layoutView: View) {
        //val alarmManager = context!!.getSystemService<AlarmManager>()

        layoutView.btn.setOnClickListener {
            showNotif()
        }
    }

    fun showNotif(){
        _Util.showNotif(context!!)
        //Toast.makeText(context!!, "Notif dikirimkan", Toast.LENGTH_LONG).show()
        toast("Notif dikirimkan")
    }
}