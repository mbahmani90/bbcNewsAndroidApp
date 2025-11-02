package com.cypress.bbcnewsapplication.presentation.newsHeadline


import android.R
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.cypress.bbcnewsapplication.AppThemeColors
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavController
import com.cypress.bbcnewsapplication.Screen
import com.cypress.bbcnewsapplication.commonComposables.SearchFieldComposable
import com.cypress.bbcnewsapplication.commonComposables.TitleIconComposable
import com.cypress.bbcnewsapplication.commonComposables.noFeedbackClickable

@Composable
fun NewsHeadlineListScreen(navController: NavController ,
                           apiKey: String,
                           sourceId: String,
                           sourceName: String) {

    val context = LocalContext.current

    val newsHandlerViewModel: NewsHandlerViewModel = koinViewModel()
    var query by remember { mutableStateOf("") }

    var page by remember { mutableIntStateOf(1) }
    val pageSize = 20
    val newsHandlerState by remember { newsHandlerViewModel.newsHandlerState }

    LaunchedEffect(Unit) {
        page = 1
        newsHandlerViewModel.searchNewsHeadline(
            SearchParams(sourceId , query, apiKey, page))
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppThemeColors.appBarColor)
                    .statusBarsPadding(),
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .padding(2.dp)
                        .noFeedbackClickable {
                            navController.popBackStack()
                        },
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = AppThemeColors.focusedSearchColor)

//                AsyncImage(
//                    model = ImageRequest.Builder(context)
//                        .data(null)
//                        .crossfade(true)
//                        .memoryCachePolicy(CachePolicy.ENABLED)
//                        .diskCachePolicy(CachePolicy.ENABLED)
//                        .build(),
//                    contentDescription = "source image",
//                    modifier = Modifier.size(48.dp),
//                    contentScale = ContentScale.Crop,
//                    placeholder = painterResource(R.drawable.ic_menu_report_image),
//                    error = painterResource(R.drawable.ic_menu_report_image)
//                )

                TitleIconComposable()

                Spacer(modifier = Modifier.width(4.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    Text(text = sourceName,
                        fontSize = 14.sp,
                        fontWeight = Bold,
                        lineHeight = 14.sp
                    )

                    Text(text = if(!newsHandlerState.isLoading) {
                        "${newsHandlerState.newsDomain?.totalResults ?: "No"} news"
                        }else{
                            "Loading..."
                        },
                        fontSize = 12.sp,
                        lineHeight = 14.sp
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                SearchFieldComposable(Modifier.weight(1f).padding(8.dp)) { keyword ->
                    page = 1
                    newsHandlerViewModel.searchNewsHeadline(
                        SearchParams(sourceId, keyword, apiKey, page)
                    )
                }

            }
        },
        content = { paddingValues ->

            Column(
                modifier = Modifier.padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if(newsHandlerState.isLoading){
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth(),
                        color = AppThemeColors.lightBlue
                    )
                }


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(onClick = {
                        if (page > 1)
                            page--
                        newsHandlerViewModel.searchNewsHeadline(
                            SearchParams(sourceId, query, apiKey, page)
                        )
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "KeyboardArrowRight" ,
                            tint = AppThemeColors.lightBlue)
                    }

                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "${page}", color = Color.Black)
                    Spacer(modifier = Modifier.width(6.dp))

                    newsHandlerState.newsDomain?.let { newsDto ->

                        IconButton(onClick = {
                            if (page * pageSize < newsDto.totalResults) {
                                page++
                                newsHandlerViewModel.searchNewsHeadline(
                                    SearchParams(sourceId, query, apiKey, page)
                                )
                            }
                        }) {
                            Icon(
                                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "KeyboardArrowRight" ,
                                tint = AppThemeColors.lightBlue)
                        }

                    }

                }

                LazyColumn(modifier = Modifier.fillMaxSize()) {

                    newsHandlerState.newsDomain?.let { newsList ->
                        items(newsList.articles) { item ->
                            Column(modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable{
                                    item.url?.let { articleUrl ->
                                        navController.navigate(
                                            Screen.ArticleDetails.createRoute(
                                                Uri.encode(articleUrl)))
                                    }

                                }
                                .testTag("headline_tag")) {
                                SubcomposeAsyncImage(
                                    model = ImageRequest.Builder(context)
                                        .data(item.urlToImage)
                                        .crossfade(true)
                                        .memoryCachePolicy(CachePolicy.ENABLED)
                                        .diskCachePolicy(CachePolicy.ENABLED)
                                        .build(),
                                    contentDescription = "${item.title} image",
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    when (painter.state) {
                                        is AsyncImagePainter.State.Loading -> {
                                            Box(
                                                modifier = Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Image(
                                                    painter = painterResource(R.drawable.ic_menu_report_image),
                                                    contentDescription = "placeholder",
                                                    modifier = Modifier.size(96.dp)
                                                )
                                            }
                                        }
                                        is AsyncImagePainter.State.Error -> {
                                            Box(
                                                modifier = Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Image(
                                                    painter = painterResource(R.drawable.ic_menu_report_image),
                                                    contentDescription = "error",
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .padding(96.dp)
                                                )
                                            }
                                        }
                                        else -> {
                                            SubcomposeAsyncImageContent(
                                                modifier = Modifier.fillMaxSize(),
                                                contentScale = ContentScale.Crop
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(3.dp))
                                item.title?.let {
                                    Text(modifier = Modifier.padding(horizontal = 10.dp),
                                        text = it,
                                        fontSize = 18.sp,
                                        fontWeight = Bold)
                                }
                                Spacer(modifier = Modifier.height(3.dp))
                                item.description?.let {
                                    Text(modifier = Modifier.padding(horizontal = 10.dp),
                                        text = it,
                                        fontSize = 12.sp,
                                        fontStyle = FontStyle.Italic)
                                }
                                Spacer(modifier = Modifier.height(3.dp))
                                item.publishedAt?.let {
                                    Text(modifier = Modifier.padding(horizontal = 10.dp),
                                        text = it,
                                        fontSize = 12.sp,
                                        color = Color.DarkGray,
                                        fontStyle = FontStyle.Normal)
                                }
                                HorizontalDivider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp, horizontal = 8.dp),
                                    thickness = 1.dp,
                                    color = Color.DarkGray.copy(alpha = 0.4f)
                                )
                            }
                        }
                    }

                }


            }
        }
    )



}