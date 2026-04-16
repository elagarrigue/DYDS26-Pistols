@file:Suppress("FunctionName")

package edu.dyds.movies.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import edu.dyds.movies.di.MoviesDependencyInjector
import edu.dyds.movies.presentation.home.HomeScreen
import edu.dyds.movies.presentation.detail.DetailScreen

private const val HOME = "home"

private const val DETAIL = "detail"

private const val MOVIE_ID = "movieId"

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = HOME) {
        homeDestination(navController)
        detailDestination(navController)
    }
}

private fun NavGraphBuilder.homeDestination(navController: NavHostController) {
    composable(HOME) {
        HomeScreen(
            viewModel = MoviesDependencyInjector.getHomeViewModel(),
            /*TODO: Tecnicamente rompe DIP ya que llama a MoviesDependencyInjector.getHomeViewModel() directamente,
                pero es compose asi que no estoy 100% seguro de si habria que inyectarlo con algo como:
                @Composable
                fun Navigation(
                    homeViewModel: HomeViewModel = MoviesDependencyInjector.getHomeViewModel(),
                    detailViewModel: DetailViewModel = MoviesDependencyInjector.getDetailViewModel()
                )
             */
            onGoodMovieClick = {
                navController.navigate("$DETAIL/${it.id}")
            }
        )
    }
}

private fun NavGraphBuilder.detailDestination(navController: NavHostController) {
    composable(
        route = "$DETAIL/{$MOVIE_ID}",
        arguments = listOf(navArgument(MOVIE_ID) { type = NavType.IntType })
    ) { backstackEntry ->
        val movieId = backstackEntry.arguments?.getInt(MOVIE_ID)

        movieId?.let {
            DetailScreen(MoviesDependencyInjector.getDetailViewModel(), it, onBack = { navController.popBackStack() })
        }
    }
}

