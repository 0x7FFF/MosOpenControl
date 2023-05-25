package ru.gms.mosopencontrol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import ru.gms.mosopencontrol.ui.component.text.Text
import ru.gms.mosopencontrol.ui.component.text.TextViewState
import ru.gms.mosopencontrol.ui.screens.auth.AuthScreen
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
                    AuthScreen(
                        hiltViewModel(),
                    )
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
