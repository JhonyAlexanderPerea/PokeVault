package co.edu.uniquindio.pokevault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import co.edu.uniquindio.pokevault.ui.navigation.AppNavigation
import co.edu.uniquindio.pokevault.ui.theme.PokeVaultTheme
import co.edu.uniquindio.pokevault.util.NetworkObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var networkObserver: NetworkObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        networkObserver = NetworkObserver(applicationContext)

        setContent {
            PokeVaultTheme {
                val isOnline by networkObserver.observe().collectAsState(initial = true)
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        AppNavigation(isOnline = isOnline)
                    }
                }
            }
        }
    }
}
