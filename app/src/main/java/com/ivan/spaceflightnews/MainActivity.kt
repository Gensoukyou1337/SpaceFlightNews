package com.ivan.spaceflightnews

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.ivan.spaceflightnews.common.ItemType
import com.ivan.spaceflightnews.screens.details.DetailsScreen
import com.ivan.spaceflightnews.screens.login.LoginScreen
import com.ivan.spaceflightnews.screens.main.MainScreen
import com.ivan.spaceflightnews.screens.search.SearchScreen
import com.ivan.spaceflightnews.screens.section.SectionScreen
import com.ivan.spaceflightnews.ui.theme.SpaceFlightNewsTheme
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpaceFlightNewsTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    // Greeting("Android")
                    MainNavHost()
                }
            }
        }
    }
}

@Serializable
object Login
@Serializable
object Main
@Serializable
data class Search(val itemType: ItemType)
@Serializable
data class Section(val itemType: ItemType)
@Serializable
data class ItemDetails(
    val itemType: ItemType,
    val itemId: Int
)

@Composable
fun MainNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Login) {
        composable<Login> { LoginScreen(navController = navController) }
        composable<Main> { MainScreen(navController = navController) }
        composable<Section> { navBackStackEntry ->
            SectionScreen(navController = navController, data = navBackStackEntry.toRoute<Section>())
        }
        composable<Search> { navBackStackEntry ->
            SearchScreen(navController = navController, data = navBackStackEntry.toRoute<Search>())
        }
        composable<ItemDetails> { navBackStackEntry ->
            DetailsScreen(data = navBackStackEntry.toRoute<ItemDetails>())
        }
    }
}