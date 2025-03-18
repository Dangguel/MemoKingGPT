package kr.co.dangguel.memokinggpt.presentation.util

import android.app.Activity
import com.google.android.play.core.review.ReviewManagerFactory

object InAppReviewManager {
    fun launchReviewFlow(activity: Activity) {
        val reviewManager = ReviewManagerFactory.create(activity)
        val request = reviewManager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                reviewManager.launchReviewFlow(activity, reviewInfo)
            }
        }
    }
}
