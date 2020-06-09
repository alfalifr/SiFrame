package sidev.lib.android.siframe.intfc.adp

import android.database.DataSetObserver
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter

/**
 * Interface dasar yg menunjukan bahwa suatu kelas adalah Adapter pada framework ini.
 */
interface Adp{
    fun isEmpty(): Boolean= getItemCount() == 0
//    fun getView(position: Int, convertView: View?, parent: ViewGroup?): View
    fun getView(pos: Int): View?
    fun getItem(pos: Int): Any?
    fun getItemCount(): Int

    fun getItemViewType(pos: Int): Int = 0
    fun getViewTypeCount(): Int = 1
    fun getItemId(pos: Int): Long = pos.toLong()

//    override fun registerDataSetObserver(observer: DataSetObserver?) {}
//    override fun unregisterDataSetObserver(observer: DataSetObserver?) {}
//    override fun hasStableIds(): Boolean = true
}