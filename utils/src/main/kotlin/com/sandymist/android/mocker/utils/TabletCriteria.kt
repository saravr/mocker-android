package com.scribd.android.mocker.utils

import android.content.Context
import android.content.res.Configuration
import android.util.DisplayMetrics
import android.view.WindowManager
import javax.inject.Inject
import kotlin.math.sqrt

/*
 * This utility is here to determine if we consider a device or a tablet.  We have defined this as a 6inch diagonal,
 * for analytics purposes.
 * For many UI concerns, you may wish to use current screen width instead, to display based on
 * portrait/landscape configuration, but for determining the type of device, the configuration should
 * not affect device type.
 */
class TabletCriteria @Inject constructor(config: Configuration) {
    private val tabletSize = 6.0 // arbitrary 6inch diagonal cutoff

    // Example sizes:
    // Galaxy Nexus: 360 dp
    // Nexus 5: 360 dp
    // Nexus 6: 410 dp
    // Nexus 7 (2013): 600 dp
    private val minTabletSwDp = 480

    @Volatile
    private var smallestWidthDp = 0

    init {
        smallestWidthDp = config.smallestScreenWidthDp
    }

    /**
     * This method is intended for analytics and does not correspond to any layout qualifier - using this to make
     * decisions about what views to use is generally not a good idea.
     * (for extra bonus points, this can likely change on rotation due to the fact that getMetrics does not include
     * decorations such as the status bar and navigation area)
     */
    @Deprecated("")
    @Suppress("DEPRECATION") // TODO: Fix the algorithm, ensure it is optimal, doesn't pull more dependencies
    fun isTablet(context: Context): Boolean {
        val metrics = DisplayMetrics()
        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(
            metrics,
        )

        // a^2 + b^2 = c^2, said pythagoras.  then divide by dpi for inches.
        val inches =
            (
                sqrt((metrics.widthPixels * metrics.widthPixels + metrics.heightPixels * metrics.heightPixels).toDouble()) /
                    metrics.densityDpi
                )
        return inches > tabletSize
    }

    fun shouldBeReferredToAsTablet(): Boolean {
        return smallestWidthDp >= minTabletSwDp
    }
}
