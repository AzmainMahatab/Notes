package com.example.notes.ui.screen.single_note.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun HintedTransparentTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    contentTextStyle: TextStyle = TextStyle.Default,
    hintTextStyle: TextStyle = TextStyle.Default,
    singleLine: Boolean = true,
) {
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .apply {
                if (!singleLine) {
                    fillMaxHeight()
                    horizontalScroll(rememberScrollState())
                }
            },
        value = value,
        onValueChange = onValueChange,
        textStyle = contentTextStyle,
        singleLine = singleLine,
        placeholder = {
            Text(
                hint,
                style = hintTextStyle,
//                color = DarkGray
            )
        },
        colors = TextFieldDefaults
            .colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            )
    )
}
