package com.example.chat

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.chat.ui.home.HomeFragment
import org.json.JSONArray
import org.json.JSONObject

@Suppress("DEPRECATION")
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

        val nav: View = inflater.inflate(R.layout.nav_header_main, parent, false) as View
        val num2: String = nav.findViewById<TextView>(R.id.namesuka).text.toString()

        val messLeft = rowView2.findViewById(R.id.getter) as TextView
        val messRight = rowView2.findViewById(R.id.sender) as TextView
        val messD = getItem(position) as JSONObject

        val dbon: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val num: String? = dbon.getString("num","unknown")


        Log.e("Adapter Mess","ok $position")

        if(num == messD.getString("sub")){
            messLeft.visibility = INVISIBLE
            Log.e("Mess left","  ok")
            messRight.text = messD.getString("mess")

        }else{
            messRight.visibility = INVISIBLE
            Log.e("Mess right","  ok")

            messLeft.text = messD.getString("mess")
        }



        return rowView2


    }

}