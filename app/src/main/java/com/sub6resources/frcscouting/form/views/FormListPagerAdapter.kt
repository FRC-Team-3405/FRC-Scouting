package com.sub6resources.frcscouting.form.views

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.nightlynexus.viewstatepageradapter.ViewStatePagerAdapter

/**
 * Created by whitaker on 12/26/17.
 */


class FormListPagerAdapter(val context: Context, val viewMap: List<Pair<String, View>>) : ViewStatePagerAdapter() {
    override fun createView(container: ViewGroup?, position: Int): View {
        return viewMap[position].second.apply {
            tag = viewMap[position].first
        }
    }

    override fun getCount(): Int = viewMap.size
    override fun getPageTitle(position: Int) = viewMap[position].first

}
