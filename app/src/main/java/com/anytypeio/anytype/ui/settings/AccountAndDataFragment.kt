package com.anytypeio.anytype.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.anytypeio.anytype.R
import com.anytypeio.anytype.core_utils.ext.toast
import com.anytypeio.anytype.core_utils.ui.BaseBottomSheetComposeFragment
import com.anytypeio.anytype.di.common.componentManager
import com.anytypeio.anytype.ui.dashboard.ClearCacheAlertFragment
import com.anytypeio.anytype.ui_settings.account.AccountAndDataScreen
import com.anytypeio.anytype.ui_settings.account.AccountAndDataViewModel
import javax.inject.Inject

class AccountAndDataFragment : BaseBottomSheetComposeFragment() {

    @Inject
    lateinit var factory: AccountAndDataViewModel.Factory

    private val vm by viewModels<AccountAndDataViewModel> { factory }

    private val onKeychainPhraseClicked = {
        findNavController().navigate(R.id.keychainDialog)
    }

    private val onLogoutClicked = {
        findNavController().navigate(R.id.logoutWarningScreen)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme(typography = typography) {
                    AccountAndDataScreen(
                        onKeychainPhraseClicked = onKeychainPhraseClicked,
                        onClearFileCachedClicked = { proceedWithClearFileCacheWarning() },
                        onDeleteAccountClicked = { toast(resources.getString(R.string.coming_soon)) },
                        onResetAccountClicked = { toast(resources.getString(R.string.coming_soon)) },
                        onLogoutClicked = onLogoutClicked,
                        onPinCodeClicked = { toast(resources.getString(R.string.coming_soon)) },
                        isLogoutInProgress = vm.isLoggingOut.collectAsState().value,
                        isClearCacheInProgress = vm.isClearFileCacheInProgress.collectAsState().value
                    )
                }
            }
        }
    }

    private fun proceedWithClearFileCacheWarning() {
        val dialog = ClearCacheAlertFragment.new()
        dialog.onClearAccepted = { vm.onClearFileCacheAccepted() }
        dialog.show(childFragmentManager, null)
    }

    override fun injectDependencies() {
        componentManager().accountAndDataComponent.get().inject(this)
    }

    override fun releaseDependencies() {
        componentManager().accountAndDataComponent.release()
    }
}
