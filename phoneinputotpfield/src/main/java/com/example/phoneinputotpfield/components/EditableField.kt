package com.example.phoneinputotpfield.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
internal fun EditableField(
    modifier: Modifier = Modifier,
    dividerColor: Color = Color.LightGray,
    text: String?,
) {
    Column(
        modifier = modifier
            .width(IntrinsicSize.Max),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(
                    if (text == null) 0f else 1f
                ),
            text = text ?: "0",
        )

        HorizontalDivider(
            thickness = 2.dp,
            color = dividerColor
        )
    }
}