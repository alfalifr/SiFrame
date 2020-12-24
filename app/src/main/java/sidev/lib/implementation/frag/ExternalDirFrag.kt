package sidev.lib.implementation.frag

import android.provider.MediaStore
import android.view.View
import androidx.core.net.toFile
import kotlinx.android.synthetic.main.page_external_dir.view.*
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.std.tool.util._EnvUtil
import sidev.lib.implementation.R

class ExternalDirFrag: Frag() {
    override val layoutId: Int = R.layout.page_external_dir

    override fun _initView(layoutView: View) {
        val extDir= _EnvUtil.externalDir(context!!, false)
        val extDirAppSpec= _EnvUtil.externalDir(context!!, true)
        val extDir2= try {
            MediaStore.Files.getContentUri("external")?.toFile()?.absolutePath
        } catch (e: Exception){ null }

        layoutView.apply {
            tv_dir.text= extDir
            tv_dir_2.text= extDir2.toString()
            tv_dir_spec.text= extDirAppSpec
        }
    }
}