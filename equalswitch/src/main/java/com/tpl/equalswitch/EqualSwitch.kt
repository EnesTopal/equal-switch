package com.tpl.equalswitch

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Surface

@Immutable
data class EqualSwitchSizes(
    val trackWidth: Dp = 44.dp,
    val trackHeight: Dp = 26.dp,
    val thumbSize: Dp = 18.dp,
    val padding: Dp = 4.dp
)

@Immutable
data class EqualSwitchColors(
    val trackOn: Color,
    val trackOff: Color,
    val thumbOn: Color,
    val thumbOff: Color,
    val trackDisabled: Color,
    val thumbDisabled: Color,
)

object EqualSwitchDefaults {
    @Composable
    fun sizes(
        trackWidth: Dp = 44.dp,
        trackHeight: Dp = 26.dp,
        thumbSize: Dp = 18.dp,
        padding: Dp = 4.dp
    ) = EqualSwitchSizes(trackWidth, trackHeight, thumbSize, padding)

    @Composable
    fun colors(
        trackOn: Color = MaterialTheme.colorScheme.primary,
        trackOff: Color = MaterialTheme.colorScheme.surfaceVariant,
        thumbOn: Color = MaterialTheme.colorScheme.onPrimary,
        thumbOff: Color = MaterialTheme.colorScheme.onSurface,
        trackDisabled: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
        thumbDisabled: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
    ) = EqualSwitchColors(
        trackOn, trackOff, thumbOn, thumbOff, trackDisabled, thumbDisabled
    )
}

/**
 * EqualSwitch: iki durumda da knob çapı *aynı* kalır.
 * Şimdilik: tıkla → toggle + yumuşak konum animasyonu.
 * (Drag/sürükleme ve ikon/border gibi ekler bir sonraki adımda.)
 */
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

    // 0f..1f arası konum
    val target = if (checked) 1f else 0f
    val fraction by animateFloatAsState(targetValue = target, label = "equalSwitchFraction")
    val visualFraction = if (layoutDir == LayoutDirection.Ltr) fraction else 1f - fraction

    val minTouch = 48.dp

    Box(
        modifier
            .sizeIn(minWidth = minTouch, minHeight = minTouch)
            .toggleable(
                value = checked,
                enabled = enabled,
                role = Role.Switch,
                interactionSource = interactionSource,
                indication = null, // istersen Ripple ekleyebilirsin
                onValueChange = onCheckedChange
            )
            .semantics {
                stateDescription = if (checked) "On" else "Off"
            }
    ) {
        Canvas(
            modifier = Modifier.size(sizes.trackWidth, sizes.trackHeight)
        ) {
            // renk seçimi
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

            // TRACK
            drawRoundRect(
                color = trackColor,
                cornerRadius = CornerRadius(size.height / 2f, size.height / 2f)
            )

            // KNOB (çap sabit)
            val padPx = with(density) { sizes.padding.toPx() }
            val thumbR = with(density) { (sizes.thumbSize / 2).toPx() }
            val span = size.width - (2 * padPx) - (2 * thumbR)
            val start = padPx + thumbR
            val cx = start + span * visualFraction
            val cy = size.height / 2f

            drawCircle(
                color = thumbColor,
                radius = thumbR,
                center = Offset(cx, cy)
            )
        }
    }
}

/* --------- PREVIEW --------- */

@Preview(name = "EqualSwitch On", showBackground = true)
@Composable
private fun PreviewEqualSwitchOn() {
    Surface {
        var checked by remember { mutableStateOf(true) }
        EqualSwitch(checked = checked, onCheckedChange = { checked = it })
    }
}

@Preview(name = "EqualSwitch Off", showBackground = true)
@Composable
private fun PreviewEqualSwitchOff() {
    Surface {
        var checked by remember { mutableStateOf(false) }
        EqualSwitch(checked = checked, onCheckedChange = { checked = it })
    }
}

@Preview(name = "RTL", locale = "ar", showBackground = true)
@Composable
private fun PreviewEqualSwitchRTL() {
    Surface {
        var checked by remember { mutableStateOf(true) }
        EqualSwitch(checked = checked, onCheckedChange = { checked = it })
    }
}
