package sidev.lib.android.siframe.adapter

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import sidev.lib.android.siframe.intfc.adp.Adp
import sidev.lib.android.siframe.lifecycle.fragment.SimpleAbsFrag
import sidev.lib.android.siframe.tool.util._ViewUtil
import sidev.lib.android.siframe.tool.util.`fun`.addView
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.universal.`fun`.asNotNull
import java.lang.Exception
/*
//class ViewPagerFragAdp (var fm: FragmentManager, vararg items: SimpleAbsFrag): PagerAdapter(){
class ViewPagerFragAdp (val c: Context, vararg items: SimpleAbsFrag): PagerAdapter(){
    var items: List<SimpleAbsFrag> = ArrayList()
        private set

    constructor(c: Context, items: ArrayList<out SimpleAbsFrag>) : this(c) {
        for(item in items)
            (this.items as ArrayList).add(item)
    }
/*
    constructor(fm: FragmentManager, items: ArrayList<out SimpleAbsFrag>) : this(fm) {
        for(item in items)
            (this.items as ArrayList).add(item)
    }
 */

    init {
        for(searchElement in items)
            (this.items as ArrayList).add(searchElement)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val containerView= items[position].inflateView(c, container, null)
//        items[0].context= c
        container.addView(containerView)
        return containerView
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun getPageTitle(pos: Int): CharSequence? {
        return items[pos].fragTitle
    }
}
 */

/**
 * Hanya sekali pake. Jika ingin update adapter, maka instantiate lagi kelas ini.
 */
class VpFragAdp(fm: FragmentManager, vararg items: SimpleAbsFrag) : FragmentStatePagerAdapter(fm), Adp {
    val items: ArrayList<SimpleAbsFrag> = ArrayList()
/*
    var items: ArrayList<SimpleAbsFrag>?= null// = ArrayList()
        set(v){
            field= v
            notifyDataSetChanged()
        }
 */

    init {
        for(element in items)
            this.items.add(element)
    }

    constructor(fm: FragmentManager, items: ArrayList<out SimpleAbsFrag>) : this(fm) {
        for(element in items)
            this.items.add(element)
    }

    override fun getItemId(pos: Int): Long = pos.toLong() //super<FragmentPagerAdapter>.getItemId(pos)
    override fun getItemCount(): Int= count
    override fun getView(pos: Int): View? {
        return try{ items[pos].layoutView }
        catch (e: Exception){ null }
    }

/*
    override fun instantiateItem(container: View, position: Int): Any {
        val any= super.instantiateItem(container, position) as View
        any.asNotNull { v: View ->
            loge("instantiateItem() any is View")
            v.parent.asNotNull { parent: ViewGroup ->

            }
        }
//        v.setPadding(v.paddingLeft, v.paddingTop +_ViewUtil.dpToPx(20f, v.context), v.paddingRight, v.paddingBottom)

        return any
    }
 */

/*
    override fun destroyItem(container: View, position: Int, `object`: Any) {
        (container as ViewGroup).removeView(`object` as View)
    }
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }
 */

    override fun getItem(position: Int): Fragment {
        return items[position]
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun getPageTitle(pos: Int): CharSequence? {
        return items[pos].fragTitle
    }
}