package com.example.chat.Login

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.chat.R

class DialogTest( var phone: String ,var c: Context) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity!!).setTitle("+7 $phone")
            .setMessage("На указанный номер придет SMS с кодом активации")
            .setPositiveButton("Далее") { dialog, which ->
                val intent: Intent = Intent(c, Code::class.java)
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