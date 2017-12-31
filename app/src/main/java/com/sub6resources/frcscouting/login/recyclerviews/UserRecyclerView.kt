package com.sub6resources.frcscouting.login.recyclerviews

import android.support.v7.widget.CardView
import android.view.View
import android.widget.TextView
import com.sub6resources.frcscouting.R
import com.sub6resources.frcscouting.login.User
import com.sub6resources.utilities.BaseRecyclerViewAdapter
import com.sub6resources.utilities.BaseRecyclerViewHolder
import com.sub6resources.utilities.bind
import com.sub6resources.utilities.onClick

/**
 * Created by whitaker on 12/30/17.
 */

class UserRecyclerViewHolder(v: View, val selectUser: (user: User) -> Unit): BaseRecyclerViewHolder<User>(v) {

    val container by bind<CardView>(R.id.user_container)
    val userName by bind<TextView>(R.id.user_name)

    override fun onBind(data: User) {
        userName.text = data.username

        container.onClick {
            selectUser(data)
        }
    }
}

class UserRecyclerAdapter(users: List<User>, selectUser: (user: User) -> Unit):
        BaseRecyclerViewAdapter<User>(users.toMutableList(), R.layout.item_user, {
            UserRecyclerViewHolder(it, selectUser)
        })
