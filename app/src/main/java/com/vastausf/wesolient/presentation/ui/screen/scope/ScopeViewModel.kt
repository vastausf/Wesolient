package com.vastausf.wesolient.presentation.ui.screen.scope

import androidx.lifecycle.ViewModel
import com.vastausf.wesolient.data.client.Frame
import com.vastausf.wesolient.data.common.Scope
import com.vastausf.wesolient.data.common.Settings
import com.vastausf.wesolient.model.store.scope.ScopeStore
import com.vastausf.wesolient.model.store.settings.SettingsStore
import com.vastausf.wesolient.model.store.template.TemplateStore
import com.vastausf.wesolient.model.store.vatiable.VariableStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ScopeViewModel
@Inject
constructor(
    private val scopeStore: ScopeStore,
    private val templateStore: TemplateStore,
    private val variableStore: VariableStore,
    settingsStore: SettingsStore
) : ViewModel() {
    var settings = settingsStore.getSettings()

    lateinit var scope: StateFlow<Scope?>

    private val _messages = MutableStateFlow<List<Frame>>(emptyList())
    val messages = _messages.asStateFlow()

    fun loadScope(uid: String) {
        scope = scopeStore.getScope(uid)
    }
}
