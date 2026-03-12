package cc.fastcv.api_adapter.api.sensor.compass

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import cc.fastcv.api_adapter.R

class CompassView : FrameLayout, DefaultLifecycleObserver {

    companion object {
        private const val TAG = "CompassView"
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    private var compass: Compass? = null
    private var arrowView: ImageView? = null
    private var currentAzimuth = 0f
    private var sotwFormatter: SOTWFormatter? = null

    private fun init(context: Context) {
        inflate(context, R.layout.fastcv_view_compass, this)
        sotwFormatter = SOTWFormatter(context)
        arrowView = findViewById(R.id.main_image_hands)
        setupCompass(context)
    }

    private fun setupCompass(context: Context) {
        compass = Compass(context)
        val cl: CompassListener = getCompassListener()
        compass?.setListener(cl)
    }

    fun bindLifecycle(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    override fun onStart(owner: LifecycleOwner) {
        Log.d(TAG, "start compass")
        compass?.start()
    }

    override fun onPause(owner: LifecycleOwner) {
        Log.d(TAG, "stop compass")
        compass?.stop()
    }

    override fun onResume(owner: LifecycleOwner) {
        Log.d(TAG, "start compass")
        compass?.start()
    }

    override fun onStop(owner: LifecycleOwner) {
        Log.d(TAG, "stop compass")
        compass?.stop()
    }

    private val handler = Handler(Looper.getMainLooper())

    private fun getCompassListener(): CompassListener {
        return object : CompassListener {
            override fun onNewAzimuth(azimuth: Float) {
                handler.post {
                    adjustArrow(azimuth)
                    val text = sotwFormatter?.format(azimuth)
                }
            }
        }
    }

    private fun adjustArrow(azimuth: Float) {
        Log.d(
            "xcl_debug", "will set rotation from " + currentAzimuth + " to "
                    + azimuth
        )
        val an: Animation = RotateAnimation(
            -currentAzimuth, -azimuth,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
            0.5f
        )
        currentAzimuth = azimuth
        an.duration = 500
        an.repeatCount = 0
        an.fillAfter = true
        arrowView!!.startAnimation(an)
    }

}