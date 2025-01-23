package io.github.bluestormdna.randomusers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import io.github.bluestormdna.randomusers.ui.navigation.MainNavHost
import io.github.bluestormdna.randomusers.ui.theme.RandomUsersTheme
import org.koin.compose.KoinContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RandomUsersTheme {
                KoinContext {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        contentWindowInsets = WindowInsets.safeContent
                    ) { innerPadding ->
                        MainNavHost(
                            modifier = Modifier.padding(innerPadding),
                            navController = rememberNavController(),
                        )
                    }
                }
            }
        }
    }
}
