package com.cypress.bbcnewsapplication.commonComposables

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import com.cypress.bbcnewsapplication.AppThemeColors


@Composable
fun SearchFieldComposable(modifier: Modifier,
                          searchText: TextFieldValue,
                          onTextChange: (TextFieldValue) -> Unit,
                          onSearch : (String) -> Unit ){

    TextField(
        value = searchText,
        onValueChange = { onTextChange(it) },
        placeholder = { Text("Search...") },
        singleLine = true,
        trailingIcon = {
            Icon(modifier = Modifier.noFeedbackClickable {
                onSearch(searchText.text)
            },
                imageVector = Icons.Filled.Search,
                contentDescription = "Search")
        },
        colors = TextFieldDefaults.colors(
            focusedTextColor = AppThemeColors.focusedSearchColor,
            unfocusedTextColor = AppThemeColors.lightGray,
            disabledTextColor = AppThemeColors.lightGray.copy(alpha = 0.4f),
            cursorColor = AppThemeColors.focusedSearchColor,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedTrailingIconColor = AppThemeColors.focusedSearchColor,
            unfocusedTrailingIconColor = AppThemeColors.lightGray.copy(alpha = 0.6f),
            disabledTrailingIconColor = AppThemeColors.lightGray.copy(alpha = 0.4f),
            focusedPlaceholderColor = AppThemeColors.focusedSearchColor.copy(alpha = 0.6f),
            unfocusedPlaceholderColor = AppThemeColors.lightGray.copy(alpha = 0.6f),
            disabledPlaceholderColor = AppThemeColors.lightGray.copy(alpha = 0.4f)
        ),
        modifier = modifier
    )
}