package ci.nsu.mobile.auth.qr

import android.content.Context
import android.media.MediaPlayer
import ci.nsu.mobile.auth.R

class SoundManager(context: Context) {

    private val successPlayer: MediaPlayer? = runCatching {
        MediaPlayer.create(context, R.raw.qr_success)
    }.getOrNull()

    private val failurePlayer: MediaPlayer? = runCatching {
        MediaPlayer.create(context, R.raw.qr_failure)
    }.getOrNull()

    fun playSuccess() = successPlayer?.let {
        if (it.isPlaying) it.seekTo(0)
        it.start()
    }

    fun playFailure() = failurePlayer?.let {
        if (it.isPlaying) it.seekTo(0)
        it.start()
    }

    fun release() {
        successPlayer?.release()
        failurePlayer?.release()
    }
}