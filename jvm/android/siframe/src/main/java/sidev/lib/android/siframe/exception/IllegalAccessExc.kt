package sidev.lib.android.siframe.exception

class IllegalAccessExc(rc: Class<*>?= IllegalAccessExc::class.java, msg: String= "")
    : Exc(rc, "Terjadi kesalahan saat mengakses fungsi atau properti.", msg)