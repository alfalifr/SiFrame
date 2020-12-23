package sidev.lib.android.std.tool.util

import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import sidev.lib.`val`.SuppressLiteral
import sidev.lib.android.std.tool.util.`fun`.loge
import java.io.File

//import org.jetbrains.anko.internals.AnkoInternals.createAnkoContext

object _AudioUtil {
    /** Objek standar untuk audio player. */
    object Player {
        var activePlayer: MediaPlayer?= null

        @JvmOverloads
        fun play(
            url: String, async: Boolean= false, player: MediaPlayer?= null
//        onPreparedListener: MediaPlayer.OnPreparedListener?= null,
//        onCompletionListener: MediaPlayer.OnCompletionListener?= null,
//        onErrorListener: MediaPlayer.OnErrorListener?= null
        ): MediaPlayer? =
            try {
//                checkPermission()
                @Suppress(SuppressLiteral.NAME_SHADOWING)
                val player = player ?: MediaPlayer() //.createAnkoContext()
                activePlayer= player
                player.setAudioStreamType(AudioManager.STREAM_MUSIC)
//                player.setDataSource(/*"\n" + */"http://www.everyayah.com/data/Abdul_Basit_Murattal_64kbps/001004.mp3")
                player.setDataSource(url)
                if(!async){
                    player.prepare()
                    player.seekTo(0)
                    player.start()
                } else
                    player.prepareAsync()

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
                null
//                ToastApp.tampilkanTeks(this, "HASIL_GAGAL!!! ${e.message}", Toast.LENGTH_LONG)
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
        @JvmOverloads
        fun stop(player: MediaPlayer?= activePlayer): MediaPlayer? = (player ?: MediaPlayer()).apply {
//        setAudioStreamType(AudioManager.STREAM_MUSIC)
            stop()
            release()
            activePlayer= null
        }
        fun release(): MediaPlayer? = stop(activePlayer)
    }

    /** Objek standar untuk audio recorder. */
    object Recorder {
        var activeRecorder: MediaRecorder?= null
//        private var fileOutput= File(pathFileOutput)

        fun start(outputPath: String, recorder: MediaRecorder?= null): MediaRecorder? {
            @Suppress(SuppressLiteral.NAME_SHADOWING)
            val recorder= recorder ?: MediaRecorder()
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            val file= File(outputPath)
            if(file.parentFile?.exists() != true)
                file.mkdirs()
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                recorder.setOutputFile(file)
            else
                recorder.setOutputFile(file.absolutePath)

            return try{
                recorder.prepare()
                recorder.start()
                recorder
            } catch(error: Exception){
                loge("Terjadi kesalahan saat menyiapkan media.")
                null
            }
        }

        fun stop(recorder: MediaRecorder?= activeRecorder): MediaRecorder? = (recorder ?: MediaRecorder()).apply {
            stop()
            release()
        }
    }
}