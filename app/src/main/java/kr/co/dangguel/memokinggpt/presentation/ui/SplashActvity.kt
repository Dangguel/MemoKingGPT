package kr.co.dangguel.memokinggpt.presentation.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import kr.co.dangguel.memokinggpt.MainActivity

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen() // ✅ SplashScreen API 사용
        super.onCreate(savedInstanceState)

        // ✅ 스플래시 화면 유지할 시간 설정 가능
        splashScreen.setKeepOnScreenCondition { false }

        // ✅ 메인 액티비티로 이동
        startActivity(Intent(this, MainActivity::class.java))
        finish() // ✅ 스플래시 액티비티 종료
    }
}
