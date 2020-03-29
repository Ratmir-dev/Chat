package com.example.chat

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.chat.ui.home.HomeFragment
import org.json.JSONArray
import org.json.JSONObject

class AdapterMess (private val context: Context?,
                   private val dataSource: JSONArray
) : BaseAdapter() {


    //1
    override fun getCount(): Int {
        Log.e("Adapter Dialogs","ok")
        return HomeFragment.COUNT_MESS!!
    }

    //2
    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    //3
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {



        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val rowView2: View = inflater.inflate(R.layout.mess, parent, false) as View
        val mess = rowView2.findViewById(R.id.getter) as TextView
        Log.e("Adapter Mess","ok $position")

        val messD = getItem(position) as JSONObject

        mess.text = messD.getString("mess")

        return rowView2


    }

}