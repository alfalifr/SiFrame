package sidev.lib.android.siframe.customizable.lifecycle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import sidev.lib.android.siframe.customizable._init._Config


class _BlankFrag: Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //R.layout._t_blank_frag
        return inflater.inflate(_Config.LAYOUT_BLANK, container, false)
    }
}