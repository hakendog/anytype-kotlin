package com.anytypeio.anytype.di.feature.types

import androidx.lifecycle.ViewModelProvider
import com.anytypeio.anytype.core_utils.di.scope.PerScreen
import com.anytypeio.anytype.di.common.ComponentDependencies
import com.anytypeio.anytype.domain.block.repo.BlockRepository
import com.anytypeio.anytype.domain.types.CreateType
import com.anytypeio.anytype.presentation.types.TypeCreationViewModel
import com.anytypeio.anytype.ui.types.TypeCreationFragment
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    dependencies = [TypeCreationDependencies::class],
    modules = [
        TypeCreationModule::class,
        TypeCreationModule.Declarations::class
    ]
)
@PerScreen
interface TypeCreationComponent {

    @Component.Factory
    interface Factory {
        fun create(dependencies: TypeCreationDependencies): TypeCreationComponent
    }

    fun inject(fragment: TypeCreationFragment)
}

@Module
object TypeCreationModule {

    @JvmStatic
    @PerScreen
    @Provides
    fun provideCreateTypeInteractor(
        blockRepository: BlockRepository
    ): CreateType = CreateType(blockRepository)

    @Module
    interface Declarations {

        @PerScreen
        @Binds
        fun bindViewModelFactory(factory: TypeCreationViewModel.Factory): ViewModelProvider.Factory

    }

}

interface TypeCreationDependencies : ComponentDependencies {
    fun blockRepository(): BlockRepository
}