package com.example.evan.homies


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.*
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.evan.homies.entities.User
import com.example.evan.homies.viewmodels.UserListViewModel


class RegisterFragment : Fragment() {

    //get ViewModel to do DB operations
    private lateinit var userViewModel: UserListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel = UserListViewModel(activity?.application!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "Create Account"
        val view =  inflater.inflate(R.layout.fragment_register,container,false)

        // listen for create account button click
        view.findViewById<Button>(R.id.registerButton).setOnClickListener {
            // create new User object
            val newUser = User(
                view.findViewById<EditText>(R.id.newEmailEditText).text.toString(),
                view.findViewById<EditText>(R.id.newPasswordEditText).text.toString().hashCode(),
                view.findViewById<EditText>(R.id.firstName).text.toString(),
                view.findViewById<EditText>(R.id.lastName).text.toString()
            )
            // use ViewModel to insert into DB
            userViewModel.insertUser(newUser)
            //TODO: maybe have insert return true or false for success status
            Toast.makeText(context, "Thanks ${view.findViewById<EditText>(R.id.firstName).text}! You can log in now.", Toast.LENGTH_LONG).show()
        }
        return view
    }

    companion object {
        fun newInstance() = RegisterFragment()
    }

}
