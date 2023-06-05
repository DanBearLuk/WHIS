package com.danluk.whis.utils

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

fun navigateTo(
    navController: NavController,
    route: String,
    restore: Boolean = true,
) {
    navController.navigate(route) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = restore
    }
}