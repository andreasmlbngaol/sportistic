package com.jawapbo.sportistic.navigation

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jawapbo.sportistic.features.auth.sign_in.SignInScreen
import com.jawapbo.sportistic.features.main.home.HomeScreen
import com.jawapbo.sportistic.features.main.maps.MapsScreen
import com.jawapbo.sportistic.features.splash.SplashScreen

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavKey.Splash
    ) {
        composable<NavKey.Splash> {
            SplashScreen(
                onNavigate = { navKey ->
                    navController.navigate(navKey) {
                        launchSingleTop = true
                        popUpTo<NavKey.Splash> {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<NavKey.SignIn> {
            SignInScreen(
                onNavigateToHome = {
                    navController.navigate(NavKey.Home) {
                        launchSingleTop = true
                        popUpTo<NavKey.SignIn> {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<NavKey.Home> {
            HomeScreen(
                onNavigateToSignIn = {
                    navController.navigate(NavKey.SignIn) {
                        launchSingleTop = true
                        popUpTo<NavKey.Home> {
                            inclusive = true
                        }
                    }
                },
                onNavigateToMaps = {
                    navController.navigate(NavKey.Maps)
                }
            )
        }

        composable<NavKey.Maps> {
            MapsScreen(
                onBack = {
                    navController.navigateUp()
                }
            )
        }
    }

}