package com.example.chat.Login

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.chat.R
import io.github.rybalkinsd.kohttp.ext.httpGet
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.net.URL
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import android.os.AsyncTask
import com.example.chat.Login.Code.Companion.NUM
import com.example.chat.NetworkUtils
import com.example.chat.NetworkUtils.Companion.generateUrlGetCode
import com.example.chat.NetworkUtils.Companion.getResponseFromURL
import com.google.gson.JsonObject
import io.github.rybalkinsd.kohttp.dsl.httpGet
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection


class DialogTest( var phone: String ,var c: Context) : DialogFragment() {
companion object{
    var RESPONSE: String?=null
}



    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return AlertDialog.Builder(activity!!).setTitle("+7 $phone")
            .setMessage("На указанный номер придет SMS с кодом активации")
            .setPositiveButton("Далее") { dialog, which ->



                var jsonStr: String? = null

                fun nextcode(){

                    val intent = Intent(c, Code::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }

                fun analysisResponse (status: String){
                    if (status == "5"){
                        Log.e("Login status getcode: ", "5")
                        NUM = "7$phone"

                        nextcode()
                    }
                    if(status == "2") {
                        Log.e("Login status getcode: ", "2")
                        NUM = "7$phone"

                        nextcode()
                    }
                }
                val job = CoroutineScope(Dispatchers.IO)
                job.launch {

                    var url: URL = generateUrlGetCode("7$phone")
                    Log.e("Login url", url.toString())
                    // val jsonStr = URL(url.toString()).readText()
                    //var jsonStr: Response = url.toString().httpGet()



                    var con: HttpURLConnection = url.openConnection() as HttpURLConnection

                    if (con.responseCode == HttpURLConnection.HTTP_OK) {
                        jsonStr = URL(url.toString()).readText()

                        RESPONSE = jsonStr.toString()
                        Log.e("Login response: ", jsonStr.toString())
                        if (jsonStr != null) {
                            val jsonResponse = JSONObject(jsonStr.toString())
                            val status: String = jsonResponse.getString("response")
                            analysisResponse(status)
                        }

                    } else {
                        Log.e("Login errors: ", "url не отвечает/nссылка: $url")
                    }


                }

            }
            .setNegativeButton("Изменить") { dialog, which ->

            }.show()
    }

    override fun onStart() {
        super.onStart()
        (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(
            this.context!!, R.color.colorAccent
        ))
        (dialog as AlertDialog).getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(
            this.context!!, R.color.colorAccent
        ))
    }

}

private fun CoroutineScope.launch(c: Context, block: suspend CoroutineScope.() -> Unit) {

}


