package sidev.lib.implementation.frag

import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.page_rv_btn.view.*
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.support.v4.sendSMS
import sidev.lib.android.siframe.arch.intent_state.IntentConverter
import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.arch.intent_state.StateProcessor
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.ViewModelBase
import sidev.lib.android.siframe.lifecycle.fragment.mvi.MviFrag
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.android.siframe.tool.util.`fun`.toast
import sidev.lib.implementation.R
import sidev.lib.implementation.adp.ContentAdp
import sidev.lib.implementation.intent_state.ContentFragIntent
import sidev.lib.implementation.presenter.ContentPresenter
import sidev.lib.implementation.intent_state.ContentFragState
import sidev.lib.implementation.intent_state.processor.ContentFragIntentConverter
import sidev.lib.implementation.intent_state.processor.ContentFragStatePros
import sidev.lib.implementation.presenter.MviContentPresenter

class ContentMviFrag : MviFrag<ContentFragState, ContentFragIntent>(){
//    override val vmBase: ViewModelBase= this
    override val layoutId: Int
        get() = R.layout.page_rv_btn
    override val isInterruptable: Boolean
        get() = false

    lateinit var rvAdp: ContentAdp

    override fun initPresenter(): Presenter? = MviContentPresenter(null)
    override fun initStateProcessor(): StateProcessor<ContentFragState, ContentFragIntent>?
            = ContentFragStatePros(this)
/*
    override fun initIntentCoverter(presenter: Presenter): IntentConverter<ContentFragIntent>?
        = ContentFragIntentConverter(null, null)
 */

    override fun _initView(layoutView: View) {
        rvAdp= ContentAdp(context!!)
        rvAdp.rv= layoutView.rv

        registerAutoRestoreView("et", layoutView.et)
        registerAutoRestoreView("iv", layoutView.iv)

        (layoutView.btn as Button).text= "Login"
        layoutView.btn.setOnClickListener { sendRequest(ContentFragIntent.Login("kinap oy")) }
        layoutView.srl.setOnRefreshListener { sendRequest(ContentFragIntent.DownloadData) }
    }

    override fun onNoCurrentState() {
        sendRequest(ContentFragIntent.DownloadData)
        layoutView.iv.imageResource= R.drawable.ic_arrow_thick
    }

    override fun onInterruptedWhenBusy() {
        toast("Harap tunggu hingga proses pada layar selesai.")
    }

    override fun render(state: ContentFragState) {
        when(state){
            is ContentFragState.DownloadData -> {
                layoutView.rv.visibility= if(state.isLoading) View.GONE
                    else View.VISIBLE
                layoutView.tv_no_data.visibility= if(state.isLoading) View.GONE
                    else View.VISIBLE
                layoutView.pb.visibility= if(state.isLoading
                            && !state.isError) View.VISIBLE
                    else View.GONE

                layoutView.srl.isRefreshing= state.isLoading
                        && !state.isError

                if(!state.isPreState){
                    layoutView.tv_no_data.visibility=
                        if(state.rvDataList.isNullOrEmpty()) View.VISIBLE
                        else View.GONE

                    rvAdp.dataList= state.rvDataList
                }
            }
            is ContentFragState.Login -> {
                loge("render() isPreState= ${state.isPreState} state.isError= ${state.isError}")
                layoutView.srl.isRefreshing= state.isLoading
                        && !state.isError
                if(!state.isPreState && !state.isError){
                    loge("render() !!isPreState TOAST state.toastMsg!!= ${state.toastMsg!!}")
                    if(!state.isSucces)
                        toast(state.toastMsg!!)
                    else
                        toast("Apapun alasannya, yg penting login berhasil")
                }
            }
        }
        if(state.isError)
            toast(state.errorMsg!!)
//        state.isPreState= false
    }
}