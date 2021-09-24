package com.duonglh.retrofitapi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val flagFullScreen = WindowManager.LayoutParams.FLAG_FULLSCREEN
        window.setFlags(flagFullScreen, flagFullScreen)
        setContentView(R.layout.activity_splash)

        findViewById<ImageView>(R.id.image_logo).apply {
            this.animation = AnimationUtils.loadAnimation(this@SplashActivity, R.anim.top_to_middle)
        }
        findViewById<TextView>(R.id.name_app).apply {
            this.animation = AnimationUtils.loadAnimation(this@SplashActivity, R.anim.bottom_to_middle)
        }

        lifecycleScope.launchWhenStarted {
            delay(3000)
            toMainActivity()
        }
    }
    private fun toMainActivity(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}