package com.core.coreSupport

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


enum class SupportMode {
    DIALOG, FULLSCREEN
}

@Parcelize
data class SupportUiConfig(
    val headerBackgroundColor: Long,
    val headingColor: Long,
    val backGroundColor: Long = 0xFFFFFFFF,
    val roundedCornerInDp: Int = 24
) : Parcelable


enum class SupportProviderType {
    FRESHDESK,
    HUBSPOT,
    CUSTOM_HTML
}

sealed class SupportData: Parcelable {
    @Parcelize
    data class Url(val url: String,val desc:String?=null) : SupportData()

    @Parcelize
    data class Html(val html: String) : SupportData()
}


interface SupportProvider {
    val type: SupportProviderType
    fun getSupportData(): SupportData
}

class FreshdeskProvider(private val url: String) : SupportProvider {
    override val type = SupportProviderType.FRESHDESK
    override fun getSupportData(): SupportData = SupportData.Url(url)
}


class HubSpotProvider(private val url: String,private val desc: String?) : SupportProvider {
    override val type = SupportProviderType.HUBSPOT
    override fun getSupportData(): SupportData = SupportData.Url(url,desc)
}


class CustomHtmlProvider(private val html: String) : SupportProvider {
    override val type = SupportProviderType.CUSTOM_HTML
    override fun getSupportData(): SupportData = SupportData.Html(html)
}