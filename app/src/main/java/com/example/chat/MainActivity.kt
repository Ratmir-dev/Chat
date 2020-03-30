package com.example.chat

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.GravityCompat
import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.content.Intent
import android.content.SharedPreferences
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.preference.PreferenceManager
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.net.toUri
import androidx.core.view.isVisible
import com.example.chat.Login.Login
import com.squareup.picasso.Picasso


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(){

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val navigationView:NavigationView = findViewById(R.id.nav_view)

        val myBase: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val db:SharedPreferences.Editor = myBase.edit()
        val dbon: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        val headerlayout = navigationView.inflateHeaderView(R.layout.nav_header_main)
        val name: String? = dbon.getString("name", "unknown")
        val num: String? = dbon.getString("num","unknown")
        val photo: String? = dbon.getString("photo","unknown")
        val namesuka: TextView = headerlayout.findViewById(R.id.namesuka)
        val numsuka: TextView = headerlayout.findViewById(R.id.numsuka)
        val photosuka: ImageView = headerlayout.findViewById(R.id.photosuka)
        Picasso.get() .load(photo!!.toUri()) .resize(230, 230) .centerCrop() .into(photosuka)

        namesuka.text = name
        numsuka.text = num



        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

    }
    fun ok ():Boolean{
        val dialogsLayout: CoordinatorLayout = findViewById(R.id.dialogs_layout)
        val dialogLayout: LinearLayout = findViewById(R.id.dialog_layout)
        if(dialogLayout.isVisible){
            dialogLayout.visibility = INVISIBLE
            dialogsLayout.visibility = VISIBLE
            return false
        }else{
            return true
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
       // menuInflater.inflate(R.menu.main, menu)
        return true
    }
    @SuppressLint("CommitPrefEdits")
    fun exit(item: MenuItem){

        val dbon: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val  db: SharedPreferences.Editor = dbon.edit()
        db.putString("AUTO", "0")
        db.apply()

        val intent: Intent = Intent(this,Login::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        //Toast.makeText(this, "Че происходит при выходе", Toast.LENGTH_SHORT).show()
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override  fun onBackPressed() {
        if(ok()){
            super.onBackPressed()
        }
    }
}
