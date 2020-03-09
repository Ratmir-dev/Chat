package com.example.chat

import android.net.Uri
import android.os.Build
import android.util.Log
import com.example.chat.Login.DialogTest
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset
import java.util.*



class NetworkUtils {
companion object {
    //_______________________________________________________________________Методы
    val API         = "http://eclipsedevelop.ru/api.php/"
    val GET_CODE    = "getcode"
    val CHECK_CODE  = "checkcode"
    val SET_NAME    = "setname"
    val SET_NICK    = "setnick"
    val SEND_MESS   = "sendmess"
    val GET_MESS    = "getmess"
    val GET_DIALOGS = "alldialogs"

    //_______________________________________________________________________Параметры
    val NUM  = "num"
    val CODE = "code"


    fun generateUrlGetCode(num: String): URL {
        val builtUri: Uri =
            Uri.parse(API + GET_CODE).buildUpon().appendQueryParameter(NUM, num).build()

        var url: URL? = null
        try {
            url = URL(builtUri.toString())
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return url!!
    }
    fun generateUrlCheckCode(num: String,code: String): URL {
        val builtUri: Uri =
            Uri.parse(API + CHECK_CODE).buildUpon()
                .appendQueryParameter(NUM, num)
                .appendQueryParameter(CODE,code)
                .build()

        var url: URL? = null
        try {
            url = URL(builtUri.toString())
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return url!!
    }

    fun getResponseFromURL(url: URL): String? {
        val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
        Log.e("getResponseFromUrl", "urlConnction: $urlConnection")

        var inp: InputStream = urlConnection.inputStream
        Log.e("getResponseFromUrl", "inp: $inp")
        val scanner: Scanner = Scanner(inp)

        scanner.useDelimiter("\\A")

        val hasInput: Boolean = scanner.hasNext()
        urlConnection.disconnect()
        if (hasInput) {
            return scanner.next()
        } else {
            return null.toString()


    }


}}}