package com.vastausf.wesolient.model

import com.vastausf.wesolient.model.data.ScopeData

interface DesktopDataStore {
    fun create(title: String, url: String): ScopeData
}
