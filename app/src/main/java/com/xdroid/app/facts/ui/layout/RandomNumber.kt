package com.xdroid.app.facts.ui.layout

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.xdroid.app.service.utils.helper.DebugMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


@Composable
fun RandomNumberInRange(min: Int, max: Int) {
    var randomNumber by remember { mutableStateOf(1) }

    val lifecycleOwner = LocalLifecycleOwner.current
    val random = remember { Random() }

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.Default) {
                val newRandomNumber = random.nextInt(max - min + 1) + min
                withContext(Dispatchers.Main) {
                    randomNumber = newRandomNumber

                    DebugMode.e("-------->>>>> second $randomNumber")
                }
            }
        }
    }


    // UI code here to display the random number
}