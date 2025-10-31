package com.cypress.bbcnewsapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cypress.bbcnewsapplication.presentation.newsDetails.NewsDetailsScreen
import com.cypress.bbcnewsapplication.presentation.newsHeadline.NewsHeadlineListScreen
import com.cypress.bbcnewsapplication.presentation.sourceList.SourceListScreen
import com.cypress.bbcnewsapplication.ui.theme.BbcNewsApplicationTheme

sealed class Screen(val route: String) {
    object SourceListScreen : Screen("sourceList")
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

                val apiKey = "b0fa1fc2ee984834aaceb8f77d7f6185"

                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Screen.SourceListScreen.route) {

                    composable(Screen.SourceListScreen.route) {
                        SourceListScreen(navController)
                    }

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
