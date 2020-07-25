package sidev.lib.android.siframe.intfc.lifecycle

import androidx.lifecycle.Lifecycle

interface LifecycleBase{
    val currentState: State
        get()= State.INITIALIZED

    enum class State(val no: Int){
        /** Sama dg [Lifecycle.State.INITIALIZED] */
        INITIALIZED(0),

        /** Sama dg [Lifecycle.State.CREATED] */
        CREATED(1),

        /** Sama dg [Lifecycle.State.STARTED] */
        STARTED(2),

        /**
         * Mirip dg [Lifecycle.State.RESUMED], namun lifecycle dikatakan [ACTIVE] jika
         * terlihat di layar. Jika ada Activity atau Fragment lain yg menutupinya,
         * maka [currentState] menjadi [PAUSED].
         */
        ACTIVE(3),

        /**
         * Saat Activity atau Fragment lain yg menutupinya, atau jika itu sesuatu yg lain,
         * maka status ini adalah untuk menunjukan lifecycle sedang tidak aktif namun
         * belum dihancurkan.
         */
        PAUSED(4),

        /** Sama dg [Lifecycle.State.DESTROYED] */
        DESTROYED(5),
    }
}