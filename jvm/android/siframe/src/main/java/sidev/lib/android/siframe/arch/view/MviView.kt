package sidev.lib.android.siframe.arch.view

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import sidev.lib.android.siframe.arch._obj.InternalFiewModel
import sidev.lib.android.siframe.arch.intent_state.ViewState
import sidev.lib.android.siframe.arch.intent_state.StateProcessor
import sidev.lib.android.siframe.arch.intent_state.ViewIntent
import sidev.lib.android.siframe.arch.presenter.InteractivePresenterDependent
import sidev.lib.android.siframe.arch.presenter.MviPresenter
import sidev.lib.android.siframe.arch.type.Mvi
import sidev.lib.android.siframe.intfc.lifecycle.ExpirableBase
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.ViewModelBase
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.universal.`fun`.*
import java.lang.Exception

interface MviView<S: ViewState, I: ViewIntent>: ArchView, Mvi,
    InteractivePresenterDependent<MviPresenter<S, I>, I>,
    AutoRestoreViewClient,
    ExpirableBase {
    companion object{
//        val KEY_VM_MVI_STATE_PROS= "_internal_vm_mvi_state_pros" -> StateProcessor disimpan pada referensi presenter.
        val KEY_VM_MVI_STATE_PRESENTER= "_internal_vm_mvi_state_presenter"
//        val KEY_VM_MVI_STATE= "_internal_vm_mvi_state"
//        val KEY_VM_MVI_STATE_STATUS= "_internal_vm_mvi_state_status"
    }
    var currentViewState: S?
///    override var intentConverter: IntentConverter<I>?
    override fun initPresenter(): MviPresenter<S, I>?
    fun initStateProcessor(): StateProcessor<S, I>?
/*
    /**
     * Berguna jika programmer ingin memakai pendekatan MVI murni, yaitu pass intent
     * sbg turunan kelas ViewIntent, bkn string.
     */
    fun initIntentCoverter(presenter: Presenter): IntentConverter<I>? = null
 */

    fun __initMviPresenter(): MviPresenter<S, I>?{
        return getVmData()
            ?: initPresenter().notNullTo { presenter ->
            initStateProcessor().notNull { stateProcessor ->
                stateProcessor.view= this
                presenter.callback= stateProcessor
/*
                initIntentCoverter(presenter).notNull { intentConverter ->
                    this.intentConverter= intentConverter
                    intentConverter.expirableView= this
                    intentConverter.presenter= presenter
                    intentConverter.stateProcessor= stateProcessor
//                    loge("statePros is changed MviView")
//                    stateProcessor.intentConverter= intentConverter
                }
 */

                //<5 Juli 2020> => Yg disimpan adalah Presenternya karena sudah mencakup
                // referensi ke StateProcessor.
                // Prioritas Activity dulu. Jika ternyata null, baru Fragment.
                (this.asNotNullTo { act: Activity -> act }
                    ?: this.asNotNullTo { frag: Fragment -> frag.activity ?: frag })
                    .asNotNullTo { view: ViewModelBase ->
                        view.getViewModel(InternalFiewModel::class.java)
                    }.notNull { vm ->
                        vm.addValue(KEY_VM_MVI_STATE_PRESENTER, presenter)
                    }
/*

                //Yg diambil adalah ViewModel milik Activity.
                act.asNotNullTo { view: ViewModelBase ->
                    view.getViewModel(MviFiewModel::class.java)
                }.notNull { vm ->
                    //Jika kelas ini merupakan ViewModelBase, maka ambil dulu StateProcessor
                    // yg telah disimpan di dalam MviFiewModel.
                    // Knp StateProcessor nya disimpan di FiewModel?
                    // Agar dapat StateProcessor.restoreCurrentState() dapat dipanggil
                    // walau View di-recreate.
                    // <3 Juli 2020> => Yg disimpan di VM adalah State dan State Status (isPreState), bkn StateProcessor.
                    // <5 Juli 2020> => Yg disimpan adalah Presenternya karena sudah mencakup referensi ke StateProcessor.
                    loge("MviView.__initMviPresenter() > act is ViewModelBase")
                    vm.get<S>(KEY_VM_MVI_STATE) //.observe<StateProcessor<S>?>(KEY_MVI_VM){}
                        .notNull { stateLifeData ->
                            stateProcessor.currentState= stateLifeData
                            loge("MviView.__initMviPresenter() > act is ViewModelBase notNull")
/*
                            if(stateLifeData.value != null){
//                                stateProcessor.currentState= stateLifeData.value!!
//                                presenter.callback= stateLifeData.value
//                                stateLifeData.value!!.view= this //jangan lupa assign kembali view stelah recreate.
                            } else{
                                initStateProcessor().notNull { stateProcessor ->
                                    presenter.callback= stateProcessor
                                    stateLifeData.value= stateProcessor
                                }
                            }
 */
                        }.isNull {
                            vm.addLifeData(KEY_VM_MVI_STATE, stateProcessor.currentState)
                            loge("MviView.__initMviPresenter() > act is ViewModelBase isNull")
                        }
                    loge("MviView.__initMviPresenter() > act is ViewModelBase vm.get<S>($KEY_VM_MVI_STATE) SELESAI")
                    vm.get<Boolean>(KEY_VM_MVI_STATE_STATUS)
                        .notNull { stateStatusLifeData ->
                            stateProcessor.currentStateIsPreState= stateStatusLifeData
                            loge("MviView.__initMviPresenter() > act is ViewModelBase notNull")
                        }.isNull {
                            vm.addLifeData(KEY_VM_MVI_STATE_STATUS, stateProcessor.currentStateIsPreState)
                            loge("MviView.__initMviPresenter() > act is ViewModelBase isNull")
                        }
//                    loge("MviView.__initMviPresenter() > act is ViewModelBase vm.get<Boolean>($KEY_VM_MVI_STATE_STATUS) SELESAI")
                }
 */
            }
            presenter
        }
    }

    private fun getVmData(): MviPresenter<S, I>?{
        // Prioritas Activity dulu. Jika ternyata null, baru Fragment.
        return (this.asNotNullTo { act: Activity -> act }
            ?: this.asNotNullTo { frag: Fragment -> frag.activity ?: frag})
            .asNotNullTo { view: ViewModelBase ->
                view.getViewModel(InternalFiewModel::class.java)
            }.notNullTo { vm ->
                vm.get<MviPresenter<S, I>>(KEY_VM_MVI_STATE_PRESENTER)
                    .notNullTo { presenter ->
                        presenter.value?.callback.asNotNull { sp: StateProcessor<S, I> ->
                            sp.view= this
//                            intentConverter= sp.intentConverter aa
                        }
                        presenter.value
                    }
    //                    presenter.value?.callback= stateProcessor -> gak perlu karena yg di-recreate adalah view-nya saja.
            }
    }

    /**
     * Sesuai namanya, fungsi ini berfungsi untuk mengembalikan interface [MviView] ini
     * ke state yg ada.
     *
     * Fungsi ini berguna saat interface [MviView] di-recreate sehingga harus mengembalikan state
     * sebelumnya yg aktif.
     *
     * @return true jika berhasil me-revert ke state sebelumnya.
     *          Jika berhasil, maka harus memenuhi bbrp kondisi:
     *              1. Presenter harus merupakan MviPresenter<S>
     *              2. View ini memiliki current state pada MviPresenter<S>
     *
     * @param [isInit] true jika fungsi [restoreCurrentState] ini dipanggil pertama kali saat
     * interface [MviView] ini di-create.
     */
    fun restoreCurrentState(isInit: Boolean= false): Boolean{
        val isSuccess=
            presenter?.callback.asNotNullTo { stateProcessor: StateProcessor<S, I> ->
                try{
                    stateProcessor.restoreCurrentState()
                    true
                } catch (e: Exception){
                    false
                }
            } ?: false

        if(isInit && currentViewState == null)
            onNoCurrentState()

        return isSuccess
    }

    override fun registerAutoRestoreView(id: String, v: View) {
        loge("registerAutoRestoreView() mulai")
        presenter?.callback.asNotNull { statePros: StateProcessor<*, *> ->
            loge("presenter?.callback as StateProcessor")
            statePros.registerAutoRestoreView(id, v)
        }
    }

    /**
     * Fungsi dipanggil sesaat sebelum view dihancurkan atau saat onDestroy
     */
    override fun extractAllViewContent() {
        loge("extractAllViewContent() mulai")

        val calname= presenter?.callback?.classSimpleName()
        val calnull= presenter?.callback == null
        loge("calnull= $calnull calname= $calname")

        presenter?.callback.asNotNull { statePros: StateProcessor<*, *> ->
            loge("presenter?.callback as StateProcessor")
            statePros.extractAllViewContent()
        }
    }

    /**
     * <4 Juli 2020> => parameter isPreState dihilangkan. Sbg gantinya, isPreState disimpan
     *   di dalam [state]. Tujuannya adalah agar lebih intuitif.
     */
    fun __render(state: S/*, isPreState: Boolean*/){
        isBusy= state.isPreState
        render(state)
        currentViewState= state
    }
    fun render(state: S/*, isPreState: Boolean*/)

    /**
     * Dipanggil saat interface [MviView] ini tidak punya [currentViewState].
     */
    fun onNoCurrentState()
}