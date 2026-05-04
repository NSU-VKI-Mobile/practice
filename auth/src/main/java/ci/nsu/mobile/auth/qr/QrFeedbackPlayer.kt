package ci.nsu.mobile.auth.qr

import android.content.Context
import android.media.MediaPlayer
import ci.nsu.mobile.auth.R

object QrFeedbackPlayer {
    fun playSuccess(context: Context) {
        play(context, R.raw.qr_success)
    }

    fun playFailure(context: Context) {
        play(context, R.raw.qr_failure)
    }

    private fun play(context: Context, rawResId: Int) {
        runCatching {
            MediaPlayer.create(context.applicationContext, rawResId)?.apply {
                setOnCompletionListener { player -> player.release() }
                start()
            }
        }
    }
}
