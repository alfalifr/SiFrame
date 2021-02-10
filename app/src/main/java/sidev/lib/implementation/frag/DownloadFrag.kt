package sidev.lib.implementation.frag

import android.os.Environment
import android.view.View
import kotlinx.android.synthetic.main.page_progres.view.*
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.runOnUiThread
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.std.tool.util.`fun`.asyncBuffer
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.async.async
import sidev.lib.implementation.R
import sidev.lib.jvm.tool.`fun`.contentLengthLong_
import sidev.lib.jvm.tool.`fun`.responseStr
import sidev.lib.jvm.tool.`fun`.saveBufferByteToFile
import sidev.lib.jvm.tool.util.TimeUtil
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

class DownloadFrag: Frag() {
    override val layoutId: Int = R.layout.page_progres

    @ExperimentalStdlibApi
    override fun _initView(layoutView: View) {
        val no= 289
        val url= URL("https://cdn.alquran.cloud/media/audio/ayah/ar.alafasy/$no")
        //val con= URL("https://cdn.alquran.cloud/media/audio/ayah/ar.alafasy/1")
        val downDir= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val timestamp= TimeUtil.timestamp(pattern = "dd-MM-yyyy_HH:mm:ss")
        val fileName= "ayat$${no}_$timestamp.mp3"
        val file= File("$downDir/_develop/$fileName")
        file.parentFile!!.mkdirs()

/*
        val len= conn.contentLengthLong_
        runOnUiThread { layoutView.pb.max = len.toInt() }

        conn.saveBufferByteToFile("$downDir/_develop/$fileName") { readByteLen, current, len2 ->
            runOnUiThread {
                layoutView.apply {
                    pb.progress = current.toInt()
                    val percent= current / len
                    tv_progres.text = "$percent %"
                }
            }
        }
// */
///*
        //val act= act
        async ({
            val conn= url.openConnection() as HttpURLConnection
            val len= conn.contentLengthLong_

            layoutView.pb.max = len.toInt()
            //act.runOnUiThread { layoutView.pb.max = len.toInt() }

            loge("conn.saveBufferByteToFile HAMPIR!====== conn.responseCode= ${conn.responseCode}")
            val conn2= URL("https://api.banghasan.com/quran/format/json/surat/3").openConnection() as HttpURLConnection
            loge("conn2.inputStream.available()= ${conn2.inputStream.available()}")
            //loge("conn2.inputStream.read()= ${conn2.inputStream.read()}")
            val resp= conn2.responseStr()
            loge("======== resp= $resp conn2.responseCode= ${conn2.responseCode}")

            try{
                conn.saveBufferByteToFile(file, false) { readByteLen, current, len2 ->
                    loge("conn.saveBufferByteToFile readByteLen= $readByteLen current= $current len2= $len2")
                    act.runOnUiThread {
                    layoutView.apply {
                        pb.progress = current.toInt()
                        val percent= current / len
                        tv_progres.text = "$percent %"
                    }
                    }
                }
            } catch (e: Exception){
                e.printStackTrace()
                loge("INET ERROR =============")
            }
        }) {
            act.runOnUiThread {
                layoutView.tv_progres.text = "Disimpan di $downDir/_develop/$fileName"
            }
        }
/*
        async({
            val len= conn.contentLengthLong_
            layoutView.pb.max = len.toInt()
            //act.runOnUiThread { layoutView.pb.max = len.toInt() }

            conn.saveBufferByteToFile("$downDir/_develop/$fileName") { readByteLen, current, len2 ->
                act.runOnUiThread {
                    layoutView.apply {
                        pb.progress = current.toInt()
                        val percent= current / len
                        tv_progres.text = "$percent %"
                    }
                }
            }
        }){
            layoutView.tv_progres.text = "Selesai"
        }
// */
/*
        asyncBuffer(0, { it.index < 10 }, {
            layoutView.apply {
                pb.progress = it
                tv_progres.text = "$it %"
            }
        }, {
            layoutView.tv_progres.text = "Selesai"
        }, 100){
            it + 10
        }
// */
    }
}