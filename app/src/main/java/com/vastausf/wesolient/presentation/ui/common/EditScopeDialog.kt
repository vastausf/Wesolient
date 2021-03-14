package com.vastausf.wesolient.presentation.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.vastausf.wesolient.R
import com.vastausf.wesolient.data.common.Scope


@Composable
fun EditScopeCompose(
    scope: Scope,
    onApply: (String, String) -> Unit,
    onDelete: (String) -> Unit
) {
    var title by remember { mutableStateOf(scope.title) }
    var url by remember { mutableStateOf(scope.url) }

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
        Row {
            FilledButton(
                title = stringResource(R.string.delete),
                enabled = title.isNotEmpty() && url.isNotEmpty(),
                onClick = {
                    onDelete(scope.uid)
                }
            )

            Spacer(Modifier.weight(1f))

            //@TODO: Animate enable button state changes
            FilledButton(
                title = stringResource(R.string.edit_scope_apply),
                enabled = title.isNotEmpty() && url.isNotEmpty(),
                onClick = {
                    onApply(title, url)
                }
            )
        }
    }
}
