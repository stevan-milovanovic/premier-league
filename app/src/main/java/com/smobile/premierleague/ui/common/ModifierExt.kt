package com.smobile.premierleague.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.debugInspectorInfo

fun Modifier.clickableWithoutIndication(
    onClick: () -> Unit
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "clickable"
        properties["onClick"] = onClick
    }
) {
    Modifier.clickable(
        enabled = true,
        onClickLabel = null,
        onClick = onClick,
        role = null,
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    )
}