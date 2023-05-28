package ru.gms.mosopencontrol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import ru.gms.mosopencontrol.ui.component.text.Text
import ru.gms.mosopencontrol.ui.component.text.TextViewState
import ru.gms.mosopencontrol.ui.screens.auth.AuthScreen
import ru.gms.mosopencontrol.ui.screens.code.AuthCodeScreen
import ru.gms.mosopencontrol.ui.theme.MosOpenControlTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            MosOpenControlTheme {
                val systemUiController = rememberSystemUiController()
                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = true,
                    )
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MosOpenControlTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "auth"
                    ) {
                        composable("auth") {
                            AuthScreen(
                                hiltViewModel(),
                                navController,
                            )
                        }
                        composable("code/{phoneNumber}") { backStackEntry ->
                            AuthCodeScreen(
                                backStackEntry.arguments?.getString("phoneNumber").orEmpty(),
                                hiltViewModel(),
                                navController,
                            )
                        }
                        composable("main") {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .wrapContentSize(),
                                    state = TextViewState(
                                        text = "Main",
                                        style = MosOpenControlTheme.typography.headlineLarge
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        state = TextViewState(
            style = MosOpenControlTheme.typography.headlineMedium,
            text = "Hello $name!",
            color = MosOpenControlTheme.colorScheme.onBackground,
        ),
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(),
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MosOpenControlTheme {
        Greeting("Android")
    }
}
