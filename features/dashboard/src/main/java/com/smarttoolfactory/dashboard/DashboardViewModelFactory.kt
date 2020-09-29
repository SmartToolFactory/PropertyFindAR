package com.smarttoolfactory.dashboard

import androidx.lifecycle.SavedStateHandle
import com.smarttoolfactory.dashboard.di.ViewModelFactory
import com.smarttoolfactory.domain.usecase.property.GetDashboardStatsUseCase
import com.smarttoolfactory.domain.usecase.property.SetPropertyStatsUseCase
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope

class DashboardViewModelFactory @Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val dashboardStatsUseCase: GetDashboardStatsUseCase,
    private val setPropertyStatsUseCase: SetPropertyStatsUseCase
) : ViewModelFactory<DashboardViewModel> {

    override fun create(handle: SavedStateHandle): DashboardViewModel {
        return DashboardViewModel(
            handle,
            coroutineScope,
            dashboardStatsUseCase,
            setPropertyStatsUseCase
        )
    }
}
