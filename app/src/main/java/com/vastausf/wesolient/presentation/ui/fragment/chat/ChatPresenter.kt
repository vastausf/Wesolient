package com.vastausf.wesolient.presentation.ui.fragment.chat

import android.util.Log
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import com.vastausf.wesolient.data.Message
import com.vastausf.wesolient.model.ScopeStore
import com.vastausf.wesolient.model.SocketService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter
import okhttp3.OkHttpClient
import javax.inject.Inject

@InjectViewState
class ChatPresenter
@Inject
constructor(
    private val scopeStoreRealm: ScopeStore,
    private val scarletBuilder: Scarlet.Builder
) : MvpPresenter<ChatView>() {
    private lateinit var service: SocketService

    private val messageList: MutableList<Message> = mutableListOf()

    fun provideScopeTitle(title: String) {
        val scope = scopeStoreRealm
            .getByTitle(title)

        if (scope != null) {
            createService(title)
        } else {
            viewState.showMessageMissScope()
        }
    }

    fun onMessageSend(message: String) {
        if (message.isNotEmpty()) {
            sendMessage(message)
        }
    }

    private fun sendMessage(message: String) {
        service.sendMessage(message)
        addNewMessage(message, Message.Source.Client)
    }

    private fun systemMessage(message: String) {
        addNewMessage(message, Message.Source.Client)
    }

    private fun createService(title: String) {
        service = scarletBuilder
            .webSocketFactory(
//                OkHttpClient.Builder().build().newWebSocketFactory(title)
                OkHttpClient.Builder().build().newWebSocketFactory("wss://echo.websocket.org")
            )
            .build()
            .create()

        subscribeOnService()
    }

    private fun addNewMessage(message: String, source: Message.Source) {
        messageList.add(
            Message(
                messageList.lastOrNull()?.id?.inc() ?: 0,
                source,
                message,
                0
            )
        )
        viewState.updateChatHistory(messageList.toList())
    }

    private fun subscribeOnService() {
        service
            .observeMessage()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                addNewMessage(it, Message.Source.Server)
                Log.d("ss", it)
            }
    }
}
