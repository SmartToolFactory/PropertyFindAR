package com.smarttoolfactory.home

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.smarttoolfactory.core.ui.fragment.DynamicNavigationFragment
import com.smarttoolfactory.core.util.Event
import com.smarttoolfactory.core.util.observe
import com.smarttoolfactory.core.viewmodel.NavControllerViewModel
import com.smarttoolfactory.core.viewmodel.PropertyDetailNavigationVM
import com.smarttoolfactory.home.adapter.HomeViewPager2FragmentStateAdapter
import com.smarttoolfactory.home.databinding.FragmentHomeBinding
import com.smarttoolfactory.home.viewmodel.HomeToolbarVM

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

    /**
     * ViwModel for getting [NavController] for setting Toolbar navigation
     */
    private val navControllerViewModel by activityViewModels<NavControllerViewModel>()

    /**
     * ViewModel for setting sort filter on top menu and property list fragments
     */
    private val toolbarVM by activityViewModels<HomeToolbarVM>()

    /**
     * ViewModel for navigating to property detail screen from Home Fragment
     */
    private val propertyDetailNavigationVM by activityViewModels<PropertyDetailNavigationVM>()

    override fun bindViews() {

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

        dataBinding!!.toolbar.inflateMenu(R.menu.menu_home)

        // Bind tabs and viewpager
        TabLayoutMediator(tabLayout, viewPager, tabConfigurationStrategy).attach()

        setToolbarMenuItemListener()

        subscribeAppbarNavigation()

        subscribePropertyDetailNavigation()
    }

    /**
     * Navigates to Property Detail fragment from this fragment that replacing main fragment
     * that contains [BottomNavigationView]
     */
    private fun subscribePropertyDetailNavigation() {
        viewLifecycleOwner.observe(propertyDetailNavigationVM.goToPropertyDetailFromHome) {

            it.getContentIfNotHandled()?.let { propertyItem ->
                val bundle = bundleOf("property" to propertyItem)

                findNavController()
                    .navigate(
                        R.id.action_home_dest_to_propertyDetailFragment,
                        bundle
                    )
            }
        }
    }

    private fun setToolbarMenuItemListener() {
        dataBinding!!.toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.menu_item_sort) {
                val dialogFragment = SortDialogFragment().show(
                    requireActivity().supportFragmentManager,
                    "sort-dialog"
                )
                true
            }

            false
        }
    }

    private fun subscribeAppbarNavigation() {
        navControllerViewModel.currentNavController.observe(
            viewLifecycleOwner,
            { it ->

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

        // Remove menu item click listener
        dataBinding!!.toolbar.setOnMenuItemClickListener(null)

        super.onDestroyView()
    }

    private val tabConfigurationStrategy =
        TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            when (position) {
                0 -> tab.text = "Flow"
                1 -> tab.text = "RxJava3"
                else -> tab.text = "Flow+Pagination"
            }
        }
}

class SortDialogFragment : DialogFragment() {

    private lateinit var viewModel: HomeToolbarVM

    private var currentItem = 0
    private var checkedItem = currentItem
    private var canceled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(HomeToolbarVM::class.java)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        currentItem = viewModel.sortPropertyList.indexOf(viewModel.currentSortFilter)
        checkedItem = currentItem
        canceled = false

        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Sorting")
            .setNegativeButton("CANCEL") { dialog, which ->
                canceled = true
                dismiss()
            }
            .setSingleChoiceItems(
                viewModel.sortFilterNames.toTypedArray(),
                currentItem
            ) { dialog, which ->
                checkedItem = which

                // Alternative 1 works as soon as user changes the option
                //                if (currentItem != checkedItem) {
//                    viewModel.queryBySort.value = Event(viewModel.sortPropertyList[checkedItem])
//                }
            }
        return builder.create()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        // Alternative works after dialog is dismissed
        if (currentItem != checkedItem && !canceled) {
            viewModel.queryBySort.value = Event(viewModel.sortPropertyList[checkedItem])
        }
    }
}
