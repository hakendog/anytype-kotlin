package com.anytypeio.anytype.di.feature

import com.anytypeio.anytype.analytics.base.Analytics
import com.anytypeio.anytype.core_utils.di.scope.PerScreen
import com.anytypeio.anytype.domain.auth.interactor.CheckAuthorizationStatus
import com.anytypeio.anytype.domain.auth.interactor.GetLastOpenedObject
import com.anytypeio.anytype.domain.auth.interactor.LaunchAccount
import com.anytypeio.anytype.domain.auth.interactor.LaunchWallet
import com.anytypeio.anytype.domain.auth.repo.AuthRepository
import com.anytypeio.anytype.domain.base.AppCoroutineDispatchers
import com.anytypeio.anytype.domain.block.repo.BlockRepository
import com.anytypeio.anytype.domain.config.ConfigStorage
import com.anytypeio.anytype.domain.config.FeaturesConfigProvider
import com.anytypeio.anytype.domain.config.UserSettingsRepository
import com.anytypeio.anytype.domain.device.PathProvider
import com.anytypeio.anytype.domain.launch.GetDefaultPageType
import com.anytypeio.anytype.domain.launch.SetDefaultEditorType
import com.anytypeio.anytype.domain.misc.AppActionManager
import com.anytypeio.anytype.domain.page.CreateObject
import com.anytypeio.anytype.domain.search.ObjectTypesSubscriptionManager
import com.anytypeio.anytype.domain.search.RelationsSubscriptionManager
import com.anytypeio.anytype.domain.templates.GetTemplates
import com.anytypeio.anytype.domain.workspace.WorkspaceManager
import com.anytypeio.anytype.presentation.splash.SplashViewModelFactory
import com.anytypeio.anytype.ui.splash.SplashFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@PerScreen
@Subcomponent(modules = [SplashModule::class])
interface SplashSubComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build(): SplashSubComponent
        fun module(module: SplashModule): Builder
    }

    fun inject(fragment: SplashFragment)
}

@Module
object SplashModule {

    @JvmStatic
    @PerScreen
    @Provides
    fun provideSplashViewModelFactory(
        checkAuthorizationStatus: CheckAuthorizationStatus,
        launchAccount: LaunchAccount,
        launchWallet: LaunchWallet,
        analytics: Analytics,
        getLastOpenedObject: GetLastOpenedObject,
        getDefaultPageType: GetDefaultPageType,
        setDefaultEditorType: SetDefaultEditorType,
        createObject: CreateObject,
        appActionManager: AppActionManager,
        relationsSubscriptionManager: RelationsSubscriptionManager,
        objectTypesSubscriptionManager: ObjectTypesSubscriptionManager
    ): SplashViewModelFactory = SplashViewModelFactory(
        checkAuthorizationStatus = checkAuthorizationStatus,
        launchAccount = launchAccount,
        launchWallet = launchWallet,
        analytics = analytics,
        getLastOpenedObject = getLastOpenedObject,
        setDefaultEditorType = setDefaultEditorType,
        getDefaultPageType = getDefaultPageType,
        createObject = createObject,
        appActionManager = appActionManager,
        relationsSubscriptionManager = relationsSubscriptionManager,
        objectTypesSubscriptionManager = objectTypesSubscriptionManager
    )

    @JvmStatic
    @PerScreen
    @Provides
    fun provideCheckAuthorizationStatusUseCase(
        authRepository: AuthRepository
    ): CheckAuthorizationStatus = CheckAuthorizationStatus(
        repository = authRepository
    )

    @JvmStatic
    @PerScreen
    @Provides
    fun provideLaunchAccountUseCase(
        authRepository: AuthRepository,
        pathProvider: PathProvider,
        featuresConfigProvider: FeaturesConfigProvider,
        configStorage: ConfigStorage,
        workspaceManager: WorkspaceManager
    ): LaunchAccount = LaunchAccount(
        repository = authRepository,
        pathProvider = pathProvider,
        featuresConfigProvider = featuresConfigProvider,
        configStorage = configStorage,
        workspaceManager = workspaceManager
    )

    @JvmStatic
    @PerScreen
    @Provides
    fun provideLaunchWalletUseCase(
        authRepository: AuthRepository,
        pathProvider: PathProvider
    ): LaunchWallet =
        LaunchWallet(
            repository = authRepository,
            pathProvider = pathProvider
        )

    @JvmStatic
    @Provides
    @PerScreen
    fun getLastOpenedObject(
        repo: BlockRepository,
        auth: AuthRepository
    ): GetLastOpenedObject = GetLastOpenedObject(
        authRepo = auth,
        blockRepo = repo
    )

    @JvmStatic
    @PerScreen
    @Provides
    fun provideGetDefaultPageType(
        userSettingsRepository: UserSettingsRepository,
        blockRepository: BlockRepository,
        workspaceManager: WorkspaceManager,
        dispatchers: AppCoroutineDispatchers
    ): GetDefaultPageType = GetDefaultPageType(
        userSettingsRepository,
        blockRepository,
        workspaceManager,
        dispatchers
    )

    @JvmStatic
    @PerScreen
    @Provides
    fun provideSetDefaultPageType(repo: UserSettingsRepository): SetDefaultEditorType =
        SetDefaultEditorType(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun getCreateObject(
        repo: BlockRepository,
        getTemplates: GetTemplates,
        getDefaultPageType: GetDefaultPageType,
        dispatchers: AppCoroutineDispatchers
    ): CreateObject = CreateObject(
        repo = repo,
        getTemplates = getTemplates,
        getDefaultPageType = getDefaultPageType,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideGetTemplates(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): GetTemplates = GetTemplates(
        repo = repo,
        dispatchers = dispatchers
    )
}