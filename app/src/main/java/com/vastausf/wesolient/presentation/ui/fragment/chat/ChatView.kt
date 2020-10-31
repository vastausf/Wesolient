package com.vastausf.wesolient.presentation.ui.fragment.chat

import com.vastausf.wesolient.data.Message
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.Skip

interface ChatView : MvpView {
    @AddToEndSingle
    fun updateChatHistory(chatHistory: List<Message>)

    @Skip
    fun showMessageMissScope()
}
