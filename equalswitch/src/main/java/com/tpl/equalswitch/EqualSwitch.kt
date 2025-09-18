package com.tpl.equalswitch

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.ripple
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

/* -------------------- Sizes & Colors -------------------- */

@Immutable
data class EqualSwitchSizes(
    val trackWidth: Dp = 44.dp,
    val trackHeight: Dp = 26.dp,
    val thumbSize: Dp = 18.dp,     // equal knob
    val padding: Dp = 4.dp,
    val borderWidth: Dp = 1.dp,
    val scale: Float = 1f          // Multiplier for ALL sizes
)

@Immutable
data class EqualSwitchColors(
    // enabled
    val trackOn: Color,
    val trackOff: Color,
    val thumbOn: Color,
    val thumbOff: Color,
    val border: Color,             // Single type border, desired to be the same in both cases
    // disabled
    val trackDisabled: Color,
    val thumbDisabled: Color,
    val borderDisabled: Color
)

object EqualSwitchDefaults {

    /** Default sizes (+ scale). */
    @Composable
    fun sizes(
        trackWidth: Dp = 44.dp,
        trackHeight: Dp = 26.dp,
        thumbSize: Dp = 18.dp,
        padding: Dp = 4.dp,
        borderWidth: Dp = 1.dp,
        scale: Float = 1f
    ) = EqualSwitchSizes(trackWidth, trackHeight, thumbSize, padding, borderWidth, scale)

    /** Default colors (compatible with Material 3, compositeOver-like appearance for disabled states). */
    @Composable
    fun colors(
        trackOn: Color = MaterialTheme.colorScheme.primary,
        trackOff: Color = MaterialTheme.colorScheme.surfaceVariant,
        thumbOn: Color = MaterialTheme.colorScheme.onPrimary,
        thumbOff: Color = MaterialTheme.colorScheme.onSurface,
        border: Color = MaterialTheme.colorScheme.outlineVariant,
        trackDisabled: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
        thumbDisabled: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        borderDisabled: Color = MaterialTheme.colorScheme.outline.copy(alpha = 0.38f),
    ) = EqualSwitchColors(
        trackOn, trackOff, thumbOn, thumbOff, border, trackDisabled, thumbDisabled, borderDisabled
    )
}

/* -------------------- EqualSwitch -------------------- */

@Composable
fun EqualSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    sizes: EqualSwitchSizes = EqualSwitchDefaults.sizes(),
    colors: EqualSwitchColors = EqualSwitchDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val density = LocalDensity.current
    val layoutDir = LocalLayoutDirection.current

    // Helper applying scale (Dp * scale)
    fun Dp.scaled(): Dp = (this.value * sizes.scale).dp
    val trackW = sizes.trackWidth.scaled()
    val trackH = sizes.trackHeight.scaled()
    val thumbSize = sizes.thumbSize.scaled()
    val padding = sizes.padding.scaled()
    val borderW = sizes.borderWidth.scaled()

    // Position (0f..1f)
    val target = if (checked) 1f else 0f
    val fraction by animateFloatAsState(targetValue = target, label = "equalSwitchFraction")
    val visualFraction = if (layoutDir == LayoutDirection.Ltr) fraction else 1f - fraction

    // Temporary fraction + state for drag
    var dragFraction by remember { mutableStateOf(target) }
    var isDragging by remember { mutableStateOf(false) }
    val draggableState = rememberDraggableState { deltaPx ->
        val span = with(density) { (trackW - padding * 2 - thumbSize).toPx() }
        if (span > 0f) {
            val cur =
                (if (layoutDir == LayoutDirection.Ltr) dragFraction else 1f - dragFraction) * span
            val next = (cur + deltaPx).coerceIn(0f, span)
            val raw = (next / span).coerceIn(0f, 1f)
            dragFraction = if (layoutDir == LayoutDirection.Ltr) raw else (1f - raw)
        }
    }

    fun settleFromDrag() {
        val newChecked = dragFraction >= 0.5f
        if (newChecked != checked) onCheckedChange(newChecked) else dragFraction = target
    }

    // A11y: min 48dp touch area
    val minTouch = 48.dp

    // Ripple: bounded=false, radius = thumb radius + padding (same in both cases)
    val indication = ripple(
        bounded = false,
        radius = thumbSize / 2 + padding
    )

    Box(
        modifier
            .sizeIn(minWidth = minTouch, minHeight = minTouch)
            .toggleable(
                value = checked,
                enabled = enabled,
                role = Role.Switch,
                interactionSource = interactionSource,
                indication = indication,
                onValueChange = onCheckedChange
            )
            .draggable(
                state = draggableState,
                orientation = Orientation.Horizontal,
                enabled = enabled,
                onDragStarted = {
                    isDragging = true
                    dragFraction = fraction
                },
                onDragStopped = {
                    isDragging = false
                    settleFromDrag()
                }
            )
            .semantics {
                stateDescription = if (checked) "On" else "Off"
            }
    ) {
        Canvas(Modifier.size(trackW, trackH)) {
            // Select colors
            val trackColor = when {
                !enabled -> colors.trackDisabled
                checked -> colors.trackOn
                else -> colors.trackOff
            }
            val thumbColor = when {
                !enabled -> colors.thumbDisabled
                checked -> colors.thumbOn
                else -> colors.thumbOff
            }
            val borderColor = if (enabled) colors.border else colors.borderDisabled

            // Track (fill)
            drawRoundRect(
                color = trackColor,
                cornerRadius = CornerRadius(size.height / 2f, size.height / 2f)
            )
            // Track border (thin, same appearance in both cases)
            val bw = with(density) { borderW.toPx().coerceAtLeast(0f) }
            if (bw > 0f) {
                drawRoundRect(
                    color = borderColor,
                    cornerRadius = CornerRadius(size.height / 2f, size.height / 2f),
                    style = Stroke(width = bw, cap = StrokeCap.Round)
                )
            }

            // Knob center (equal knob)
            val padPx = with(density) { padding.toPx() }
            val rPx = with(density) { (thumbSize / 2).toPx() }
            val span = size.width - (2 * padPx) - (2 * rPx)
            val start = padPx + rPx
            val cx = start + span * (
                    if (isDragging) {
                        if (layoutDir == LayoutDirection.Ltr) dragFraction else 1f - dragFraction
                    } else {
                        visualFraction
                    })
            val cy = size.height / 2f

            drawCircle(
                color = thumbColor,
                radius = rPx,
                center = Offset(cx, cy)
            )
        }
    }
}
