package com.core.coreSupport

import android.content.Context
import android.content.Intent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb


object SupportHelper {

    fun showFreshdesk(
        context: Context,
        url: String,
        uiConfig: SupportUiConfig = SupportUiConfig(
            headerBackgroundColor = Color.Black.toArgb().toLong(),
            headingColor = Color.White.toArgb().toLong()
        )
    ) {
        launch(
            context = context,
            mode = SupportMode.DIALOG,
            type = SupportProviderType.FRESHDESK,
            data = SupportData.Url(url),
            uiConfig=uiConfig
        )
    }

    fun showHubspot(
        context: Context,
        url: String,
        desc: String? = null,
        uiConfig: SupportUiConfig = SupportUiConfig(
            headerBackgroundColor = Color.Black.toArgb().toLong(),
            headingColor = Color.White.toArgb().toLong()
        )
    ) {
        launch(
            context = context,
            mode = SupportMode.DIALOG,
            type = SupportProviderType.HUBSPOT,
            data = SupportData.Url(url, desc),
            uiConfig=uiConfig
        )
    }

    fun showCustomHtml(
        context: Context,
        html: String,
        uiConfig: SupportUiConfig = SupportUiConfig(
            headerBackgroundColor = Color.Black.toArgb().toLong(),
            headingColor = Color.White.toArgb().toLong()
        )
    ) {
        launch(
            context = context,
            mode = SupportMode.FULLSCREEN,
            type = SupportProviderType.CUSTOM_HTML,
            data = SupportData.Html(html),
            uiConfig=uiConfig
        )
    }

    // 🔒 Private common launcher
    private fun launch(
        context: Context,
        mode: SupportMode,
        type: SupportProviderType,
        data: SupportData,
        uiConfig: SupportUiConfig = SupportUiConfig(
            headerBackgroundColor = Color.Black.toArgb().toLong(),
            headingColor = Color.White.toArgb().toLong()
        )
    ) {

        val intent = Intent(context, SupportDialogActivity::class.java).apply {
            putExtra("mode", mode)
            putExtra("providerType", type)
            putExtra("supportData", data)
            putExtra("uiConfig", uiConfig)
        }
        context.startActivity(intent)
    }
}