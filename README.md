# EqualSwitch

<div align="center">

<!-- Add your screenshots here -->
<!-- Example: ![EqualSwitch Demo](docs/screenshots/demo.gif) -->
<!-- Example: ![EqualSwitch Comparison](docs/screenshots/comparison.png) -->

[![Version](https://img.shields.io/badge/version-0.0.2-blue.svg)](https://github.com/EnesTopal/equal-switch/releases)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE)
[![Android](https://img.shields.io/badge/platform-Android-green.svg)](https://developer.android.com/)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-1.0+-blue.svg)](https://developer.android.com/jetpack/compose)

*A lightweight Jetpack Compose switch component that guarantees an **equal thumb size** in both ON and OFF states that designed for **choosing between two equal options**

</div>

## 📸 Screenshots

> (Under construction - screenshots will be added soon)


## 🎯 Why EqualSwitch?

**EqualSwitch** is designed for scenarios where you need to choose between two distinct options with equal visual weight. Unlike standard switches that emphasize an "on/off" state, EqualSwitch maintains the same thumb size in both positions, making it perfect for:

- **Option A vs Option B** selections (e.g., "Light Mode" vs "Dark Mode")
- **Toggle between two modes** where neither state is inherently "active"
- **Equal-priority choices** where both options deserve the same visual emphasis
- **Consistent UI** when you need multiple switches that look uniform regardless of state

## ✨ Features

- ✅ **Equal thumb** size for both option states
- ✅ **Tap + Drag** interaction (snaps to nearest state)
- ✅ **Material 3 ripple** feedback
- ✅ **Single border** style for both states
- ✅ **Comprehensive colors API** (track/thumb/border + disabled variants)
- ✅ **Global scale factor** (resize everything proportionally)
- ✅ **RTL support**
- ✅ **Accessibility** (minimum 48dp touch target, semantics, `Role.Switch`, state description)
- ✅ **Theme integration** with Material 3 color schemes

## 📦 Installation

### Option 1: Maven Central (Recommended)

Add the dependency to your module's `build.gradle.kts`:

```kotlin
dependencies {
    implementation("io.github.enestopal:equalswitch:0.0.2")
}
```

### Option 2: AAR (Quick Test)

1. Build the library:
   ```bash
   ./gradlew :equalswitch:assembleRelease
   ```

2. Copy the generated AAR from `equalswitch/build/outputs/aar/equalswitch-release.aar`

3. Add to your project:
   ```kotlin
   dependencies {
       implementation files('libs/equalswitch-release.aar')
   }
   ```

## 🚀 Quick Start

### Basic Usage

```kotlin
import com.tpl.equalswitch.EqualSwitch

@Composable
fun MyScreen() {
    var isDarkMode by remember { mutableStateOf(false) }
    
    EqualSwitch(
        checked = isDarkMode,
        onCheckedChange = { isDarkMode = it }
    )
}
```

### With Custom Colors

```kotlin
@Composable
fun CustomEqualSwitch() {
    var isOptionA by remember { mutableStateOf(false) }
    
    EqualSwitch(
        checked = isOptionA,
        onCheckedChange = { isOptionA = it },
        colors = EqualSwitchDefaults.colors(
            trackOn = Color(0xFF4CAF50),
            trackOff = Color(0xFFE0E0E0),
            thumbOn = Color.White,
            thumbOff = Color(0xFF9E9E9E),
            border = Color(0xFFBDBDBD)
        )
    )
}
```

### With Custom Sizes

```kotlin
@Composable
fun LargeEqualSwitch() {
    var isCompactView by remember { mutableStateOf(false) }
    
    EqualSwitch(
        checked = isCompactView,
        onCheckedChange = { isCompactView = it },
        sizes = EqualSwitchDefaults.sizes(
            trackWidth = 56.dp,
            trackHeight = 32.dp,
            thumbSize = 24.dp,
            scale = 1.2f  // Makes everything 20% larger
        )
    )
}
```

## 🎨 API Reference

### EqualSwitch

The main composable function for creating a switch with equal thumb sizes.

#### Parameters

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `checked` | `Boolean` | **Required** | Whether the switch is currently checked |
| `onCheckedChange` | `(Boolean) -> Unit` | **Required** | Callback invoked when the switch state changes |
| `modifier` | `Modifier` | `Modifier` | Modifier to be applied to the switch |
| `enabled` | `Boolean` | `true` | Whether the switch is enabled for interaction |
| `sizes` | `EqualSwitchSizes` | `EqualSwitchDefaults.sizes()` | Configuration for switch dimensions |
| `colors` | `EqualSwitchColors` | `EqualSwitchDefaults.colors()` | Color configuration for the switch |
| `interactionSource` | `MutableInteractionSource` | `remember { MutableInteractionSource() }` | Interaction source for testing |

### EqualSwitchSizes

Configuration class for switch dimensions.

```kotlin
data class EqualSwitchSizes(
    val trackWidth: Dp = 44.dp,     // Width of the track
    val trackHeight: Dp = 26.dp,    // Height of the track
    val thumbSize: Dp = 18.dp,      // Diameter of the thumb (equal in both states)
    val padding: Dp = 4.dp,         // Padding between track edge and thumb
    val borderWidth: Dp = 1.dp,     // Width of the track border
    val scale: Float = 1f           // Global scale factor for all dimensions
)
```

### EqualSwitchColors

Configuration class for switch colors.

```kotlin
data class EqualSwitchColors(
    // Enabled state colors
    val trackOn: Color,             // Track color when checked
    val trackOff: Color,            // Track color when unchecked
    val thumbOn: Color,             // Thumb color when checked
    val thumbOff: Color,            // Thumb color when unchecked
    val border: Color,              // Border color (same for both states)
    
    // Disabled state colors
    val trackDisabled: Color,       // Track color when disabled
    val thumbDisabled: Color,       // Thumb color when disabled
    val borderDisabled: Color       // Border color when disabled
)
```

## 🎨 Customization Examples

### Material 3 Theme Integration

```kotlin
@Composable
fun ThemedEqualSwitch() {
    var isGridLayout by remember { mutableStateOf(false) }
    
    // Automatically uses Material 3 colors from theme
    EqualSwitch(
        checked = isGridLayout,
        onCheckedChange = { isGridLayout = it },
        colors = EqualSwitchDefaults.colors() // Uses theme colors by default
    )
}
```

### Custom Dark Theme

```kotlin
@Composable
fun DarkEqualSwitch() {
    var isAdvancedMode by remember { mutableStateOf(false) }
    
    EqualSwitch(
        checked = isAdvancedMode,
        onCheckedChange = { isAdvancedMode = it },
        colors = EqualSwitchDefaults.colors(
            trackOn = Color(0xFFBB86FC),
            trackOff = Color(0xFF424242),
            thumbOn = Color(0xFF121212),
            thumbOff = Color(0xFFE0E0E0),
            border = Color(0xFF616161)
        )
    )
}
```

### Compact Switch

```kotlin
@Composable
fun CompactEqualSwitch() {
    var isListMode by remember { mutableStateOf(false) }
    
    EqualSwitch(
        checked = isListMode,
        onCheckedChange = { isListMode = it },
        sizes = EqualSwitchDefaults.sizes(
            trackWidth = 32.dp,
            trackHeight = 18.dp,
            thumbSize = 12.dp,
            padding = 3.dp
        )
    )
}
```

## 🔧 Advanced Usage

### With State Management

```kotlin
class SettingsViewModel : ViewModel() {
    private val _viewMode = MutableStateFlow(false) // false = Card View, true = List View
    val viewMode: StateFlow<Boolean> = _viewMode.asStateFlow()
    
    fun toggleViewMode() {
        _viewMode.value = !_viewMode.value
    }
}

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val viewMode by viewModel.viewMode.collectAsState()
    
    Column {
        Text("View Mode: ${if (viewMode) "List" else "Card"}")
        EqualSwitch(
            checked = viewMode,
            onCheckedChange = { viewModel.toggleViewMode() }
        )
    }
}
```

### With Accessibility Labels

```kotlin
@Composable
fun AccessibleEqualSwitch() {
    var isDarkMode by remember { mutableStateOf(false) }
    
    EqualSwitch(
        checked = isDarkMode,
        onCheckedChange = { isDarkMode = it },
        modifier = Modifier.semantics {
            contentDescription = "Choose between light and dark mode"
        }
    )
}
```

## 🧪 Testing

### Unit Testing

```kotlin
@Test
fun testEqualSwitchToggle() {
    var isChecked = false
    val onCheckedChange: (Boolean) -> Unit = { isChecked = it }
    
    composeTestRule.setContent {
        EqualSwitch(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
    }
    
    composeTestRule.onNode(hasTestTag("equal-switch"))
        .performClick()
    
    assertTrue(isChecked)
}
```

### UI Testing

```kotlin
@Test
fun testEqualSwitchInteraction() {
    composeTestRule.setContent {
        var isOptionSelected by remember { mutableStateOf(false) }
        EqualSwitch(
            checked = isOptionSelected,
            onCheckedChange = { isOptionSelected = it },
            modifier = Modifier.testTag("equal-switch")
        )
    }
    
    // Test tap interaction
    composeTestRule.onNode(hasTestTag("equal-switch"))
        .performClick()
    
    // Test drag interaction
    composeTestRule.onNode(hasTestTag("equal-switch"))
        .performGesture {
            drag(start = Offset.Zero, end = Offset(100f, 0f))
        }
}
```

## 📱 Requirements

- **Minimum SDK**: 30 (Android 11)
- **Compile SDK**: 36
- **Kotlin**: 2.0.21+
- **Jetpack Compose**: 1.0+
- **Material 3**: Latest

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request. For major changes, please open an issue first to discuss what you would like to change.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Enes Topal**
- GitHub: [@EnesTopal](https://github.com/EnesTopal)
- Email: enestopal.053@gmail.com

## 🙏 Acknowledgments

- Jetpack Compose team for the amazing UI toolkit
- Material Design team for the design guidelines
- Android community for inspiration and feedback

---

<div align="center">

**⭐ If you found this project helpful, please give it a star! ⭐**

</div>
