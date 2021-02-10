package sidev.lib.implementation.frag

import android.provider.MediaStore
import android.view.View
import androidx.core.net.toFile
import kotlinx.android.synthetic.main.page_external_dir.view.*
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.std.tool.util._EnvUtil
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.implementation.R
import java.io.File

class ExternalDirFrag: Frag() {
    override val layoutId: Int = R.layout.page_external_dir

    override fun _initView(layoutView: View) {
        val extDir= _EnvUtil.externalDir(context!!, false)
        val extDirAppSpec= _EnvUtil.externalDir(context!!, true)
        val projDirAppSpec= _EnvUtil.projectDir(context!!, true)
        val projDirAppSpecNot= _EnvUtil.projectDir(context!!, false)
        val tempDir= _EnvUtil.projectTempDir(context!!)
        val logDir= _EnvUtil.projectLogDir(context!!)
        val extDir2= try {
            MediaStore.Files.getContentUri("external")?.toFile()?.absolutePath
        } catch (e: Exception){
            loge("ExternalDirFrag extDir2 `null` e= $e", e)
            null
        }

        loge("File(projDirAppSpecNot).mkdirs() = ${File(projDirAppSpecNot).mkdirs()} projDirAppSpecNot= $projDirAppSpecNot")

        layoutView.apply {
            tv_dir.text= extDir
            tv_dir_2.text= extDir2.toString()
            tv_dir_spec.text= extDirAppSpec
            tv_dir_proj_spec.text = projDirAppSpec
            tv_dir_proj_spec_not.text = projDirAppSpecNot
            tv_dir_temp.text = tempDir
            tv_dir_log.text = logDir
        }
    }
}