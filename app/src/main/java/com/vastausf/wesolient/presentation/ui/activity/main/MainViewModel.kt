package com.vastausf.wesolient.presentation.ui.activity.main

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    val desktopList: MutableLiveData<List<String>> = MutableLiveData(emptyList())

    val newDesktopTitle: MutableLiveData<TextFieldValue> = MutableLiveData(TextFieldValue())

    init {
        desktopList.value = listOf(
            "First desktop",
            "Second desktop"
        )
    }
}
