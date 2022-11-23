package com.mikko.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
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
            delayStart()
        }
    }
    private suspend fun delayStart() {
        delay(1000L)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
