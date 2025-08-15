package com.example.notes.ui.screen.all_notes.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

@Composable
fun DefaultRadioButton(
    text: String,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier
            .clip(RoundedCornerShape(5))
            .clickable {
                onSelect()
            },
        verticalAlignment = CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect,
//            onClick = { },
        )
        Text(
            text = text, style = MaterialTheme.typography.bodyLarge
        )
    }

}