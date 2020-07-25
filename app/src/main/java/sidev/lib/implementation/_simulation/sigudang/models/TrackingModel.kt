package com.sigudang.android.models

import java.io.Serializable

class TrackingModel(var total: Int, progres: Int= 0, diff: Int= total): Serializable{
//    var total: Int= total
    var progres: Int= progres
        set(v){
            field= v
            if(!isInternalEdit)
                internalEdit {
                    diff = total -v
                }
        }
    var diff: Int= diff
        set(v){
            field= v
            if(!isInternalEdit)
                internalEdit {
                    progres = total -v
                }
        }

    private var isInternalEdit= false
    private fun internalEdit(f: () -> Unit){
        isInternalEdit= true
        f()
        isInternalEdit= false
    }
}