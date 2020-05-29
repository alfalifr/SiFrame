package sidev.lib.android.siframe.util.goolgevision

/**
 * Di-disable untuk menghindari download yang gak terpakai.
 * Jika memang dipakai, tambah line
 *   implementation 'com.google.android.gms:play-services-vision:19.0.0'
 * pada build.gradle
 */
/*
import android.util.SparseArray
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Frame
import sidev.lib.android.siframe.util._BitmapUtil

class DetectorFlex<T>(private var delegate: Detector<T>) : Detector<T>(){
    var detectionWidth= -1
    var detectionHeight= -1

    override fun detect(frame: Frame?): SparseArray<T> {
        var croppedFrame= frame
        if(frame != null){
            if(detectionWidth <= 0) processFrameWidthHeight(frame)
            croppedFrame= _BitmapUtil.cropFrame(frame, detectionWidth, detectionHeight)
        }
        return delegate.detect(croppedFrame)
    }

    private fun processFrameWidthHeight(frame: Frame){
        if(detectionHeight <= 0) detectionHeight = frame.metadata.height
        if(detectionWidth <= 0) detectionWidth = frame.metadata.width
    }

    override fun isOperational(): Boolean {
        return delegate.isOperational
    }

    override fun setFocus(id: Int): Boolean {
        return delegate.setFocus(id)
    }
}
 */