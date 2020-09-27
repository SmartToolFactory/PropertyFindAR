package com.smarttoolfactory.core.ui.fragment

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.dynamicfeatures.DynamicExtras
import androidx.navigation.dynamicfeatures.DynamicInstallMonitor
import com.google.android.play.core.splitinstall.SplitInstallSessionState
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus

/**
 * Fragment with [DynamicInstallMonitor] to navigate with dynamic
 * features based on [SplitInstallSessionStatus]
 */
abstract class DynamicNavigationFragment<ViewBinding : ViewDataBinding> :
    BaseDataBindingFragment<ViewBinding>() {

    fun navigateWithInstallMonitor(
        navController: NavController,
        @IdRes resId: Int,
        args: Bundle? = null,
        navOptions: NavOptions? = null,
    ) {

        val installMonitor = DynamicInstallMonitor()

        navController.navigate(
            resId,
            args,
            navOptions,
            DynamicExtras(installMonitor)
        )

        println("DynamicInstallFragment isInstallRequired: ${installMonitor.isInstallRequired}")

        if (installMonitor.isInstallRequired) {

            installMonitor.status.observe(
                viewLifecycleOwner,
                object : Observer<SplitInstallSessionState> {

                    override fun onChanged(sessionState: SplitInstallSessionState) {

                        when (sessionState.status()) {

                            SplitInstallSessionStatus.INSTALLED -> {
                                // Call navigate again here or after user taps again in the UI:
                                navController.navigate(
                                    resId,
                                    args,
                                    navOptions,
                                    DynamicExtras(installMonitor)
                                )
                            }
                            SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                            }

                            // Handle all remaining states:
                            SplitInstallSessionStatus.FAILED -> {
                            }
                            SplitInstallSessionStatus.CANCELED -> {
                            }
                        }

                        if (sessionState.hasTerminalStatus()) {
                            installMonitor.status.removeObserver(this)
                        }
                    }
                }
            )
        }
    }
}
