package io.github.bluestormdna.randomusers.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.github.bluestormdna.randomusers.ui.userdetails.UserDetailsScreenRoute
import io.github.bluestormdna.randomusers.ui.users.UsersScreenRoute
import kotlinx.serialization.Serializable

@Serializable
sealed interface TopLevelDestination

@Serializable
data object Users : TopLevelDestination

@Serializable
data class UserDetails(val id: String): TopLevelDestination

@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Users,
        modifier = modifier,
    ) {
        composable<Users> {
            UsersScreenRoute(
                onUserDetail = { id -> navController.navigate(UserDetails(id)) }
            )
        }
        composable<UserDetails> {
            UserDetailsScreenRoute()
        }
    }
}