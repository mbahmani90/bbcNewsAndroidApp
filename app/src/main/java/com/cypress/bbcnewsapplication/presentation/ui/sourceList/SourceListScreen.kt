package com.cypress.bbcnewsapplication.presentation.ui.sourceList

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cypress.bbcnewsapplication.AppThemeColors
import com.cypress.bbcnewsapplication.Screen
import com.cypress.bbcnewsapplication.common.toFlagEmoji
import com.cypress.bbcnewsapplication.commonComposables.noFeedbackClickable
import com.cypress.bbcnewsapplication.data.repository.SourceParams
import com.cypress.bbcnewsapplication.presentation.viewModel.SourceViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun SourceListScreen(navController: NavController) {

    val sourceViewModel : SourceViewModel = koinViewModel()
    val sourceState by sourceViewModel.sourceState
    val categoryListState by sourceViewModel.categoryListState
    var apiKey by remember { mutableStateOf("b0fa1fc2ee984834aaceb8f77d7f6185") }

    LaunchedEffect(Unit) {
        val tempCategory = sourceViewModel.getSelectedCategory() ?: ""
        sourceViewModel.getSources(SourceParams(
            apiKey = apiKey,
            category = tempCategory
        ))
    }

    Scaffold(
        content = { paddingValues ->
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues)
            ) {

                LazyRow(modifier = Modifier.padding(top = 12.dp , bottom = 8.dp)) {
                    items(categoryListState){ item ->
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(
                                    width = 1.dp,
                                    color = AppThemeColors.lightBlue,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .background(
                                    AppThemeColors.lightBlue.copy(
                                        alpha = if(item.isSelected) 1f else 0f))
                                .padding(horizontal = 10.dp , vertical = 6.dp)
                                .noFeedbackClickable{
                                    var tempCategory = ""
                                    if(sourceViewModel.toggleCategoryItemSelection(item.name)){
                                       tempCategory = item.name
                                    }
                                    sourceViewModel.getSources(SourceParams(
                                        apiKey = apiKey,
                                        category = tempCategory
                                    ))
                                }
                            ,
                            text = item.name,
                            fontSize = 16.sp,
                            color = if(item.isSelected) AppThemeColors.selectTextColor else AppThemeColors.lightBlue
                        )
                    }
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalArrangement = Arrangement.Top
                ) {
                    sourceState.sourceDomain?.let { sourceDomain ->
                        items(sourceDomain.sources) { item ->
                            Card(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .aspectRatio(2f)
                                    .noFeedbackClickable{
                                        navController.navigate(
                                            Screen.HeadlineListScreen.createRoute(
                                                 apiKey , item.id , item.name))
                                    }
                                    .testTag("source_tag"),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(
                                        text = item.name,
                                        fontWeight = Bold,
                                        modifier = Modifier.padding(
                                            top = 8.dp, bottom = 2.dp,
                                            start = 16.dp , end = 16.dp),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth().padding(
                                            start = 16.dp , end = 16.dp,
                                            bottom = 4.dp)
                                    ) {
                                        Text(
                                            text = item.category.replaceFirstChar {
                                                if (it.isLowerCase()) it.titlecase() else it.toString()
                                            },
                                            fontSize = 13.sp,
                                            color = Color.DarkGray
                                        )
                                        Spacer(modifier = Modifier.weight(1f))
                                        Text(
                                            text = item.country.toFlagEmoji(),
                                            fontSize = 16.sp,
                                            color = Color.DarkGray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )

}