package sidev.lib.implementation.frag

import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.page_rv_btn.view.*
import org.jetbrains.anko.imageResource
import sidev.lib.android.siframe.arch.intent_state.*
import sidev.lib.android.siframe.arch.presenter.MviPresenter
import sidev.lib.android.siframe.lifecycle.fragment.mvi.MviFrag
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.android.siframe.tool.util.`fun`.toast
import sidev.lib.implementation.R
import sidev.lib.implementation.adp.ContentAdp
import sidev.lib.implementation.intent_state.*
import sidev.lib.implementation.intent_state.processor.ContentFragStatePros
import sidev.lib.implementation.presenter.MviContentPresenter

class ContentMviFrag : MviFrag<CFIntent, CFRes, CFState<*>>(){

    //    override val vmBase: ViewModelBase= this
    override val layoutId: Int
        get() = R.layout.page_rv_btn
    override val isInterruptable: Boolean
        get() = false

    lateinit var rvAdp: ContentAdp


    override fun initPresenter(): MviPresenter<CFIntent, CFRes, CFState<*>>? = MviContentPresenter(null)
    override fun initStateProcessor(): StateProcessor<CFIntent, CFRes, CFState<*>>? = ContentFragStatePros(this)
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
        layoutView.btn.setOnClickListener { sendRequest(ContentFragConf.Intent.Login("kinap oy")) }
        layoutView.srl.setOnRefreshListener { sendRequest(ContentFragConf.Intent.DownloadData) }
    }

    override fun onNoCurrentState() {
        sendRequest(ContentFragConf.Intent.DownloadData)
        layoutView.iv.imageResource= R.drawable.ic_arrow_thick
    }

    override fun onInterruptedWhenBusy() {
        toast("Harap tunggu hingga proses pada layar selesai.")
    }

    override fun render(state: CFState<*>) {
        when(state){
            is ContentFragConf.State.DownloadData -> {
                layoutView.rv.visibility= if(state.isLoading) View.GONE
                else View.VISIBLE
                layoutView.tv_no_data.visibility= if(state.isLoading) View.GONE
                else View.VISIBLE
                layoutView.pb.visibility= if(state.isLoading
                    && state.error == null) View.VISIBLE
                else View.GONE

                layoutView.srl.isRefreshing= state.isLoading
                        && state.error == null

                if(!state.isPreState){
                    layoutView.tv_no_data.visibility=
                        if(state.result?.rvDataList.isNullOrEmpty()) View.VISIBLE
                        else View.GONE

                    rvAdp.dataList= state.result?.rvDataList
                }
            }
            is ContentFragConf.State.Login -> {
                loge("render() isPreState= ${state.isPreState} state.isError= ${state.error != null}")
                layoutView.srl.isRefreshing= state.isLoading
                        && state.error == null
                if(!state.isPreState && state.error == null){
                    loge("render() !!isPreState TOAST state.toastMsg!!= ${state.toastMsg!!}")
                    if(state.result?.isSuccess == false)
                        toast(state.toastMsg!!)
                    else
                        toast("Apapun alasannya, yg penting login berhasil")
                }
            }
        }
        if(state.error != null)
            toast(state.error!!.message!!)
//        state.isPreState= false
    }

/*
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
 */
}