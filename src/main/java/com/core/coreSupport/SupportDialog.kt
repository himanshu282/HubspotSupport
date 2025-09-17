package com.core.coreSupport

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.icu.text.ListFormatter.Width
import android.net.Uri
import android.util.Log
import android.view.RoundedCorner
import android.view.ViewGroup
import android.webkit.ConsoleMessage
import android.webkit.CookieManager
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch


@Composable
fun SupportDialog(
    desc: String? = null,
    provider: SupportProvider,
    widthInDp: Dp=512.dp,
    paddingValues: PaddingValues=PaddingValues(vertical = 24.dp),
    roundedCornerInDp: Dp=24.dp,
    backGroundColor: Color= Color.White,
    headerBackgroundColor:Color = Color.Black,
    headingColor: Color = Color.White,
    setShowDialog: (Boolean) -> Unit,
) {

    Dialog(onDismissRequest = {
        setShowDialog(false)
    }) {
        SupportContent(
            desc = desc,
            provider = provider,
            widthInDp=widthInDp,
            paddingValues=paddingValues,
            roundedCornerInDp=roundedCornerInDp,
            backGroundColor = backGroundColor,
            headerBackgroundColor = headerBackgroundColor,
            headingColor = headingColor,
            setShowDialog = setShowDialog
        )
    }


}


@Composable
fun SupportFullScreen(
    desc: String? = null,
    provider: SupportProvider,
    paddingValues: PaddingValues=PaddingValues(vertical = 0.dp),
    roundedCornerInDp: Dp=0.dp,
    widthInDp: Dp?=null,
    backGroundColor: Color= Color.White,
    headerBackgroundColor:Color = Color.Black,
    headingColor: Color = Color.White,
    setShowDialog: (Boolean) -> Unit,
) {
    Scaffold(
        modifier = Modifier.padding(paddingValues),
    ) { padding ->
        SupportContent(
            desc = desc,
            provider = provider,
            widthInDp=widthInDp,
            paddingValues= padding,
            roundedCornerInDp=roundedCornerInDp,
            backGroundColor = backGroundColor,
            headerBackgroundColor = headerBackgroundColor,
            headingColor = headingColor,
            setShowDialog = setShowDialog
        )
    }
}

@Composable
fun SupportContent(
    desc: String? = null,
    provider: SupportProvider,
    widthInDp: Dp?=512.dp,
    paddingValues: PaddingValues=PaddingValues(vertical = 24.dp),
    roundedCornerInDp: Dp=24.dp,
    backGroundColor: Color= Color.White,
    headerBackgroundColor:Color = Color.Black,
    headingColor: Color = Color.White,
    setShowDialog: (Boolean) -> Unit,
) {

    val contentModifier = Modifier.then(
        if (widthInDp != null) Modifier
            .widthIn(widthInDp)
            .fillMaxWidth() else Modifier.fillMaxWidth()
        ).fillMaxHeight()



    Surface(
        modifier = contentModifier
            .padding(paddingValues),
        color = backGroundColor,
        shape = RoundedCornerShape(roundedCornerInDp)
    ) {
        Box(
            modifier = Modifier,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                // Header
                Box (
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(headerBackgroundColor)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            modifier = Modifier.padding(24.dp),
                            text = "support",
                            style = typography.headlineMedium,
                            color = headingColor
                        )
                        Image(
                            painter = painterResource(id = R.drawable.close),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(color = headingColor),
                            modifier = Modifier
                                .clickable { setShowDialog(false) }
                                .padding(24.dp)
                                .size(24.dp)
                        )
                    }
                }

                // Body
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .fillMaxSize()
                ) {
                    when(provider.type){
                        SupportProviderType.FRESHDESK ->{

                        }
                        SupportProviderType.HUBSPOT -> {
                            val data = provider.getSupportData() as SupportData.Url
                            WebView(
                                modifier = Modifier.fillMaxSize(),
                                url = "${data.url}?content=${data.desc ?: ""}"
                            )
                        }
                        SupportProviderType.CUSTOM_HTML -> {

                        }
                    }


                }
            }

        }
    }


}



@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebView(modifier: Modifier=Modifier,url: String) {
    var loadURL = url
    val context = LocalContext.current
    val openFullDialogCustom = remember { mutableStateOf(false) }
    val refreshing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var backEnabled by remember { mutableStateOf(false) }
    var webView: WebView? = remember {
        null
    }

    val networkAvailable = remember { mutableStateOf(true) }
    val showErrorDialog = remember(networkAvailable.value) {
        derivedStateOf {
            !networkAvailable.value
        }
    }

    fun refresh() = coroutineScope.launch {
        webView?.reload()
    }

//    val state = rememberPullToRefreshState(refreshing, ::refresh)
    var filePathCallback_ by remember { mutableStateOf<ValueCallback<Array<Uri>>?>(null) }
    val getFileResultLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        filePathCallback_?.onReceiveValue(uris.toTypedArray())
        filePathCallback_ = null
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
//            .pullRefresh(state = state),

        ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            AndroidView(
                modifier = Modifier
                    .weight(1f),
                factory = {
                    WebView(it).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                        )

                        // to play video on a web view
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true

                        CookieManager.getInstance().setAcceptCookie(true)

                        // to verify that the client requesting your web page is actually your Android app.
                        settings.userAgentString = "mobile_app"+ settings.userAgentString

                        settings.useWideViewPort = true

                        webChromeClient = object : WebChromeClient() {
                            override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                                Log.d("WebViewConsole", "${consoleMessage.message()} at ${consoleMessage.sourceId()}:${consoleMessage.lineNumber()}")
                                return true
                            }
                            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                if (progress >= 20) {
                                    openFullDialogCustom.value = false
                                }
                            }
                            override fun onShowFileChooser(
                                webView: WebView?,
                                filePathCallback: ValueCallback<Array<Uri>>?,
                                fileChooserParams: FileChooserParams?
                            ): Boolean {
                                filePathCallback_ = filePathCallback
                                val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                                    addCategory(Intent.CATEGORY_OPENABLE)
                                    type = "*/*"
                                }
                                getFileResultLauncher.launch(intent.toString())
                                return true
                            }
                        }

                        webViewClient = object : WebViewClient() {

                            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                                super.onReceivedError(view, request, error)
                                Log.d("onReceivedError", "error")
                            }

                            override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
                                Log.d("onPageStarted", "url- $url")

                                openFullDialogCustom.value = true
                                backEnabled = view.canGoBack()
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                openFullDialogCustom.value = false
//                                networkAvailable.value = InternetUtil.isNetworkAvailable(context)

                            }
                        }
                        loadUrl(loadURL)
                        webView = this
                    }
                },
                update = {
                    webView = it
                },
            )
        }

        BackHandler(enabled = backEnabled) {
            webView?.goBack()
        }

        if (showErrorDialog.value) {
//            PlaceholderScreen(
//                modifier = Modifier.fillMaxSize(),
//                title = stringResource(id = com.danish.common.R.string.something_went_wrong),
//                subTitle = "",
//                resId = R.drawable.error_state_icon,
//                buttonTitle = stringResource(id = com.danish.common.R.string.retry)
//            ) {
//                webView?.reload()
//            }
        }


//        PullRefreshIndicator(
//            refreshing,
//            state,
//            Modifier.align(Alignment.TopCenter),
//        )
    }
}