package com.vastausf.wesolient.presentation.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.vastausf.wesolient.R


enum class ScopeDialogType {
    CREATE,
    EDIT
}

@Composable
fun ScopeDialog(
    type: ScopeDialogType,
    titleValue: String = "",
    urlValue: String = "",
    onDismiss: () -> Unit,
    onAction: (title: String, url: String) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        var title by remember { mutableStateOf(titleValue) }
        var url by remember { mutableStateOf(urlValue) }

        Surface(
            modifier = Modifier
                .padding(8.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Column {
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
                //@TODO: Animate enable state changes
                FilledButton(
                    title = when (type) {
                        ScopeDialogType.CREATE -> {
                            stringResource(R.string.create_scope_create)
                        }
                        ScopeDialogType.EDIT -> {
                            stringResource(R.string.edit_scope_apply)
                        }
                    },
                    enabled = titleValue.isNotEmpty() && urlValue.isNotEmpty(),
                    onClick = {
                        onAction(title, url)
                    }
                )
            }
        }
    }
}
