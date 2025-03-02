# Compose Phone OTP Text Field

[![phone_OTP_text_field_logo.gif](phone_OTP_text_field_logo.gif)](https://example.com) This library provides a customizable `PhoneOtpTextField` Composable for Android, designed for entering phone numbers with a predefined format (mask).  It supports features like:

*   **Customizable Masking:** Define the phone number format using a sequence of `PhoneNumberElement`s (either `Mask` for static characters or `EditableDigit` for digit placeholders).
*   **Digit-Only Input:**  Restricts input to numeric characters only.
*   **Length Limit:**  Automatically enforces the maximum number of digits based on the provided mask.
*   **Cursor Position Tracking:**  Provides a callback to get the current *visual* cursor position within the formatted text (including mask characters).
*   **Composable UI:** Built with Jetpack Compose for easy integration into your Compose-based UI.
*   **Customizable Appearance:**  Allows customization of text style and colors for both mask and editable digit elements.

## Demo

![phone_OTP_text_field_sample.gif](phone_OTP_text_field_sample.gif)

## Usage

1.  **Define the Phone Number Format:**

    Create a `List<PhoneNumberElement>` to represent your desired phone number format.  Use `PhoneNumberElement.Mask` for static characters (like parentheses, spaces, dashes) and `PhoneNumberElement.EditableDigit` for each digit the user can enter.

    ```kotlin
    // Example: Russian phone number format
    val russianPhoneNumberFormat: List<PhoneNumberElement> = listOf(
        PhoneNumberElement.Mask("+7"),
        PhoneNumberElement.Mask("("),
        PhoneNumberElement.EditableDigit,
        PhoneNumberElement.EditableDigit,
        PhoneNumberElement.EditableDigit,
        PhoneNumberElement.Mask(")"),
        PhoneNumberElement.EditableDigit,
        PhoneNumberElement.EditableDigit,
        PhoneNumberElement.EditableDigit,
        PhoneNumberElement.Mask("-"),
        PhoneNumberElement.EditableDigit,
        PhoneNumberElement.EditableDigit,
        PhoneNumberElement.Mask("-"),
        PhoneNumberElement.EditableDigit,
        PhoneNumberElement.EditableDigit,
    )
    ```

2.  **Use the `PhoneOtpTextField` Composable:**

    ```kotlin
    @Composable
    fun MyScreen() {
        var phoneNumber by remember { mutableStateOf("") }
        var cursorPosition by remember { mutableStateOf(0) }

        Column {
            PhoneOtpTextField(
                mask = russianPhoneNumberFormat, // Or any other format
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                onCursorPositionChanged = { position ->
                    cursorPosition = position
                    // Do something with the cursor position (e.g., update UI)
                },
                enabled = true, // Optional: enable/disable the field
                textStyle = MaterialTheme.typography.h6.copy( // Customize text style
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = PhoneOtpTextFieldDefault.color().copy( // Customize colors
                    maskColor = { enabled -> if (enabled) Color.DarkGray else Color.LightGray }
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Text("Current Cursor Position: $cursorPosition")
        }
    }
    ```
    **Result:**

    ![phone_OTP_text_field_usage_sample.png](phone_OTP_text_field_usage_sample.png)
