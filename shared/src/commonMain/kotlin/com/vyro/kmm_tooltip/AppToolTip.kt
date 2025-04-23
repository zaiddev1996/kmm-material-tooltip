package com.vyro.kmm_tooltip

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTooltip(
    state: TooltipState,
    content: TooltipContent,
    config: TooltipConfig = TooltipConfig(),
    position: TooltipPosition = TooltipPosition.TopLeft,
    radius: TooltipRadius = TooltipRadius.Medium,
    onDismiss: () -> Unit,
    action: @Composable () -> Unit
) {
    CustomPositionedTooltip(
        state = state,
        position = position,
        config = config,
        radius = radius,
        tooltipContent = { TooltipContentView(content) },
        action = action,
        onDismiss = onDismiss
    )
}

@Composable
private fun TooltipContentView(content: TooltipContent) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 2.dp, vertical = 2.dp)
    ) {
        content.leadingIcon?.let { icon ->
            TooltipIcon(icon) { content.onLeadingIconClick.invoke() }
            Spacer(Modifier.width(6.dp))
        }

        Text(
            text = stringResource(content.text),
            color = MaterialTheme.colorScheme.inverseOnSurface,
            style = content.textStyle
        )

        content.trailingIcon?.let { icon ->
            Spacer(Modifier.width(6.dp))
            TooltipIcon(icon) { content.onTrailingIconClick.invoke() }
        }
    }
}

@Composable
private fun TooltipIcon(icon: DrawableResource, onClick: () -> Unit) {
    Icon(
        modifier = Modifier
            .size(20.dp)
            .clickable { onClick() }
            .padding(1.dp),
        painter = painterResource(icon),
        tint = MaterialTheme.colorScheme.inverseOnSurface,
        contentDescription = "Tooltip Icon"
    )
}