package sidev.lib.implementation.frag

import android.util.SparseIntArray
import android.view.View
import androidx.core.util.set
import kotlinx.android.synthetic.main.frag_arrangeable.*
import sidev.lib.algo.insertionSort
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.std.tool.util.`fun`.asArrangeable
import sidev.lib.android.std.tool.util.`fun`.iterator
import sidev.lib.android.std.tool.util.`fun`.values
import sidev.lib.implementation.R

class ArrangeableFrag: Frag() {
    override val layoutId: Int
        get() = R.layout.frag_arrangeable

    override fun _initView(layoutView: View) {
        val sparseArr= SparseIntArray()
        sparseArr[0]= 3
        sparseArr[3]= 7
        sparseArr[5]= 2

        sparseArr.values

        val arr1= sparseArr.asArrangeable()
        arr1.insertionSort()
        var str= "arr1= "
        for((k, v) in sparseArr){
            str += "k= $k v= $v, "
        }
        tv1.text= str
    }
}