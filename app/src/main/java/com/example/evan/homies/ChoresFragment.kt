package com.example.evan.homies

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


class ChoresFragment : Fragment() {

    private var userId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = arguments!!.getLong(USER_ID,0)
        if (userId == 0.toLong()) {
            //TODO: no user found, redirect to login
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chores, container, false)
    }

    companion object {
        const val USER_ID = "user_id"
        const val USER_NAME = "user_name"
    }

}
