package com.rocatoro.musichub.activities.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rocatoro.musichub.fragments.client.ClientOrdersStatusFragment
import com.rocatoro.musichub.fragments.musichub.MusicHubOrdersStatusFragment

class MusicHubTabsAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    var numberOfTabs: Int
): FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return numberOfTabs
    }

    override fun createFragment(position: Int): Fragment {

        when(position) {
            0 -> {
                val bundle = Bundle()
                bundle.putString("status","PAGADO")
                val musichubOrdersStatusFragment = MusicHubOrdersStatusFragment()
                musichubOrdersStatusFragment .arguments = bundle
                return musichubOrdersStatusFragment
            }
            1 -> {
                val bundle = Bundle()
                bundle.putString("status","DESPACHADO")
                val musichubOrdersStatusFragment = MusicHubOrdersStatusFragment()
                musichubOrdersStatusFragment .arguments = bundle
                return musichubOrdersStatusFragment
            }
            2 -> {
                val bundle = Bundle()
                bundle.putString("status","EN CAMINO")
                val musichubOrdersStatusFragment = MusicHubOrdersStatusFragment()
                musichubOrdersStatusFragment .arguments = bundle
                return musichubOrdersStatusFragment
            }
            3 -> {
                val bundle = Bundle()
                bundle.putString("status","ENTREGADO")
                val musichubOrdersStatusFragment = MusicHubOrdersStatusFragment()
                musichubOrdersStatusFragment .arguments = bundle
                return musichubOrdersStatusFragment
            }
            else -> return MusicHubOrdersStatusFragment()
        }

    }

}