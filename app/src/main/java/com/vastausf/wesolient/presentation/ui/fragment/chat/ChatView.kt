package com.vastausf.wesolient.presentation.ui.fragment.chat

import com.vastausf.wesolient.data.client.Frame
import com.vastausf.wesolient.data.common.Scope
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.Skip

interface ChatView : MvpView {
    @AddToEndSingle
    fun bindData(scope: Scope)

    @Skip
    fun bindMessageTemplate(template: String)

    @AddToEndSingle
    fun updateChatHistory(chatHistory: List<Frame>)

    @Skip
    fun onConnectionError()

    @Skip
    fun onIllegalUrl()

    @Skip
    fun onMissScope()

    @AddToEndSingle
    fun changeConnectionState(newState: Boolean?)

    @AddToEndSingle
    fun onSend()

    @AddToEndSingle
    fun onDisconnectWithReason(code: Int, reason: String)
}
