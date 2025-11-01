package com.cypress.bbcnewsapplication.presentation.sourceList

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cypress.bbcnewsapplication.AppThemeColors
import com.cypress.bbcnewsapplication.Screen
import com.cypress.bbcnewsapplication.commonComposables.noFeedbackClickable
import com.cypress.bbcnewsapplication.data.repository.SourceParams
import org.koin.androidx.compose.koinViewModel


@Composable
fun SourceListScreen(navController: NavController) {

    val sourceViewModel : SourceViewModel = koinViewModel()
    val sourceState by sourceViewModel.sourceState
    val categoryListState by sourceViewModel.categoryListState
    var apiKey by remember { mutableStateOf("b0fa1fc2ee984834aaceb8f77d7f6185") }

    LaunchedEffect(Unit) {
        sourceViewModel.getSources(SourceParams(
            apiKey = apiKey,
            category = ""
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
                                    sourceViewModel.toggleCategoryItemSelection(item.name)
                                    sourceViewModel.getSources(SourceParams(
                                        apiKey = apiKey,
                                        category = item.name
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
                                    .clickable{
                                        navController.navigate(
                                            Screen.HeadlineListScreen.createRoute(
                                                 apiKey , item.id , item.name))
                                    },
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(
                                        text = item.name,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )

}