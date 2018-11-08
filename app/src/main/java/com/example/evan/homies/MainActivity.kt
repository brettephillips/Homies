package com.example.evan.homies

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast

import kotlinx.android.synthetic.main.fragment_login.*

class MainActivity : AppCompatActivity() {

    private var userId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //TODO: get global user id? If not logged in, send to login fragment
        //supportFragmentManager.beginTransaction()
            //.add(R.id.login_fragment, LoginFragment()).commit()

        if(userId == null) {
            // send to login
            startLogin()
        } else {
            //by default, set the fragment to chore list
            startHomies()
        }
    }

    fun startHomies() {
        val intent = Intent(this, Homies::class.java)
        // To pass any data to next activity
        //intent.putExtra("keyIdentifier", value)
        // start your next activity
        startActivity(intent)
    }

    fun startLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        // To pass any data to next activity
        //intent.putExtra("keyIdentifier", value)
        // start your next activity
        startActivity(intent)
    }

}
