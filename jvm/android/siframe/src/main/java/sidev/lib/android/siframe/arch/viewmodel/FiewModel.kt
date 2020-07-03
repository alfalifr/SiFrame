package sidev.lib.android.siframe.arch.viewmodel

import androidx.annotation.CallSuper
import androidx.annotation.RestrictTo
import androidx.lifecycle.*
import sidev.lib.android.siframe.arch.presenter.PresenterCallbackCommon
import sidev.lib.android.siframe.arch.presenter.InteractivePresenterDependent
import sidev.lib.android.siframe.arch.presenter.InteractivePresenterDependentCommon
import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.ViewModelBase
import sidev.lib.android.siframe.arch.type.Mvvm
import sidev.lib.android.siframe.exception.TypeExc
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.universal.`fun`.classSimpleName
import sidev.lib.universal.`fun`.isNull
import sidev.lib.universal.`fun`.notNull
import sidev.lib.universal.`fun`.notNullTo
import java.lang.RuntimeException

/**
 * <27 Juni 2020> => Ditujukan untuk penyimpanan data dari view.
 * <27 Juni 2020> => Sementara ViewModel hanya dapat mengambil data dari repo baik dari lokal maupun remote
 *                   scr satu-per-satu, artinya tiap kali request.
 * <27 Juni 2020> => Ada 2 istilah untuk menunjukan key suatu value pada ViewModel ini:
 *                     -Key: Biasanya digunakan untuk operasi yg tidak membutuhkan keterlibatan async atau Repo.
 *                           Sprti pada fungsi add() di mana dapat dilakukan scr sync.
 *                     -ReqCode: Digunakan sebagai pengganti Key pada operasi async yg melibatkan Repo.
 *                               Sprti pada fungsi observe().
 *                     ReqCode sama saja dg Key, namun tujuan penggunaannya yg berbeda.
 *
 * Kelas yg ditujukan untuk menyimpan data dari view agar bertahan saat perubahan state.
 * Selain digunakan untuk mengambil / menyimpan data ke repo, kelas ini juga dapat digunakan sbg
 * perantara untuk mengirim request ke repo atau remote server, walau pada onSuccess tidak memberikan
 * data apapun yg akan ditampilkan pada view. On request sprti yg telah disebutkan, hal teresbut dapat
 * diakali dg cara menunggu (observe) data berupa Boolean pada LifeData.
 *
 * Contoh: Kasus login:
 *           Programmer dapat mengisi true ke dalam LifeData pada fungsi onRepoRes sbg tanda login berhasil
 *           dan false untuk sebaliknya.
 */
abstract class FiewModel(val vmBase: ViewModelBase)
    : ViewModel(), ArchViewModel, Mvvm,
    InteractivePresenterDependentCommon<Presenter>,
    PresenterCallbackCommon {

    final override var isExpired: Boolean= false
        private set

    //    protected abstract val repo: R
/*
    /**
     * Sekali assign
     */
    var lifecycleOwner: LifecycleOwner?= null
        set(v){
            if(field == null)
                if(v != null)
                    field= v
        }
 */
    private var mData= HashMap<String, LifeData<Any>>() //Nullable karena kemungkinan user pingin ada kondisi saat value == null
    private var mIsDataTemporary= HashMap<String, Boolean>()
/*
    /**
     * Berisi pemetaan Key dari LiveData dg ReqCode ke repo.
     * Berguna untuk mengambil LiveData dari {@link mData}
     * Format: ReqCode => Key
     * Jika ingin mengambil ReqCode dg menggunakan Key, maka gunakan getRepoReq()
     */
    private var mReqKeyMap= HashMap<String, String>()
 */
/*
    /**
     * Menyimpan isi mData ke repositori.
     */
    abstract fun save(key: String)
 */
//    protected abstract fun getRepoReq(key: String): String
    /**
     * @param liveData harus diisi sesuai hasil yg ada pada:
     * @param data
     */
    protected abstract fun onRepoRes(reqCode: String, resCode: Int, data: Map<String, Any>?, liveData: LifeData<Any>)

    @CallSuper
    override fun onCleared() {
        isExpired= true
    }
/*
    fun <T: Any> getByReqCode(reqCode: String): SifLiveData<T>? {
        return filter {
            val key= mReqKeyMap[reqCode]
            mData[key] as SifLiveData<T>
        }
    }
 */

    /**
     * Menambahkan nilai scr manual ke ViewModel
     */
    override fun <T> addValue(key: String, data: T, replaceExisting: Boolean): LifeData<T>?{
        return doWhenNotExpired {
            val bool= replaceExisting
                    || (mData[key].notNullTo { false } ?: true)

            if(bool && data is LifeData<*>)
                throw TypeExc(msg = "Data yg dimasukan adalah tipe LifeData<*>. Gunakan addLifeData().")

            val liveData=
                (
                    if(bool){
                        val lv= LifeData<T>()
                        lv.value= data
                        mData[key]= lv as LifeData<Any>
                        lv
                    } else mData[key]!!
                ) as LifeData<T>
            liveData
        }
    }

    override fun <T> addLifeData(key: String, lifeData: T, replaceExisting: Boolean): Boolean{
        return doWhenNotExpired {
            val bool= replaceExisting
                    || (mData[key].notNullTo { false } ?: true)
            if(bool)
                mData[key]= lifeData as LifeData<Any>
            bool
        } ?: false
    }

    /**
     * @return null jika FiewModel ini expired atau data sebelumnya belum ada.
     */
    override fun <T> get(key: String): LifeData<T>?{
        return doWhenNotExpired {
            mData[key] as? LifeData<T>
        }
    }

    /**
     * Digunakan untuk memasang observer pada LifeData. Jika sebelumnya data tidak terdapat pada
     * <code>mData</code>, maka fungsi ini meng-instantiate <code>LifeData</code> yg baru dan isinya kosong.
     * Fungsi ini akan meng-assign fungsi observer baru sekalipun data sudah ada pada <code>mData</code>.
     * Untuk hanya mendapat data, gunakan <code>get()</code>.
     *
     * @param forceReload false jika data sudah ada di {@link #mData} sehingga tidak direload dari repo.
     *                      true jika data sudah ada namun tetap direload dari repo.
     */
    override fun <T> observe(reqCode: String, vararg params: Pair<String, Any>,
                    safeValue: Boolean/*= true*/, forceReload: Boolean/*= false*/,
                    onPreLoad: (() -> Unit)?/*= null*/, //fungsi saat LifeData msh loading
                    onObserve: (T) -> Unit): LifeData<T>?{
        return doWhenNotExpired {
            var data= mData[reqCode] as? LifeData<T>
            val isDataNotAvailable= data == null
            if(isDataNotAvailable){
                data= LifeData()
//                loge("observe() data == null")

                //Masukan dulu LifeData ke list agar dapat dilakukan reload()
                mIsDataTemporary[reqCode]= !safeValue
                mData[reqCode]= data as LifeData<Any>

                //Gak perlu dilakukan di luar MainThread karena nanti Repo otomatis melakukan
                //operasi pengambilan data scr async.
                reload(reqCode, *params)
            }
            data!!.observe(vmBase, onPreLoad, onObserve)

            if(isDataNotAvailable || forceReload)
                reload(reqCode, *params)

            data
        }
    }

    /**
     * Untuk mereload data dari repo. Fungsi ini berguna bagi data yg sudah ada di {@link #mData}.
     * Fungsi ini juga digunakan scr internal untuk melakukan sendRequest() yg didahului dg LifeData.onPreLoad()
     * @return true jika data sudah ada di {@link #mData}.
     *          false jika data belum ada di {@link #mData} sehingga
     *          tidak dilakukan sendRequest(reqCode, *params) ke repo.
     */
    override fun reload(reqCode: String, vararg params: Pair<String, Any>): Boolean{
        return mData[reqCode].notNullTo { lifeData ->
            lifeData.onPreLoad(vmBase)
            sendRequest(reqCode, *params)
            true
        } ?: false
    }
/*
    private fun <T> doWhenNotExpired(func: () -> T): T?{
        return if(!isExpired) func()
        else {
            loge("\"${this.classSimpleName()}\" tidak lagi terpakai karena Lifecycle: \"${vmBase.classSimpleName()}\" sudah dihancurkan")
            null
        }
    }
 */


    @RestrictTo(RestrictTo.Scope.LIBRARY)
    override fun onPresenterSucc(reqCode: String, resCode: Int, data: Map<String, Any>?) {
//        val key= mReqKeyMap[reqCode]
        val liveData= mData[reqCode]
        try{
            onRepoRes(reqCode, resCode, data, liveData!!)
            if(mIsDataTemporary[reqCode] == true)
                mData.remove(reqCode)
        } catch (e: Exception){ throw RuntimeException("reqCode: \"$reqCode\" tidak memiliki atau sama dg null") }
    }
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    override fun onPresenterFail(reqCode: String, resCode: Int, msg: String?, e: Exception?) {
//        if(vmBase.lifecycle.currentState == Lifecycle.State.DESTROYED) return
        mData[reqCode].notNull { data ->
            data.postOnFail(vmBase, resCode, msg, e)
            if(mIsDataTemporary[reqCode] == true)
                mData.remove(reqCode)
        }
    }
}