package com.vastausf.wesolient.presentation.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ScreenHeader(
    leftActionIcon: @Composable (() -> Unit)? = null,
    rightActionIcon: @Composable (() -> Unit)? = null,
    onLeftActionClick: (() -> Unit)? = null,
    onRightActionClick: (() -> Unit)? = null,
    isLeftEnabled: Boolean = true,
    isRightEnabled: Boolean = true,
    withDivider: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(8.dp, 0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                ) {
                    if (leftActionIcon != null) {
                        IconButton(
                            modifier = Modifier
                                .size(48.dp),
                            onClick = {
                                onLeftActionClick?.invoke()
                            },
                            enabled = isLeftEnabled,
                            content = leftActionIcon
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    content = content
                )

                Box(
                    modifier = Modifier
                        .size(48.dp)
                ) {
                    if (rightActionIcon != null) {
                        IconButton(
                            modifier = Modifier
                                .size(48.dp),
                            onClick = {
                                onRightActionClick?.invoke()
                            },
                            enabled = isRightEnabled,
                            content = rightActionIcon
                        )
                    }
                }
            }

            if (withDivider) ThickHorizontalSpacer()
        }
    }
}
