package com.xdroid.app.facts.ui.layout

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.rememberInfiniteTransition
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
import androidx.compose.runtime.R
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.xdroid.app.facts.BuildConfig
import com.xdroid.app.facts.captureBitmapFromView
import com.xdroid.app.facts.data.models.*
import com.xdroid.app.facts.data.personal.ApiClient
import com.xdroid.app.facts.data.personal.AppExecutors
import com.xdroid.app.facts.data.urls.*
import com.xdroid.app.facts.saveBitmapToFile
import com.xdroid.app.facts.ui.theme.*
import com.xdroid.app.facts.ui.vm.MyViewModel
import com.xdroid.app.service.App
import com.xdroid.app.service.App.Companion.preferenceHelper
import com.xdroid.app.service.utils.constants.PrefConstant
import com.xdroid.app.service.utils.enums.Resource
import com.xdroid.app.service.utils.enums.Status
import com.xdroid.app.service.utils.helper.DebugMode
import com.xdroid.app.service.utils.helper.DynamicResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
import java.util.*


@Composable
fun QuoteScreen(navController: NavController, url: String) {
    val myViewModel: MyViewModel = koinViewModel()
    val page = rememberSaveable { mutableStateOf(1) }
    LaunchedEffect(Unit) {
        if (BuildConfig.FLAVOR != "user") {
            myViewModel.getFacts(url)
        } else {
            myViewModel.getFactsUser(url, page = page.value)
        }
    }
    val states by myViewModel.getFacts.collectAsState(initial = Resource.idle())
    val statesUser by myViewModel.getFactsUser.collectAsState(initial = Resource.idle())

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

//                RandomNumberInRange(min = 1, max = 10)
                when (url) {
                    GetFacts -> {
                        val res = DynamicResponse.myArray<FactLists>(states.data)
                        quotes = "\n\n${res[0].fact}"
                        titles = "Did you know?"
                        myViewModel.addToMyDb(states.data!!, category = "facts", type = "facts")
                    }
                    GetJokes -> {
                        val res = DynamicResponse.myArray<JokeList>(states.data)
                        quotes = "${res[0].joke}"
                        myViewModel.addToMyDb(states.data!!, category = "jokes", type = "jokes")
                    }
                    GetDadJokes -> {
                        val res = DynamicResponse.myArray<JokeList>(states.data)
                        quotes = "${res[0].joke}"
                        myViewModel.addToMyDb(
                            states.data!!,
                            category = "dadJokes",
                            type = "dadJokes"
                        )
                    }
                    GetQuotes -> {
                        val res = DynamicResponse.myArray<QuoteList>(states.data)
                        quotes = "${res[0].quote} \n\n\n -${res[0].author}"
                        val type = "${res[0].category}"
                        myViewModel.addToMyDb(states.data!!, category = "quotes", type = type)
                    }
//                    GetRandomWords -> {
//                        val res = DynamicResponse.myObject<WordElement>(states.data)
//                        quotes = res.word
//                    }
                    else -> {
                        myViewModel.addToMyDb(states.data!!, category = "facts", type = "facts")
                        val res = DynamicResponse.myArray<FactLists>(states.data)
                        quotes = "${res[0].fact}"
                    }
                }
//                val res = DynamicResponse.myArray<FactLists>(states.data)
//                quotes = "${res[0].fact}"

                CardDesign(quotes, myViewModel, url, titles)
                /*  Spacer(modifier = Modifier.height(20.dp))
                  Button(onClick = {
                      myViewModel.getFacts()

                  }) {
                      Text(text = "Next Fact", style = normalText, color = white)

                  }*/
            }
            Status.ERROR -> {}
            Status.LOADING -> {
                CircularProgressIndicator()
            }
            Status.IDLE -> {
            }
        }
        when (statesUser.status) {
            Status.SUCCESS -> {
                var quotes = ""
                var titles = ""
                var max = 1

//                RandomNumberInRange(min = 1, max = 10)
                when (url) {
                    GetFacts -> {
                        val res =
                            DynamicResponse.myObject<BaseModel<FactListElement>>(statesUser.data)
                        quotes = "${res.items?.get(0)?.data?.get(0)?.fact}"
                        titles = "Did you know?"
                        max = res.totalPages!!
                    }
                    GetJokes -> {
                        val res =
                            DynamicResponse.myObject<BaseModel<JokeListElement>>(statesUser.data)
                        quotes = "${res.items?.get(0)?.data?.get(0)?.joke}"
                        max = res.totalPages!!
                    }
                    GetDadJokes -> {
                        val res =
                            DynamicResponse.myObject<BaseModel<JokeListElement>>(statesUser.data)
                        if (res.items!!.isNotEmpty()) {
                            quotes = "${res.items[0].data?.get(0)?.joke}"
                            max = res.totalPages!!
                        } else {
                            quotes = ""
                            max = 0
                        }
                    }
                    GetQuotes -> {
                        val res =
                            DynamicResponse.myObject<BaseModel<QuoteListElement>>(statesUser.data)
                        quotes = "${res.items?.get(0)?.data?.get(0)?.quote} \n\n\n -${
                            res.items?.get(0)?.data?.get(0)?.author
                        }"
                        max = res.totalPages!!

                    }
                    GetRandomWords -> {
                        val res =
                            DynamicResponse.myObject<BaseModelObject<DictionaryElement>>(statesUser.data)
                        if (res.items!!.isNotEmpty()) {
                            quotes = "${res.items[0].data?.definition}"
                            titles = "${res.items[0].data?.word}"
                            if (quotes.isEmpty()) {
                                quotes = "$titles definition not found"
                            }
                            titles = titles.uppercase()
                            max = res.totalPages!!
                        } else {
                            quotes = ""
                            max = 0
                        }
                    }
                    else -> {
                        val res =
                            DynamicResponse.myObject<BaseModel<FactListElement>>(statesUser.data)
                        quotes = "${res.items?.get(0)?.data?.get(0)?.fact}"
                        max = res.totalPages!!
                    }
                }
//                val res = DynamicResponse.myArray<FactLists>(states.data)
//                quotes = "${res[0].fact}"

//                CardDesign(quotes, myViewModel, url, titles)
                AnimatedCard(quotes, myViewModel, url, titles, max, true, page)
                /*  Spacer(modifier = Modifier.height(20.dp))
                  Button(onClick = {
                      myViewModel.getFacts()

                  }) {
                      Text(text = "Next Fact", style = normalText, color = white)

                  }*/
            }
            Status.ERROR -> {}
            Status.LOADING -> {
                CircularProgressIndicator()
//                AnimatedCard(isOpenEndRange = false)
            }
            Status.IDLE -> {
            }
        }
    }
}


@Composable
fun AnimatedCard(
    quotes: String = "",
    myViewModel: MyViewModel = koinViewModel(),
    url: String = "",
    qTitle: String = "",
    max: Int = 1,
    isOpenEndRange: Boolean = false,
    page: MutableState<Int> = mutableStateOf(1)
) {
    var isOpen by remember {
        mutableStateOf(isOpenEndRange)
    }
    val scale by animateFloatAsState(
        targetValue = if (isOpen) 1f else 0f,
        animationSpec = tween(1000),
        label = "scale"
    )


    Box(
        modifier = Modifier
    ) {

        CardDesign(quotes, myViewModel, url, qTitle, max,
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    transformOrigin = TransformOrigin.Center
                }

                .align(Alignment.Center),
            page)


    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardDesign(
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

            /*    IconButton(onClick = { }) {
                    Icon(
                        Icons.Default.FavoriteBorder,
                        contentDescription = "Share Button",
                        tint = black
                    )

                }*/
            Button(onClick = {
                if (BuildConfig.FLAVOR != "user") {
                    myViewModel.getFacts(url)
                } else {
                    isUser = true
                }

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

            if (isUser) {
                var randomNumber by rememberSaveable { mutableStateOf(1) }

                val lifecycleOwner = LocalLifecycleOwner.current
                val random = remember { Random() }

                LaunchedEffect(Unit) {
                    lifecycleOwner.lifecycleScope.launch {
                        withContext(Dispatchers.Default) {
                            if (max > 1) {
                                val newRandomNumber = random.nextInt(max - 1 + 1) + 1
                                withContext(Dispatchers.Main) {
                                    randomNumber = newRandomNumber
                                    page.value = randomNumber
                                    myViewModel.getFactsUser(url, randomNumber)

                                }
                            }
                        }
                    }
                }
            }

        }

    }

}


@Composable
fun ShareCardView() {
    val context = LocalContext.current
    val view = LocalView.current

    var sd by remember {
        mutableStateOf(
            false
        )
    }

    Box(modifier = Modifier
        .border(1.dp, color = white, shape = RoundedCornerShape(40.dp))
        .clickable {
            sd = true
        }) {
        Row(
            modifier = Modifier
                .padding(horizontal = 25.dp, vertical = 15.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(text = "Share", style = title, color = white)
            Spacer(modifier = Modifier.width(5.dp))
            Icon(
                Icons.Default.Share, contentDescription = "Share Button", tint = white,
            )
        }


    }
    if (sd) {
        val bitmap = captureBitmapFromView(view)
        // Share the screenshot as an image

        val file: Uri = saveBitmapToFile(context, bitmap)


        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "image/png"
            addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            putExtra(Intent.EXTRA_STREAM, file)
        }

        val shareChooser = Intent.createChooser(shareIntent, null)
        val resInfoList: List<ResolveInfo> = context.packageManager
            .queryIntentActivities(shareChooser, PackageManager.MATCH_DEFAULT_ONLY)

        for (resolveInfo in resInfoList) {
            val packageName: String = resolveInfo.activityInfo.packageName
            context.grantUriPermission(
                packageName,
                file,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
        context.startActivity(shareChooser)
        sd = false
        /* val shareIntent = Intent().apply {
             action = Intent.ACTION_SEND
             type = "image/png"
             val bitmapUri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues())
             putExtra(Intent.EXTRA_STREAM, bitmapUri)
         }
         val shareChooser = Intent.createChooser(shareIntent, null)
         context.startActivity(shareChooser)*/
    }
}

