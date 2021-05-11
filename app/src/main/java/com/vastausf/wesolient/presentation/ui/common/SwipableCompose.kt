package com.vastausf.wesolient.presentation.ui.common

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ResistanceConfig
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt


@ExperimentalMaterialApi
@Composable
fun SwipableCompose(
    primaryCompose: @Composable () -> Unit,
    leftCompose: @Composable (() -> Unit)? = null,
    rightCompose: @Composable (() -> Unit)? = null
) {
    val swipeableState = rememberSwipeableState(1)

    val anchors = mutableMapOf(0f to 1).apply {
        val offset = 320f * LocalDensity.current.density

        if (leftCompose != null) this[offset] = 0
        if (rightCompose != null) this[-offset] = 2
    }

    Box(
        modifier = Modifier
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                orientation = Orientation.Horizontal,
                resistance = ResistanceConfig(0f, 0f, 0f)
            )
    ) {
        val offset = swipeableState.offset.value

        if (offset > 0) {
            Box(
                modifier = Modifier
                    .width(320f.dp)
            ) {
                leftCompose?.invoke()
            }
        } else if (offset < 0) {
            Box(
                modifier = Modifier
                    .offset(40f.dp, 0f.dp)
                    .width(320f.dp)
            ) {
                rightCompose?.invoke()
            }
        }

        Box(
            modifier = Modifier
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .shadow(8f.dp),
        ) {
            primaryCompose()
        }
    }
}
