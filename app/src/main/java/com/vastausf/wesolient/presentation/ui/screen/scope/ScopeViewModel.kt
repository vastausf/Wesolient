package com.vastausf.wesolient.presentation.ui.screen.scope

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vastausf.wesolient.add
import com.vastausf.wesolient.data.client.ClientMessage
import com.vastausf.wesolient.data.client.ConnectionState
import com.vastausf.wesolient.data.client.Message
import com.vastausf.wesolient.data.client.SystemMessage
import com.vastausf.wesolient.data.common.Scope
import com.vastausf.wesolient.model.ServiceCreator
import com.vastausf.wesolient.model.store.scope.ScopeStore
import com.vastausf.wesolient.model.store.settings.SettingsStore
import com.vastausf.wesolient.model.store.template.TemplateStore
import com.vastausf.wesolient.model.store.vatiable.VariableStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
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

    private var _connectionState = MutableStateFlow(ConnectionState.CLOSED)
    var connectionState = _connectionState.asStateFlow()

    private var _messages = MutableStateFlow<List<Message>>(emptyList())
    var messages = _messages.asStateFlow()

    fun loadScope(uid: String) {
        scope = scopeStore.getScope(uid)
    }

    fun connect() {
        viewModelScope.launch {
            val scope = scope.filterNotNull().first()
            val settings = settings.filterNotNull().first()

            _connectionState.value = ConnectionState.OPENING

            serviceHolder = serviceCreator.create(
                scope.url,
                settings.retryOnConnectionFailure
            )

            serviceHolder.connect().collect { message ->
                _messages.add(message)

                if (message is SystemMessage) {
                    _connectionState.value = message.code
                }
            }
        }
    }

    fun disconnect(
        code: Int? = null,
        reason: String? = null
    ) {
        _connectionState.value = ConnectionState.CLOSING

        serviceHolder.disconnect(code, reason)
    }

    fun sendTextMessage(string: String) {
        serviceHolder.service.sendMessage(string)
        _messages.add(
            ClientMessage(
                content = string,
                templateUid = null
            )
        )
    }

    override fun onCleared() {
        viewModelScope.cancel()

        try {
            serviceHolder.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
