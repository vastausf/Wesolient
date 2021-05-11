package com.vastausf.wesolient.presentation.ui.screen.scope

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vastausf.wesolient.add
import com.vastausf.wesolient.data.client.*
import com.vastausf.wesolient.data.common.Scope
import com.vastausf.wesolient.data.common.Settings
import com.vastausf.wesolient.model.ServiceCreator
import com.vastausf.wesolient.model.store.scope.ScopeStore
import com.vastausf.wesolient.model.store.settings.SettingsStore
import com.vastausf.wesolient.model.store.template.TemplateStore
import com.vastausf.wesolient.model.store.vatiable.VariableStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScopeViewModel
@Inject
constructor(
    private val scopeStore: ScopeStore,
    private val templateStore: TemplateStore,
    private val variableStore: VariableStore,
    settingsStore: SettingsStore,
    private val serviceCreator: ServiceCreator
) : ViewModel() {
    var settings = settingsStore.getSettings()

    lateinit var scope: StateFlow<Scope?>

    lateinit var serviceHolder: ServiceCreator.ServiceHolder

    private var _connectionState = MutableStateFlow(SystemCode.CONNECTION_CLOSED)
    var connectionState = _connectionState.asStateFlow()

    private var _messages = MutableStateFlow<List<Message>>(emptyList())
    var messages = _messages.asStateFlow()

    fun loadScope(uid: String) {
        scope = scopeStore.getScope(uid)

        viewModelScope.launch {
            val scopeNotNull = scope.filterNotNull().first()
            val settingsNotNull = settings.filterNotNull().first()

            connectToService(
                scopeNotNull,
                settingsNotNull
            )
        }
    }

    private fun connectToService(
        scope: Scope,
        settings: Settings
    ) {
        _connectionState.value = SystemCode.CONNECTION_OPENING

        serviceHolder = serviceCreator.create(
            scope.url,
            settings.retryOnConnectionFailure
        )

        viewModelScope.launch {
            serviceHolder.connect().collect { message ->
                _messages.add(message)

                if (message is SystemMessage) {
                    _connectionState.value = message.code
                }
            }
        }
    }

    fun sendTextMessage(string: String) {
        serviceHolder.service.sendMessage(string)
        _messages.add(ClientMessage(
            content = string,
            templateUid = null
        ))
    }
}
