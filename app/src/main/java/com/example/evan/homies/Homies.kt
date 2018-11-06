package com.example.evan.homies

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_homies.*
import kotlinx.android.synthetic.main.fragment_login.*

class Homies : AppCompatActivity() {

    private var userId: Int? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var selectedFragment: Fragment? = null
        // get the selected item and replace the fragment
        when (item.itemId) {
            R.id.navigation_chores -> selectedFragment = ChoresFragment()
            R.id.navigation_house -> selectedFragment = HouseFragment()
            R.id.navigation_profile -> selectedFragment = ProfileFragment()
        }

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment!!).commit()
         true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homies)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        //TODO: get global user id? If not logged in, send to login fragment

        if(userId == null) {
            // send to login
            setContentView(R.layout.fragment_login)
        } else {
            //by default, set the fragment to chore list
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ChoresFragment()).commit()
        }


    }

    fun logIn(v: View){

        val inflator: LayoutInflater = layoutInflater

        val email:String = emailEditText.text.toString()
        val password:String = passwordEditText.text.toString()

        Toast.makeText(this, "Hello $email", Toast.LENGTH_LONG).show()

        //TODO: change this is real userId after fetch
        userId = 1
        setContentView(R.layout.activity_homies)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ChoresFragment()).commit()
    }
}
