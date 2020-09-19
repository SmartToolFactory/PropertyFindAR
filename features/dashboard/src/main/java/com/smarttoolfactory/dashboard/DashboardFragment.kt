package com.smarttoolfactory.dashboard

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.smarttoolfactory.core.di.CoreModuleDependencies
import com.smarttoolfactory.core.ui.fragment.DynamicNavigationFragment
import com.smarttoolfactory.dashboard.adapter.DashboardListAdapter
import com.smarttoolfactory.dashboard.databinding.FragmentDashboardBinding
import com.smarttoolfactory.dashboard.di.DaggerDashboardComponent
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject

class DashboardFragment : DynamicNavigationFragment<FragmentDashboardBinding>() {

    @Inject
    lateinit var viewModel: DashboardViewModel

    override fun getLayoutRes(): Int = R.layout.fragment_dashboard

    lateinit var itemListAdapter: DashboardListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        initCoreDependentInjection()
        super.onCreate(savedInstanceState)
        viewModel.getFavoriteProperties()
    }

    override fun bindViews() {
        dataBinding!!.viewModel = viewModel

        dataBinding!!.recyclerView.apply {

            // Set Layout manager
            this.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

            // Set RecyclerViewAdapter
            itemListAdapter = DashboardListAdapter(
                R.layout.item_property_list,
                viewModel::onClick

            )
            this.adapter = itemListAdapter
        }

        val swipeRefreshLayout = dataBinding!!.swipeRefreshLayout

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun initCoreDependentInjection() {

        val coreModuleDependencies = EntryPointAccessors.fromApplication(
            requireActivity().applicationContext,
            CoreModuleDependencies::class.java
        )

        DaggerDashboardComponent.factory().create(
            dependentModule = coreModuleDependencies,
            fragment = this
        )
            .inject(this)
    }
}
