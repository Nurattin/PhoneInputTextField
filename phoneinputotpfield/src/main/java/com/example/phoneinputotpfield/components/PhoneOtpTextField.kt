package com.example.phoneinputotpfield.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.core.text.isDigitsOnly
import com.example.phoneinputotpfield.PhoneNumberElement

object PhoneOtpTextFieldDefault {

    internal val SpaceBetweenElements = 3.dp

    @Composable
    fun color(
        maskColor: Color = Color.Black,
        disabledMaskColor: Color = Color.LightGray,
        editableDigitColor: Color = Color.Black,
        disabledEditableDigitColor: Color = Color.LightGray,
        dividersColor: Color = Color.LightGray,
        focusedDividersColor: Color = Color.Blue,
    ) = PhoneOtpColor(
        maskColor = maskColor,
        disabledMaskColor = disabledMaskColor,
        editableDigitColor = editableDigitColor,
        disabledEditableDigitColor = disabledEditableDigitColor,
        dividersColor = dividersColor,
        focusedDividersColor = focusedDividersColor,
    )
}

/**
 * A custom text field Composable for phone number input with formatting, using [PhoneNumberElement] for defining the format.
 *
 * @param mask The list of [PhoneNumberElement] defining the phone number format.
 * @param value The current value of the input.
 * @param onValueChange A callback invoked when the input value changes.
 * @param enabled Whether the text field is enabled.
 * @param color A set of colors to use for the text field.
 * @param textStyle The style to use for the text.
 */
@Composable
fun PhoneOtpTextField(
    modifier: Modifier = Modifier,
    mask: List<PhoneNumberElement>,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    color: PhoneOtpColor = PhoneOtpTextFieldDefault.color(),
    textStyle: TextStyle = LocalTextStyle.current,
) {
    val maxLength = rememberSaveable(mask) {
        mask.count { element -> element is PhoneNumberElement.EditableDigit }
    }
    var isFocused by remember {
        mutableStateOf(false)
    }

    @Composable
    fun cursorPosition(mask: List<PhoneNumberElement>, value: String): Int {
        var reminder = value.length
        return mask.indexOfFirst {
            if (it is PhoneNumberElement.EditableDigit && reminder == 0) {
                true
            } else if (it is PhoneNumberElement.EditableDigit) {
                reminder--
                false
            } else {
                false
            }
        }
    }

    BasicTextField(
        modifier = modifier
            .onFocusChanged { state ->
                isFocused = state.isFocused
            },
        value = value,
        onValueChange = { newValue ->
            if (newValue.length <= maxLength && newValue.isDigitsOnly()) {
                onValueChange(newValue)
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
        ),
        enabled = enabled,
        decorationBox = {
            val mergedStyle = LocalTextStyle.current.merge(textStyle)
            CompositionLocalProvider(
                LocalTextStyle provides mergedStyle,
            ) {
                val digitIndexMap = remember(value) {
                    var start = 0
                    mask.mapIndexedNotNull { index, it ->
                        if (it is PhoneNumberElement.EditableDigit) {
                            index to start++
                        } else null
                    }.toMap()
                }
                val cursorPosition = cursorPosition(
                    mask = mask,
                    value = value,
                )
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(PhoneOtpTextFieldDefault.SpaceBetweenElements)
                ) {
                    mask.fastForEachIndexed { index, phoneNumberElement ->
                        when (phoneNumberElement) {
                            PhoneNumberElement.EditableDigit -> {
                                CompositionLocalProvider(
                                    LocalContentColor provides color.editableColor(enabled),
                                ) {
                                    digitIndexMap[index]?.let { digitIndex ->
                                        EditableField(
                                            text = value.getOrNull(digitIndex)?.toString(),
                                            dividerColor = if (cursorPosition == index && isFocused)
                                                color.focusedDividersColor
                                            else color.dividersColor,
                                        )
                                    }
                                }
                            }

                            is PhoneNumberElement.Mask -> {
                                CompositionLocalProvider(
                                    LocalContentColor provides color.maskColor(enabled),
                                ) {
                                    Mask(
                                        text = phoneNumberElement.text,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@Immutable
data class PhoneOtpColor(
    val maskColor: Color,
    val disabledMaskColor: Color,
    val editableDigitColor: Color,
    val disabledEditableDigitColor: Color,
    val dividersColor: Color,
    val focusedDividersColor: Color,
) {

    @Stable
    internal fun maskColor(enabled: Boolean): Color =
        if (enabled) maskColor else disabledMaskColor

    @Stable
    internal fun editableColor(enabled: Boolean): Color =
        if (enabled) editableDigitColor else disabledEditableDigitColor
}

@Preview(
    fontScale = 2f,
    showBackground = true,
)
@Preview(showBackground = true)
@Composable
private fun PreviewPhoneOtpTextField() {
    val usFormat: List<PhoneNumberElement> = listOf(
        PhoneNumberElement.Mask("+1 ("),
        PhoneNumberElement.EditableDigit,
        PhoneNumberElement.EditableDigit,
        PhoneNumberElement.EditableDigit,
        PhoneNumberElement.Mask(") "),
        PhoneNumberElement.EditableDigit,
        PhoneNumberElement.EditableDigit,
        PhoneNumberElement.EditableDigit,
        PhoneNumberElement.Mask("-"),
        PhoneNumberElement.EditableDigit,
        PhoneNumberElement.EditableDigit,
        PhoneNumberElement.EditableDigit,
        PhoneNumberElement.EditableDigit
    )

    Column(modifier = Modifier.padding(16.dp)) {
        PhoneOtpTextField(
            onValueChange = {},
            mask = usFormat,
            value = "1234567890",
            modifier = Modifier.fillMaxWidth()
        )
    }
}