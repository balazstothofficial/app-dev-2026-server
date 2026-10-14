package tcs.app.dev

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.LifecycleResumeEffect

/** Keep platform permission handling outside the selection and loading exercise. */
@Composable
fun ServerApp() {
    val context = LocalContext.current
    fun hasLocalNetworkPermission() = Build.VERSION.SDK_INT < 37 ||
        context.checkSelfPermission(Manifest.permission.ACCESS_LOCAL_NETWORK) ==
        PackageManager.PERMISSION_GRANTED

    var hasAccess by remember { mutableStateOf(hasLocalNetworkPermission()) }
    val requestPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasAccess = granted }

    // Recheck when returning from Settings, including after a permission revocation.
    LifecycleResumeEffect(Unit) {
        hasAccess = hasLocalNetworkPermission()
        onPauseOrDispose { }
    }

    if (hasAccess) {
        UniversitySelectionApp()
    } else {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(R.string.local_network_explanation))
            Button(onClick = {
                if (Build.VERSION.SDK_INT >= 37) {
                    requestPermission.launch(Manifest.permission.ACCESS_LOCAL_NETWORK)
                }
            }) {
                Text(stringResource(R.string.allow_local_network))
            }
            // Settings also works if repeated denials prevent another system prompt.
            TextButton(onClick = {
                context.startActivity(Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    "package:${context.packageName}".toUri()
                ))
            }) {
                Text(stringResource(R.string.open_app_settings))
            }
        }
    }
}
