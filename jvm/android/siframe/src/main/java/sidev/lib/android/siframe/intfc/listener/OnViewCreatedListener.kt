package sidev.lib.android.siframe.intfc.listener

import android.os.Bundle
import android.view.View

interface OnViewCreatedListener: Listener {
    fun onViewCreated_(view: View, savedInstanceState: Bundle?)
}