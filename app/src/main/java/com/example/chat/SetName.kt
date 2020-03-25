package com.example.chat

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.chat.Login.Login
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.URL

@Suppress("DEPRECATION")
class SetName: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setname)
        val btnNext: MaterialButton = findViewById(R.id.material_next_button)
        val name: TextInputEditText = findViewById(R.id.userName)

        val builder2 = AlertDialog.Builder(this)
        val alert2 = builder2.create()
        builder2.setCancelable(false)
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.alertdialog, null)

        val dbon: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val token: String? = dbon.getString("TOKEN", "unknown")

        fun startAct(i:Int){

            if(i == 1){
                val intent: Intent = Intent(this,SplashActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }


        btnNext.setOnClickListener{
            builder2.setView(mDialogView)
            builder2.show()

            val job = CoroutineScope(Dispatchers.IO)
            job.launch {

                fun analysisResponse3(res: String){
                    Log.e("Name res ", res)
                    if(res != JSONObject.NULL) {
                        val jsonResponse = JSONObject(res)
                        val jsonstatus: String = jsonResponse.getString("response")
                        if (jsonstatus == "1") {
                            startAct(1)
                        } else
                            if (jsonstatus == "6") {
                                startAct(6)
                            }
                    }

                }



                val url = NetworkUtils.generateUrlSetNick(token!!,name.text.toString())
                Log.e("Set Name ", url.toString())
                val jsonStr = URL(url.toString()).readText()



                val status: Deferred<String> = async{jsonStr}

                analysisResponse3(status.await())

            }


        }


    }

}