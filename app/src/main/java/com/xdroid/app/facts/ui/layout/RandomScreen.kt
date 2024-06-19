package com.xdroid.app.facts.ui.layout

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.xdroid.app.facts.BuildConfig
import com.xdroid.app.facts.R
import com.xdroid.app.facts.captureBitmapFromView
import com.xdroid.app.facts.data.models.*
import com.xdroid.app.facts.data.urls.*
import com.xdroid.app.facts.saveBitmapToFile
import com.xdroid.app.facts.ui.theme.*
import com.xdroid.app.facts.ui.vm.MyViewModel
import com.xdroid.app.service.utils.enums.Resource
import com.xdroid.app.service.utils.enums.Status
import com.xdroid.app.service.utils.helper.DynamicResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.wait
import org.koin.androidx.compose.koinViewModel
import java.util.*


@Composable
fun RandomScreen(navController: NavController, url: String) {
    val myViewModel: MyViewModel = koinViewModel()
    val page = rememberSaveable { mutableStateOf(1) }
    LaunchedEffect(Unit) {
        myViewModel.getObject(GetRandomWords)

    }
    val states by myViewModel.getObject.collectAsState(initial = Resource.idle())
    val dicStates by myViewModel.getDicObject.collectAsState(initial = Resource.idle())

    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {


        Spacer(modifier = Modifier.height(20.dp))

        when (states.status) {
            Status.SUCCESS -> {
                var quotes = ""
                var titles = ""
                val res = DynamicResponse.myObject<WordElement>(states.data)
                LaunchedEffect(Unit) {
                    if (GetDictionary != null) {
                        myViewModel.getDicObject(GetDictionary + res.word?.get(0))
                    }
                }

            }
            Status.ERROR -> {}
            Status.LOADING -> {
                CircularProgressIndicator()
            }
            Status.IDLE -> {
            }
        }

        when (dicStates.status) {
            Status.SUCCESS -> {
                var quotes = ""
                var titles = ""

                val res = DynamicResponse.myObject<DictionaryElement>(dicStates.data)
                titles = res.word ?: ""
                quotes = res.definition ?: ""
                myViewModel.addToMyDb(
                    dicStates.data!!,
                    category = "randomWords",
                    type = "randomWords"
                )

                if (quotes.isEmpty()) {
                    quotes = "$titles definition not found"
                }

                CardDesignRandom(quotes, myViewModel, url, titles.uppercase())

            }
            Status.ERROR -> {}
            Status.LOADING -> {
                CircularProgressIndicator()
            }
            Status.IDLE -> {
            }
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardDesignRandom(
    quotes: String,
    myViewModel: MyViewModel,
    url: String,
    qTitle: String = "",
    max: Int = 1,
    modifier: Modifier = Modifier,
    page: MutableState<Int> = mutableStateOf(1)
) {

    val selectedTextRanges = mutableStateListOf<TextRange>()
    var isUser by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp), horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ShareCardView()
        }

        Spacer(modifier = Modifier.height(100.dp))
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(white),
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.cardElevation(5.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp), horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = com.xdroid.app.facts.R.drawable.info_me),
                        contentDescription = "App image",
                        modifier = Modifier
                            .width(35.dp)
                            .height(70.dp),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(50.dp))
                    Text(
                        text = qTitle,
                        style = quoteTitle,
                    )

                }

//                Spacer(modifier = Modifier.height(10.dp))

                SelectionContainer() {
                    Text(
                        text = quotes,
                        style = quoteText,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 60.dp),
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))


            }

        }
        Spacer(modifier = Modifier.height(100.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp), horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = {
                myViewModel.resetDic()
                myViewModel.getObject(GetRandomWords)



            }) {
                Row(
                    modifier = Modifier
                        .padding(10.dp), horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(text = "Next", style = title, color = Color.Black)
                    Spacer(modifier = Modifier.width(5.dp))
                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = "Share Button",
                        tint = black
                    )
                }


            }

        }

    }

}



