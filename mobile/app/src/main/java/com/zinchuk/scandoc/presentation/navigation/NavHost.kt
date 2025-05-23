package com.zinchuk.scandoc.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.zinchuk.scandoc.presentation.screens.details.DetailsScreen
import com.zinchuk.scandoc.presentation.screens.details.DetailsScreenVM
import com.zinchuk.scandoc.presentation.screens.main.MainScreen
import com.zinchuk.scandoc.presentation.screens.main.MainScreenVM
import java.util.UUID

private const val UUID_ARG_NAME = "UUID_STRING"

@Composable
fun NavigationHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Route.MAIN_SCREEN.route,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popEnterTransition = popEnterTransition,
        popExitTransition = popExitTransition,
    ) {
        composable(route = Route.MAIN_SCREEN.route) {
            val vm = hiltViewModel<MainScreenVM>()
            MainScreen(vm, navController)
        }

        composable(
            route = Route.DETAILS_SCREEN.route + "/{$UUID_ARG_NAME}",
            arguments =
                listOf(
                    navArgument(UUID_ARG_NAME) {
                        type = NavType.StringType
                    },
                ),
        ) { backStackEntry ->
            val vm = hiltViewModel<DetailsScreenVM>()
            val uuid =
                backStackEntry
                    .arguments
                    ?.getString(UUID_ARG_NAME)
                    ?.let { UUID.fromString(it) }

            LaunchedEffect(uuid) {
                vm.init(uuid!!)
            }
            DetailsScreen(vm, navController)
        }
    }
}
