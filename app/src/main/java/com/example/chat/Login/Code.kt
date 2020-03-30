package com.example.chat.Login

import android.app.PendingIntent.getActivity
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.preference.PreferenceManager
import android.widget.EditText
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_DEL
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.example.chat.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.IOException
import java.net.URL


@Suppress("DEPRECATION")
class Code : AppCompatActivity() {



    companion object {
        var back_pressed: Long = System.currentTimeMillis()
        var RESPONSE_CODE: String?=null
        var TOKEN: String? = null
        var NUM: String? = null

    }

        fun startNext(){

            val myBase: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
            val myBaseEdit:SharedPreferences.Editor = myBase.edit()
            myBaseEdit.putString("TOKEN", TOKEN)
            myBaseEdit.commit()




        val intent: Intent = Intent(this,SplashActivity::class.java)
        intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

//    class QueryCheckCode : AsyncTask<URL, Void, String>() {
//
//        override fun doInBackground(vararg params: URL?): String {
//            Log.e("Code","asynk start: "+ params[0].toString())
//            var response: String? = null
//            try {
//                response = NetworkUtils.getResponseFromURL(params[0]!!)
//            }catch (e: IOException){
//                e.printStackTrace()
//            }
//            Log.e("Code","response: "+ response.toString())
//
//            return response.toString()
//        }
//
//        override fun onPostExecute(result: String?) {
//
//            Log.e("Code", RESPONSE_CODE.toString())
//            var jsonResponse = JSONObject(result)
//            var jsonstatus: String = jsonResponse.getString("response")
//            RESPONSE_CODE = jsonstatus
//            if(RESPONSE_CODE == "3") {
//                TOKEN = jsonResponse.getString("token")
//
//            }else{
//
//            }
//        }
//    }


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
        val code1: TextInputEditText = findViewById(R.id.code1)
        val code2:TextInputEditText = findViewById(R.id.code2)
        val code3:TextInputEditText = findViewById(R.id.code3)
        val code4:TextInputEditText = findViewById(R.id.code4)
        val btnNext: MaterialButton = findViewById(R.id.material_text_button)
        val btnBack: MaterialButton = findViewById(R.id.back)
        val btnSend: MaterialButton = findViewById(R.id.send)
        val timer: MaterialTextView = findViewById(R.id.timer)
        val timerText: MaterialTextView = findViewById(R.id.timer_text)
        val builder2 = AlertDialog.Builder(this)


        btnSend.visibility = View.INVISIBLE
        code1.requestFocus()

        btnBack.setOnClickListener {
            val intent: Intent = Intent(this,Login::class.java)
            intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        //Таймер повторной отправки
        btnSend.setOnClickListener {
            btnSend.visibility = View.INVISIBLE
            timer.visibility = View.VISIBLE
            timerText.visibility = View.VISIBLE
            Toast.makeText(this, "Отправлено", Toast.LENGTH_SHORT).show()
            object: CountDownTimer(24000,1000){
                override fun onFinish() {
                    btnSend.visibility = View.VISIBLE
                }

                override fun onTick(millisUntilFinished: Long) {
                    val s: Long = millisUntilFinished % 60000 / 1000
                    val m: Long = millisUntilFinished / 60000
                    timer.text = String.format("%02d:%02d", m, s)
                }
            }.start()
        }

        //Первичный таймер
        object: CountDownTimer(300000,1000){
            override fun onFinish() {
                btnSend.visibility = View.VISIBLE
                timer.visibility = View.INVISIBLE
                timerText.visibility = View.INVISIBLE
            }

            override fun onTick(millisUntilFinished: Long) {
                val s: Long = millisUntilFinished % 60000 / 1000
                val m: Long = millisUntilFinished / 60000
                timer.text = String.format("%02d:%02d", m, s)
            }
        }.start()

        fun checkCode():Boolean{

            val code: String = code1.text.toString()+code2.text.toString()+code3.text.toString()+code4.text.toString()
            Log.e("Code", "get code: $code")
            if(RESPONSE_CODE == "3" || RESPONSE_CODE == "5") {

                return true
            }else {
                code1.clearComposingText()
                code2.clearComposingText()
                code3.clearComposingText()
                code4.clearComposingText()
                code1.setFocusable(true)
                Log.e("Code", "check code false")
                return false
            }
        }

        fun checkForEmpty(code1: String,code2: String,code3: String,code4: String):Boolean{

            val code: String = code1+code2+code3+code4
            Log.e("Code", "codeEmpty: $code")
            if(code1 == ""||code2 == ""||code3 == ""||code4 == "")
                return false
            Log.e("Code", "check code true")
            return true
        }


        val dbon: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val  db: SharedPreferences.Editor = dbon.edit()

        btnNext.setOnClickListener {
           if( checkForEmpty(code1.text.toString(),code2.text.toString(),code3.text.toString(),code4.text.toString())){


                   val job = CoroutineScope(Dispatchers.IO)
                   job.launch {

                       fun analysisResponse2(jsonStr: String){

                           val jsonResponse = JSONObject(jsonStr)
                           val jsonstatus: String = jsonResponse.getString("response")

                           RESPONSE_CODE = jsonstatus
                           Log.e("Code", "RESPONSE CODE: "+RESPONSE_CODE.toString())

                           if (checkCode()) {
                               TOKEN = jsonResponse.getString("token")
                               db.putString("AUTO", "1")
                               db.putString("TOKEN", TOKEN)
                               db.apply()
                               startNext()
                               Log.e("Code", "next")
                           }

                       }


                       val code = code1.text.toString() + code2.text.toString() + code3.text.toString() + code4.text.toString()
                       Log.e("Code params in url", NUM+" "+code)
                       val url = NetworkUtils.generateUrlCheckCode(NUM!!,code)
                       Log.e("Code ", url.toString())
                       val jsonStr = URL(url.toString()).readText()
                       Log.e("Code.btnnext ", jsonStr)



                       RESPONSE_CODE = jsonStr

                       val status: Deferred<String> = async{jsonStr}

                       analysisResponse2(status.await())





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
