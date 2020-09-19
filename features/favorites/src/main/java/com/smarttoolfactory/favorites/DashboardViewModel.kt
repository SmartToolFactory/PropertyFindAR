package com.smarttoolfactory.favorites

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.smarttoolfactory.domain.usecase.property.GetDashboardStatsUseCase
import kotlinx.coroutines.CoroutineScope

class DashboardViewModel @ViewModelInject constructor(
    private val coroutineScope: CoroutineScope,
    private val dashboardStatsUseCase: GetDashboardStatsUseCase
) : ViewModel()
