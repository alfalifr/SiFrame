package sidev.lib.android.siframe.adapter

import android.view.View
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class TabLayoutAdp(tabLayout: TabLayout?, titleList: ArrayList<String>?){
    private var isDataEditedDirectly= false
    private var isTabLayoutChangeFirst= false
    private var isViewPagerChangeFirst= false

    var titleList: ArrayList<String>? = null
        set(v){
            field= v
            if(isDataEditedDirectly){
                isDataEditedDirectly= true
                updateTitle(*field?.toTypedArray() ?: arrayOf())
                isDataEditedDirectly= false
            }
        }
    var tabLayout: TabLayout?= null
        set(v){
            field= v
            attachTitleToTabLayout()
        }

    init{
        this.titleList= titleList
        this.tabLayout= tabLayout
    }

    fun updateTitle(vararg title: String){
        val list= ArrayList<String>()
        for(perTitle in title)
            list.add(perTitle)
        if(!isDataEditedDirectly)
            titleList= list
        attachTitleToTabLayout()
    }

    private fun attachTitleToTabLayout(){
        if(tabLayout != null){
//            tabLayout!!.removeAllTabs()
            if(titleList != null)
                for((i, title) in titleList!!.withIndex()){
                    val tab= if(i < tabLayout!!.tabCount) tabLayout!!.getTabAt(i)!!
                        else {
                            val tabInt= tabLayout!!.newTab()
                            tabLayout!!.addTab(tabInt)
                            tabInt
                        }
                    tab.text= title
                }
        }
    }

    fun setupWithViewPager(vp: ViewPager?){
        tabLayout?.setupWithViewPager(vp)
        attachTitleToTabLayout()
/*
        onItemSelectedListener= object: OnItemSelectedListener{
            override fun onSelectItem(containerView: View?, pos: Int, data: String) {

            }

            override fun onUnSelectItem(containerView: View?, pos: Int, data: String) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
        tabLayout?.setupWithViewPager()
        vp?.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                if(isTabLayoutChangeFirst)
                    tabLayout?.getTabAt(position)?.select()
            }

            override fun onPageSelected(position: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
 */
    }

    var onItemSelectedListener: OnItemSelectedListener?= null
        set(v){
            field= v
            tabLayout?.addOnTabSelectedListener(object : TabLayout.BaseOnTabSelectedListener<TabLayout.Tab>{
                override fun onTabReselected(p0: TabLayout.Tab?) {}

                override fun onTabUnselected(p0: TabLayout.Tab?) {
                    field?.onUnSelectItem(p0?.view, p0?.position ?: -1, p0?.text?.toString() ?: "")
                }

                override fun onTabSelected(p0: TabLayout.Tab?) {
                    field?.onSelectItem(p0?.view, p0?.position ?: -1, p0?.text?.toString() ?: "")
                }
            })
        }
     interface OnItemSelectedListener{
        fun onSelectItem(v: View?, pos: Int, data: String)
        fun onUnSelectItem(v: View?, pos: Int, data: String)
    }
}