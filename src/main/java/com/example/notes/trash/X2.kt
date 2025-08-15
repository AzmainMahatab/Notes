package com.example.notes.trash

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle

//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun sx2(
//    value: String,
//    onValueChange: (String) -> Unit,
//    hint: String = "",
//    contentTextStyle: TextStyle = TextStyle.Default,
//    hintTextStyle: TextStyle = TextStyle.Default,
//    singleLine: Boolean = true,
//) {
//    Surface {
//        TextField(
//            modifier = Modifier.fillMaxWidth().apply {
//                if (!singleLine) {
//                    fillMaxHeight()
//                    horizontalScroll(rememberScrollState())
//                }
//            },
//            value = value,
//            onValueChange = onValueChange,
//            textStyle = contentTextStyle,
//            singleLine = singleLine,
//            placeholder = {
//                Text(
//                    hint,
//                    style = hintTextStyle,
////                color = DarkGray
//                )
//            },
//            colors = TextFieldDefaults.textFieldColors(
//                containerColor = Color.Transparent,
////            textColor = Color.Black,
////            disabledTextColor = Color.Transparent,
//                focusedIndicatorColor = Color.Transparent,
//                unfocusedIndicatorColor = Color.Transparent,
//                disabledIndicatorColor = Color.Transparent,
//            )
//        )
//    }
//
//}
//
