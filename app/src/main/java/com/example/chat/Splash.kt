package com.example.chat

import androidx.core.content.ContextCompat.startActivity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.chat.Login.Login
import java.lang.Thread.sleep


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, Def::class.java)
        startActivity(intent)
        finish()
    }
}