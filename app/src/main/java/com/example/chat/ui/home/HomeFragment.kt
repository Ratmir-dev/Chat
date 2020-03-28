package com.example.chat.ui.home

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.provider.Contacts
import android.provider.Contacts.Intents.UI
import android.util.Log
import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.widget.TextView.*
import androidx.annotation.UiThread
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.chat.Login.Code
import com.google.android.material.textview.MaterialTextView
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Thread.sleep
import java.net.URL

import android.app.NotificationChannel

import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.graphics.Color
import android.widget.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.get
import com.example.chat.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.dialog.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.EOFException

import com.example.chat.RecipeAdapter as RecipeAdapter1

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    companion object {
        var COUNT_DIALOGS: Int? = null
        var COUNT_MESS: Int? = null
        var STEP: JSONArray? = null
        var STEPMESS: JSONArray? = null
    }

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        var root = inflater.inflate(R.layout.fragment_home, container, false)
        // val textView: TextView = root.findViewById(R.id.text_home)
        //  homeViewModel.text.observe(this, Observer {
        //       textView.text = it
        //   })


        val send_text: EditText = root.findViewById(R.id.send_text)
        val send_btn: ImageButton = root.findViewById(R.id.send_btn)
        val token: String = SplashActivity.TOKEN.toString()
        val noDialogs: MaterialTextView = root.findViewById(R.id.no_dialogs)
        val dialogName: MaterialTextView = root.findViewById(R.id.dialog_name)
        val dialogsList: ListView = root.findViewById(R.id.dialogs)
        val dialogMess: ListView = root.findViewById(R.id.dialog_mess)
        val dialogsLayout: CoordinatorLayout = root.findViewById(R.id.dialogs_layout)
        val dialogLayout: LinearLayout = root.findViewById(R.id.dialog_layout)
        val btn: MaterialButton = root.findViewById(R.id.btn)
        val dialogBack: ImageButton = root.findViewById(R.id.dialog_back)
        var dialogsArr: JSONArray? = null
        var messArr: JSONArray? = null
        val job = CoroutineScope(Dispatchers.IO)

        val NOTIFY_ID: Int = 100;
        val notificationIntent = Intent(context, HomeFragment::class.java)
        val contentIntent = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        val builder = NotificationCompat.Builder(context)

        dialogLayout.visibility = View.INVISIBLE
        dialogsLayout.visibility = View.VISIBLE

        fun replaceXML(i: Int) {
            Log.e("Home ", "replace")
            if (i == 1) {
                dialogsLayout.visibility = View.INVISIBLE
                dialogLayout.visibility = View.VISIBLE
            } else {
                dialogLayout.visibility = View.INVISIBLE
                dialogsLayout.visibility = View.VISIBLE
            }

        }

        dialogBack.setOnClickListener {
            replaceXML(2)

        }

        @UiThread
        fun ad() {

            if (dialogsArr != null) {

                val adapter = RecipeAdapter1(getContext(), dialogsArr!!)

                Log.e("HomeFragment ad()   ", STEP.toString())
                Log.e("HomeFragment ad()   ", dialogsArr.toString())
                btn.visibility = INVISIBLE
                dialogsList.adapter = adapter

            } else {
                btn.visibility = VISIBLE
            }

        }

        @UiThread
        fun messAd() {

            if (messArr != null) {

                val adapter2 = AdapterMess(getContext(), messArr!!)

                Log.e("Home messAd()   ", STEPMESS.toString())
                Log.e("Home messAd()   ", messArr.toString())
                dialogMess.adapter = adapter2

            } else {
                btn.visibility = VISIBLE
            }

        }

        var openDialog: String? = null


        fun sendMessage(sub: String, mess: String) {
            job.launch {


                val url = NetworkUtils.generateUrlSendMess(token, sub, mess)
                Log.e("Home SendMess", url.toString())
                val jsonStr = URL(url.toString()).readText()

            }


        }

        fun downloadMassage(sub: String) {

            job.launch {


                val url = NetworkUtils.generateUrlGetMess(token, sub)
                Log.e("Home ", url.toString())
                val jsonStr = URL(url.toString()).readText()
                if (jsonStr != "null") {
                    Log.e("Home.mess ", jsonStr)
                    val jsonResponse = JSONObject(jsonStr)
                    val ob =
                        jsonResponse.getJSONObject("Error")         //хранятся response и text       "Error":{"response":"1","text":"Ok"}
                    var messageU =
                        jsonResponse.getJSONObject("messages")   // хранятся count и dialogs   "dialogs":{"count":2,"dialogs":{"1":{"unread":1,"number":1,"sub":"88005553535","name":"Андрей","lastname":"Хуеротов","mess":"запрос14","photo":"https://avatars.mds.yandex.net/get-pdb/1381440/2becdede-c4c2-4e6c-9b3d-05d5ae7e0409/s1200?webp=false","date":"2020-02-21 17:31:57"},"2":{"unread":3,"number":2,"sub":"89380794324","name":"Adam","lastname":"Amirbekov","mess":"запрос15","photo":"https://avatars.mds.yandex.net/get-pdb/938499/bb3e5208-82ad-48bd-a3be-a40a666132e4/s1200?webp=false","date":"2020-02-23 07:36:52"}}}}
                    //                            "dialogs":{"count":0,"dialogs":{"null"}}

                    var response = ob.getString("response")
                    if (response == "1") {
                        if (messageU.getString("count") == "0") {
                            noDialogs.visibility = VISIBLE
                            btn.visibility = INVISIBLE
                        } else {
                            noDialogs.visibility = INVISIBLE
                            val messArr2 = messageU.getJSONArray("messages")
                            COUNT_MESS = messageU.getInt("count")
                            messArr = messArr2

                        }

                    }
                }
            }


        }

        dialogsList.setOnItemClickListener { _, _, position, _ ->

            val selectedDialog = dialogsList[position]
            val sub: String = selectedDialog.num.text.toString()
            openDialog = sub
            replaceXML(1)
            dialogName.text = selectedDialog.name_d.text


        }

        //Кнопка отправить
        send_btn.setOnClickListener {
            sendMessage(openDialog!!, send_text.text.toString())
            send_text.setText("")
        }


        fun downloadDialogs() {
            job.launch {


                val url = NetworkUtils.generateUrlAllDialogs(token)
                Log.e("Home ", url.toString())
                val jsonStr = URL(url.toString()).readText()
                if (jsonStr != "null") {
                    Log.e("Home.btnnext ", jsonStr)
                    val jsonResponse = JSONObject(jsonStr)
                    var ob =
                        jsonResponse.getJSONObject("Error")         //хранятся response и text       "Error":{"response":"1","text":"Ok"}
                    var dialogs =
                        jsonResponse.getJSONObject("dialogs")   // хранятся count и dialogs   "dialogs":{"count":2,"dialogs":{"1":{"unread":1,"number":1,"sub":"88005553535","name":"Андрей","lastname":"Хуеротов","mess":"запрос14","photo":"https://avatars.mds.yandex.net/get-pdb/1381440/2becdede-c4c2-4e6c-9b3d-05d5ae7e0409/s1200?webp=false","date":"2020-02-21 17:31:57"},"2":{"unread":3,"number":2,"sub":"89380794324","name":"Adam","lastname":"Amirbekov","mess":"запрос15","photo":"https://avatars.mds.yandex.net/get-pdb/938499/bb3e5208-82ad-48bd-a3be-a40a666132e4/s1200?webp=false","date":"2020-02-23 07:36:52"}}}}
                    //                            "dialogs":{"count":0,"dialogs":{"null"}}

                    var response = ob.getString("response")
                    if (response == "1") {
                        if (dialogs.getString("count") == "0") {
                            noDialogs.visibility = VISIBLE
                            btn.visibility = INVISIBLE
                        } else {
                            noDialogs.visibility = INVISIBLE
                            val dialogsArr2 = dialogs.getJSONArray("dialogs")
                            COUNT_DIALOGS = dialogs.getInt("count")
                            dialogsArr = dialogsArr2

                            try {
                                val jsonStr = URL(url.toString()).readText()
                                if (jsonStr != "null") {
                                    Log.e("Home.btnnext ", jsonStr)
                                    val jsonResponse = JSONObject(jsonStr)
                                    var ob =
                                        jsonResponse.getJSONObject("Error")         //хранятся response и text       "Error":{"response":"1","text":"Ok"}
                                    var dialogs =
                                        jsonResponse.getJSONObject("dialogs")   // хранятся count и dialogs   "dialogs":{"count":2,"dialogs":{"1":{"unread":1,"number":1,"sub":"88005553535","name":"Андрей","lastname":"Хуеротов","mess":"запрос14","photo":"https://avatars.mds.yandex.net/get-pdb/1381440/2becdede-c4c2-4e6c-9b3d-05d5ae7e0409/s1200?webp=false","date":"2020-02-21 17:31:57"},"2":{"unread":3,"number":2,"sub":"89380794324","name":"Adam","lastname":"Amirbekov","mess":"запрос15","photo":"https://avatars.mds.yandex.net/get-pdb/938499/bb3e5208-82ad-48bd-a3be-a40a666132e4/s1200?webp=false","date":"2020-02-23 07:36:52"}}}}
                                    //                            "dialogs":{"count":0,"dialogs":{"null"}}

                                    var response = ob.getString("response")
                                    if (response == "1") {
                                        if (dialogs.getString("count") == "0") {
                                            noDialogs.visibility = VISIBLE
                                            btn.visibility = INVISIBLE
                                        } else {
                                            noDialogs.visibility = INVISIBLE
                                            val dialogsArr2 = dialogs.getJSONArray("dialogs")
                                            COUNT_DIALOGS = dialogs.getInt("count")
                                            dialogsArr = dialogsArr2


                                        }

                                    }
                                }
                            } catch (e: EOFException) {
                                Log.e("Home Fragment", "Исключение")
                            }
                        }
                    }
                }
            }
        }
                    val mainHandler = Handler(Looper.getMainLooper())

                    mainHandler.post(object : Runnable {
                        override fun run() {


                            if (dialogLayout.isVisible) {
                                if (STEPMESS.toString() != messArr.toString()) {
                                    messAd()
                                    STEPMESS = messArr
                                }
                                downloadMassage(openDialog!!)
                            } else {
                                downloadDialogs()
                                if (STEP.toString() != dialogsArr.toString()) {
                                    ad()
                                    STEP = dialogsArr
                                }
                            }

                            mainHandler.postDelayed(this, 1000)
                        }
                    })




                    btn.setOnClickListener {
                        btn.visibility = INVISIBLE
                        ad()
                    }


                    return root
                }
            }

