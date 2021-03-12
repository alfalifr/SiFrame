package sidev.lib.implementation.frag

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.frag_txt.view.*
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.std.tool.util.`fun`.bgColorTint
import sidev.lib.android.std.tool.util.`fun`.get
import sidev.lib.android.std.tool.util.`fun`.toast
import sidev.lib.check.notNull
import sidev.lib.implementation.R
import sidev.lib.implementation.util.Const

class TxtFrag(val msg: String): Frag() {
    constructor(): this("txt")

    override val layoutId: Int = R.layout.frag_txt

    private var colorInt= -1

    /**
     * Mirip dg [_initData(intent)], namun parameter yg di-pass adalah [Fragment.getArguments].
     * Untuk case [SingleFragActBase], [arguments] merupakan gabungan dari [Fragment.getArguments] + [Intent.getExtras].
     * [arguments] jadi `null` jika [Fragment.getArguments] atau gabungannya dg [Intent.getExtras] `null` atau jumlahnya 0.
     */
    override fun _initAllData(arguments: Bundle?) {
        arguments?.get<Int>(Const.DATA_COLOR).notNull { colorInt = it * 0x555555 }
    }

    override fun _initView(layoutView: View) {
        layoutView.apply {
            tv.text= msg
            //Color.RED
            //if(colorInt >= 0)
//                rl.bgColorTint = colorInt
            toast("colorInt= $colorInt")
        }
    }
}