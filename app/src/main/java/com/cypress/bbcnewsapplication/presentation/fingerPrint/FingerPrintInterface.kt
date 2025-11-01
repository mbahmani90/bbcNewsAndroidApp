package com.cypress.bbcnewsapplication.presentation.fingerPrint

import android.hardware.biometrics.BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE
import android.hardware.biometrics.BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE
import android.hardware.biometrics.BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED
import android.os.Build
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt

sealed class FingerPrintSupportResource<T>(val data: T? = null) {
    class Success<T>() : FingerPrintSupportResource<T>()
    class Error<T>(data: T) : FingerPrintSupportResource<T>()
}

interface FingerPrintInterface{
    fun checkAuthentication(prompt: BiometricPrompt)
    fun canAuthenticate(manager : BiometricManager, onResult: (FingerPrintSupportResource<Int?>) -> Unit)
}

class FingerPrintInterfaceImp(
    private val biometricPromptInfo: BiometricPrompt.PromptInfo
) : FingerPrintInterface{

    override fun checkAuthentication(prompt: BiometricPrompt){
        prompt.authenticate(biometricPromptInfo)
    }

    override fun canAuthenticate(
        manager: BiometricManager,
        onResult: (FingerPrintSupportResource<Int?>) -> Unit
    ) {
        if(Build.VERSION.SDK_INT >= 30) {
            val authenticators = BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.DEVICE_CREDENTIAL
            when(manager.canAuthenticate(authenticators)) {
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                    onResult(FingerPrintSupportResource.Error(
                        BIOMETRIC_ERROR_HW_UNAVAILABLE))
                    return
                }
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                    onResult(FingerPrintSupportResource.Error(
                        BIOMETRIC_ERROR_NO_HARDWARE))
                    return
                }
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                    onResult(FingerPrintSupportResource.Error(
                        BIOMETRIC_ERROR_NONE_ENROLLED))
                    return
                }
                else -> {
                    onResult(FingerPrintSupportResource.Success())
                }
            }
        }else{
            onResult(FingerPrintSupportResource.Error(-1))
        }
    }
}