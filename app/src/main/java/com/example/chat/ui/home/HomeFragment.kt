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
import android.text.Editable
import android.widget.*
import androidx.annotation.MainThread
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.os.postDelayed
import androidx.core.view.get
import com.example.chat.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.dialog.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.EOFException

import com.example.chat.RecipeAdapter as RecipeAdapter1

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    companion object {
        var PAUSE: Int = 1000
        var COUNT_DIALOGS: Int? = null
        var COUNT_MESS: Int? = null
        var STEP: JSONArray? = null
        var STEPMESS: JSONArray? = null
        var REPLASE: Int? = null
    }

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val numDialog = inflater.inflate(R.layout.addnumberdialog, container, false)

        // val textView: TextView = root.findViewById(R.id.text_home)
        //  homeViewModel.text.observe(this, Observer {
        //       textView.text = it
        //   })

        REPLASE = 2

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

        var openDialog: String? = "1"
        var dialogUName: String? = null
        var dialogUNum: String? = null
        var dialogUPhoto: String? = null

        val NOTIFY_ID: Int = 100;
        val notificationIntent = Intent(context, HomeFragment::class.java)
        val contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT)
        val builder = NotificationCompat.Builder(context)

        dialogLayout.visibility = View.INVISIBLE
        dialogsLayout.visibility = View.VISIBLE

        val fab: FloatingActionButton = root.findViewById(R.id.fab)
        val builderAddNumber = AlertDialog.Builder(root.context)
        val suchka = LayoutInflater.from(context).inflate(R.layout.addnumberdialog, null)
        val addNumberEdt: TextInputEditText = suchka.findViewById(R.id.edt_addNumber)

        //val jopaNext:MaterialButton = numDialog.findViewById(R.id.btn_next)
        builderAddNumber.setView(suchka)

        builderAddNumber.setNegativeButton("Назад") { dialog, which ->
            Toast.makeText(
                root.context,
                android.R.string.no, Toast.LENGTH_SHORT
            ).show()
        }


        //Открыть диалог с переданным номером
        fun openDialogFromUser(num: String){
            job.launch {
                val url = NetworkUtils.generateUrlUserInfo(token, num)
                Log.e("Home fab dialog", url.toString())
                val jsonStr = URL(url.toString()).readText()
                if (jsonStr != "null") {
                    Log.e("Home.positive btn ", jsonStr)
                    val jsonResponse = JSONObject(jsonStr)

                    var response = jsonResponse.getString("response")
                    if (response == "1") {
                        openDialog = "7"+addNumberEdt.text.toString()
                        dialogUName = jsonResponse.getString("user_name")
                        dialogUNum = jsonResponse.getString("user_num")
                        dialogUPhoto = jsonResponse.getString("user_photo")
                        REPLASE = 1
                    }
                    else{

                    }
                }
            }
        }

        //При клике на плавающую кнопку и кнопке написать
        builderAddNumber.setPositiveButton("Написать") { dialog, which ->
            var fabNum: String = addNumberEdt.text.toString()
            openDialogFromUser("7$fabNum")
            Toast.makeText(
                root.context,
                addNumberEdt.text, Toast.LENGTH_SHORT
            ).show()

        }
        //Установить функционал плавающей кнопки
        var ad = builderAddNumber.create()


        //Скрывает кнопку обновить и нет диалогов
        noDialogs.visibility = INVISIBLE
        btn.visibility = INVISIBLE


        //сменить окно Диалоги или Диалог
        fun replaceXML(i: Int) {    //1 - диалоги 2 - диалог
            Log.e("Home ", "replace")
            if (i == 1) {

                dialogName.text = dialogUName
                openDialogFromUser(openDialog!!)

                dialogsLayout.visibility = View.INVISIBLE
                fab.visibility = View.INVISIBLE
                dialogLayout.visibility = View.VISIBLE
            } else {
                dialogLayout.visibility = View.INVISIBLE
                dialogsLayout.visibility = View.VISIBLE
                fab.visibility = View.VISIBLE
            }

        }

        //Кнопка в диалоге меняет окно на список диалогов
        dialogBack.setOnClickListener {
            REPLASE = 2

        }


        //Заполняет список диалогов, показывает ОБНОВИТЬ, если диалогов нет
        @UiThread
        fun ad() {

            if (dialogsArr != null) {

                val adapter = RecipeAdapter1(getContext(), dialogsArr!!)
                Log.e("HomeFragment ad()   ", STEP.toString())
                Log.e("HomeFragment ad()   ", dialogsArr.toString())
                btn.visibility = INVISIBLE
                fab.visibility = INVISIBLE
                dialogsList.adapter = adapter

            } else {
                btn.visibility = VISIBLE
                fab.visibility = VISIBLE
            }

        }

        //Заполняет лист сообщениями
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


        //Отправляет сообщение. Параметры sub:Кому mess:Текст сообщения
        fun sendMessage(sub: String, mess: String) {
            job.launch {
                val url = NetworkUtils.generateUrlSendMess(token, sub, mess)
                Log.e("Home SendMess", url.toString())
                val jsonStr = URL(url.toString()).readText()
            }
        }

        //Загружает сообщения с sub
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
                           // noDialogs.visibility = VISIBLE
                          //  btn.visibility = INVISIBLE
                        } else {
                           // noDialogs.visibility = INVISIBLE
                            val messArr2 = messageU.getJSONArray("messages")
                            COUNT_MESS = messageU.getInt("count")
                            messArr = messArr2

                        }

                    }
                }
            }


        }

        //При клике на диалог из списка диалогов выбирается номер и меняется активность
        dialogsList.setOnItemClickListener { _, _, position, _ ->

            val selectedDialog = dialogsList[position]
            val sub: String = selectedDialog.num.text.toString()
            openDialog = sub
            REPLASE =1
            dialogName.text = selectedDialog.name_d.text


        }

        //Кнопка отправить
        send_btn.setOnClickListener {
            sendMessage(openDialog!!, send_text.text.toString())
            send_text.setText("")
        }

        //Загрузить список диалогов
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
//                            noDialogs.visibility = VISIBLE
//                            btn.visibility = INVISIBLE
                        } else {
//                            noDialogs.visibility = INVISIBLE
                            val dialogsArr2 = dialogs.getJSONArray("dialogs")
                            COUNT_DIALOGS = dialogs.getInt("count")
                            dialogsArr = dialogsArr2

                        }
                    }
                }
            }
        }

        //Плавающая кнопка открывает диалог с выбором номера получателя
        fab.setOnClickListener { view ->
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            //   .setAction("Action", null).show()
            ad.show()
        }


        //Таймер, каждую секунду отправляет запрос на загрузку данных для текущего окна (Диалоги/Сообщения)
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
                    if (dialogsLayout.isVisible) {
                        downloadDialogs()
                        if (STEP.toString() != dialogsArr.toString()) {
                            ad()
                            STEP = dialogsArr
                        }
                    }
                }
                replaceXML(REPLASE!!)

                mainHandler.postDelayed(this, PAUSE.toLong())
            }
        })




        btn.setOnClickListener {
            btn.visibility = INVISIBLE
            ad()
        }


        return root
    }

    override fun onStop() {
        super.onStop()
        PAUSE = 1000000000
    }

    override fun onStart() {
        super.onStart()
        PAUSE = 1000
    }
}

