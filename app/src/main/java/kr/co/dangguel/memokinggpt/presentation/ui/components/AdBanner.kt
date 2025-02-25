package kr.co.dangguel.memokinggpt.presentation.ui.components

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.unit.dp
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

@Composable
fun AdBanner(context: Context) {
    LaunchedEffect(Unit) {
        MobileAds.initialize(context) {}
    }

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp), // ✅ 배너 광고 기본 크기
        factory = {
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = "ca-app-pub-8126188547687864/2940195976"
                loadAd(AdRequest.Builder().build())
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }
    )
}
