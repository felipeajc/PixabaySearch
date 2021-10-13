package com.challenge.felipeajc.pixabaysearch.ui

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.challenge.felipeajc.pixabaysearch.R
import com.challenge.felipeajc.pixabaysearch.ui.imagedetails.ImageDetailsScreen
import com.challenge.felipeajc.pixabaysearch.ui.imagesearch.SearchImagesViewModel
import com.challenge.felipeajc.pixabaysearch.ui.imagesearch.SearchScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    val viewModel: SearchImagesViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Search.route,
        modifier = modifier
    ) {

        composable(Screen.Search.route) {
            SearchScreen(viewModel) {
                navController.navigate("${Screen.ImageDetails.route}/${it}")
            }
        }

        composable(
            route = "${Screen.ImageDetails.route}/{imageId}",
            arguments = listOf(navArgument("imageId") { type = NavType.LongType })
        ) { entry ->

            val id = entry.arguments?.getLong("imageId")
            val image = id?.let { viewModel.findImage(it) }
            if (image != null) {
                ImageDetailsScreen(image)
            }
        }
    }
}

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Search : Screen("search_screen", R.string.search)
    object ImageDetails : Screen("image_details_screen", R.string.details)
}
