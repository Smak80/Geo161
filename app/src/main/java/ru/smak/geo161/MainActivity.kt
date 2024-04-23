package ru.smak.geo161

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.smak.geo161.ui.theme.Geo161Theme

class MainActivity : ComponentActivity() {

    private lateinit var requester: ActivityResultLauncher<Array<String>>

    private fun askForPermissions(){
        // Формирование самого запроса на предоставление разрешений
        requester.launch(arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Регистрация результатов запроса (должно быть вызвано до завершения onCreate)
        requester = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ){
            when{
                it.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> Unit
                it.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> askForPermissions()
                else -> android.os.Process.killProcess(android.os.Process.myPid())
            }
        }

        askForPermissions()

        setContent {
            Geo161Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Geo161Theme {
        Greeting("Android")
    }
}