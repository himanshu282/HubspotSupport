package com.core.coreSupport

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp


class SupportDialogActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mode = intent.getSerializableExtra("mode") as? SupportMode ?: SupportMode.DIALOG
        val uiConfig = intent.getParcelableExtra<SupportUiConfig>("uiConfig")
            ?: SupportUiConfig(Color.Black.toArgb().toLong(), Color.White.toArgb().toLong())

        if (mode == SupportMode.FULLSCREEN) {
            setTheme(R.style.Theme_SupportFullScreen)
        } else {
            setTheme(R.style.Theme_SupportDialog)
        }

        val type = intent.getSerializableExtra("providerType") as? SupportProviderType
        val data = intent.getParcelableExtra<SupportData>("supportData")

        if (type == null || data == null) finish()

        val provider: SupportProvider? = when (type) {
            SupportProviderType.FRESHDESK -> (data as? SupportData.Url)?.let { FreshdeskProvider(it.url) }
            SupportProviderType.HUBSPOT -> (data as? SupportData.Url)?.let { HubSpotProvider(it.url, it.desc) }
            SupportProviderType.CUSTOM_HTML -> (data as? SupportData.Html)?.let { CustomHtmlProvider(it.html) }
            else -> null
        }



        setContent {

            when (mode) {
                SupportMode.DIALOG -> {
                    SupportDialog(
                        provider = provider!!,
                        headerBackgroundColor = Color(uiConfig.headerBackgroundColor.toInt()),
                        headingColor = Color(uiConfig.headingColor.toInt()),
                        backGroundColor = Color(uiConfig.backGroundColor.toInt()),
                        roundedCornerInDp = uiConfig.roundedCornerInDp.dp
                    ) { success ->
                        val result = Intent().apply {
                            putExtra("dialogResult", success)
                        }
                        setResult(RESULT_OK, result)
                        finish()
                    }
                }
                SupportMode.FULLSCREEN -> {
                    SupportFullScreen(
                        provider = provider!!,
                        headerBackgroundColor = Color(uiConfig.headerBackgroundColor.toInt()),
                        headingColor = Color(uiConfig.headingColor.toInt()),
                        backGroundColor = Color(uiConfig.backGroundColor.toInt()),
                    ) { success ->
                        val result = Intent().apply {
                            putExtra("dialogResult", success)
                        }
                        setResult(RESULT_OK, result)
                        finish()
                    }
                }
            }
        }
    }
}
