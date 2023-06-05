package com.danluk.whis

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Down
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Left
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Right
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Up
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.danluk.whis.classes.Screen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

fun AnimatedContentTransitionScope<NavBackStackEntry>.getSlideTransitions(
    vararg directions: Pair<Screen, SlideDirection>,
    isEnter: Boolean
): Any {
    if (isEnter) {
        val direction = directions.find {
            it.first.route == initialState.destination.route
        }?.second ?: Up

        return slideIntoContainer(
            direction,
            animationSpec = tween(700)
        )
    } else {
        val direction = directions.find {
            it.first.route == targetState.destination.route
        }?.second ?: Up

        return slideOutOfContainer(
            direction,
            animationSpec = tween(700)
        )
    }
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.getSlideTransitions(
    direction: SlideDirection,
    isEnter: Boolean
): Any {
    return if (isEnter) {
        slideIntoContainer(
            direction,
            animationSpec = tween(700)
        )
    } else {
        slideOutOfContainer(
            direction,
            animationSpec = tween(700)
        )
    }
}

@Composable
@ExperimentalMaterial3Api
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalLayoutApi
fun AnimationNav(
    navController: NavHostController,
    innerPadding: PaddingValues
) {
    AnimatedNavHost(
        navController,
        startDestination = "Home",
    ) {
        composable(
            Screen.TitleInfo.route,
            arguments = listOf(navArgument("titleId") { type = NavType.IntType }),
            enterTransition = {
                getSlideTransitions(Down, true) as EnterTransition
            },
            exitTransition = {
                getSlideTransitions(Up, false) as ExitTransition
            }
        ) { backStackEntry ->
            TitleInfoScreen(innerPadding, backStackEntry.arguments?.getInt("titleId") ?: 0)
        }
        composable(
            Screen.Home.route,
            enterTransition = {
                getSlideTransitions(
                    Screen.Search to Left,
                    Screen.Profile to Right,
                    Screen.TitleInfo to Up,
                    isEnter = true
                ) as EnterTransition
            },
            exitTransition = {
                getSlideTransitions(
                    Screen.Search to Right,
                    Screen.Profile to Left,
                    Screen.TitleInfo to Down,
                    isEnter = false
                ) as ExitTransition
            }
        ) { HomeScreen(innerPadding, navController) }
        composable(
            "Search",
            enterTransition = {
                getSlideTransitions(
                    Screen.Home to Right,
                    Screen.Profile to Left,
                    Screen.TitleInfo to Up,
                    isEnter = true
                ) as EnterTransition
            },
            exitTransition = {
                getSlideTransitions(
                    Screen.Home to Left,
                    Screen.Profile to Right,
                    Screen.TitleInfo to Down,
                    isEnter = false
                ) as ExitTransition
            }
        ) { SearchScreen(innerPadding, navController) }
        composable(
            "Profile",
            enterTransition = {
                getSlideTransitions(
                    Screen.Home to Left,
                    Screen.Search to Right,
                    Screen.TitleInfo to Up,
                    isEnter = true
                ) as EnterTransition
            },
            exitTransition = {
                getSlideTransitions(
                    Screen.Home to Right,
                    Screen.Search to Left,
                    Screen.TitleInfo to Down,
                    isEnter = false
                ) as ExitTransition
            }
        ) { ProfileScreen(innerPadding, navController) }
    }
}
