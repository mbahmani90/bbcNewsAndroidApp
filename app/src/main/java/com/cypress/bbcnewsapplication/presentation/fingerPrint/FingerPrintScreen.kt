package com.cypress.bbcnewsapplication.presentation.fingerPrint

import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cypress.bbcnewsapplication.R
import androidx.navigation.NavController
import com.cypress.bbcnewsapplication.Screen
import com.cypress.bbcnewsapplication.commonComposables.noFeedbackClickable
import org.koin.compose.koinInject

@Composable
fun FingerPrintScreen(navController: NavController) {

    val context = LocalContext.current
    val activity = context as AppCompatActivity
    val fingerPrintInterface: FingerPrintInterface = koinInject()
    var bioAuthResultState by remember { mutableStateOf("") }
    val biometricPrompt by remember { mutableStateOf(BiometricPrompt(activity,
        BiometricAuthCallback({
            bioAuthResultState = it
            if(it == "Authentication Success"){
                navController.navigate(
                    Screen.SourceListScreen.createRoute()
                )
            }
        }))) }

    LaunchedEffect(Unit) {

        fingerPrintInterface.canAuthenticate(
            BiometricManager.from(activity)){ result ->

            when(result){
                is FingerPrintSupportResource.Error -> {
                    navController.navigate(
                        Screen.SourceListScreen.createRoute())
                }
                is FingerPrintSupportResource.Success -> {
                    fingerPrintInterface.checkAuthentication(biometricPrompt)
                }
            }

        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.ic_biometric_authentication),
                contentDescription = "Authenticate",
                modifier = Modifier.noFeedbackClickable {
                    fingerPrintInterface.checkAuthentication(biometricPrompt)
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(bioAuthResultState)
        }
    }

}

class BiometricAuthCallback(
    private val onCallback: (String) -> Unit
) : BiometricPrompt.AuthenticationCallback() {

    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
        super.onAuthenticationError(errorCode, errString)
        onCallback(errString.toString())
    }

    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
        super.onAuthenticationSucceeded(result)
        onCallback("Authentication Success")
    }

    override fun onAuthenticationFailed() {
        super.onAuthenticationFailed()
        onCallback("Authentication Failed")
    }
}