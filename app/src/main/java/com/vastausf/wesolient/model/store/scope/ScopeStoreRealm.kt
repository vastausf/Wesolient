package com.vastausf.wesolient.model.store.scope

import com.vastausf.wesolient.data.common.Scope
import com.vastausf.wesolient.model.getAndSubscribe
import io.realm.Realm
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ScopeStoreRealm
@Inject
constructor(
    private val realm: Realm
) : ScopeStore {
    override fun createScope(title: String, url: String) {
        realm.executeTransactionAsync {
            val scope = Scope().apply {
                this.title = title
                this.url = url
            }

            it.copyToRealmOrUpdate(scope)
        }
    }

    override fun editScope(uid: String, block: Scope?.() -> Unit) {
        realm.executeTransactionAsync {
            val scope = it.where(Scope::class.java)
                .equalTo(Scope::uid.name, uid)
                .findFirst()

            scope.block()

            it.copyToRealmOrUpdate(scope)
        }
    }

    override fun deleteScope(uid: String) {
        realm.executeTransactionAsync {
            it.where(Scope::class.java)
                .equalTo(Scope::uid.name, uid)
                .findFirst()
                ?.deleteFromRealm()

        }
    }

    override fun getScope(uid: String): StateFlow<Scope?> {
        return realm.getAndSubscribe {
            where(Scope::class.java)
                .equalTo(Scope::title.name, uid)
                .findFirst()
        }
    }

    override fun getScopeList(): StateFlow<List<Scope>?> {
        return realm.getAndSubscribe {
            where(Scope::class.java).findAll()
        }
    }
}
