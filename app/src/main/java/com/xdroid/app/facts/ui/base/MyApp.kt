package com.xdroid.app.facts.ui.base

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.xdroid.app.facts.R
import com.xdroid.app.facts.ui.layout.NavigationScreen
import com.xdroid.app.facts.ui.layout.QuoteScreen
import com.xdroid.app.facts.ui.layout.RandomScreen
import com.xdroid.app.facts.ui.screens.ScreenName
import com.xdroid.app.service.App.Companion.preferenceHelper
import com.xdroid.app.service.utils.helper.PreferenceHelper


@Composable
fun MyApp() {
    val navController = rememberNavController()
    preferenceHelper = PreferenceHelper(LocalContext.current)
    /*All navigation are included here and all navigation are done from here.*/
    /*Add new route to app for navigation*/

    NavHost(navController, startDestination = ScreenName.NavigationScreen) {
        composable(ScreenName.Home) {
//            HomeScreen(navController)
        }
        composable(ScreenName.NavigationScreen) {
            NavigationScreen(navController)
        }
        composable(ScreenName.SimpleInterest) {
//            SimpleInterestScreen(navController)
        }
        composable(ScreenName.TimeConverter) {
//            TimeConverterScreen(navController)
        }
        composable(ScreenName.QuoteScreen + "?url={url}") { backstack ->
            val quote = backstack.arguments?.getString("url") ?: ""
            QuoteScreen(navController, quote)
        }
        composable(ScreenName.RandomScreen + "?url={url}") { backstack ->
            val quote = backstack.arguments?.getString("url") ?: ""
            RandomScreen(navController, quote)
        }

    }
}


@Composable
fun BannerAdView() {

    val context = LocalContext.current
    val adUnitIds = context.getString(R.string.bannerId)
    //"ca-app-pub-3940256099942544/6300978111"
    AndroidView(
        modifier = Modifier
            .fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                // Add your adUnitID, this is for testing.
                adUnitId = adUnitIds
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}