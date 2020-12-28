package sidev.lib.android.std.tool.util

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import androidx.core.net.toFile
import org.jetbrains.anko.ctx
import sidev.lib.`val`.SuppressLiteral
import sidev.lib.android.std.lifecycle.app.StdApp
import sidev.lib.android.std.lifecycle.app.StdApp.Companion.ctx
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.android.std.tool.util.`fun`.toast
import sidev.lib.check.getLateinit
import sidev.lib.exception.IllegalArgExc
import sidev.lib.exception.IllegalStateExc
import java.io.File

//import org.jetbrains.anko.internals.AnkoInternals.createAnkoContext

/**
 * Objek yang berisi fungsi template standar untuk melakukan operasi
 * yang berkaitan dengan media, seperti [Player] dan [Recorder].
 */
object _MediaUtil {
//    enum class Type { AUDIO, VIDEO }

    /** Objek standar untuk media player. */
    object Player {
        var activePlayer: MediaPlayer?= null

        @JvmOverloads
        fun init(
            path: String,
            player: MediaPlayer?= null
        ): MediaPlayer = init(path, null, player)

        @JvmOverloads
        fun init(
            file: File,
            player: MediaPlayer?= null
        ): MediaPlayer {
            try{
                if(!file.exists())
                    throw IllegalArgExc(
                        paramExcepted = *arrayOf("file"),
                        detailMsg = "Param `file` tidak terdapat pada sistem."
                    )
            } catch (e: SecurityException) { //Karena app gak bisa membaca filenya.
                throw IllegalStateExc(
                    detMsg = "Pembacaan (read) param `file` menyebabkan `SecurityException`."
                ).apply { cause= e }
            }
            return init(file.absolutePath, null, player)
        }

        @JvmOverloads
        fun init(
            context: Context, uri: Uri,
            player: MediaPlayer?= null
        ): MediaPlayer = init(null, context to uri, player)

        /**
         * Fungsi yang digunakan untuk meng-init [MediaPlayer] dengan pengaturan standar.
         */
        private fun init(
            path: String?,
            uri: Pair<Context, Uri>?,
            player: MediaPlayer?= null
        ): MediaPlayer {
            @Suppress(SuppressLiteral.NAME_SHADOWING)
            val player = player ?: MediaPlayer() //.createAnkoContext()
            activePlayer= player

            //Untuk mendefinisikan bahwa audio yg keluar dari [player] merupakan audio musik atau media.
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                player.setAudioAttributes(AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_MUSIC).build())
            else
                @Suppress(SuppressLiteral.DEPRECATION)
                player.setAudioStreamType(AudioManager.STREAM_MUSIC)

//                player.setDataSource(/*"\n" + */"http://www.everyayah.com/data/Abdul_Basit_Murattal_64kbps/001004.mp3")
            when {
                path != null -> player.setDataSource(path)
                uri != null -> player.setDataSource(uri.first, uri.second)
                else -> throw IllegalArgExc(
                    paramExcepted = *arrayOf("path", "uri"),
                    detailMsg = "Param `path` dan `uri` tidak boleh sama-sama null."
                )
            }
            return player
        }

        @JvmOverloads
        fun play(
            context: Context, uri: Uri,
            async: Boolean= true, player: MediaPlayer?= null,
            onPreparedListener: MediaPlayer.OnPreparedListener?= null
        ): MediaPlayer? = play(null, context to uri, async, player, onPreparedListener)

        @JvmOverloads
        fun play(
            path: String,
            async: Boolean= true, player: MediaPlayer?= null,
            onPreparedListener: MediaPlayer.OnPreparedListener?= null
        ): MediaPlayer? = play(path, null, async, player, onPreparedListener)

        @JvmOverloads
        fun play(
            file: File,
            async: Boolean= true, player: MediaPlayer?= null,
            onPreparedListener: MediaPlayer.OnPreparedListener?= null
        ): MediaPlayer? {
            try{
                if(!file.exists())
                    throw IllegalArgExc(
                        paramExcepted = *arrayOf("file"),
                        detailMsg = "Param `file` tidak terdapat pada sistem."
                    )
            } catch (e: SecurityException) { //Karena app gak bisa membaca filenya.
                return null
            }
            return play(file.absolutePath, null, async, player, onPreparedListener)
        }

        /**
         * [onPreparedListener] berguna untuk mempertahankan [MediaPlayer.OnPreparedListener] yang sudah ada
         * pada [player] yg dipass saat [async] == `true`, karena fungsi ini scr otomatis
         * memutar media saat `onPrepared`.
         */
//        @JvmOverloads
        private fun play(
            path: String?,
            uri: Pair<Context, Uri>?,
//            context: Context?= null,
            async: Boolean= true, player: MediaPlayer?= null,
            onPreparedListener: MediaPlayer.OnPreparedListener?= null
//        onPreparedListener: MediaPlayer.OnPreparedListener?= null,
//        onCompletionListener: MediaPlayer.OnCompletionListener?= null,
//        onErrorListener: MediaPlayer.OnErrorListener?= null
        ): MediaPlayer? {
            @Suppress(SuppressLiteral.NAME_SHADOWING)
            val player = init(path, uri, player)
            return try {
//                checkPermission()
                if(!async){
                    player.prepare()
                    player.seekTo(0)
                    player.start()
                } else {
                    player.setOnPreparedListener {
                        onPreparedListener?.onPrepared(it)
                        player.start()
                    }
                    player.prepareAsync()
                }

//                ToastApp.tampilkanTeks(this, "Mencoba!!!", Toast.LENGTH_LONG)
                player
/*
                .apply {
                    if(onPreparedListener != null) setOnPreparedListener(onPreparedListener)
                    if(onCompletionListener != null) setOnCompletionListener(onCompletionListener)
                    if(onErrorListener != null) setOnErrorListener(onErrorListener)
                }
 */
            } catch (e: Exception) {
                val msg= "Terjadi kesalahan saat menyiapkan media player."
                (uri?.first ?: StdApp.getLateinit { ctx })?.toast(msg)
                loge(msg, e)
                null
//                ToastApp.tampilkanTeks(this, "HASIL_GAGAL!!! ${e.message}", Toast.LENGTH_LONG)
            }
        }

        @JvmOverloads
        fun resume(player: MediaPlayer?= activePlayer): MediaPlayer? = (player ?: MediaPlayer()).apply {
            seekTo(currentPosition)
            start()
        }

        @JvmOverloads
        fun pause(player: MediaPlayer?= activePlayer): MediaPlayer? = (player ?: MediaPlayer()).apply {
//        setAudioStreamType(AudioManager.STREAM_MUSIC)
            pause()
        }

        /**
         * Fungsi menghentikan sekaligus me-release sumber daya yang dipakai dengan segera.
         */
        @JvmOverloads
        fun stop(player: MediaPlayer?= activePlayer): MediaPlayer? = (player ?: MediaPlayer()).apply {
//        setAudioStreamType(AudioManager.STREAM_MUSIC)
            stop()
            release()
            activePlayer= null
        }

        fun release(): MediaPlayer? = stop(activePlayer)
    }

    /** Objek standar untuk media recorder. */
    object Recorder {
        var activeRecorder: MediaRecorder?= null
//        private var fileOutput= File(pathFileOutput)

        @JvmOverloads
        fun init(
            outputPath: String,
            format: Int = MediaRecorder.OutputFormat.THREE_GPP,
            audioEncoder: Int = MediaRecorder.AudioEncoder.AMR_NB,
            videoEncoder: Int = MediaRecorder.VideoEncoder.DEFAULT,
            recorder: MediaRecorder?= null
        ): MediaRecorder = init(File(outputPath), format, audioEncoder, videoEncoder, recorder)

        /**
         * Fungsi yang digunakan untuk meng-init [MediaRecorder] dengan pengaturan standar.
         * Fungsi ini tidak mengatur sumber dari video.
         */
        @JvmOverloads
        fun init(
            output: File,
            format: Int = MediaRecorder.OutputFormat.THREE_GPP,
            audioEncoder: Int = MediaRecorder.AudioEncoder.AMR_NB,
            videoEncoder: Int = MediaRecorder.VideoEncoder.DEFAULT,
            recorder: MediaRecorder?= null
        ): MediaRecorder {
            @Suppress(SuppressLiteral.NAME_SHADOWING)
            val recorder= recorder ?: MediaRecorder()
            activeRecorder= recorder

            recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
//            recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT)

            if(format != MediaRecorder.OutputFormat.DEFAULT)
                recorder.setOutputFormat(format)
            if(audioEncoder != MediaRecorder.AudioEncoder.DEFAULT)
                recorder.setAudioEncoder(audioEncoder)
            if(videoEncoder != MediaRecorder.VideoEncoder.DEFAULT)
                recorder.setVideoEncoder(videoEncoder)

//            val file= File(outputPath)
            output.parentFile?.apply {
                if(!exists()) mkdirs()
            }
/*
            if(output.parentFile?.exists() != true)
                output.mkdirs()
 */
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                recorder.setOutputFile(output)
            else
                recorder.setOutputFile(output.absolutePath)

            return recorder
        }

        @JvmOverloads
        fun start(
            outputPath: String,
            format: Int = MediaRecorder.OutputFormat.THREE_GPP,
            audioEncoder: Int = MediaRecorder.AudioEncoder.AMR_NB,
            videoEncoder: Int = MediaRecorder.VideoEncoder.DEFAULT,
            recorder: MediaRecorder?= null
        ): MediaRecorder? = start(File(outputPath), format, audioEncoder, videoEncoder, recorder)

        @JvmOverloads
        fun start(
            output: File,
            format: Int = MediaRecorder.OutputFormat.THREE_GPP,
            audioEncoder: Int = MediaRecorder.AudioEncoder.AMR_NB,
            videoEncoder: Int = MediaRecorder.VideoEncoder.DEFAULT,
            recorder: MediaRecorder?= null
        ): MediaRecorder? {
            @Suppress(SuppressLiteral.NAME_SHADOWING)
            val recorder= init(output, format, audioEncoder, videoEncoder, recorder)
            return try{
                recorder.prepare()
                recorder.start()
                recorder
            } catch(error: Exception){
                val msg= "Terjadi kesalahan saat menyiapkan media recorder."
                StdApp.getLateinit { ctx.toast(msg) }
                loge(msg, error)
                null
            }
        }

        /**
         * Fungsi menghentikan sekaligus me-release sumber daya yang dipakai dengan segera.
         */
        @JvmOverloads
        fun stop(recorder: MediaRecorder?= activeRecorder): MediaRecorder? = (recorder ?: MediaRecorder()).apply {
            stop()
            release()
            activeRecorder= null
        }

        fun release(): MediaRecorder? = stop(activeRecorder)
    }
}