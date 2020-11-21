package com.smarttoolfactory.home.propertylist.flow

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.SharedElementCallback
import androidx.core.os.bundleOf
import androidx.core.view.doOnLayout
import androidx.core.view.doOnNextLayout
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Transition
import androidx.transition.TransitionListenerAdapter
import com.google.android.material.transition.MaterialElevationScale
import com.smarttoolfactory.core.di.CoreModuleDependencies
import com.smarttoolfactory.core.ui.fragment.DynamicNavigationFragment
import com.smarttoolfactory.core.ui.recyclerview.adapter.ItemBinder
import com.smarttoolfactory.core.ui.recyclerview.adapter.SingleViewBinderAdapter
import com.smarttoolfactory.core.util.observe
import com.smarttoolfactory.core.viewmodel.PropertyDetailNavigationVM
import com.smarttoolfactory.domain.model.PropertyItem
import com.smarttoolfactory.home.R
import com.smarttoolfactory.home.adapter.viewholder.PropertyListViewBinder
import com.smarttoolfactory.home.databinding.FragmentPropertyListBinding
import com.smarttoolfactory.home.databinding.ItemPropertyListBinding
import com.smarttoolfactory.home.di.DaggerHomeComponent
import com.smarttoolfactory.home.viewmodel.HomeToolbarVM
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject

class PropertyListFragment : DynamicNavigationFragment<FragmentPropertyListBinding>() {

    @Inject
    lateinit var viewModel: PropertyListViewModel

    private val propertyDetailNavigationVM by activityViewModels<PropertyDetailNavigationVM>()

    /**
     * ViewModel for setting sort filter on top menu and property list fragments
     */
    private val toolbarVM by activityViewModels<HomeToolbarVM>()

    override fun getLayoutRes(): Int = R.layout.fragment_property_list

    override fun onCreate(savedInstanceState: Bundle?) {
        initCoreDependentInjection()

        super.onCreate(savedInstanceState)
    }

    override fun bindViews(view: View, savedInstanceState: Bundle?) {

        dataBinding.viewModel = viewModel

        dataBinding.contentLoadingProgressBar.show()

        // Fetch offline-first data
        viewModel.getPropertyList()

        dataBinding.recyclerView.apply {

            // Set Layout manager
            this.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

            val propertyListViewBinder =
                PropertyListViewBinder(
                    { propertyItem: PropertyItem, binding: ItemPropertyListBinding ->

                        val direction: NavDirections = PropertyListFragmentDirections
                            .actionPropertyListFragmentToNavGraphPropertyDetail(propertyItem)

                        val extras = FragmentNavigatorExtras(
                            binding.cardView to binding.cardView.transitionName
                        )

                        findNavController().navigate(direction, extras)
                    },
                    viewModel::onLikeButtonClick
                )

            val singleViewBinderAdapter =
                SingleViewBinderAdapter(propertyListViewBinder as ItemBinder)

            this.adapter = singleViewBinderAdapter
        }

        val swipeRefreshLayout = dataBinding.swipeRefreshLayout

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            viewModel.refreshPropertyList()
        }

        viewModel.propertyListViewState.observe(
            viewLifecycleOwner,
            {
                println("LIVEDATA SUBMITTED status: ${it.status}")
            }
        )

        subscribeViewModelSortChange()

        subscribeGoToDetailScreen()

        // Set up transition for exiting and re-entering this fragment
        prepareTransitions()
        postponeEnterTransition()

        view.doOnLayout {
            println("🔥 onLayout")
        }
        view.doOnNextLayout {

            (it.parent as? ViewGroup)?.doOnPreDraw {
                startPostponedEnterTransition()
                println("💗 doOnPreDraw()")
//                dataBinding.swipeRefreshLayout.visibility = View.INVISIBLE
//                TransitionManager.beginDelayedTransition(dataBinding.swipeRefreshLayout, MaterialElevationScale(true)
//                    .apply { duration = 1300 })
//                dataBinding.swipeRefreshLayout.visibility = View.VISIBLE
            }
        }

        dataBinding.recyclerView.doOnPreDraw {
            println("RV onPreDraw")
        }
    }

    private fun prepareTransitions() {

        setExitSharedElementCallback(object : SharedElementCallback() {

            override fun onMapSharedElements(
                names: MutableList<String>?,
                sharedElements: MutableMap<String, View>?
            ) {
                super.onMapSharedElements(names, sharedElements)
                Toast.makeText(
                    requireContext(),
                    "onMapSharedElements " +
                        "names: $names",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        exitTransition = MaterialElevationScale(false)
            .apply {
                duration = 300
            }

        reenterTransition =

            MaterialElevationScale(true)
//            Slide(Gravity.BOTTOM)
                .apply {
                    duration = 300
                }
                .addListener(object : TransitionListenerAdapter() {

                    override fun onTransitionStart(transition: Transition) {
                        super.onTransitionStart(transition)
                        Toast.makeText(
                            requireContext(),
                            "Transition start", Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onTransitionEnd(transition: Transition) {
                        super.onTransitionEnd(transition)
                        Toast.makeText(
                            requireContext(),
                            "Transition End: $transition",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
    }

    /**
     * When sort key is fetched from database change the one belong to Toolbar
     */
    private fun subscribeViewModelSortChange() {
        viewLifecycleOwner.observe(viewModel.orderKey) {
            toolbarVM.currentSortFilter = it
        }
    }

    private fun subscribeToolbarSortChange() {

        viewLifecycleOwner.observe(toolbarVM.queryBySort) {
            it.getContentIfNotHandled()?.let { orderBy ->
                viewModel.refreshPropertyList(orderBy)
                toolbarVM.currentSortFilter = orderBy
            }
        }
    }

    private fun subscribeGoToDetailScreen() {

        viewModel.goToDetailScreen.observe(
            viewLifecycleOwner,
            {

                it.getContentIfNotHandled()?.let { propertyItem ->

                    val bundle = bundleOf("property" to propertyItem)
                    findNavController().navigate(
                        R.id.action_propertyListFragment_to_nav_graph_property_detail,
                        bundle
                    )
                }
            }
        )
    }

    private fun initCoreDependentInjection() {

        val coreModuleDependencies = EntryPointAccessors.fromApplication(
            requireActivity().applicationContext,
            CoreModuleDependencies::class.java
        )

        DaggerHomeComponent.factory().create(
            dependentModule = coreModuleDependencies,
            fragment = this
        )
            .inject(this)
    }

    override fun onResume() {
        super.onResume()
        subscribeToolbarSortChange()
    }

    override fun onPause() {
        super.onPause()
        toolbarVM.queryBySort.removeObservers(viewLifecycleOwner)
    }

    override fun onDestroyView() {
        dataBinding.swipeRefreshLayout.setOnRefreshListener(null)
        dataBinding.recyclerView.adapter = null
        super.onDestroyView()
    }
}
