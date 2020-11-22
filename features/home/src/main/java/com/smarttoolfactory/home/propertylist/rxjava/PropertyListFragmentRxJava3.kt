package com.smarttoolfactory.home.propertylist.rxjava

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.core.view.doOnNextLayout
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFadeThrough
import com.smarttoolfactory.core.di.CoreModuleDependencies
import com.smarttoolfactory.core.ui.fragment.DynamicNavigationFragment
import com.smarttoolfactory.core.ui.recyclerview.adapter.ItemBinder
import com.smarttoolfactory.core.ui.recyclerview.adapter.SingleViewBinderAdapter
import com.smarttoolfactory.core.util.observe
import com.smarttoolfactory.core.viewmodel.PropertyDetailNavigationVM
import com.smarttoolfactory.core.viewstate.Status
import com.smarttoolfactory.domain.model.PropertyItem
import com.smarttoolfactory.home.R
import com.smarttoolfactory.home.adapter.viewholder.PropertyListViewBinder
import com.smarttoolfactory.home.databinding.FragmentPropertyListBinding
import com.smarttoolfactory.home.databinding.ItemPropertyListBinding
import com.smarttoolfactory.home.di.DaggerHomeComponent
import com.smarttoolfactory.home.viewmodel.HomeToolbarVM
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject

class PropertyListFragmentRxJava3 : DynamicNavigationFragment<FragmentPropertyListBinding>() {

    @Inject
    lateinit var viewModel: PropertyListViewModelRxJava3

    private val propertyDetailNavigationVM by activityViewModels<PropertyDetailNavigationVM>()

    /**
     * ViewModel for setting sort filter on top menu and property list fragments
     */
    private val toolbarVM by activityViewModels<HomeToolbarVM>()

    override fun getLayoutRes(): Int = R.layout.fragment_property_list

    override fun onCreate(savedInstanceState: Bundle?) {
        initCoreDependentInjection()
        super.onCreate(savedInstanceState)

        // FIXME !!! Calling getPropertyList in onViewCreated causing
        //  Transition to not work with RxJava3 ViewModel !!!
        // Fetch offline-first data
        viewModel.getPropertyList()
    }

    override fun bindViews(view: View, savedInstanceState: Bundle?) {
        dataBinding.viewModel = viewModel

        dataBinding.recyclerView.apply {

            // Set Layout manager
            this.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

            val propertyListViewBinder =
                PropertyListViewBinder(
                    { propertyItem: PropertyItem, binding: ItemPropertyListBinding ->

                        // Increase click count and save to DB
                        viewModel.onClick(propertyItem)

                        // Create direction with property item
                        val direction: NavDirections = PropertyListFragmentRxJava3Directions
                            .actionPropertyListFragmentToNavGraphPropertyDetail(propertyItem)

                        // Set transition name to match next fragment
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

        subscribePropertyData()
        subscribeViewModelSortChange()
//        subscribeGoToDetailScreen()

        // Set up transition for exiting and re-entering this fragment
        prepareTransitions()
        postponeEnterTransition()

        view.doOnNextLayout {

            (it.parent as? ViewGroup)?.doOnPreDraw {
                /*
                    🔥🔥🔥 After back press RecyclerView is not laid out here,
                     so wait for it to be drawn
                 */
                if (dataBinding.recyclerView.isLaidOut) {
                    startPostponedEnterTransition()
                }

                println(
                    "💗 ${this::class.java.simpleName} doOnPreDraw()" +
                        "Adapter item count: ${dataBinding.recyclerView.adapter?.itemCount}" +
                        " RV isLaidOut: ${dataBinding.recyclerView.isLaidOut}"
                )
            }
        }

//        /* 🔥 Calling startPostponedEnterTransition here does not work since data is not loaded */
//        view.doOnLayout {
//            println("🔥 ${this::class.java.simpleName} doOnLayout()")
//        }
//
//        view.doOnPreDraw {
//            println(
//                "🍒 ${this::class.java.simpleName} doOnPreDraw()" +
//                        " RV isLaidOut: ${dataBinding.recyclerView.isLaidOut}"
//            )
//        }
//
//        dataBinding.recyclerView.doOnPreDraw {
//            println(
//                "🎃 ${this::class.java.simpleName} Binding RecyclerView onPreDraw()" +
//                    " RV isLaidOut: ${dataBinding.recyclerView.isLaidOut}"
//            )
//        }
//
//        dataBinding.recyclerView.doOnLayout {
//            println(
//                "🧭️ ${this::class.java.simpleName} Binding RecyclerView doOnLayout()" +
//                    " RV isLaidOut: ${dataBinding.recyclerView.isLaidOut}"
//            )
//        }
//
//        dataBinding.recyclerView.doOnNextLayout {
//            println(
//                "☕️ ${this::class.java.simpleName} Binding RecyclerView doOnNextLayout()" +
//                    " RV isLaidOut: ${dataBinding.recyclerView.isLaidOut}"
//            )
//        }
    }

    private fun subscribePropertyData() {
        viewModel.propertyListViewState.observe(
            viewLifecycleOwner,
            { viewState ->

                println(
                    "🤘 ${this::class.java.simpleName} subscribePropertyData()" +
                        " state: ${viewState.status}"
                )

                if (viewState.status == Status.SUCCESS || viewState.status == Status.ERROR) {
                    println(
                        "TEST1 " +
                            " RV isLaidOut: ${dataBinding.recyclerView.isLaidOut}"
                    )
                    dataBinding.recyclerView.doOnNextLayout {
                        println(
                            "TEST2 " +
                                " RV isLaidOut: ${dataBinding.recyclerView.isLaidOut}"
                        )
                        (it.parent as? ViewGroup)?.doOnPreDraw {
                            println(
                                "TEST3 " +
                                    " RV isLaidOut: ${dataBinding.recyclerView.isLaidOut}"
                            )
                            startPostponedEnterTransition()
                        }
                    }
                }
            }
        )
    }

    // FIXME This fragments Shared transitions are not working most of the time
    private fun prepareTransitions() {

        setExitSharedElementCallback(object : SharedElementCallback() {

            override fun onMapSharedElements(
                names: MutableList<String>,
                sharedElements: MutableMap<String, View>
            ) {
                super.onMapSharedElements(names, sharedElements)

                println(
                    "🔥🔥 setExitSharedElementCallback  " +
                        "names: $names," +
                        "sharedElements: $sharedElements"
                )
            }

            override fun onRejectSharedElements(rejectedSharedElements: MutableList<View>?) {
                super.onRejectSharedElements(rejectedSharedElements)
                println(
                    "❌ setExitSharedElementCallback() " +
                        "rejectedSharedElements: $rejectedSharedElements"
                )
            }

            override fun onSharedElementStart(
                sharedElementNames: MutableList<String>?,
                sharedElements: MutableList<View>?,
                sharedElementSnapshots: MutableList<View>?
            ) {
                super.onSharedElementStart(
                    sharedElementNames,
                    sharedElements,
                    sharedElementSnapshots
                )
                println("🤩 setExitSharedElementCallback() START")
            }

            override fun onSharedElementEnd(
                sharedElementNames: MutableList<String>?,
                sharedElements: MutableList<View>?,
                sharedElementSnapshots: MutableList<View>?
            ) {
                super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots)

                println("🥵 setExitSharedElementCallback() END")
            }
        })

        exitTransition =
            MaterialFadeThrough()
                .apply {
                    duration = 500
                }

        reenterTransition =
            MaterialFadeThrough()
                .apply {
                    duration = 500
                }

        enterTransition =
            MaterialElevationScale(true)
                .apply {
                    duration = 500
                }
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

//    private fun subscribeGoToDetailScreen() {
//
//        viewModel.goToDetailScreen.observe(
//            viewLifecycleOwner,
//            {
//                it.getContentIfNotHandled()?.let { propertyItem ->
//                    /*
//                        Navigates from Main fragment of the app which replaces everything with
//                        detail
//                     */
//                    propertyDetailNavigationVM.goToPropertyDetailFromHome.value =
//                        (Event(propertyItem))
//                }
//            }
//        )
//    }

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
