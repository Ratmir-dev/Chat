package com.example.chat.ui.home

import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Contacts
import android.provider.Contacts.Intents.UI
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.TextView.*
import androidx.annotation.UiThread
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.chat.Login.Code
import com.example.chat.MainActivity
import com.example.chat.NetworkUtils
import com.example.chat.R
import com.google.android.material.textview.MaterialTextView
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Thread.sleep
import java.net.URL
import com.example.chat.RecipeAdapter as RecipeAdapter1

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    companion object{
        var COUNT_DIALOGS: Int? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
       // val textView: TextView = root.findViewById(R.id.text_home)
      //  homeViewModel.text.observe(this, Observer {
     //       textView.text = it
     //   })
        val  token: String = "F7JAH4ZktDduzIx9sBfej2rQ5"
        val noDialogs: TextView = root.findViewById(R.id.no_dialogs)
        val dialogsList: ListView = root.findViewById(R.id.dialogs)
        val btn: Button = root.findViewById(R.id.btn)
        var dialogsArr: JSONArray? = null



        @UiThread
        fun ad(){

            if(dialogsArr !=null){
            val adapter = RecipeAdapter1(getContext(),dialogsArr!!)

            dialogsList.adapter = adapter}else{
                btn.visibility = VISIBLE
            }
        }




        val job = CoroutineScope(Dispatchers.IO)

        job.launch {


            val url = NetworkUtils.generateUrlAllDialogs(token)
            Log.e("Code ", url.toString())
            val jsonStr = URL(url.toString()).readText()
            if(jsonStr != "null") {
                Log.e("Code.btnnext ", jsonStr)
                val jsonResponse = JSONObject(jsonStr)
                var ob = jsonResponse.getJSONObject("Error")         //хранятся response и text       "Error":{"response":"1","text":"Ok"}
                var dialogs = jsonResponse.getJSONObject("dialogs")   // хранятся count и dialogs   "dialogs":{"count":2,"dialogs":{"1":{"unread":1,"number":1,"sub":"88005553535","name":"Андрей","lastname":"Хуеротов","mess":"запрос14","photo":"https://avatars.mds.yandex.net/get-pdb/1381440/2becdede-c4c2-4e6c-9b3d-05d5ae7e0409/s1200?webp=false","date":"2020-02-21 17:31:57"},"2":{"unread":3,"number":2,"sub":"89380794324","name":"Adam","lastname":"Amirbekov","mess":"запрос15","photo":"https://avatars.mds.yandex.net/get-pdb/938499/bb3e5208-82ad-48bd-a3be-a40a666132e4/s1200?webp=false","date":"2020-02-23 07:36:52"}}}}
                                                                                        //                            "dialogs":{"count":0,"dialogs":{"null"}}

                var response = ob.getString("response")
                if (response == "1"){
                    if(dialogs.getString("count") == "0"){
                        noDialogs.visibility = VISIBLE
                    }
                    else{
                        noDialogs.visibility = INVISIBLE
                        val dialogsArr2 = dialogs.getJSONArray("dialogs")
                        COUNT_DIALOGS = dialogs.getInt("count")
                        dialogsArr  = dialogsArr2

                        ad()






                    }

                }
            }
        }



        btn.setOnClickListener {
            btn.visibility = INVISIBLE
            ad()
        }


        return root
    }
}