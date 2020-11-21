package com.smarttoolfactory.dashboard

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnNextLayout
import androidx.core.view.doOnPreDraw
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Slide
import com.google.android.material.transition.MaterialElevationScale
import com.smarttoolfactory.core.ui.fragment.DynamicNavigationFragment
import com.smarttoolfactory.core.ui.recyclerview.adapter.ItemBinder
import com.smarttoolfactory.core.ui.recyclerview.adapter.SingleViewBinderAdapter
import com.smarttoolfactory.dashboard.adapter.viewholder.PropertySeeAllListViewBinder
import com.smarttoolfactory.dashboard.databinding.FragmentDashboardSeeAllBinding
import com.smarttoolfactory.dashboard.databinding.ItemPropertySeeAllBinding
import com.smarttoolfactory.domain.model.PropertyItem

class DashboardSeeAllFragment : DynamicNavigationFragment<FragmentDashboardSeeAllBinding>() {

    override fun getLayoutRes() = R.layout.fragment_dashboard_see_all

    override fun bindViews(view: View, savedInstanceState: Bundle?) {

        dataBinding.recyclerView.apply {

            // Set Layout manager
            this.layoutManager =
                LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

            val propertyListViewBinder =
                PropertySeeAllListViewBinder { propertyItem: PropertyItem,
                                               binding: ItemPropertySeeAllBinding ->

                    val direction: NavDirections = DashboardSeeAllFragmentDirections
                        .actionDashboardSeeAllFragmentToNavGraphPropertyDetail(propertyItem)

                    val extras = FragmentNavigatorExtras(
                        binding.cardView to binding.cardView.transitionName
                    )

                    findNavController().navigate(direction, extras)
                }

            val itemListAdapter =
                SingleViewBinderAdapter(propertyListViewBinder as ItemBinder)

            this.adapter = itemListAdapter

            // Add vertical dividers
            val verticalDivider = DividerItemDecoration(this.context, LinearLayoutManager.VERTICAL)
            addItemDecoration(verticalDivider)

            // Set data
            val args = requireArguments()
            val propertyItemListModel =
                DashboardSeeAllFragmentArgs.fromBundle(args).propertyListModelArgs

            itemListAdapter.submitList(propertyItemListModel.items)
        }

        // Set up transition for exiting and re-entering this fragment
        prepareTransitions()
        postponeEnterTransition()

        view.doOnNextLayout {
            (it.parent as? ViewGroup)?.doOnPreDraw {
                startPostponedEnterTransition()
            }
        }
    }

    private fun prepareTransitions() {

        // exitTransition and reEnterTransition are for navigating to/from PropertyDetailFragment
        exitTransition = MaterialElevationScale(false)
            .apply {
                duration = 500
            }

        reenterTransition =
            MaterialElevationScale(true)
                .apply {
                    duration = 500
                }

        // enterTransition and returnTransition are for navigating from/to DashboardFragment
        enterTransition =
            MaterialElevationScale(true)
                .apply {
                    duration = 500
                }

        returnTransition =
            Slide(Gravity.BOTTOM)
                .apply {
                    duration = 300
                }
    }
}
