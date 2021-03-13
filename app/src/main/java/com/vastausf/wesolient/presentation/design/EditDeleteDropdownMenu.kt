package com.vastausf.wesolient.presentation.design

import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import com.vastausf.wesolient.R

@Composable
fun EditDeleteDropdownMenu(
    dropdownMenuExpanded: MutableState<Boolean>,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    //@TODO: DropdownMenu alignment Start -> End
    DropdownMenu(
        expanded = dropdownMenuExpanded.value,
        onDismissRequest = { dropdownMenuExpanded.value = false },
    ) {
        DropdownMenuItem(onClick = {
            onEdit()
            dropdownMenuExpanded.value = false
        }) {
            Text(stringResource(R.string.select_scope_menu_item_edit))
        }

        DropdownMenuItem(onClick = {
            onDelete()
            dropdownMenuExpanded.value = false
        }) {
            Text(stringResource(R.string.select_scope_menu_item_delete))
        }
    }
}
