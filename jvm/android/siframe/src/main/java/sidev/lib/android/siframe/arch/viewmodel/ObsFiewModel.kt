package sidev.lib.android.siframe.arch.viewmodel

import android.content.Context
import androidx.annotation.RestrictTo
import androidx.lifecycle.*
import sidev.lib.android.siframe.arch.presenter.PresenterCallbackCommon
import sidev.lib.android.siframe.arch.presenter.InteractivePresenterDependentCommon
import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.ViewModelBase
import sidev.lib.android.siframe.arch.view.ArchView
import sidev.lib.android.siframe.intfc.lifecycle.ExpirableBase
import sidev.lib.android.siframe.intfc.lifecycle.InterruptableBase
import sidev.lib.android.siframe.intfc.lifecycle.InterruptableLinkBase
import sidev.lib.android.siframe.lifecycle.app.App
import sidev.lib.android.std.tool.util.`fun`.toast
import sidev.lib.check.asNotNull
import sidev.lib.check.asNotNullTo
import sidev.lib.check.notNull
import sidev.lib.check.notNullTo
import sidev.lib.exception.RuntimeExc

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
 *
 * <5 Juli 2020> => Versi [FiewModel] yg dapat diobservasi.
 *
 * @param [vmBase] merupakan pemilik utamanya. Dalam konteks ini adalah Activity.
 */
abstract class ObsFiewModel(val vmBase: ViewModelBase)
    : FiewModel(), ObservableViewModel,
    InteractivePresenterDependentCommon,
    InterruptableLinkBase,
    PresenterCallbackCommon {
/*
    var vmBase: ViewModelBase= vmBase
        internal set
 */
/*
    final override var isExpired: Boolean= false
        private set
 */
    final override val _prop_ctx: Context?
        get() = vmBase._prop_ctx

    final override var interruptable: InterruptableBase?= vmBase
        private set
        get(){
            val isExpired= field.asNotNullTo { exp: ExpirableBase -> exp.isExpired }

            return if(isExpired == false) field ?: vmBase
            else vmBase
        }

    final override var isBusy: Boolean= false

    override val isInterruptable: Boolean
        get() = interruptable?.isInterruptable ?: true

    /*
    override var callbackCtx: Context
        get() {}
        set(value) {}
 */

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
    /**
     * Untuk menyimpan data owner. Karena kelas [FiewModel] ini dipakai oleh banyak LifecycleOwner.
     */
    private var mOwner= HashMap<String, LifecycleOwner>()

    /**
     * Untuk menyimpan data apakah tiap owner sedang menjalankan proses request atau tidak.
     *
     * Knp kok harus disimpan difield ini? Knp kok gak langsung disimpen di [mOwner] saja?
     * Karena ada kemungkinan [mOwner] di-recreate sehingga data [ViewModelBase.isBusy]
     * ke nilai awal sehingga tidak akurat.
     */
    private var mOwnerIsOnPreLoad= HashMap<String, Boolean>()
/*
    /**
     * Untuk menyimpan data yg dipakai di view atau hasil request.
     */
    protected var mData= HashMap<String, LifeData<Any>>() //Nullable karena kemungkinan programmer pingin ada kondisi saat value == null
        private set
 */
    /**
     * Untuk menyimpan info apakah [mData] dg key tertentu bersifat sementara.
     * Jika bersifat sementara, maka data sbg [LifeData] tetap disimpan,
     * namun [LifeData.getValue] akan dirubah menjadi null setelah fungsi [onRepoRes]
     * dipanggil.
     */
    protected var mDataIsTemporary= HashMap<String, Boolean>() //Nullable karena kemungkinan programmer pingin ada kondisi saat value == null
        private set

    /**
     * Parameter yg digunakan saat pemanggilan fungsi [observe].
     * Parameter disimpan agar selanjutnya jika fungsi [reload] dipanggil, programmer tidak perlu
     * memasukan lagi parameter yg sama. Namun, programmer juga dapat memasukan parameter dg nilai
     * yg beda pada fungsi reload.
     */
    private var mParam= HashMap<String, Array<out Pair<String, Any>>>()
//    private var mIsDataTemporary= HashMap<String, Boolean>() //Mode temprary ditiadakan karena dapat menyebabkan runtime error
                //jika terdapat banyak proses async yg berjalan yg melibatkan mData yg sama.
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
/*
    @CallSuper
    override fun onCleared() {
        isExpired= true
    }
 */
/*
    fun <T: Any> getByReqCode(reqCode: String): SifLiveData<T>? {
        return filter {
            val key= mReqKeyMap[reqCode]
            mData[key] as SifLiveData<T>
        }
    }
 */
/*
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
 */
    /**
     * Digunakan untuk memasang observer pada LifeData. Jika sebelumnya data tidak terdapat pada
     * <code>mData</code>, maka fungsi ini meng-instantiate <code>LifeData</code> yg baru dan isinya kosong.
     * Fungsi ini akan meng-assign fungsi observer baru sekalipun data sudah ada pada <code>mData</code>.
     * Untuk hanya mendapat data, gunakan <code>get()</code>.
     *
     * @param forceReload false jika data sudah ada di {@link #mData} sehingga tidak direload dari repo.
     *                      true jika data sudah ada namun tetap direload dari repo.
     */
    final override fun <T> observe(owner: LifecycleOwner,
                             reqCode: String, vararg params: Pair<String, Any>,
                             isValueTemporary: Boolean, /*= false*/
                    /*safeValue: Boolean= true*/
                     //= null,
                             loadLater: Boolean/*= false*/, forceReload: Boolean/*= false*/,
                             onPreLoad: (() -> Unit)?/*= null*/, //fungsi saat LifeData msh loading
                             onObserve: (T) -> Unit): LifeData<T>?{
        return doWhenNotExpired {
            var data= mData[reqCode] as? LifeData<T>
            val isDataNotAvailable= data == null
            if(isDataNotAvailable){
                data= LifeData()
//                loge("observe() data == null")

                //Masukan dulu LifeData ke list agar dapat dilakukan reload()
//                mIsDataTemporary[reqCode]= !safeValue
                mData[reqCode]= data as LifeData<Any>
                data.tag= reqCode
                /**
                 * Assign nilai ke [mParam] dilakukan di sini jika fungsi [observe] dipanggil
                 * untuk pertaman kali dan programmer nge-pass nilai true untuk param [loadLater]
                 * sehingga fungsi [reload] tidak dipanggil dan menyebabkan [params] tidak disimpan
                 * di fungsi [reload].
                 */
                if(params.isNotEmpty())
                    mParam[reqCode]= params

                //Gak perlu dilakukan di luar MainThread karena nanti Repo otomatis melakukan
                //operasi pengambilan data scr async.
//                reload(reqCode, *params)
            }
            data!!.observe(owner, onPreLoad, onObserve)
            mOwner[reqCode]= owner //Ditaruh luar karena kemungkinan owner-nya bisa berubah.
            mDataIsTemporary[reqCode]= isValueTemporary //Ditaruh luar karena kemungkinan konfigurasi temporary-nya berubah.
            configOwner(reqCode, data, owner)

            if(isDataNotAvailable && !loadLater || forceReload)
                reload(reqCode, *params)

            data
        }
    }

    /**
     * Untuk mereload data dari repo. Fungsi ini berguna bagi data yg sudah ada di {@link #mData}.
     * Fungsi ini juga digunakan scr internal untuk melakukan sendRequest() yg didahului dg LifeData.onPreLoad().
     *
     * Fungsi ini menyimpan [params] pada [mParam] dan mengambilnya untuk [reqCode] yg sama.
     * Untuk pemanggilan [reqCode] yg sama, fungsi ini akan mengambil param yg tersimpan pada [mParam]
     * jika programmer tidak nge-pass [params] ke fungsi ini atau [params.size] == 0.
     * [params] tidak akan disimpan pada [mParam] jika [params.size] == 0.
     *
     * @return true jika data sudah ada di {@link #mData}.
     *          false jika data belum ada di {@link #mData} sehingga
     *          tidak dilakukan sendRequest(reqCode, *params) ke repo.
     */
    final override fun reload(reqCode: String, vararg params: Pair<String, Any>): Boolean{
        return mData[reqCode].notNullTo { lifeData ->
            val owner= mOwner[reqCode]!!
            var isRequestSent= owner.asNotNullTo { view: ArchView ->
                view.doWhenNotBusy {
                    innerReload(lifeData, owner, reqCode, *params)
                    true
                } ?: false
            }
            //Jika [isRequestSent] == null, artinya owner bkn ArchView
            // sehingga kode di atas tidak dieksekusi.
            if(isRequestSent == null){
                //Knp kok gak di-assert?
                // Karena assignment nilai pada [mOwnerIsOnPreLoad} hanya dilakukan
                // pada fungsi [reload], [onPresenterSucc], dan [onPresenterFail].
                if(mOwnerIsOnPreLoad[reqCode] == false){
                    innerReload(lifeData, owner, reqCode, *params)
                    isRequestSent= true
                }
            }
            isRequestSent
        } ?: false
    }

    /**
     * Fungsi private untuk mengirim request ke repo.
     * Knp kok dipisah dari fungsi [reload]?
     * Karena pada fungsi [reload] terdapat kode yg duplikat.
     * Untuk mempermudah pembacaan (readability), maka kode duplikat tersebut
     * dipisahkan ke fungsi tersendiri, yaitu [innerReload]
     */
    private fun innerReload(lifeData: LifeData<*>, owner: LifecycleOwner,
                            reqCode: String, vararg params: Pair<String, Any>){
        lifeData.onPreLoad(owner)

        owner.asNotNull { view: ArchView ->
            view.isBusy= true
            view.busyOfWhat= reqCode
            interruptable= view
            mOwnerIsOnPreLoad[reqCode]= true
        }

        if(params.isNotEmpty())
            mParam[reqCode]= params
        val sentParams= mParam[reqCode] ?: params

        sendRequest(reqCode, *sentParams)
        isBusy= true
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
    private fun configOwner(reqCode: String, lifeData: LifeData<*>, owner: LifecycleOwner){
        owner.asNotNull { innerOwner: ViewModelBase ->
            mOwnerIsOnPreLoad[reqCode].notNull { isOnPreLoad ->
                if(isOnPreLoad){
                    if(innerOwner is ArchView){
                        innerOwner.isBusy= true
                        innerOwner.busyOfWhat= reqCode
                    }
                    lifeData.onPreLoad(innerOwner)
                }
            }
        }
    }


    @RestrictTo(RestrictTo.Scope.LIBRARY)
    override fun onPresenterSucc(
        request: String,
        result: Int,
        data: Map<String, Any>?,
        resCode: Int
    ) {
//        val key= mReqKeyMap[reqCode]
        val liveData= mData[request]
        try{ liveData!! }
        catch (e: KotlinNullPointerException){
            throw RuntimeExc(detailMsg = "reqCode: \"$request\" tidak memiliki lifeData atau sama dg null")
        }

        mOwner[request]!!.asNotNull { view: ArchView ->
            view.isBusy= false
            view.busyOfWhat= InterruptableBase.DEFAULT_BUSY_OF_WHAT
            interruptable= view
            mOwnerIsOnPreLoad[request]= false
        }

        onRepoRes(request, resCode, data, liveData)
        if(mDataIsTemporary[request]!!)
            liveData.setValue(null, false)

        isBusy= false
/*
            if(mIsDataTemporary[reqCode] == true)
                mData.remove(reqCode)
 */
    }
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    override fun onPresenterFail(
        request: String,
        result: Int?,
        msg: String?,
        e: Exception?,
        resCode: Int
    ) {
//        if(vmBase.lifecycle.currentState == Lifecycle.State.DESTROYED) return
        val owner= mOwner[request]!!

        owner.asNotNull { view: ArchView ->
            view.isBusy= false
            view.busyOfWhat= InterruptableBase.DEFAULT_BUSY_OF_WHAT
            interruptable= view
            mOwnerIsOnPreLoad[request]= false
        }

        mData[request].notNull { data ->
            data.postOnFail(owner, resCode, msg, e)
/*
            if(mIsDataTemporary[reqCode] == true)
                mData.remove(reqCode)
 */
        }

        isBusy= false
    }


    override fun onInterruptedWhenBusy() {
        App.ctx.toast("sabar bro dari vm")
//        interruptable?.onInterruptedWhenBusy()
    }
}