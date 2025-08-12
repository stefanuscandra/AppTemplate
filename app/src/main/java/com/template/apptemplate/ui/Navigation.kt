package com.template.apptemplate.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.template.apptemplate.ui.screens.DetailScreen
import com.template.apptemplate.ui.screens.HomeScreen

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = NavigationItem.Home.route,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavigationItem.Home.route) {
            HomeScreen(navController)
        }
        composable(
            route = NavigationItem.Detail.route + "/{id}/{value}",
            arguments = listOf(
                navArgument(name = "id") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                },
                navArgument(name = "value") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                }
            )
        ) {
            DetailScreen(navController)
        }
    }
}