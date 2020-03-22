package com.example.chat.Login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.chat.R


class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
            val btn: Button = this.findViewById(R.id.material_text_button)
            val edt: EditText = findViewById(R.id.tel)
        btn.setOnClickListener {
            val telsize = edt.text
            if (telsize.length == 10){
                val builder2 = AlertDialog.Builder(this)
                val builder = AlertDialog.Builder(this)
                val mDialogView = LayoutInflater.from(this).inflate(R.layout.alertdialog, null)
                builder.setTitle("+7 ")
                builder.setMessage("На указанный номер придет SMS с кодом активации")
                //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

                builder.setPositiveButton("Далее") { dialog, which ->
                    Toast.makeText(applicationContext,
                        android.R.string.yes, Toast.LENGTH_SHORT).show()
                    builder2.show()
                }

                builder.setNegativeButton("Изменить") { dialog, which ->
                    Toast.makeText(applicationContext,
                        android.R.string.no, Toast.LENGTH_SHORT).show()
                }
                builder.show()
                builder2.setView(mDialogView)}
            else{
                Toast.makeText(
                    this, "Некорректный номер телефона",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
