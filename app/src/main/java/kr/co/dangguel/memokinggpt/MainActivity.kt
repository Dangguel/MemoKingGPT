package kr.co.dangguel.memokinggpt

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import dagger.hilt.android.AndroidEntryPoint
import kr.co.dangguel.memokinggpt.presentation.ads.AdManager
import kr.co.dangguel.memokinggpt.presentation.ui.navigation.AppNavGraph
import kr.co.dangguel.memokinggpt.presentation.util.UpdateManager
import kr.co.dangguel.memokinggpt.presentation.viewmodel.MainViewModel
import kr.co.dangguel.memokinggpt.ui.theme.MemoKingGPTTheme
import kr.co.dangguel.memokinggpt.util.ApiKeyProvider
import kr.co.dangguel.memokinggpt.utils.EncryptionUtil

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels() // ✅ ViewModel 연결

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MemoKingGPTTheme { // ✅ 테마 적용
                val navController = rememberNavController()

                AppNavGraph(navController = navController, viewModel = viewModel)
            }
        }

        AdManager.loadAd(this)
        UpdateManager.checkAndRedirectToStore(this)
    }
}
