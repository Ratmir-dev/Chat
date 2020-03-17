package com.example.chat.Login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.chat.R


class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
            val btn: Button = findViewById(R.id.material_text_button)
            val edt: EditText = findViewById(R.id.tel)
        btn.setOnClickListener {
            val telsize = edt.text
            if (telsize.length == 10){
                val dil = DialogTest(edt.text.toString(), this)
                val fm = supportFragmentManager
                dil.show(fm, "hui")}
            else{
                Toast.makeText(
                    this, "Некорректный номер телефона",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
