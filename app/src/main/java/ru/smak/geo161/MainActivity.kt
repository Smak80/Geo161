package ru.smak.geo161

import android.Manifest
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.smak.geo161.ui.theme.Geo161Theme

class MainActivity : ComponentActivity() {

    private lateinit var requester: ActivityResultLauncher<Array<String>>
    private val mvm by viewModels<MainViewModel>()

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
                it.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) ||
                it.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> mvm.startLocationUpdates()
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
                    val loc by mvm.location.collectAsState()
                    loc?.let{
                        LocationCard(loc = it)
                    }
                }
            }
        }
    }
}

@Composable
fun LocationCard(
    loc: Location,
    modifier: Modifier = Modifier,
){
    ElevatedCard(modifier = modifier) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(stringResource(R.string.lat, loc.latitude))
            Text(stringResource(R.string.lon, loc.longitude))
        }
    }
}