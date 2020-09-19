package com.smarttoolfactory.propertyfindar

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.smarttoolfactory.core.ui.fragment.DynamicNavigationFragment
import com.smarttoolfactory.core.util.observe
import com.smarttoolfactory.core.viewmodel.PropertyDetailNavigationVM
import com.smarttoolfactory.propertyfindar.databinding.FragmentMainBinding
import com.smarttoolfactory.propertyfindar.ui.BottomNavigationFragmentStateAdapter

class MainFragment : DynamicNavigationFragment<FragmentMainBinding>() {

    /**
     * ViewModel for navigating to property detail screen from Main Fragment
     */
    private val propertyDetailNavigationVM by activityViewModels<PropertyDetailNavigationVM>()

    override fun getLayoutRes(): Int = R.layout.fragment_main

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = dataBinding!!

        val viewPager2 = binding.viewPager
        val bottomNavigationView = binding.bottomNav

        // Cancel ViewPager swipe
        viewPager2.isUserInputEnabled = false

        // Set viewpager adapter
        viewPager2.adapter =
            BottomNavigationFragmentStateAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)

        // Listen bottom navigation tabs change
        bottomNavigationView.setOnNavigationItemSelectedListener {

            when (it.itemId) {

                R.id.nav_graph_dfm_home_start -> {
                    viewPager2.setCurrentItem(0, false)
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.nav_graph_dfm_dashboard_start -> {
                    viewPager2.setCurrentItem(1, false)
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.nav_graph_dfm_notification_start -> {
                    viewPager2.setCurrentItem(2, false)
                    return@setOnNavigationItemSelectedListener true
                }

                else -> {
                    viewPager2.setCurrentItem(3, false)
                    return@setOnNavigationItemSelectedListener true
                }
            }
        }
        subscribePropertyDetailNavigation()
    }

    /**
     * Navigates to Property Detail fragment from this fragment that replacing main fragment
     * that contains [BottomNavigationView]
     */
    private fun subscribePropertyDetailNavigation() {
        viewLifecycleOwner.observe(propertyDetailNavigationVM.goToPropertyDetailFromMain) {

            it.getContentIfNotHandled()?.let { propertyItem ->
                val bundle = bundleOf("property" to propertyItem)

                findNavController()
                    .navigate(
                        R.id.action_mainFragment_to_propertyDetailFragment,
                        bundle
                    )
            }
        }
    }

    override fun onDestroyView() {

        val viewPager2 = dataBinding?.viewPager

        /*
            Without setting ViewPager2 Adapter it causes memory leak

            https://stackoverflow.com/questions/62851425/viewpager2-inside-a-fragment-leaks-after-replacing-the-fragment-its-in-by-navig
         */
        viewPager2?.let {
            it.adapter = null
        }

        super.onDestroyView()
    }
}
