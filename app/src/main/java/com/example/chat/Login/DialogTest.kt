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
import com.example.chat.NetworkUtils.Companion.generateUrlGetCode
import com.example.chat.NetworkUtils.Companion.getResponseFromURL
import com.google.gson.JsonObject
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class DialogTest( var phone: String ,var c: Context) : DialogFragment() {
companion object{
    var RESPONSE: String?=null

}

    class QueryGetCode : AsyncTask<URL, Void, String>() {

        override fun doInBackground(vararg params: URL?): String {
            Log.e("DialogTest","asynk start: "+ params[0].toString())
            var response: String? = null
            try {
                response = getResponseFromURL(params[0]!!)
            }catch (e:IOException){
                e.printStackTrace()
            }
            Log.e("DialogTest","response: "+ response.toString())

            return response.toString()
        }

        override fun onPostExecute(result: String?) {

            Log.e("DialogTest", RESPONSE.toString())
            var jsonResponse = JSONObject(result)
            var jsonstatus: String = jsonResponse.getString("response")
            RESPONSE = jsonstatus
    }
    }




    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity!!).setTitle("+7 $phone")
            .setMessage("На указанный номер придет SMS с кодом активации")
            .setPositiveButton("Далее") { dialog, which ->

                var url: URL = generateUrlGetCode("7$phone")
                Log.e("DialogTest",url.toString())
                QueryGetCode().execute(url)

                NUM = phone
                val intent: Intent = Intent(c, Code::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            .setNegativeButton("Изменить") { dialog, which ->
                // TODO Auto-generated method stub

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