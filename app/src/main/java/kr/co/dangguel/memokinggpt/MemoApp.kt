package kr.co.dangguel.memokinggpt

import android.app.Application
import android.content.res.Configuration
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale

@HiltAndroidApp
class MemoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        setLocale()
    }

    private fun setLocale() {
        val locale = Locale.getDefault()
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}
