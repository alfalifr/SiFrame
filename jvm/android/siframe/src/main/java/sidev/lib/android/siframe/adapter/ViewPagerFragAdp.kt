package sidev.lib.android.siframe.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import sidev.lib.android.siframe.lifecycle.fragment.SimpleAbsFrag
import java.util.ArrayList
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

class ViewPagerFragAdp(fm: FragmentManager, vararg items: SimpleAbsFrag) : FragmentPagerAdapter(fm) {
    var items: List<SimpleAbsFrag> = ArrayList()
        private set

    constructor(fm: FragmentManager, items: ArrayList<out SimpleAbsFrag>) : this(fm) {
        for(item in items)
            (this.items as ArrayList).add(item)
    }

    init {
        for(element in items)
            (this.items as ArrayList).add(element)
    }
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