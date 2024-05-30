package com.example.phoneinputtextfield

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.phoneinputtextfield.ui.theme.PhoneInputTextFieldTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            PhoneInputTextFieldTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    val patterns = listOf(
                        PhoneNumberElement.FormatPatterns.RUS,
                        PhoneNumberElement.FormatPatterns.USA,
                        PhoneNumberElement.FormatPatterns.FRA,
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        patterns.forEach {
                            var value by remember {
                                mutableStateOf("")
                            }
                            PhoneInputTextField(
                                config = it,
                                onValueChange = { newValue ->
                                    value = newValue
                                },
                                value = value,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PhoneInputTextField(
    modifier: Modifier = Modifier,
    config: List<PhoneNumberElement>,
    onValueChange: (String) -> Unit,
    value: String,
) {
    val editableDigitCount = remember {
        config.filterIsInstance<PhoneNumberElement.EditableDigit>()
    }
    BasicTextField(
        modifier = modifier,
        value = value,
        onValueChange = { newValue ->
            if (newValue.length <= editableDigitCount.size) {
                onValueChange(newValue)
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
        ),
        decorationBox = {
            val digitIndexMap = remember(value) {
                var start = 0
                config.mapIndexedNotNull { index, it ->
                    if (it is PhoneNumberElement.EditableDigit) {
                        index to start++
                    } else null
                }.toMap()
            }
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                config.forEachIndexed { index, phoneNumberElement ->
                    when (phoneNumberElement) {
                        PhoneNumberElement.EditableDigit -> {
                            digitIndexMap[index]?.let { digitIndex ->
                                EditableDigit(
                                    text = value.getOrNull(digitIndex)?.toString(),
                                )
                            }
                        }

                        is PhoneNumberElement.Mask -> {
                            Mask(
                                text = phoneNumberElement.text,
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun Mask(
    modifier: Modifier = Modifier,
    text: String,
) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = 28.sp,
    )
}

@Composable
fun EditableDigit(
    modifier: Modifier = Modifier,
    text: String?,
) {
    Column(
        modifier = modifier
            .width(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AnimatedContent(
            targetState = text,
            transitionSpec = {
                (slideInVertically() + fadeIn())
                    .togetherWith(slideOutVertically() + fadeOut())

            }) { text ->
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = text ?: " ",
                fontSize = 28.sp,
            )
        }

        Divider(
            thickness = 2.dp,
            color = Color.LightGray,
        )
    }
}


sealed interface PhoneNumberElement {
    data class Mask(val text: String) : PhoneNumberElement

    data object EditableDigit : PhoneNumberElement

    object FormatPatterns {
        /**
         * The phone number pattern for Russia. The format includes the country code +7, followed by a 10-digit number
         * structured as "+7 (XXX) XXX-XX-XX".
         */
        val RUS by lazy {
            listOf(
                Mask("+7"),
                Mask("("),
                EditableDigit,
                EditableDigit,
                EditableDigit,
                Mask(")"),
                EditableDigit,
                EditableDigit,
                EditableDigit,
                Mask("-"),
                EditableDigit,
                EditableDigit,
                Mask("-"),
                EditableDigit,
                EditableDigit,
            )
        }

        /**
         * The phone number pattern for the United States. The format includes the country code +1, followed by a 10-digit number
         * structured as "+1 (XXX) XXX-XXXX".
         */
        val USA by lazy {
            listOf(
                Mask("+1"),
                Mask("("),
                EditableDigit,
                EditableDigit,
                EditableDigit,
                Mask(")"),
                EditableDigit,
                EditableDigit,
                EditableDigit,
                Mask("-"),
                EditableDigit,
                EditableDigit,
                EditableDigit,
                EditableDigit,
            )
        }

        /**
         * The phone number pattern for France. The format includes the country code +33, followed by a 9-digit number
         * structured as "+33 X XX XX XX XX".
         */
        val FRA by lazy {
            listOf(
                Mask("+33"),
                Mask(" "),
                EditableDigit,
                Mask(" "),
                EditableDigit,
                EditableDigit,
                Mask(" "),
                EditableDigit,
                EditableDigit,
                Mask(" "),
                EditableDigit,
                EditableDigit,
                Mask(" "),
                EditableDigit,
                EditableDigit
            )
        }
    }
}
