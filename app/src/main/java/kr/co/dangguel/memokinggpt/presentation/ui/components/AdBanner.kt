package kr.co.dangguel.memokinggpt.presentation.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.unit.dp
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration

@SuppressLint("RememberReturnType")
@Composable
fun AdBanner(context: Context) {
    remember {
        MobileAds.initialize(context) {}

        val testDeviceIds = listOf("440A3EF167A216D158E68C26D43B86C0")
        val requestConfiguration = RequestConfiguration.Builder()
            .setTestDeviceIds(testDeviceIds)
            .build()
        MobileAds.setRequestConfiguration(requestConfiguration)
    }

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp), // ✅ 배너 광고 기본 크기
        factory = {
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = "ca-app-pub-8126188547687864/6883155852" // 내 id
                //adUnitId = "ca-app-pub-3940256099942544/6300978111" // test 광고
                val adRequest = AdRequest.Builder().build()
                adListener = object : AdListener() {
                    override fun onAdLoaded() {
                        Log.d("AdMob", "광고 로드 성공!")
                    }

                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        Log.e("AdMob", "광고 로드 실패: ${adError.message}")
                    }
                }
                loadAd(AdRequest.Builder().build())
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }
    )
}
