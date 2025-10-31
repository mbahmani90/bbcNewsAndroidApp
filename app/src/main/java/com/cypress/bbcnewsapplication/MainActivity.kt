package com.cypress.bbcnewsapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.cypress.bbcnewsapplication.presentation.NewsHeadlineListScreen
import com.cypress.bbcnewsapplication.ui.theme.BbcNewsApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BbcNewsApplicationTheme {

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                                .background(AppThemeColors.appBarColor)
                                .statusBarsPadding(),
                        ) {
                            IconButton(onClick = {  }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back" ,
                                    tint = AppThemeColors.lightBlue)
                            }
                            AsyncImage(
                                model = ImageRequest.Builder(this@MainActivity)
                                    .data(null)
                                    .crossfade(true)
                                    .memoryCachePolicy(CachePolicy.ENABLED)
                                    .diskCachePolicy(CachePolicy.ENABLED)
                                    .build(),
                                contentDescription = "source image",
                                modifier = Modifier.size(48.dp),
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(android.R.drawable.ic_menu_report_image),
                                error = painterResource(android.R.drawable.ic_menu_report_image)
                            )
                            Text(text = "BBC",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = AppThemeColors.lightBlue)

                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(onClick = {  }) {
                                Icon(
                                    Icons.Filled.Search,
                                    contentDescription = "Search" ,
                                    tint = AppThemeColors.lightBlue)
                            }
                        }
                    },
                    content = {paddingValues ->
                        NewsHeadlineListScreen(paddingValues)
                    }
                )
            }
        }
    }

}
