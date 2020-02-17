package com.example.chat.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_DEL
import android.view.View
import android.widget.Button
import com.example.chat.DialogList
import com.example.chat.MainActivity
import com.example.chat.R


class Code : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code)
        val code1:EditText = findViewById(R.id.code1)
        val code2:EditText = findViewById(R.id.code2)
        val code3:EditText = findViewById(R.id.code3)
        val code4:EditText = findViewById(R.id.code4)
        val btn: Button = findViewById(R.id.material_text_button)
        btn.setOnClickListener {
            val intent: Intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        code1.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(count==1) {
                    code2.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {

            }

            override fun afterTextChanged(s: Editable) {

            }

        })
        code2.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(count==1) {
                    code3.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {

            }

            override fun afterTextChanged(s: Editable) {

            }

        })
        code2.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                when (keyCode) {
                    KEYCODE_DEL -> {
                        code2.setText("")
                        code1.requestFocus()
                    }
                }
                return false
            }
        })
        code3.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(count!=0) {
                    code4.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {

            }

            override fun afterTextChanged(s: Editable) {

            }

        })
        code3.setOnKeyListener { v, keyCode, event ->
            //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
            when (keyCode) {
                KEYCODE_DEL -> {
                    code3.setText("")
                    code2.requestFocus()
                    Log.e("Code", "suka")
                }
            }
            false
        }
        code4.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {

            }

            override fun afterTextChanged(s: Editable) {
                when {

                }
            }

        })
        code4.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                when (keyCode) {
                    KEYCODE_DEL -> {
                        code4.setText("")
                        code3.requestFocus()
                        Log.e("Code", "suka2")
                    }
                }
                return false
            }
        })
    }
}
