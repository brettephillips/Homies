package com.example.evan.homies

import android.content.Context
import android.content.Intent
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

    private var userId: Long? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var selectedFragment: Fragment? = null
        // get the selected item and replace the fragment
        when (item.itemId) {
            R.id.navigation_chores -> selectedFragment = ChoresFragment()
            R.id.navigation_house -> selectedFragment = HouseFragment()
            R.id.navigation_profile -> selectedFragment = ProfileFragment()
        }

        var args = Bundle()
        args.putLong("user_id", userId!!)

        selectedFragment!!.arguments = args
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment!!).commit()
         true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("preferences",Context.MODE_PRIVATE)
        userId = sharedPreferences.getLong("USER_ID",0)

        if(userId!! == 0.toLong()) {
            // send to login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        } else {
            //by default, set the fragment to chore list
            setContentView(R.layout.activity_homies)

            val fragment = ChoresFragment()
            val args = Bundle()
            args.putLong("user_id", userId!!)

            fragment.arguments = args
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit()
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        }
    }

}
