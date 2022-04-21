package com.anytypeio.anytype.ui_settings.appearance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.anytypeio.anytype.core_models.ThemeMode
import com.anytypeio.anytype.domain.base.BaseUseCase
import com.anytypeio.anytype.domain.theme.GetTheme
import com.anytypeio.anytype.domain.theme.SetTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class AppearanceViewModel(
    private val getTheme: GetTheme,
    private val setTheme: SetTheme,
    private val themeApplicator: ThemeApplicator,
) : ViewModel() {

    val selectedTheme = MutableStateFlow<ThemeMode>(ThemeMode.System)

    init {
        viewModelScope.launch {
            getTheme(BaseUseCase.None).proceed(
                success = {
                    proceedWithUpdatingSelectedTheme(it)
                },
                failure = {
                    Timber.e(it, "Error while getting current app theme")
                })
        }
    }

    private fun saveTheme(mode: ThemeMode) {
        viewModelScope.launch {
            setTheme(params = mode).proceed(
                success = {
                    proceedWithUpdatingTheme(mode)
                },
                failure = {
                    Timber.e(it, "Error while setting current app theme")
                }
            )
        }
    }

    fun onLight() {
        saveTheme(ThemeMode.Light)
    }

    fun onDark() {
        saveTheme(ThemeMode.Night)
    }

    fun onSystem() {
        saveTheme(ThemeMode.System)
    }

    private fun proceedWithUpdatingTheme(themeMode: ThemeMode) {
        themeApplicator.apply(themeMode)
        proceedWithUpdatingSelectedTheme(themeMode)
    }

    private fun proceedWithUpdatingSelectedTheme(themeMode: ThemeMode) {
        selectedTheme.value = themeMode
    }

    class Factory(
        private val getTheme: GetTheme,
        private val setTheme: SetTheme,
        private val themeApplicator: ThemeApplicator,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AppearanceViewModel(
                getTheme = getTheme,
                setTheme = setTheme,
                themeApplicator = themeApplicator
            ) as T
        }
    }
}