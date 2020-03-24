package com.example.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

class SetName: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setname)
        val userName: TextInputLayout = findViewById(R.id.userName)
        val btnNext: MaterialButton = findViewById(R.id.material_next_button)
    }

}