package com.example.chat.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.chat.NetworkUtils
import com.example.chat.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL


class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
            val btn: MaterialButton = this.findViewById(R.id.material_text_button)
            val edt: TextInputEditText = findViewById(R.id.tel)
        btn.setOnClickListener {
            val telsize = edt.text
            if (telsize!!.length == 10){
                val builder2 = AlertDialog.Builder(this)
                val builder = AlertDialog.Builder(this)
                val alert2 = builder2.create()
                builder2.setCancelable(false)
                val mDialogView = LayoutInflater.from(this).inflate(R.layout.alertdialog, null)
                builder.setTitle("+7 $telsize")
                builder.setMessage("На указанный номер придет SMS с кодом активации")
                //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

                builder.setPositiveButton("Далее") { dialog, which ->
                    Toast.makeText(applicationContext,
                        android.R.string.yes, Toast.LENGTH_SHORT).show()
                    builder2.show()
                    var jsonStr: String? = null

                    fun nextcode(){

                        val intent = Intent(this, Code::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }

                     fun analysisResponse (status: String){
                        if (status == "5"){
                            Log.e("Login status getcode: ", "5")
                            Code.NUM = "7$telsize"

                            nextcode()
                        }
                        if(status == "2") {
                            Log.e("Login status getcode: ", "2")
                            Code.NUM = "7$telsize"

                            nextcode()
                        }
                    }


                    val job = CoroutineScope(Dispatchers.IO)
                    job.launch {

                        var url: URL = NetworkUtils.generateUrlGetCode("7$telsize")
                        Log.e("Login url", url.toString())
                        // val jsonStr = URL(url.toString()).readText()
                        //var jsonStr: Response = url.toString().httpGet()



                        var con: HttpURLConnection = url.openConnection() as HttpURLConnection

                        if (con.responseCode == HttpURLConnection.HTTP_OK) {
                            jsonStr = URL(url.toString()).readText()

                            //DialogTest.RESPONSE = jsonStr.toString()
                            Log.e("Login response: ", jsonStr.toString())
                            if (jsonStr != null) {
                                val jsonResponse = JSONObject(jsonStr.toString())
                                val status: Deferred<String> = async{jsonResponse.getString("response")}
                                //delay(20000)
                                analysisResponse(status.await())
                            }

                        } else {
                            Log.e("Login errors: ", "url не отвечает/nссылка: $url")
                        }


                    }
                }

                builder.setNegativeButton("Изменить") { dialog, which ->
                    Toast.makeText(applicationContext,
                        android.R.string.no, Toast.LENGTH_SHORT).show()
                }
                builder.show()
                builder2.setView(mDialogView)}
            else{
                Toast.makeText(
                    this, "Некорректный номер телефона",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
