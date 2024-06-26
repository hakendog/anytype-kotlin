package com.anytypeio.anytype.ui.objects.appearance.choose

import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.anytypeio.anytype.R
import com.anytypeio.anytype.core_models.Id
import com.anytypeio.anytype.core_models.primitives.SpaceId
import com.anytypeio.anytype.di.common.componentManager
import com.anytypeio.anytype.di.feature.DefaultComponentParam
import com.anytypeio.anytype.presentation.objects.appearance.choose.ObjectAppearanceChooseDescriptionViewModel
import com.anytypeio.anytype.presentation.objects.appearance.choose.ObjectAppearanceChooseSettingsView
import javax.inject.Inject

class ObjectAppearanceChooseDescriptionFragment :
    ObjectAppearanceChooseFragmentBase<ObjectAppearanceChooseSettingsView.Description, ObjectAppearanceChooseDescriptionViewModel>() {

    @Inject
    lateinit var factory: ObjectAppearanceChooseDescriptionViewModel.Factory
    override val vm by viewModels<ObjectAppearanceChooseDescriptionViewModel> { factory }
    override val title: Int = R.string.description

    override fun injectDependencies() {
        componentManager()
            .objectAppearanceChooseDescriptionComponent
            .get(params = DefaultComponentParam(ctx = ctx, space = SpaceId(space)))
            .inject(this)
    }

    override fun releaseDependencies() {
        componentManager().objectAppearanceChooseDescriptionComponent.release()
    }

    companion object {
        fun new(
            ctx: Id,
            space: Id,
            block: Id
        ) = ObjectAppearanceChooseDescriptionFragment().apply {
            arguments = bundleOf(
                CONTEXT_ID_KEY to ctx,
                SPACE_KEY to space,
                BLOCK_ID_KEY to block
            )
        }
    }
}