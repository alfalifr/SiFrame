package sidev.lib.android.siframe.view.tool.dialog

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sidev.lib.android.siframe.adapter.DialogListAdp
import sidev.lib.android.siframe._customizable._Config
import sidev.lib.android.siframe.model.StringId
import sidev.lib.android.siframe.tool.util._ViewUtil

class DialogListView(c: Context): DialogAbsView<DialogListView>(c){
    override val layoutId: Int
        get() = _Config.LAYOUT_DIALOG_LIST //R.layout.dialog_list

    private lateinit var adp: DialogListAdp //DialogListAdapter
    private lateinit var rv: RecyclerView
    private lateinit var autoTv: AppCompatAutoCompleteTextView
    private lateinit var btnActionContainer: RelativeLayout
/*
    var clickListener: DialogListAdapter.DialogListClickListener?= null
    var bindListener: DialogListAdapter.DialogListBindListener?= null
 */
    var filterListener: DialogListFilterListener?= null
    var btnListener: DialogListBtnListener?= null
/*
    var ivEnabled= false
        set(v){
            field= v
            adp.ivEnabled= v
        }
 */

    var noDataViewVis= false

    interface DialogListFilterListener{
        fun onFilterItemClick(v: View, pos: Int, data: String)
    }
    interface DialogListBtnListener{
        fun onRightBtnClick(dialog: DialogListView, v: View)
        fun onLeftBtnClick(dialog: DialogListView, v: View)
    }

    override fun initView(dialogView: View) {
//        layoutView= LayoutInflater.from(c).inflate(layoutId, null, false)
        adp= DialogListAdp(c, null)
//        adp.dialog= dialog
        rv= findView(_Config.ID_RV) //R.id.rv_list
        rv.adapter= adp
        rv.layoutManager= LinearLayoutManager(c)

        addTextWatcher(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adp.searchItem(s.toString())
            }
        })
/*
        autoTv= findView<View>(R.id.fill_filter)
            .findViewById(R.id.ed_fill_auto)
        autoTv.threshold= 1 //will start working from first character
 */

        btnActionContainer= findView(_Config.ID_RL_BTN_CONTAINER) //R.id.rl_btn_container
        val btnLeft= btnActionContainer.findViewById<Button>(_Config.ID_BTN_LEFT)
        val btnRight= btnActionContainer.findViewById<Button>(_Config.ID_BTN_RIGHT)

        _ViewUtil.Comp.setBtnHollow(btnLeft)
//        btnLeft.setBackgroundResource(R.drawable.shape_border_square_round_edge_main)
//        btnLeft.setTextColor(ContextCompat.getColor(c, R.color.colorPrimaryDark))
        btnLeft.setOnClickListener{v ->
            btnListener?.onLeftBtnClick(this, v)
        }

        _ViewUtil.Comp.setBtnSolid(btnRight)
/*
        btnActionContainer.btn_right.setBackgroundResource(R.drawable.shape_solid_square_round_edge_fill)
        btnActionContainer.btn_right.background.setColorFilter(
            ContextCompat.getColor(c, R.color.colorPrimaryDark),
            PorterDuff.Mode.SRC_ATOP
        )
        (btnActionContainer.btn_right as Button).setTextColor(ContextCompat.getColor(c, R.color.white))
 */
        btnRight.setOnClickListener{v ->
            btnListener?.onRightBtnClick(this, v)
        }

        setLeftBtnString("Batal")
        setRightBtnString("Pilih")
    }

    fun getListData(): ArrayList<StringId>?{
        return adp.dataList
    }
/*
    fun setItemClickListener(func: (v: View?, selected: Boolean, pos: Int, stringId: LabelId<T>, item: MenuItem?) -> Unit): DialogListView<T> {
        clickListener= object: DialogListAdp<T>.DialogListClickListener{
            override fun onItemClick(v: View?, selected: Boolean, pos: Int, stringId: StringId, item: MenuItem?) {
                func(v, selected, pos, stringId, item)
                if(dialogCancelOnCLick)
                    cancel()
            }
        }
        adp.clickListener= this.clickListener
        return this
    }

    fun setItemBindListener(func: (v: View?, selected: Boolean, pos: Int, stringId: StringId) -> Unit): DialogListView {
        bindListener= object: DialogListAdp<T>.DialogListBindListener{
            override fun onItemSelected(v: View?, selected: Boolean, pos: Int, stringId: StringId) {
                func(v, selected, pos, stringId)
            }
        }
        adp.bindListener= this.bindListener
        return this
    }
 */

    fun setFilterClickListener(func: (v: View?, pos: Int, data: String) -> Unit): DialogListView {
        filterListener= object: DialogListFilterListener {
            override fun onFilterItemClick(v: View, pos: Int, data: String) {
                func(v, pos, data)
            }
        }
        return this
    }

    fun setOnItemClickListener(func: ((v: View?, pos: Int, data: StringId) -> Unit)?){
        adp.setOnItemClickListener { v, pos, data ->
            func?.invoke(v, pos, data)
        }
    }


    fun selectItem(pos: Int): DialogListView {
        adp.selectItem(pos)
        return this
    }
/*
    <2 Juni 2020> <Selesai:0>
    fun updateSelectedItem(selectedData: ArrayList<String>){
        adp//.updateSelectedItem(selectedData)
    }

    fun setMenu(menuId: Int): DialogListView {
        adp.setMenu(menuId)
        return this
    }
 */

    fun setSearchBarVisible(visible: Boolean): DialogListView {
        val vis= if(visible) View.VISIBLE
        else View.GONE
        layoutView.findViewById<View>(_Config.ID_LL_SEARCH_CONTAINER_OUTER).visibility= vis //.ll_search_container_outer.visibility= vis
        return this
    }
/*
    <2 Juni 2020> <Selesai:0>
    fun setFilterBarVisible(visible: Boolean): DialogListView {
        val vis= if(visible) View.VISIBLE
        else View.GONE
        layoutView.ll_filter_container.visibility= vis
        return this
    }
 */

    fun setNoDataViewVisible(visible: Boolean): DialogListView {
        val vis= if(visible) View.VISIBLE
        else View.GONE
        findView<View>(_Config.ID_TV_NO_DATA).visibility= vis //R.id.tv_no_data
        noDataViewVis= visible
        return this
    }

    fun setNoDataString(str: String): DialogListView {
        findView<TextView>(_Config.ID_TV_NO_DATA).text= str
        return this
    }

    fun showtBtnAction(show: Boolean= true): DialogListView {
        val vis= if(show) View.VISIBLE
        else View.GONE
        findView<View>(_Config.ID_RL_BTN_CONTAINER) //R.id.rl_btn_container
            .visibility= vis
        return this
    }

    fun setRightBtnString(str: String): DialogListView {
        btnActionContainer.findViewById<Button>(_Config.ID_BTN_RIGHT).text= str
        return this
    }
    fun setLeftBtnString(str: String): DialogListView {
        btnActionContainer.findViewById<Button>(_Config.ID_BTN_LEFT).text= str
        return this
    }

    fun updateData(data: ArrayList<StringId>?){
//        adp.updateData(data, true)
        adp.dataList= data
        setNoDataViewVisible(noDataViewVis && data.isNullOrEmpty())
    }
/*
    <2 Juni 2020> <Selesai:0>
    fun <T2> updateDataOfGeneric(data: Collection<T2>,
                                transformation: (T2) -> LabelId<T>){
        adp.updateDataOfGeneric(data, true, transformation = transformation)
        setNoDataViewVisible(noDataViewVis && data.isNullOrEmpty())
    }
    fun <T> updateDataOfGeneric(data: Array<T>,
                                transformation: (T) -> StringId){
        adp.updateDataOfGeneric(data, true, transformation = transformation)
        setNoDataViewVisible(noDataViewVis && data.isNullOrEmpty())
    }

    fun updateFilterData(data: ArrayList<String>?){ //: AppCompatAutoCompleteTextView
        val adapter=
            if(data != null) ArrayAdapter<String>(c, android.R.layout.select_dialog_item, data)
            else null
        autoTv.setAdapter(adapter)
        autoTv.setOnItemClickListener { parent, view, position, id ->
            filterListener?.onFilterItemClick(view, position, data!![position])
        }
//        (this, android.R.layout.select_dialog_item, fruits);
    }
 */

    fun addTextWatcher(tw: TextWatcher){
        layoutView.findViewById<EditText>(_Config.ID_ED_SEARCH) //R.id.ed_search
            .addTextChangedListener(tw)
    }
}

/*
{
    lateinit var dialog: AlertDialog
    lateinit var adp: DialogListAdapter
    lateinit var rv: RecyclerView
=======
class DialogListView(val c: Context){
    var dialog: AlertDialog
    var adp: DialogListAdapter
    var rv: RecyclerView
>>>>>>> 7abb3fb3e521b17325fd051609efdfb3870219f7
    val layoutId= R.layout.dialog_list
    val layoutView: View

    private var formatter: DialogListAdapter.DialogListListener?= null

    init{
        layoutView= LayoutInflater.from(c).inflate(layoutId, null, false)
        dialog= AlertDialog.Builder(c)
            .setView(layoutView)
            .setCancelable(true)
            .create()
        dialog.window
            .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        adp= DialogListAdapter(c, null, formatter)
        adp.dialog= dialog
        rv= layoutView.findViewById(R.id.rv_list)
        rv.adapter= adp
        rv.layoutManager= LinearLayoutManager(c)

        addTextWatcher(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
              //  Toast.makeText(c, "sedang search sekarang", Toast.LENGTH_SHORT).show()
                adp.search(s.toString())
            }
        })
    }

    fun setItemClickListener(func: (v: View?, pos: Int, data: String, item: MenuItem?) -> Unit){
        formatter= object: DialogListAdapter.DialogListListener{
            override fun onItemClick(v: View?, pos: Int, data: String, item: MenuItem?) {
                func(v, pos, data, item)
            }
        }
        adp.formatter= this.formatter
    }

    fun setMenu(menuId: Int){
        adp.setMenu(menuId)
    }

    fun setTitle(title: String){
        layoutView.tv_title.text= title
    }

    fun setTitleVisible(visible: Boolean){
        val vis= if(visible) View.VISIBLE
            else View.GONE
        layoutView.tv_title.visibility= vis
    }

    fun setSearchBarVisible(visible: Boolean){
        val vis= if(visible) View.VISIBLE
        else View.GONE
        layoutView.ll_search_container.visibility= vis
    }

    fun updateData(data: ArrayList<String>?){
        adp.updateData(data, true)
    }

    fun show(){
        dialog.show()
    }

    fun cancel(){
        dialog.cancel()
    }

    fun addTextWatcher(tw: TextWatcher){
        layoutView.findViewById<EditText>(R.id.ed_search)
            .addTextChangedListener(tw)
    }
}*/