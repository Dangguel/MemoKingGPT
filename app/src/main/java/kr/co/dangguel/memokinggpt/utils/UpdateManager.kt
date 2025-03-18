package kr.co.dangguel.memokinggpt.presentation.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import kr.co.dangguel.memokinggpt.R
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability

object UpdateManager {

    fun checkAndRedirectToStore(activity: Activity) {
        val appUpdateManager = AppUpdateManagerFactory.create(activity)

        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                AlertDialog.Builder(activity)
                    .setTitle(activity.getString(R.string.update_required_title))
                    .setMessage(activity.getString(R.string.update_required_message))
                    .setCancelable(false)
                    .setPositiveButton(activity.getString(R.string.update_now)) { _, _ ->
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse("market://details?id=${activity.packageName}")
                            setPackage("com.android.vending")
                        }
                        activity.startActivity(intent)
                        activity.finish()
                    }
                    .setOnCancelListener {
                        activity.finish() // 뒤로가기 누르면 앱 종료
                    }
                    .show()
            }
        }
    }
}
