package sidev.lib.implementation.frag

import android.view.View
import kotlinx.android.synthetic.main.frag_file_write.view.*
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.std.tool.util._FileUtil
import sidev.lib.android.std.tool.util.`fun`.txt
import sidev.lib.jvm.tool.util.FileUtil
import sidev.lib.jvm.tool.util.FileUtil.saveln
import java.io.File

class FileWriteFrag: Frag() {
    override val layoutId: Int = sidev.lib.implementation.R.layout.frag_file_write
    val fileName= "testFile.txt"

    override fun _initView(layoutView: View) {
        val dir= _FileUtil.getExternalDir(context!!, "test")!!
        val file= File(dir, fileName)
        if(file.exists())
            layoutView.ed_input.txt= FileUtil.readStrFrom(file) ?: ""
/*
        Thread.setDefaultUncaughtExceptionHandler { t: Thread?, e: Throwable ->
            val timestamp = timestamp(Calendar.getInstance(), "dd-MM-yyyy")
            val logFileName = "Log_$timestamp.txt"
            val dir = getExternalDir(context!!, "log")
            try {
                if (dir != null) {
                    val logFile = File(dir, logFileName)
                    val timestamp_ = timestamp()
                    saveln(logFile, "--$timestamp_: ============= New Exception ==========")
                    saveln(logFile, "--MSG: ====== ${e.message} ======")
                    saveln(logFile, "--CAUSE: ====== ${e.cause} ======")
                    val fw = FileWriter(logFile, true)
                    val ps = PrintWriter(fw)
                    e.printStackTrace(ps)
                    ps.close()
                }
            } catch (e2: Exception) {
                e2.printStackTrace()
            }
//            e.printStackTrace()
            exitProcess(1)
        }
        throw RuntimeException("Halo Bro Error")
*/
        layoutView.btn.setOnClickListener {
            saveln(file, layoutView.ed_input.text.toString())
        }
    }
}