# KMM Material Tooltip

A Kotlin Multiplatform Material Design Tooltip library for Android and iOS platforms using Jetpack Compose.

## Overview

KMM Material Tooltip provides a customizable tooltip implementation that follows Material Design principles while working seamlessly across Android and iOS through Kotlin Multiplatform. The library offers various tooltip positions, customizable appearance, and interactive capabilities.

## Features

- **Multiple Positioning Options**: Position tooltips at TopLeft, TopRight, BottomLeft, BottomRight, Left, or Right of the target element
- **Customizable Appearance**: Control corner radius, caret size, and spacing
- **Interactive Elements**: Add leading and trailing icons with onClick actions
- **Material Design Integration**: Seamlessly works with Material 3 theme colors and styles

## Installation

Add the dependency to your shared module's `build.gradle.kts`:

```kotlin
commonMain.dependencies {
    implementation("io.github.zaiddev1996:kmm-material-tooltip:1.0.0")
}
```

## Usage

### Basic Example

```kotlin
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.zaid.kmm_tooltip.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TooltipExample() {
    val tooltipState = remember { TooltipState() }
    val scope = rememberCoroutineScope()
    
    AppTooltip(
        state = tooltipState,
        content = TooltipContent(
            text = StringResource("This is a tooltip"),
            textStyle = MaterialTheme.typography.bodyMedium
        ),
        position = TooltipPosition.TopLeft,
        radius = TooltipRadius.Medium,
        onDismiss = { /* Handle dismiss */ }
    ) {
        // The target element that will show the tooltip
        Text(
            text = "Hover over me",
            modifier = Modifier.clickable {
                scope.launch {
                    tooltipState.show()
                }
            }
        )
    }
}
```

### Customization Options

```kotlin
AppTooltip(
    state = tooltipState,
    content = TooltipContent(
        text = StringResource("Custom tooltip with icons"),
        leadingIcon = DrawableResource("ic_info"),
        trailingIcon = DrawableResource("ic_close"),
        textStyle = MaterialTheme.typography.labelMedium,
        onLeadingIconClick = { /* Handle leading icon click */ },
        onTrailingIconClick = { /* Handle trailing icon click */ }
    ),
    config = TooltipConfig(
        horizontalSpacing = 10,
        verticalSpacing = 5,
        caretWidth = 30f,
        caretHeight = 15f,
        caretSpacing = 5f
    ),
    position = TooltipPosition.BottomRight,
    radius = TooltipRadius.Small,
    onDismiss = { /* Handle dismiss */ }
) {
    // Your target element here
}
```

## Tooltip Positions

- `TooltipPosition.TopLeft`: Positions the tooltip at the top-left of the target
- `TooltipPosition.TopRight`: Positions the tooltip at the top-right of the target
- `TooltipPosition.BottomLeft`: Positions the tooltip at the bottom-left of the target
- `TooltipPosition.BottomRight`: Positions the tooltip at the bottom-right of the target
- `TooltipPosition.Left`: Positions the tooltip at the left side of the target
- `TooltipPosition.Right`: Positions the tooltip at the right side of the target

## Corner Radius Options

- `TooltipRadius.Small`: 8dp radius
- `TooltipRadius.Medium`: 12dp radius
- `TooltipRadius.Full`: Pills shaped (fully rounded)

## Contributing

Contributions are welcome! Feel free to submit pull requests or create issues for any bugs or feature requests.

## License

This project is licensed under the Apache 2.0 License - see the LICENSE file for details.
