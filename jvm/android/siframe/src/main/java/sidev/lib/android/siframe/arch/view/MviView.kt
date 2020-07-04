package sidev.lib.android.siframe.arch.view

import sidev.lib.android.siframe.arch._obj.MviFiewModel
import sidev.lib.android.siframe.arch.intent_state.IntentConverter
import sidev.lib.android.siframe.arch.presenter.MviPresenter
import sidev.lib.android.siframe.arch.intent_state.ViewState
import sidev.lib.android.siframe.arch.intent_state.StateProcessor
import sidev.lib.android.siframe.arch.intent_state.ViewIntent
import sidev.lib.android.siframe.arch.presenter.InteractivePresenterDependentCommon
import sidev.lib.android.siframe.arch.presenter.MviInteractivePresenterDependent
import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.arch.type.Mvi
import sidev.lib.android.siframe.intfc.lifecycle.ExpirableBase
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.ViewModelBase
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.universal.`fun`.*
import java.lang.Exception

interface MviView<S: ViewState, I: ViewIntent>: ArchView, Mvi,
    MviInteractivePresenterDependent<Presenter, I>, ExpirableBase {
    companion object{
        val KEY_VM_MVI_STATE= "_internal_vm_mvi_state"
        val KEY_VM_MVI_STATE_STATUS= "_internal_vm_mvi_state_status"
    }
    override var intentConverter: IntentConverter<I>?
    override fun initPresenter(): Presenter?
    fun initStateProcessor(): StateProcessor<S, I>?

    /**
     * Berguna jika programmer ingin memakai pendekatan MVI murni, yaitu pass intent
     * sbg turunan kelas ViewIntent, bkn string.
     */
    fun initIntentCoverter(presenter: Presenter): IntentConverter<I>? = null

    fun __initMviPresenter(): Presenter?{
        return initPresenter().notNullTo { presenter ->
            initStateProcessor().notNull { stateProcessor ->
                stateProcessor.view= this
                presenter.callback= stateProcessor

                initIntentCoverter(presenter).notNull { intentConverter ->
                    this.intentConverter= intentConverter
                    intentConverter.view= this
                    intentConverter.presenter= presenter
                    intentConverter.stateProcessor= stateProcessor
//                    stateProcessor.intentConverter= intentConverter
                }

                this.asNotNullTo { act: ViewModelBase ->
                    act.getViewModel(MviFiewModel::class.java)
                }.notNull { vm ->
                    //Jika kelas ini merupakan ViewModelBase, maka ambil dulu StateProcessor
                    // yg telah disimpan di dalam MviFiewModel.
                    // Knp StateProcessor nya disimpan di FiewModel?
                    // Agar dapat StateProcessor.restoreCurrentState() dapat dipanggil
                    // walau View di-recreate.
                    // <3 Juli 2020> => Yg disimpan di VM adalah State dan State Status (isPreState), bkn StateProcessor.
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
            }
            presenter
        }
    }

    /**
     * @return true jika berhasil me-revert ke state sebelumnya.
     *          Jika berhasil, maka harus memenuhi bbrp kondisi:
     *              1. Presenter harus merupakan MviPresenter<S>
     *              2. View ini memiliki current state pada MviPresenter<S>
     */
    fun restoreCurrentState(): Boolean{
        loge("restoreCurrentState() MULAI AWAL")
        return presenter.asNotNullTo { pres: MviPresenter<S> ->
            try{
                (pres.callback as StateProcessor<S, *>).restoreCurrentState()
                true
            } catch (e: Exception){
                false
            }
        } ?: false
    }

    /**
     * <4 Juli 2020> => parameter isPreState dihilangkan. Sbg gantinya, isPreState disimpan
     *   di dalam [state]. Tujuannya adalah agar lebih intuitif.
     */
    fun __render(state: S/*, isPreState: Boolean*/){
        isBusy= state.isPreState
        render(state)
    }
    fun render(state: S/*, isPreState: Boolean*/)
}