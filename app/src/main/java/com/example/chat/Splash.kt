package com.example.chat

import androidx.core.content.ContextCompat.startActivity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.chat.Login.Login
import kotlinx.coroutines.*
import org.json.JSONObject
import org.json.JSONObject.NULL
import java.lang.Thread.sleep
import java.net.URL


@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {
companion object{
    var TOKEN: String? = null
}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        val dbon: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val token: String? = dbon.getString("TOKEN", "unknown")
        val auto: String? = dbon.getString("AUTO","unknown")

        Log.e("Splash TOKEN- ", token!!)
        Log.e("Splash AUTO- ", auto!!)

        fun startAct(i:Int){
            if(i == 1){
                TOKEN = token
                val intent: Intent = Intent(this,MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)

            }
            if(i == 7){
                val intent = Intent(this, Login::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            if(i == 8){
                val intent = Intent(this, SetName::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }


        fun startDownload(a:String,b:String){



            if(token == "unknown"){
                Log.e("Splash method: ", "token unknown")
                val intent = Intent(this, Login::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else
            {
                Log.e("Splash method: ", "token ok")
                if(auto == "unknown"){
                    Log.e("Splash method: ", "AUTO unknown")
                    val intent = Intent(this, Login::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }else {
                    if(auto == "1"){
                        Log.e("Splash method: ", "AUTO finded")
                        val job = CoroutineScope(Dispatchers.IO)
                        job.launch {

                         fun analysisResponse3(res: String) {
                              Log.e("Splash res ", res)
                                if (res != NULL) {
                                    val jsonResponse = JSONObject(res)
                                    val jsonstatus: String = jsonResponse.getString("response")
                                    if (jsonstatus == "1") {
                                        startAct(1)
                                    } else
                                        if (jsonstatus == "7") {
                                            startAct(7)
                                        } else
                                            if (jsonstatus == "8") {
                                                startAct(8)
                                            }
                                }
                         }


                        val url = NetworkUtils.generateUrlGetAccountInfo(token!!)
                        Log.e("Splash ", url.toString())
                        val jsonStr = URL(url.toString()).readText()


                        val status: Deferred<String> = async { jsonStr }

                        analysisResponse3(status.await())

                    }

                }
                    else
                        startAct(7)
                }


            }


        }

        val job2 = CoroutineScope(Dispatchers.IO)

        job2.launch {
            val status3: Deferred<String> = async { token!! }
            val status2: Deferred<String> = async { auto!! }

            startDownload(status3.await(),status2.await())
        }




    }
}