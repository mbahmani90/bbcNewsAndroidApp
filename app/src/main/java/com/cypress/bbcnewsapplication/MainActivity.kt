package com.cypress.bbcnewsapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cypress.bbcnewsapplication.ScreenArgumentsKey.API_KEY
import com.cypress.bbcnewsapplication.ScreenArgumentsKey.ARTICLE_URL
import com.cypress.bbcnewsapplication.ScreenArgumentsKey.SOURCE_ID
import com.cypress.bbcnewsapplication.ScreenArgumentsKey.SOURCE_NAME
import com.cypress.bbcnewsapplication.presentation.fingerPrint.FingerPrintScreen
import com.cypress.bbcnewsapplication.presentation.newsDetails.NewsDetailsScreen
import com.cypress.bbcnewsapplication.presentation.newsHeadline.NewsHeadlineListScreen
import com.cypress.bbcnewsapplication.presentation.sourceList.SourceListScreen
import com.cypress.bbcnewsapplication.ui.theme.BbcNewsApplicationTheme

object ScreenArgumentsKey{
    const val API_KEY = "apiKey"
    const val SOURCE_ID = "sourceId"
    const val SOURCE_NAME = "sourceName"
    const val ARTICLE_URL = "articleUrl"
}

sealed class Screen(val route: String) {

    object FingerPrintScreen : Screen("fingerPrint")
    object SourceListScreen : Screen("sourceList"){
        fun createRoute() = "sourceList"
    }
    object HeadlineListScreen : Screen("headlineList/{${API_KEY}}/{${SOURCE_ID}}/{${SOURCE_NAME}}"){
        fun createRoute(apiKey: String ,
                        sourceId: String,
                        sourceName: String) = "headlineList/$apiKey/$sourceId/$sourceName"
    }
    object ArticleDetails : Screen("articleDetails/{${ARTICLE_URL}}") {
        fun createRoute(articleUrl: String) = "articleDetails/$articleUrl"
    }
}

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BbcNewsApplicationTheme {

                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Screen.FingerPrintScreen.route) {

                    composable(
                        route = Screen.FingerPrintScreen.route
                    ) { backStackEntry ->
                        FingerPrintScreen(navController)
                    }

                    composable(
                        route = Screen.SourceListScreen.route
                    ) { backStackEntry ->
                        SourceListScreen(navController)
                    }

                    composable(
                        route = Screen.HeadlineListScreen.route,
                        arguments = listOf(
                            navArgument(API_KEY) { type = NavType.StringType },
                            navArgument(SOURCE_ID) { type = NavType.StringType },
                            navArgument(SOURCE_NAME) { type = NavType.StringType }
                        )
                        ) { backStackEntry ->
                            val apiKeyArgument = backStackEntry.arguments?.getString(API_KEY) ?: ""
                            val sourceIdArgument = backStackEntry.arguments?.getString(SOURCE_ID) ?: ""
                            val sourceNameArgument = backStackEntry.arguments?.getString(SOURCE_NAME) ?: ""
                            NewsHeadlineListScreen(navController,
                                apiKeyArgument , sourceIdArgument , sourceNameArgument)
                        }

                    composable(
                        route = Screen.ArticleDetails.route,
                        arguments = listOf(
                            navArgument(ARTICLE_URL) { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val articleUrl = backStackEntry.arguments?.getString(ARTICLE_URL) ?: ""
                        NewsDetailsScreen(articleUrl)
                    }
                }

            }
        }
    }

}
