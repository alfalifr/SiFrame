package sidev.lib.android.siframe.tool.util

import android.content.Context
import sidev.lib.android.siframe.tool.util.`fun`.logi
import sidev.lib.jvm.tool.util.FileUtil
import java.io.File

object _FileUtil {
    /**
     * Mengambil instance [File] yg merupakan writable dari direktori eksternal
     * yg merupakan file, bkn direktori. Return `null` jika tidak terdapat direktori eksternal.
     */
    fun getExternalFile(context: Context, fileName: String): File?{
        return getWritableExternalFilesDir(context)
            ?.let { File("${it.absolutePath}/$fileName") }
            ?.let {
                if(FileUtil.canWriteTo(
                        it.parentFile!! //Gak mungkin null karena parent-nya
                          // jelas dir dari [getWritableExternalFilesDir]
                    )) it
                else null
            }
    }
    /**
     * Mengambil instance [File] yg merupakan writable dari direktori eksernal
     * yg merupakan direktori, bkn file. Return `null` jika tidak terdapat direktori eksternal.
     */
    fun getExternalDir(context: Context, childDir: String): File?{
        return getWritableExternalFilesDir(context)
            ?.let { File("${it.absolutePath}/$childDir") }
            ?.let {
                if(FileUtil.canWriteTo(it)) it
                else null
            }
    }

    /**
     * Mengambil salah satu dari dari dir eksternal yg writable dari [getWritableExternalFilesDirs].
     * Return `null` jika tidak tersedia dir yg writable.
     */
    fun getWritableExternalFilesDir(context: Context): File?{
        getWritableExternalFilesDirs(context).forEach {
            if(FileUtil.canWriteTo(it))
                return it
        }
        //throw IOException("Tidak dapat mengambil direktori eksternal")
        return null
    }

    /**
     * Mengambil daftar direktori eksternal yg tersedia yg dapat ditulis (writable)
     * untuk app Android. Jika tidak tersedia, maka return `emptyList`.
     */
    fun getWritableExternalFilesDirs(context: Context): List<File>{
        val dirs= (try { context.getExternalFilesDirs(null) }
        catch (e: Exception){
            context.getExternalFilesDir(null)?.let { arrayOf(it) }
        })

        if(dirs == null || dirs.isEmpty()){
            logi("Tidak dapat mengambil direktori eksternal dari konteks: $context")
            return emptyList()
        }
        return dirs.filter { file ->
            (file.exists() || file.mkdirs())
                .also { if(!it) logi("Tidak dapat membuat direktori eksternal: ${file.absolutePath}")}
        }
    }
}