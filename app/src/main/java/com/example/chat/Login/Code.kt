package com.example.chat.Login

import android.app.PendingIntent.getActivity
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.AsyncTask
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
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.example.chat.DialogList
import com.example.chat.MainActivity
import com.example.chat.NetworkUtils
import com.example.chat.R
import org.json.JSONObject
import java.io.IOException
import java.net.URL


class Code : AppCompatActivity() {



    companion object {
        var back_pressed: Long = System.currentTimeMillis()
        var RESPONSE_CODE: String?=null
        var TOKEN: String? = null
        var NUM: String? = null


    }

    fun startNext(){
        val intent: Intent = Intent(this,MainActivity::class.java)
        intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    class QueryCheckCode : AsyncTask<URL, Void, String>() {

        override fun doInBackground(vararg params: URL?): String {
            Log.e("Code","asynk start: "+ params[0].toString())
            var response: String? = null
            try {
                response = NetworkUtils.getResponseFromURL(params[0]!!)
            }catch (e: IOException){
                e.printStackTrace()
            }
            Log.e("Code","response: "+ response.toString())

            return response.toString()
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            Log.e("Code", RESPONSE_CODE.toString())
            var jsonResponse = JSONObject(result)
            var jsonstatus: String = jsonResponse.getString("response")
            RESPONSE_CODE = jsonstatus
            if(RESPONSE_CODE == "3") {
                TOKEN = jsonResponse.getString("token")
                }
        }
    }


    override  fun onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()){
            Toast.makeText(
                this, "exit!",
                Toast.LENGTH_SHORT
            ).show()
            super.onBackPressed()
        }
        else{

            Toast.makeText(
                this, "Press once again to exit!",
                Toast.LENGTH_SHORT
            ).show()
            back_pressed = System.currentTimeMillis()}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code)
        val code1:EditText = findViewById(R.id.code1)
        val code2:EditText = findViewById(R.id.code2)
        val code3:EditText = findViewById(R.id.code3)
        val code4:EditText = findViewById(R.id.code4)
        val btn: Button = findViewById(R.id.material_text_button)








        fun checkCode():Boolean{

            val code: String = code1.text.toString()+code2.text.toString()+code3.text.toString()+code4.text.toString()
            Log.e("Code", "get code: $code")
            if(code == "0000") {

                return true
            }else {
                code1.setText("")
                code2.setText("")
                code3.setText("")
                code4.setText("")
                return false
            }
        }
        fun checkForEmpty(code1: String,code2: String,code3: String,code4: String):Boolean{

            val code: String = code1+code2+code3+code4
            Log.e("Code", "codeEmpty: $code")
            if(code1 == ""||code2 == ""||code3 == ""||code4 == "")
                return false
            return true
        }



        btn.setOnClickListener {
           if( checkForEmpty(code1.text.toString(),code2.text.toString(),code3.text.toString(),code4.text.toString())){
               if (checkCode()){

                   NetworkUtils.generateUrlCheckCode(NUM!!,code1.toString()+code2.toString()+code3.toString()+code4.toString())



               }


           }

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
