package sidev.lib.android.siframe.intfc.`fun`

import java.io.Serializable


class RunFunClass(val func: () -> Unit): RunFun, Serializable {
    override fun run() {
        func()
    }
}