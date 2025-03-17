package com.core.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

interface INavigation {
    fun composable(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        innerPadding: PaddingValues,
        showBottomBar: (Boolean) -> Unit = { true }
    )
}

interface IRouter {

}