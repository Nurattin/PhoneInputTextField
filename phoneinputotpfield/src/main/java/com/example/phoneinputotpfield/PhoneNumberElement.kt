package com.example.phoneinputotpfield

/**
 * Represents a single element within a phone number format.
 * A phone number format is defined as a sequence of these elements, which can be either static
 * mask characters (like parentheses or dashes) or placeholders for editable digits.
 *
 * This sealed interface provides a structured way to define and manipulate phone number formats
 * within a Compose `BasicTextField`, allowing for easy formatting, validation, and input handling.
 * It separates the visual representation of the phone number (the mask) from the user-entered data (the digits).
 *
 * Example (within a Composable context):
 * ```kotlin
 * val usFormat: List<PhoneNumberElement> = listOf(
 *     PhoneNumberElement.Mask("+1 ("),
 *     PhoneNumberElement.EditableDigit,
 *     PhoneNumberElement.EditableDigit,
 *     PhoneNumberElement.EditableDigit,
 *     PhoneNumberElement.Mask(") "),
 *     PhoneNumberElement.EditableDigit,
 *     PhoneNumberElement.EditableDigit,
 *     PhoneNumberElement.EditableDigit,
 *     PhoneNumberElement.Mask("-"),
 *     PhoneNumberElement.EditableDigit,
 *     PhoneNumberElement.EditableDigit,
 *     PhoneNumberElement.EditableDigit,
 *     PhoneNumberElement.EditableDigit
 * )
 *
 *  PhoneOtpTextField(
 *      mask = usFormat,
 *      value = phoneNumber,
 *      onValueChange = { phoneNumber = it },
 *  )
 *
 * ```
 * In this example, `usFormat` defines the structure of a US phone number.  The `PhoneOtpTextField`
 * Composable uses this structure to display the formatted input field, handling masking and digit input.
 *
 * @see Mask
 * @see EditableDigit
 */
sealed interface PhoneNumberElement {

    /**
     * Represents a static, non-editable part of a phone number format.
     * This could be parentheses, spaces, dashes, or any other characters that are part of the phone number's
     * visual representation but are not digits that the user can input.
     *
     * @property text The literal string representing the mask element.
     *
     * Example:
     * ```kotlin
     * val mask = PhoneNumberElement.Mask(" - ")
     * ```
     */
    data class Mask(val text: String) : PhoneNumberElement

    /**
     * Represents a single editable digit position within a phone number format.
     * This signifies a placeholder where the user can enter a digit.
     *
     * Example:
     * ```kotlin
     *  val format = listOf(PhoneNumberElement.Mask("("),
     *      PhoneNumberElement.EditableDigit,
     *      PhoneNumberElement.Mask(")")
     *  )
     * ```
     */
    data object EditableDigit : PhoneNumberElement
}