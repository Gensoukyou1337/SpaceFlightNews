package com.ivan.spaceflightnews

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
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
import org.koin.androidx.compose.koinViewModel
import org.koin.core.component.KoinComponent

class MainActivity : ComponentActivity(), KoinComponent {

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
fun MainNavHost(viewModel: MainNavHostViewModel = koinViewModel()) {
    val navController = rememberNavController()

    LaunchedEffect(true) {
        viewModel.getCurrentIDTokenFlow().collect {
            if (it.isEmpty() && navController.currentBackStackEntry?.destination?.hasRoute<Login>() == false) {
                navController.navigate(Login) {
                    popUpTo(Main) {inclusive = true}
                }
            }
        }
    }

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