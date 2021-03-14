package com.vastausf.wesolient.presentation.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.vastausf.wesolient.R


@Composable
fun ScopeDialogCompose(
    titleValue: String = "",
    urlValue: String = "",
    state: MutableState<Boolean>,
    actionTitle: String,
    onAction: (String, String) -> Unit
) {
    Dialog(
        onDismissRequest = {
            state.value = false
        }
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium
        ) {
            var title by remember { mutableStateOf(titleValue) }
            var url by remember { mutableStateOf(urlValue) }

            Column(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                TransparentTextField(
                    value = title,
                    placeholder = stringResource(R.string.create_scope_title_hint),
                    onValueChange = {
                        title = it
                    }
                )
                TransparentTextField(
                    value = url,
                    placeholder = stringResource(R.string.create_scope_url_hint),
                    onValueChange = {
                        url = it
                    }
                )
                Row {
                    Spacer(Modifier.weight(1f))

                    FilledButton(
                        title = actionTitle,
                        enabled = title.isNotEmpty() && url.isNotEmpty(),
                        onClick = {
                            onAction(title, url)
                        }
                    )
                }
            }
        }
    }
}
