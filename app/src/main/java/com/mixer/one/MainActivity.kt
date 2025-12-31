package com.mixer.one

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.mixer.one.data.PreferencesManager
import com.mixer.one.shizuku.ShizukuRepository
import com.mixer.one.ui.setup.SetupViewModel
import com.mixer.one.ui.setup.SetupWizardScreen
import com.mixer.one.ui.theme.MixerTheme
import com.mixer.one.ui.theme.NothingColors

class MainActivity : ComponentActivity() {

    private lateinit var shizukuRepository: ShizukuRepository
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        // Install splash screen BEFORE super.onCreate()
        installSplashScreen()
        
        super.onCreate(savedInstanceState)

        // Initialize repositories
        shizukuRepository = ShizukuRepository(this)
        preferencesManager = PreferencesManager(this)

        setContent {
            MixerTheme {
                MainContent()
            }
        }
    }

    @Composable
    fun MainContent() {
        val isSetupCompleted by preferencesManager.isSetupCompleted.collectAsState(initial = false)

        if (isSetupCompleted) {
            // Show main app screen
            MainAppScreen()
        } else {
            // Show setup wizard
            val viewModel = remember {
                SetupViewModel(
                    shizukuRepository = shizukuRepository,
                    preferencesManager = preferencesManager,
                    context = this
                )
            }

            SetupWizardScreen(
                viewModel = viewModel,
                onSetupComplete = {
                    // Setup completed, will automatically switch to main screen
                }
            )
        }
    }

    @Composable
    fun MainAppScreen() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(NothingColors.Black)
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // App Title with red accent - Nothing style
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "MIXER",
                        style = MaterialTheme.typography.displayMedium,
                        color = NothingColors.White
                    )
                    Text(
                        text = "(1)",
                        style = MaterialTheme.typography.headlineMedium,
                        color = NothingColors.Red
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "ACTIVATE VOLUME OVERLAY",
                    style = MaterialTheme.typography.labelLarge,
                    color = NothingColors.GreyMedium
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Step 1: Overlay Permission
                SetupStep(
                    number = "1",
                    title = "GRANT OVERLAY PERMISSION",
                    description = "Allows Mixer to show volume controls on top of other apps",
                    onClick = { requestOverlayPermission() }
                )

                // Step 2: Accessibility Service
                SetupStep(
                    number = "2",
                    title = "ENABLE VOLUME KEY DETECTION",
                    description = "Allows Mixer to detect volume button presses",
                    onClick = { openAccessibilitySettings() }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "After completing both steps,\npress volume buttons to see the overlay!",
                    style = MaterialTheme.typography.bodySmall,
                    color = NothingColors.GreyDim,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }

    @Composable
    fun SetupStep(
        number: String,
        title: String,
        description: String,
        onClick: () -> Unit
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = NothingColors.GreyContainer,
                contentColor = NothingColors.White
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Red number badge
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(NothingColors.Red, RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = number,
                        style = MaterialTheme.typography.titleMedium,
                        color = NothingColors.White
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelLarge,
                        color = NothingColors.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = NothingColors.GreyMedium
                    )
                }
            }
        }
    }

    private fun requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivity(intent)
            }
        }
    }

    private fun openAccessibilitySettings() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        shizukuRepository.cleanup()
    }
}
