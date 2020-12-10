package sidev.lib.android.siframe.lifecycle.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import sidev.lib.android.siframe.`val`._SIF_Config


class _BlankFrag: Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //R.layout._t_blank_frag
        return inflater.inflate(_SIF_Config.LAYOUT_BLANK, container, false)
    }
}