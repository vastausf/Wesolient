package com.vastausf.wesolient.presentation.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun ScreenHeader(
    leftActionIcon: Painter? = null,
    rightActionIcon: Painter? = null,
    onLeftActionClick: (() -> Unit)? = null,
    onRightActionClick: (() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = Modifier
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leftActionIcon != null)
            IconButton(
                modifier = Modifier
                    .size(48.dp, 48.dp),
                onClick = {
                    onLeftActionClick?.invoke()
                }
            ) {
                Icon(
                    painter = leftActionIcon,
                    contentDescription = null
                )
            }
        else
            Spacer(
                modifier = Modifier
                    .size(48.dp, 48.dp)
            )

        Row(
            modifier = Modifier
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            content = content
        )

        if (rightActionIcon != null)
            IconButton(
                modifier = Modifier
                    .size(48.dp, 48.dp),
                onClick = {
                    onRightActionClick?.invoke()
                }
            ) {
                Icon(
                    painter = rightActionIcon,
                    contentDescription = null
                )
            }
        else
            Spacer(
                modifier = Modifier
                    .size(48.dp, 48.dp)
            )
    }
}
