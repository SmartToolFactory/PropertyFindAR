package com.smarttoolfactory.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.smarttoolfactory.core.ui.fragment.DynamicNavigationFragment
import com.smarttoolfactory.core.util.Event
import com.smarttoolfactory.core.viewmodel.NavControllerViewModel
import com.smarttoolfactory.home.adapter.HomeViewPager2FragmentStateAdapter
import com.smarttoolfactory.home.databinding.FragmentHomeBinding

/**
 * Fragment that contains [ViewPager2] and [TabLayout].
 * If this fragments get replaced and [Fragment.onDestroyView]
 * is called there are things to be considered
 *
 * * [FragmentStateAdapter] that is not null after [Fragment.onDestroy] cause memory leak,
 * so assign null to it
 *
 * * [TabLayoutMediator] cause memory leak if not detached after [Fragment.onDestroy]
 * of this fragment is called.
 *
 * * Data-binding which is not null after [Fragment.onDestroy]  causes memory leak
 *
 * *[NavControllerViewModel] that has a [NavController] that belong to a NavHostFragment
 * that is to be destroyed also causes memory leak.
 */
class HomeFragment : DynamicNavigationFragment<FragmentHomeBinding>() {

    override fun getLayoutRes(): Int = R.layout.fragment_home

    private val navControllerViewModel by activityViewModels<NavControllerViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ViewPager2
        val viewPager = dataBinding!!.viewPager

        // TabLayout
        val tabLayout = dataBinding!!.tabLayout

        /*
            Set Adapter for ViewPager inside this fragment using this Fragment,
            more specifically childFragmentManager as param

            🔥 Create FragmentStateAdapter with viewLifeCycleOwner
            https://stackoverflow.com/questions/61779776/leak-canary-detects-memory-leaks-for-tablayout-with-viewpager2
         */
        viewPager.adapter =
            HomeViewPager2FragmentStateAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)

        // Bind tabs and viewpager
        TabLayoutMediator(tabLayout, viewPager, tabConfigurationStrategy).attach()

        subscribeAppbarNavigation()
    }

    private fun subscribeAppbarNavigation() {
        navControllerViewModel.currentNavController.observe(
            viewLifecycleOwner,
            Observer { it ->

                it?.let { event: Event<NavController?> ->
                    event.getContentIfNotHandled()?.let { navController ->
                        val appBarConfig = AppBarConfiguration(navController.graph)
                        dataBinding!!.toolbar.setupWithNavController(navController, appBarConfig)
                    }
                }
            }
        )
    }

    override fun onDestroyView() {

        // ViewPager2
        val viewPager2 = dataBinding!!.viewPager
        // TabLayout
        val tabLayout = dataBinding!!.tabLayout

        /*
            🔥 Detach TabLayoutMediator since it causing memory leaks when it's in a fragment
            https://stackoverflow.com/questions/61779776/leak-canary-detects-memory-leaks-for-tablayout-with-viewpager2
         */
        TabLayoutMediator(tabLayout, viewPager2, tabConfigurationStrategy).detach()

        /*
            🔥 Without setting ViewPager2 Adapter to null it causes memory leak
            https://stackoverflow.com/questions/62851425/viewpager2-inside-a-fragment-leaks-after-replacing-the-fragment-its-in-by-navig
         */
        viewPager2?.let {
            it.adapter = null
        }

        super.onDestroyView()
    }

    private val tabConfigurationStrategy =
        TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            when (position) {
                0 -> tab.text = "Properties With Flow"
                else -> tab.text = "Properties With RxJava3"
            }
        }
}
