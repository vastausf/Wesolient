package com.vastausf.wesolient.model

import com.vastausf.wesolient.model.data.DesktopData

interface DesktopDataStore {
    fun create(title: String, url: String): DesktopData
}
