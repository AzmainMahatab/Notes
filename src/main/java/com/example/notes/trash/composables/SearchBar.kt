package com.example.notes.trash.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction


//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SearchTopAppBar(
//    onTextChange: (String) -> Unit,
//    onCloseSearchClick: () -> Unit,
//    imeAction: ImeAction = ImeAction.Search,
//    imageVector: ImageVector = Icons.Filled.Close,
//    iconDescription: String = "Close Icon",
//
////    vm: ListScreenViewModel = viewModel()
//) {
//
//    var text by remember { mutableStateOf("") }
//
//
//    TopAppBar(
//        title = {
////        TODO
//            val focusManager = LocalFocusManager.current
//            val focusRequester = FocusRequester()
//            TextField(
//                modifier = Modifier
//                    .fillMaxWidth(),
////                    // add focusRequester modifier
////                    .focusRequester(focusRequester),
////            value = vm.searchBarText,
////                TODO
//                value = text,
//                onValueChange = {
//                    text = it
//                    onTextChange(text)
////                    vm.textChange(text)
//                },
//
//                singleLine = true,
//
//                keyboardOptions = KeyboardOptions(
//                    imeAction = imeAction
//                ),
//
//                keyboardActions = KeyboardActions(
//                    onSearch = {
//                        focusManager.clearFocus()
//                    },
//                ),
//            )
//        },
//
//        actions = {
//            IconButton(
//                content = {
//                    Icon(
//                        imageVector = imageVector,
//                        contentDescription = iconDescription,
////                            tint = MaterialTheme.colors.topAppBarContentColor
//                    )
//                },
//                onClick = {
//                    text = ""
//                    onCloseSearchClick()
////                            vm.onCloseSearchClick()
//                },
//            )
//        },
//    )
//}
//


