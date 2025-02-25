package kr.co.dangguel.memokinggpt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import dagger.hilt.android.AndroidEntryPoint
import kr.co.dangguel.memokinggpt.presentation.ui.navigation.AppNavGraph
import kr.co.dangguel.memokinggpt.presentation.viewmodel.MainViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels() // ✅ ViewModel 연결

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MobileAds.initialize(this) {} // ✅ AdMob SDK 초기화

        // ✅ 테스트 디바이스 추가
        val testDeviceIds = listOf("440A3EF167A216D158E68C26D43B86C0")
        val requestConfiguration = RequestConfiguration.Builder()
            .setTestDeviceIds(testDeviceIds)
            .build()
        MobileAds.setRequestConfiguration(requestConfiguration)

        setContent {
            val navController = rememberNavController()
            AppNavGraph(navController = navController, viewModel = viewModel)
        }
    }
}
