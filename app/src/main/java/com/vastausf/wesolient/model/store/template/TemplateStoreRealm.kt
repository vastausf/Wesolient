package com.vastausf.wesolient.model.store.template

import com.vastausf.wesolient.data.common.Template
import io.realm.Realm
import javax.inject.Inject

class TemplateStoreRealm
@Inject
constructor(
    private val realm: Realm
) : TemplateStore {
    override suspend fun createTemplate(scopeUid: String, title: String, message: String) {

    }

    override suspend fun editTemplate(scopeUid: String, uid: String, block: Template.() -> Unit) {

    }

    override suspend fun deleteTemplate(scopeUid: String, uid: String) {

    }

    override suspend fun getTemplate(scopeUid: String, uid: String) {

    }

    override suspend fun getTemplateList(scopeUid: String) {

    }
}
