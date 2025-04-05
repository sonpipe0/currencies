package com.example.currencies.common

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreInterceptKeyBeforeSoftKeyboard
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType


/** * Custom TextField that does not show the keyboard when focused
 *
 * The [FocusManager] should be initialized on each Screen Composable
 *
 * @param value The current text value of the field.
 * @param onValueChange The callback to be invoked when the text value changes.
 * @param enabled Whether the text field is enabled or not.
 * @param textStyle The style to be applied to the text.
 * @param kerBoardType The type of keyboard to be used.
 * @param modifier The [Modifier] to be applied to this layout.
 * @param focusManager The [FocusManager] to manage focus state
 * @param hideKeyBoard A [MutableState] that indicates whether the keyboard should be hidden or not.
 **/
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CustomFocusableTextField(
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean,
    textStyle: TextStyle,
    placeholder: String = "",
    placeholderStyle: TextStyle = TextStyle.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    modifier: Modifier,
    focusManager: FocusManager,
    hideKeyBoard: MutableState<Boolean>
) {
    if (hideKeyBoard.value) {
        focusManager.clearFocus()
    }
    return BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .onPreInterceptKeyBeforeSoftKeyboard {
                if (it.key == Key.Back) {
                    hideKeyBoard.value = true
                    focusManager.clearFocus()
                }
                false
            }
            .onFocusChanged {
                if (it.isFocused) {
                    hideKeyBoard.value = false
                }
                if (!it.isFocused) {
                    hideKeyBoard.value = true
                }
            },
        enabled = enabled,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true,
        decorationBox = { innerTextField ->
            if (value.isEmpty()) {
                Text(text = placeholder, style = placeholderStyle)
            }
            innerTextField()
        }
    );
}

