package com.example.evan.homies

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.share


class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        addFragment(LoginFragment.newInstance())

    }

    fun goToHomePage() {
        val intent = Intent(this, Homies::class.java)
        startActivity(intent)
    }

    fun addFragment(fragment: Fragment) {
        val currentFragment = supportFragmentManager.findFragmentByTag(fragment.javaClass.simpleName)
        if(currentFragment != null) {
            supportFragmentManager.popBackStackImmediate(fragment.javaClass.simpleName,0)
        } else {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
                .replace(R.id.login_fragment, fragment, fragment.javaClass.simpleName)
                .addToBackStack(fragment.javaClass.simpleName)
                .commit()
        }
    }

}
