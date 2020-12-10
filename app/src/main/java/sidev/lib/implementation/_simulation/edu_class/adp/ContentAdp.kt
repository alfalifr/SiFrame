package sidev.lib.implementation._simulation.edu_class.adp

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.SparseArray
import android.util.SparseIntArray
import android.view.View
import android.widget.*
import androidx.core.net.toUri
import androidx.core.util.containsKey
import androidx.core.util.set
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main._simul_edu_comp_question.view.*
import kotlinx.android.synthetic.main._simul_edu_comp_read.view.*
import kotlinx.android.synthetic.main._simul_edu_comp_video.view.*
import sidev.kuliah.tekber.edu_class.model.ContentQuestion
import sidev.kuliah.tekber.edu_class.model.ContentRead
import sidev.kuliah.tekber.edu_class.model.ContentVideo
import sidev.lib.android.siframe.adapter.RvMultiViewAdp
import sidev.lib.android.siframe.tool.util._SIF_ViewUtil.Comp.getTvNote
import sidev.lib.android.std.`val`._Config
import sidev.lib.android.std.tool.util._ViewUtil
import sidev.lib.android.std.tool.util._ViewUtil.setColorTintRes
import sidev.lib.android.std.tool.util.`fun`.*
import sidev.lib.check.*
import sidev.lib.implementation.R
import sidev.lib.implementation._simulation.edu_class.model.Content
import sidev.lib.implementation._simulation.edu_class.util.Edu_Class_Const
import sidev.lib.implementation._simulation.edu_class.util.Edu_Class_ViewUtil.Comp.enableEd
import java.lang.Exception

class ContentAdp(c: Context, data: ArrayList<Content<*>>?)
    : RvMultiViewAdp<Content<*>, LinearLayoutManager>(c, data){

    val rvCheckBoxList= SparseArray<QuestionCheckBoxAdp>()
    val radioSelIndexList= SparseArray<Int>()
    val radioViewList= SparseArray<List<RadioButton>>()
    var isEditable= false
        set(v){
            field= v
            notifyDataSetChanged_()
        }
    val screenWidth= _ViewUtil.getScreenWidth(c) //if(c is Activity) _ViewUtil.getScreenWidth(c)
    //else ViewGroup.LayoutParams.MATCH_PARENT

    private val questionIndex= SparseIntArray()
    var isQuestionNumberShown= true
        set(v){
            field= v
            notifyDataSetChanged_()
        }

    init{
        val cName= c.javaClass.simpleName //.classSimpleName()
        loge("init{} cName= $cName")
    }

    override fun getItemViewType(pos: Int, data: Content<*>): Int {
        return when(data){
            is ContentRead -> R.layout._simul_edu_comp_read
            is ContentVideo -> R.layout._simul_edu_comp_video
            is ContentQuestion -> {
                if(!questionIndex.containsKey(pos))
                    questionIndex[pos]= questionIndex.size() +1
                R.layout._simul_edu_comp_question
            }
            else -> _Config.INT_EMPTY
        }
    }

    override fun bindVhMulti(vh: SimpleViewHolder, pos: Int, viewType: Int, data: Content<*>) {
        val v= vh.itemView
//        val parName= v.parent?.classSimpleName()
//        loge("bindVhMulti() parName= $parName")
/*
        v.findViewById<View>(_Config.ID_VG_CONTENT_CONTAINER).notNull { container ->
            container.layoutParams.width= screenWidth
//            loge("bindVhMulti() setting parent width to MATCH_PARENT screenWidth= $screenWidth")
        }
// */
        when(data){
            is ContentRead -> {
                val titleVis= if(data.title != null){
                    (v.et_title as EditText).txt= data.title!! //.setText()
                    View.VISIBLE
                } else
                    View.GONE
                v.et_title.visibility= titleVis
                (v.et_desc as EditText).setText(data.desc)
                enableEd(v.et_title, isEditable)
                enableEd(v.et_desc, isEditable)
                loge("bindVhMulti() ContentRead isEditable= $isEditable")
            }
            is ContentVideo -> {
                loge("video url= ${data.link}")
                val mc= MediaController(ctx)
                mc.setAnchorView(v.vv)
                v.vv.setMediaController(mc)
                v.vv.setVideoURI(data.link.toUri())

                v.pb.visibility= View.VISIBLE
                v.iv_play.visibility= View.GONE
                setColorTintRes(v.iv_play, R.color.putihTerawang)

                v.vv.setOnPreparedListener { mp ->
                    var isPlaying= false
                    v.pb.visibility= View.GONE
                    v.iv_play.visibility= View.VISIBLE
                    setColorTintRes(v.iv_play, R.color.putihTerawang)
                    v.iv_play.setOnClickListener {
                        if(!isPlaying){
                            mp.start()
                            it.visibility= View.GONE
                            isPlaying= true
                        } else {
                            mp.pause()
                            it.visibility= View.VISIBLE
                            isPlaying= false
                        }
                    }
                }

                v.vv.setOnErrorListener { mp, what, extra ->
                    v.pb.visibility= View.GONE
                    v.iv_play.setImageResource(R.drawable.ic_cross)
                    setColorTintRes(v.iv_play, R.color.merah)
                    true
                }

                getTvNote?.invoke(v).notNull { tv ->
                    if(data.note != null){
                        tv.text= data.note!!
                        tv.visibility= View.VISIBLE
                    } else
                        tv.visibility= View.GONE
                }
            }
            is ContentQuestion -> {
                val no=
                    if(isQuestionNumberShown) "${questionIndex[pos]}. "
                    else ""
                (v.tv_question as TextView).text= "$no${data.question}"

                v.cv_text_container.visibility= View.GONE
                v.checkbox_container.visibility= View.GONE
                v.rg_container.visibility= View.GONE

                if(data.answerByReader == null)
                    data.answerByReader= ArrayList()

                when(data.answerKind){
                    Edu_Class_Const.QUESTION_KIND_FILL -> {
                        v.cv_text_container.visibility= View.VISIBLE
                        v.cv_text_container.et_fill.clearTextChangedListener()//.removeAllTextChangedListener()

                        if(data.answerByReader!!.isEmpty())
                            data.answerByReader!!.add("")
                        else
                            v.cv_text_container.et_fill.setText(data.answerByReader!!.first())

//                        vh.setIsRecyclable(false) //Karena akan merepotkan jika vh di posisi ini bisa di recycle karena ada TextWatcher nya. !!!
                        v.cv_text_container.et_fill.addTextChangedListener(object : TextWatcher{
                            override fun afterTextChanged(s: Editable?) {
                                data.answerByReader!![0]= s.toString()
                            }
                            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                        })
                    }
                    Edu_Class_Const.QUESTION_KIND_MULTIPLE -> {
                        v.checkbox_container.visibility= View.VISIBLE
                        rvCheckBoxList[pos].notNull { adp ->
                            adp.rv= v.checkbox_container
                            adp.rv!!.isNestedScrollingEnabled= false
                            //Gak usah bind adp.cbCheckedList di sini karena adp udah disimpan di list.
                        }.isNull {
                            val adp= QuestionCheckBoxAdp(ctx, data.answerChoice)
                            adp.rv= v.checkbox_container
                            adp.rv!!.isNestedScrollingEnabled= false
                            rvCheckBoxList[pos]= adp

                            if(data.answerChoice != null && data.answerByReader!!.isEmpty())
                                for(i in data.answerChoice!!.indices)
                                    data.answerByReader!!.add("")
                            else if(data.answerByReader!!.isNotEmpty()){
                                for(perData in data.answerByReader!!)
                                    if(perData.isNotBlank())
                                        adp.cbCheckedList[perData.toInt()]= true
                            }
                            adp.onCheckedChangeListener= { isChecked, pos ->
                                data.answerByReader!![pos]=
                                    if(isChecked) pos.toString()
                                    else ""
                            }
                        }
                    }
                    Edu_Class_Const.QUESTION_KIND_PILGAN -> {
                        v.rg_container.visibility= View.VISIBLE
                        fillRadioView(v.rg_container, pos, data)
                    }
                }
            }

        }
    }

    override fun setupLayoutManager(context: Context): LinearLayoutManager = LinearLayoutManager(context)

    /**
     * @return map [question.id, question.answerByReader] di mana question.answerByReader yg berupa string kosong (")
     *          sudah dihilangkan.
     */
    fun getAnswerList(): Map<String, List<String>>{
        //jika pembaca tidak menjawab, maka value dari map memiliki size == 0
        //map [key, value] => [question.id, question.answerByReader]
        val map= HashMap<String, ArrayList<String>>()
        for((i, no) in questionIndex){
            val quest= dataList!![i] as ContentQuestion
            val questId= quest.id
//            loge("questId= $questId quest.question= ${quest.question}")
            when(quest.answerKind){
                Edu_Class_Const.QUESTION_KIND_PILGAN -> {
                    val answer= ArrayList<String>()
                    radioSelIndexList[i].notNull { ind -> answer.add(ind.toString()) }
                    map[questId]= answer
                }
                Edu_Class_Const.QUESTION_KIND_FILL -> {
                    if(quest.answerByReader!!.first().isBlank())
                        quest.answerByReader!!.clear()
                    map[questId]= quest.answerByReader!!
                }
                Edu_Class_Const.QUESTION_KIND_MULTIPLE -> {
                    val removedInd= ArrayList<Int>()
                    for((i, answer) in quest.answerByReader!!.withIndex()){
                        if(answer.isBlank())
                            removedInd.add(i)
                    }
                    val newList= ArrayList<String>() //jika pakai quest.answerByReader!!.removeAt(i), maka nti akan ada IndexOutOfBoundError
                    for((i, answer) in quest.answerByReader!!.withIndex()){
                        if(removedInd.indexOf(i) < 0)
                            newList.add(answer)
                    }
                    quest.answerByReader= newList
                    map[questId]= quest.answerByReader!!
                }
            }
        }
        return map
    }
    /**
     * Isi fungsi ini mirip dengan fungsi getAnswerList() karena pada dasarnya,
     * cara kerjanya sama-sama menggunakan iterasi questionIndex
     *
     * @return true jika pertanyaan sudah diisi semua dan sebaliknya.
     */
    fun checkAnswer(): Boolean{
        //jika pembaca tidak menjawab, maka value dari map memiliki size == 0
        //map [key, value] => [question.id, question.answerByReader]
        for((i, no) in questionIndex){
            val quest= dataList!![i] as ContentQuestion
            when(quest.answerKind){
                Edu_Class_Const.QUESTION_KIND_PILGAN -> {
                    val isFilled= radioSelIndexList[i]
                        .notNullTo { true }
                        .orDefault(false)
                    if(!isFilled) return false
                }
                Edu_Class_Const.QUESTION_KIND_FILL -> {
                    if(quest.answerByReader!!.first().isBlank())
                        return false
                }
                Edu_Class_Const.QUESTION_KIND_MULTIPLE -> {
                    var isAllBlank= true
                    for(answer in quest.answerByReader!!){
                        if(answer.isNotBlank()){
                            isAllBlank= false
                            break
                        }
                    }
                    if(isAllBlank) return false
                }
            }
        }
        return true
    }

    fun clearAllAnswer(){
        dataList.notNull { list ->
            for((i, data) in list.withIndex())
                if(data is ContentQuestion){
                    data.answerByReader= ArrayList() //Knp gak null? agar tidak terjadi error saat bindVh()
                    radioSelIndexList.removeAt(i)
                }
        }
    }

    private fun fillRadioView(rg: RadioGroup, pos: Int, data: ContentQuestion){
        rg.removeAllViews()
        rg.setOnCheckedChangeListener(null)
        radioViewList[pos].notNull {
            for(view in it){
                view.detachFromParent()
                rg.addView(view)
            }

            val childInd= radioSelIndexList[pos].notNullTo { it }
                .isNullTo {
                    try{ data.answerByReader!!.first().toInt() }
                    catch (e: Exception){null}
                }
            if(childInd != null)
                rg.getChildAt(childInd).asNotNull { rb: RadioButton -> rb.isChecked= true }
        }.isNull {
            data.answerChoice.notNull { choiceList ->
                val list= ArrayList<RadioButton>()
                for(choice in choiceList){
                    ctx.inflate(R.layout._simul_edu_v_rb, rg).notNull { choiceV ->
                        (choiceV as RadioButton).text= choice
                        rg.addView(choiceV)
                        list.add(choiceV)
                    }
                }
                data.answerByReader.notNull { ansList ->
                    try{
                        val checkedInd= ansList.first().toInt()
                        list[checkedInd].isChecked= true
                    } catch (e: Exception){}
                }
                radioViewList[pos]= list
            }
        }
        rg.setOnCheckedChangeListener { group, checkedId ->
            val ind= group.selectedInd //.getSelectedInd()
            if(ind >= 0){
                radioSelIndexList[pos]= ind
                data.answerByReader.notNull { arrList ->
                    if(arrList.isEmpty()) arrList.add(ind.toString())
                    else arrList[0]= ind.toString()
                }
            }
        }
    }
}