package com.xdroid.app.facts

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.content.FileProvider
import com.google.android.gms.ads.MobileAds
import com.xdroid.app.facts.data.urls.ApiToken
import com.xdroid.app.facts.ui.base.BannerAdView
import com.xdroid.app.facts.ui.base.MyApp
import com.xdroid.app.facts.ui.theme.*
import com.xdroid.app.facts.ui.vm.MyViewModel
import com.xdroid.app.service.utils.constants.PrefConstant
import com.xdroid.app.service.utils.helper.DebugMode
import com.xdroid.app.service.utils.helper.NetworkHelper
import org.koin.android.ext.android.inject
import java.io.File
import java.io.FileOutputStream

class MainActivity : ComponentActivity() {
    private val networkHelper: NetworkHelper by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        com.xdroid.app.service.App.preferenceHelper.setValue(PrefConstant.AUTH_TOKEN, ApiToken)

        MobileAds.initialize(this) {}
        setContent {
            FactsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = backGroundColor
                ) {
                    Column() {
                    Box(modifier = Modifier.fillMaxWidth().weight(1f))
                    {
                        MyApp()
                    }


                    if (networkHelper.isNetworkConnected()) {
                        Box(modifier = Modifier
                            .fillMaxWidth())
                        {
                            BannerAdView()
                        }
                    }
                }
                }
            }
        }
    }
}

fun captureBitmapFromView(view: android.view.View): Bitmap {
    val bitmap = Bitmap.createBitmap(view.width, (view.height-200), Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bitmap)
    view.draw(canvas)
    return bitmap
}

fun saveBitmapToFile(context: Context, bitmap: Bitmap): Uri {
    val cachePath = File(context.cacheDir, "images")
    cachePath.mkdirs()
    val file = File(cachePath, "${currentTimeMillis}_quotes.png")
    val outputStream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    outputStream.close()
    // Grant URI
    /*  context.grantUriPermission(
        context.packageName,
        fileUri,
        Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
    )*/
    return FileProvider.getUriForFile(context, context.packageName + ".fileprovider", file)
}

val currentTimeMillis = System.currentTimeMillis()
