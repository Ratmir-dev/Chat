package com.example.chat

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import com.squareup.picasso.Picasso
import java.util.zip.Inflater

class ClassDialog (var nameD: String, var messD: String, var timeD: String,var countMessD: String, var imageD: String)

abstract class RecipeAdapter(private val context: Context,
                             private val dataSource: ArrayList<ClassDialog>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    //1
    override fun getCount(): Int {
        return dataSource.size
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



        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val rowView2: View = inflater.inflate(R.layout.dialog, parent, false) as View

        val nameD = rowView2.findViewById(R.id.name_d) as TextView
        val timeD = rowView2.findViewById(R.id.time_d) as TextView
        val countD = rowView2.findViewById(R.id.count_d) as TextView
        val messD = rowView2.findViewById(R.id.mess_d) as TextView
        val imageD = rowView2.findViewById(R.id.image_d) as ImageView

        // 1
        val dialogD = getItem(position) as ClassDialog
// 2
        nameD.text = dialogD.nameD
        timeD.text = dialogD.timeD
        countD.text = dialogD.countMessD
        messD.text = dialogD.messD


// 3
        Picasso.get() .load(dialogD.imageD) .resize(50, 50) .centerCrop() .into(imageD)



        return rowView2
    }

}