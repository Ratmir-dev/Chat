package com.example.chat

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import com.example.chat.ui.home.HomeFragment.Companion.COUNT_DIALOGS
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject
import java.util.zip.Inflater

class ClassDialog (var nameD: String, var messD: String, var timeD: String,var countMessD: String, var imageD: String)

class RecipeAdapter(private val context: Context?,
                             private val dataSource: JSONArray) : BaseAdapter() {

//    private val inflater: LayoutInflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    //1
    override fun getCount(): Int {
        Log.e("Adapter Dialogs","ok")
        return COUNT_DIALOGS!!
    }

    //2
    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    //3
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    //4
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {



        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val rowView2: View = inflater.inflate(R.layout.dialog, parent, false) as View

        val nameD = rowView2.findViewById(R.id.name_d) as TextView
        val timeD = rowView2.findViewById(R.id.time_d) as TextView
        val countD = rowView2.findViewById(R.id.count_d) as TextView
        val messD = rowView2.findViewById(R.id.mess_d) as TextView
        val imageD = rowView2.findViewById(R.id.image_d) as ImageView
Log.e("Adapter Dialogs","ok $position")
        // 1
        val dialogD = getItem(position) as JSONObject
// 2
        nameD.text = dialogD.getString("name")+" "+dialogD.getString("lastname")
        timeD.text = "12:00"//dialogD.getString("date")
        if(dialogD.getString("unread") == "0") {
            countD.visibility = INVISIBLE
        }else{
            countD.text = "  "+dialogD.getString("unread")+"  "
        }
        messD.text = dialogD.getString("mess")


// 3
        Picasso.get() .load(dialogD.getString("photo").toUri()) .resize(50, 50) .centerCrop() .into(imageD)



        return rowView2
    }

}