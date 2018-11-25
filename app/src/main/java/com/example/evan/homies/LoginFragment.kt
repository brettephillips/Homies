package com.example.evan.homies

import android.arch.lifecycle.Observer
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.TextView
import com.example.evan.homies.entities.User
import com.example.evan.homies.viewmodels.UserListViewModel


class LoginFragment : Fragment() {

    //get ViewModel to do DB operations
    private lateinit var userViewModel: UserListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel = UserListViewModel(activity?.application!!)
        userViewModel.getAllUsers().observe(this,
            Observer { users ->
                println("Current Users: $users")
            })
        userViewModel.getUserInfo().observe(this,
            Observer { userInfo ->
                println("User info has been updated, store it in shared prefs: $userInfo")
                val preferences = this.activity?.getSharedPreferences("preferences",Context.MODE_PRIVATE)
                preferences!!
                    .edit()
                    .putLong("USER_ID", userInfo!!.id!!)
                    .putString("USER_NAME", "${userInfo!!.firstName!!} " +
                            "${userInfo.lastName!!}")
                    .apply()
                (activity as LoginActivity).goToHomePage()
            })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "Log In"
        val view =  inflater.inflate(R.layout.fragment_login,container,false)

        view.findViewById<Button>(R.id.loginButton).setOnClickListener {
            //log in
            userViewModel.checkUserCredentials(
                view.findViewById<EditText>(R.id.emailEditText).text.toString(),
                view.findViewById<EditText>(R.id.passwordEditText).text.toString().hashCode()
            )
            //get user id
            //set in sharedpreferences
            // somehow notify activity and change activities
        }

        view.findViewById<TextView>(R.id.createAccountTextView).setOnClickListener {
            // go to create account page
            // this is probably bad :( ask French, use interface?
            (activity as LoginActivity).addFragment(RegisterFragment.newInstance())
        }
        return view
    }

    companion object {
        fun newInstance() = LoginFragment()
    }

}
