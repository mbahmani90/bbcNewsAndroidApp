package com.cypress.bbcnewsapplication.presentation

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.cypress.bbcnewsapplication.commonComposables.WebViewWithLoading

@Composable
fun NewsDetailsScreen(articleUrl: String) {

    val decodeArticleUrl = Uri.decode(articleUrl)
    Scaffold(
        content = { paddingValues ->
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues)){
                WebViewWithLoading(decodeArticleUrl)
            }
        }
    )



}