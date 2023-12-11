package com.mikko.todo

import android.content.Intent
import android.hardware.biometrics.BiometricManager
import android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG
import android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import android.hardware.biometrics.BiometricPrompt
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import com.mikko.todo.databinding.ActivityStartBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val anim = AnimationUtils.loadAnimation(this, R.anim.start_anim)
        MainScope().launch {
            binding.textStart.startAnimation(anim)
            /*AlertDialog.Builder(this@StartActivity)
                .setTitle("Warning")
                .setMessage("Password is not installed")
                .setPositiveButton("setup later") { _, _ ->
                    finish()
                }
                .create()*/
            delayStart()
        }
    }
    private suspend fun delayStart() {
        delay(1000L)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
