package com.cypress.bbcnewsapplication.presentation


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NewsHeadlineListScreen(innerPadding : PaddingValues) {

    val context = LocalContext.current

    val newsHandlerViewModel: NewsHandlerViewModel = koinViewModel()
    var sources by remember { mutableStateOf("trump") }
    var apiKey by remember { mutableStateOf("efa31af9176c4a05b9f7a69d3c469cf3") }
    var page by remember { mutableIntStateOf(1) }
    val pageSize = 20
    val newsHandlerState by remember { newsHandlerViewModel.newsHandlerState }

    Column(
        modifier = Modifier.padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LaunchedEffect(Unit) {
            page = 1
            newsHandlerViewModel.searchNewsHeadline(
                SearchParams(sources , apiKey , page))
        }

//        Button(
//            onClick = {
//                page = 1
//                newsHandlerViewModel.searchNewsHeadline(
//                    SearchParams(sources , apiKey , page))
//            }
//        ) {
//            Text("Search")
//        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp)
        ) {
            Text("Total news: ${newsHandlerState.newsDto?.totalResults ?: " -"}")
            Spacer(modifier = Modifier.weight(1f))

            newsHandlerState.newsDto?.let { newsDto ->

                Text(
                    "Previous",
                    modifier = Modifier.clickable
                    {
                        if (page > 1)
                            page--
                        newsHandlerViewModel.searchNewsHeadline(
                            SearchParams(sources, apiKey, page)
                        )
                    })
                Spacer(modifier = Modifier.width(6.dp))
                Text("${page}")
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Next",
                    modifier = Modifier.clickable
                    {
                        if (page * pageSize < newsDto.totalResults) {
                            page++
                            newsHandlerViewModel.searchNewsHeadline(
                                SearchParams(sources, apiKey, page)
                            )
                        }
                    })
            }

        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {

            newsHandlerState.newsDto?.let { newsList ->
                items(newsList.articles) { item ->
                    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(item.urlToImage)
                                .crossfade(true)
                                .memoryCachePolicy(CachePolicy.ENABLED)
                                .diskCachePolicy(CachePolicy.ENABLED)
                                .build(),
                            contentDescription = "${item.title} image",
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            when (painter.state) {
                                is AsyncImagePainter.State.Loading -> {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Image(
                                            painter = painterResource(android.R.drawable.ic_menu_report_image),
                                            contentDescription = "placeholder",
                                            modifier = Modifier.size(96.dp) // smaller size
                                        )
                                    }
                                }
                                is AsyncImagePainter.State.Error -> {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Image(
                                            painter = painterResource(android.R.drawable.ic_menu_report_image),
                                            contentDescription = "error",
                                            modifier = Modifier.fillMaxSize()
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

                    }
                }
            }

        }


    }

}