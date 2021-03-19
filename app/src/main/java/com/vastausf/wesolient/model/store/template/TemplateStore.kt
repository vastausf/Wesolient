package com.vastausf.wesolient.model.store.template

import com.vastausf.wesolient.data.common.Template

interface TemplateStore {
    suspend fun createTemplate(scopeUid: String, title: String, message: String)

    suspend fun editTemplate(scopeUid: String, uid: String, block: Template.() -> Unit)

    suspend fun deleteTemplate(scopeUid: String, uid: String)

    suspend fun getTemplate(scopeUid: String, uid: String)

    suspend fun getTemplateList(scopeUid: String)
}