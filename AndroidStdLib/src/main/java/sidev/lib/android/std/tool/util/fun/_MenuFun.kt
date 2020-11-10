package sidev.lib.android.std.tool.util.`fun`

import android.view.Menu
import android.view.MenuItem
import androidx.core.view.forEachIndexed

fun MenuItem.getPosFrom(menu: Menu): Int{
    var pos= -1
    menu.forEachIndexed { i, item ->
        if(item.itemId == this.itemId){
            pos= i
            return@forEachIndexed
        }
    }
    return pos
}