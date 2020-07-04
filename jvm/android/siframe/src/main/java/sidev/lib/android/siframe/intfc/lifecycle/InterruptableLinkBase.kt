package sidev.lib.android.siframe.intfc.lifecycle

/**
 * Interface ini digunakan untuk menahan interaksi dari user.
 * Sehingga user harus menunggu agar proses yg berjalan hingga selesai.
 * Interface ini tidak dapat digunakan untuk manajemen antrian Thread.
 */
interface InterruptableLinkBase {
    val interruptable: InterruptableBase?
    fun <T> doWhenLinkNotBusy(func: () -> T): T?{
        return if(interruptable?.isBusy?.not() == true) func()
        else null
    }
}