package com.plcoding.runique

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.junka.core.presentation.designsystem.RuniqueTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val mainViewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                mainViewModel.state.isCheckingAuth
            }
        }
        enableEdgeToEdge()
        setContent {
            RuniqueTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(
                    contentWindowInsets = WindowInsets.safeDrawing,
                    topBar = {}
                ) { padding ->
                    Surface(
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {

                        if (!mainViewModel.state.isCheckingAuth){
                            val navController = rememberNavController()
                            NavigationRoot(
                                navController = navController,
                                isLoggedIn = mainViewModel.state.isLoggedIn
                            )
                        }
                    }
                }

            }
        }
    }
}