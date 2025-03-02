package com.example.phoneinputtextfield

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.phoneinputotpfield.components.PhoneOtpTextField
import com.example.phoneinputotpfield.components.PhoneOtpTextFieldDefault
import com.example.phoneinputotpfield.PhoneNumberElement.EditableDigit
import com.example.phoneinputotpfield.PhoneNumberElement.Mask
import com.example.phoneinputtextfield.ui.theme.PhoneInputTextFieldTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PhoneInputTextFieldTheme {
                var enabled by remember { mutableStateOf(true) }
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    val patterns = listOf(
                        FormatPatterns.RUS,
                        FormatPatterns.USA,
                        FormatPatterns.FRA,
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        patterns.forEach {
                            var value by remember {
                                mutableStateOf("")
                            }
                            PhoneOtpTextField(
                                mask = it,
                                onValueChange = { newValue ->
                                    value = newValue
                                },
                                value = value,
                                enabled = enabled,
                                textStyle = MaterialTheme.typography.titleMedium
                                    .copy(fontSize = 30.sp),
                                color = PhoneOtpTextFieldDefault.color(
                                    maskColor = Color.Gray,
                                    disabledMaskColor = Color.Black,
                                    editableDigitColor = Color.Black,
                                    disabledEditableDigitColor = Color.Gray,
                                ),
                            )
                        }
                        Button(
                            onClick = {
                                enabled = !enabled
                            }
                        ) {
                            Text(
                                text = if (enabled) "Disable" else "Enable"
                            )
                        }
                    }
                }
            }
        }
    }
}

private object FormatPatterns {
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
