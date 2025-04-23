package com.vyro.kmm_tooltip

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupPositionProvider
import com.vyro.kmm_tooltip.CaretPathFactory.create
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource


sealed class TooltipPosition(val alignment: Alignment) {
    object BottomLeft : TooltipPosition(Alignment.BottomStart)
    object BottomRight : TooltipPosition(Alignment.BottomEnd)
    object TopLeft : TooltipPosition(Alignment.TopStart)
    object TopRight : TooltipPosition(Alignment.TopEnd)
    object Left : TooltipPosition(Alignment.CenterStart)
    object Right : TooltipPosition(Alignment.CenterEnd)
}

enum class TooltipRadius(val value: Int) {
    Small(8),
    Medium(12),
    Full(1000);

    fun toDp() = value.dp
}

data class TooltipConfig(
    val horizontalSpacing: Int = 0,
    val verticalSpacing: Int = 0,
    val caretWidth: Float = 40f,
    val caretHeight: Float = 20f,
    val caretSpacing: Float = 0f,
)

data class TooltipContent(
    val text: StringResource,
    val leadingIcon: DrawableResource? = null,
    val trailingIcon: DrawableResource? = null,
    val textStyle: TextStyle,
    val onLeadingIconClick: () -> Unit = {},
    val onTrailingIconClick: () -> Unit = {},
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomPositionedTooltip(
    modifier: Modifier = Modifier,
    state: TooltipState,
    position: TooltipPosition,
    config: TooltipConfig,
    radius: TooltipRadius,
    onDismiss: () -> Unit,
    tooltipContent: @Composable () -> Unit,
    action: @Composable () -> Unit,
) {
    val caretColor = MaterialTheme.colorScheme.inverseSurface
    val density = LocalDensity.current
    val spacing90 = with(density) { 25.dp.toPx() }
    val spacing60 = with(density) { 15.dp.toPx() }

    TooltipBox(
        modifier = modifier,
        tooltip = {
            PlainTooltip(
                shape = RoundedCornerShape(radius.toDp()),
                modifier = Modifier.drawCaret { _ ->
                    val path = create(
                        position = position,
                        config = config,
                        size = size,
                        spacing90 = spacing90,
                        spacing60 = spacing60
                    )
                    onDrawWithContent {
                        drawContent()
                        drawPath(path = path, color = caretColor)
                    }
                }
            ) { tooltipContent() }
        },
        state = state,
        positionProvider = TooltipPositionProvider(
            position = position,
            spacing = IntOffset(config.horizontalSpacing, config.verticalSpacing)
        ),
        //onDismissRequest = { onDismiss() },
        enableUserInput = false
    ) {
        action()
    }
}

private object CaretPathFactory {
    fun create(
        position: TooltipPosition,
        config: TooltipConfig,
        size: Size,
        spacing60: Float,
        spacing90: Float
    ): Path = Path().apply {
        when (position) {
            TooltipPosition.BottomLeft -> drawBottomLeftCaret(config, size.width, spacing90)
            TooltipPosition.BottomRight -> drawBottomRightCaret(config, size.width, spacing60)
            TooltipPosition.TopLeft -> drawTopLeftCaret(config, size.width, size.height, spacing90)
            TooltipPosition.TopRight -> drawTopRightCaret(config, size.width, size.height, spacing60)
            TooltipPosition.Left -> drawLeftCaret(config, size.width, size.height)
            TooltipPosition.Right -> drawRightCaret(config, size.height)
        }
    }

    private fun Path.drawBottomLeftCaret(config: TooltipConfig, width: Float, spacing90: Float) {
        moveTo(width - spacing90 + config.caretSpacing, 0f)
        lineTo(width - spacing90 + config.caretWidth / 2 + config.caretSpacing, -config.caretHeight)
        lineTo(width - spacing90 + config.caretWidth + config.caretSpacing, 0f)
    }

    private fun Path.drawBottomRightCaret(config: TooltipConfig, width: Float, spacing60: Float) {
        moveTo(spacing60 + config.caretSpacing, 0f)
        lineTo(spacing60 + config.caretWidth / 2 + config.caretSpacing, -config.caretHeight)
        lineTo(spacing60 + config.caretWidth + config.caretSpacing, 0f)
    }

    private fun Path.drawTopLeftCaret(config: TooltipConfig, width: Float, height: Float, spacing90: Float) {
        moveTo(width - spacing90 + config.caretSpacing, height)
        lineTo(
            width - spacing90 + config.caretWidth / 2 + config.caretSpacing,
            height + config.caretHeight
        )
        lineTo(width - spacing90 + config.caretWidth + config.caretSpacing, height)
    }

    private fun Path.drawTopRightCaret(config: TooltipConfig, width: Float, height: Float, spacing60: Float) {
        moveTo(spacing60 + config.caretSpacing, height)
        lineTo(spacing60 + config.caretWidth / 2 + config.caretSpacing, height + config.caretHeight)
        lineTo(spacing60 + config.caretWidth + config.caretSpacing, height)
    }

    private fun Path.drawLeftCaret(config: TooltipConfig, width: Float, height: Float) {
        moveTo(width, height / 2 - config.caretWidth / 2 + config.caretSpacing)
        lineTo(width + config.caretHeight, height / 2 + config.caretSpacing)
        lineTo(width, height / 2 + config.caretWidth / 2 + config.caretSpacing)
    }

    private fun Path.drawRightCaret(config: TooltipConfig, height: Float) {
        moveTo(0f, height / 2 - config.caretWidth / 2 + config.caretSpacing)
        lineTo(-config.caretHeight, height / 2 + config.caretSpacing)
        lineTo(0f, height / 2 + config.caretWidth / 2 + config.caretSpacing)
    }
}


private class TooltipPositionProvider(
    private val position: TooltipPosition,
    private val spacing: IntOffset
) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        val centerOffset = (anchorBounds.width - popupContentSize.width) / 2

        val verticalCenterOffset = (anchorBounds.height - popupContentSize.height) / 2

        val (x, y) = when (position) {
            TooltipPosition.BottomLeft -> Pair(
                anchorBounds.right - popupContentSize.width + spacing.x,
                anchorBounds.bottom + spacing.y
            )

            TooltipPosition.BottomRight -> Pair(
                anchorBounds.left + spacing.x,
                anchorBounds.bottom + spacing.y
            )

            TooltipPosition.TopLeft -> Pair(
                anchorBounds.right - popupContentSize.width + spacing.x,
                anchorBounds.top - popupContentSize.height - spacing.y
            )

            TooltipPosition.TopRight -> Pair(
                anchorBounds.left + spacing.x,
                anchorBounds.top - popupContentSize.height - spacing.y
            )

            TooltipPosition.Left -> Pair(
                anchorBounds.left - popupContentSize.width - spacing.y + spacing.x,
                anchorBounds.top + verticalCenterOffset
            )

            TooltipPosition.Right -> Pair(
                anchorBounds.right + spacing.y + spacing.x,
                anchorBounds.top + verticalCenterOffset
            )
        }

        return IntOffset(
            x = x.coerceIn(
                0,
                (windowSize.width - popupContentSize.width).coerceAtLeast(0)
            ),
            y = y.coerceIn(
                0,
                (windowSize.height - popupContentSize.height).coerceAtLeast(0)
            )
        )
    }

}
