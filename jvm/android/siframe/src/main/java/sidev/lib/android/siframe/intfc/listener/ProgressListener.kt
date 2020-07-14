package sidev.lib.android.siframe.intfc.listener

import android.app.Activity

abstract class ProgressListener<M>(var act: Activity?= null,
                                   var listenerId: String? = null): Listener{
    companion object {
        val RES_OK= 1
        val RES_PARTIAL= 0
        val RES_FAILED= -1
        val RES_NOTHING_TO_DO= -2
        val RES_STOP_ERROR= -3
        val PROGRES_RUNNING= 12 //saat pekerjan masih berjalan (dilakukan != total)
        val PROGRES_FINISHED= 11 //saat pekerjaan selesai (bisa HASIL_BERHASIL atau HASIL_GAGAL)
        val PROGRES_NOT_STARTED= 10 //saat total diganti namun progres belum bertambah
    }

    override var tag: Any?= null

    private var progresStatus= PROGRES_NOT_STARTED
    var total= 0
        private set
/*
        (value){
            progres= 0
            dilakukan= 0
            statusKerja= KERJA_BELUM
            field= value
        }
    var perubahanTotal= 0
        private set
*/
    var progresSucc= 0
        private set
    var progresDone= 0
        private set
    var progresId: String = ""
        protected set
    var errorExists= false
        protected set

    constructor(total: Int, act: Activity?= null, listenerId: String? = null): this(act, listenerId){
        this.total= total
    }

    fun total(total: Int, progresId: String= ""){
        this.total += total
        progresSucc= 0
        progresDone= 0
        progresStatus= PROGRES_NOT_STARTED
        this.progresId= progresId
        errorExists= false
        if(total == 0)
            processProgres(status= RES_NOTHING_TO_DO)
/*
        else
            perubahanTotal++
*/
    }
    fun progresDone(error: Exception?= null, contSucc: Boolean = false){
        if(++progresDone > total)
//            if(--perubahanTotal <= 0)
                throw ProgresExc("Progres yang dilakukan ($progresDone) melebihi total ($total) ! \nidPengawas= $listenerId, idProgres= $progresId")

        if(error != null)
            sendError(error)

        progresStatus= PROGRES_RUNNING
        if(!contSucc)
            processProgres(progresDone, total, null, progresId)
        if(progresDone == total){
            if(progresDone == progresSucc)
                processProgres(status= RES_OK)
            else if(progresSucc > 0)
                processProgres(status= RES_PARTIAL)
            else
                processProgres(status= RES_FAILED)
            progresStatus= PROGRES_FINISHED
            total= 0
        }
    }
    fun progresSucc(isiProgres: M){
        progresSucc++
        processProgres(progresSucc, total, isiProgres, progresId)

        //diletakkan di akhir agar @line `if(dilakukan == total)` dapat dilakukan di akhir
        progresDone(contSucc= true)
    }

    /**
     * Fungsi internal untuk memproses progres yg terjadi dari input parameter.
     */
    private fun processProgres(
        progres: Int?= null, total: Int?= null, content: M?= null, id: String= "",
        status: Int? = null){
        if(act != null)
            act!!.runOnUiThread {
                if(progres != null)
                    onProgres(progres, total!!, content, id)
                else
                    onFinish(status!!)
            }
        else
            if(progres != null)
                onProgres(progres, total!!, content, id)
            else
                onFinish(status!!)
    }

    fun isRunning(): Boolean{return progresStatus == PROGRES_RUNNING}
    fun notYetRunning(): Boolean{return progresStatus == PROGRES_NOT_STARTED}
    fun isFinished(): Boolean{return progresStatus == PROGRES_FINISHED}

    /*
     * @param isiProgres: M?
     *         ada isinya jika progres berhasil
     *         null jika progres gagal
    */
    protected abstract fun onProgres(progres: Int, total: Int, isiProgres: M?, idProgres: String = "")
    open fun onFinish(status: Int){}

    //untuk dipanggil dari kelas luar karena method bersifat final
    fun sendError(error: Exception, hentikanProgres: Boolean= false, idProgres: String?= null){
        if(idProgres != null)
            this.progresId= idProgres
        errorExists= true
        onError(error, this.progresId)
        if(hentikanProgres){
            progresDone= total
            processProgres(status= RES_STOP_ERROR)
        }
    }
    //untuk di-override saat meng-extend kelas PengawasProgres<>
    protected open fun onError(error: Exception, idProgres: String){}

    private class ProgresExc(pesan: String?): Exception(pesan)
}