package com.cypress.bbcnewsapplication

import android.os.Bundle
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.cypress.bbcnewsapplication.domain.model.ArticleDomain
import com.cypress.bbcnewsapplication.presentation.NewsDetailsScreen
import com.cypress.bbcnewsapplication.presentation.NewsHeadlineListScreen
import com.cypress.bbcnewsapplication.ui.theme.BbcNewsApplicationTheme

sealed class Screen(val route: String) {
    object HeadlineListScreen : Screen("headlineList")
    object ArticleDetails : Screen("articleDetails/{articleUrl}") {
        fun createRoute(articleUrl: String) = "articleDetails/$articleUrl"
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BbcNewsApplicationTheme {

                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Screen.HeadlineListScreen.route) {

                    composable(Screen.HeadlineListScreen.route) {
                        NewsHeadlineListScreen(navController)
                    }

                    composable(
                        route = Screen.ArticleDetails.route,
                        arguments = listOf(navArgument("articleUrl") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val articleUrl = backStackEntry.arguments?.getString("articleUrl") ?: ""
                        NewsDetailsScreen(articleUrl)
                    }
                }

            }
        }
    }

}
