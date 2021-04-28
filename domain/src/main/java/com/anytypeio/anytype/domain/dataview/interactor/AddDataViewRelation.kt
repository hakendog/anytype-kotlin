package com.anytypeio.anytype.domain.dataview.interactor

import com.anytypeio.anytype.core_models.Id
import com.anytypeio.anytype.core_models.Payload
import com.anytypeio.anytype.core_models.Relation
import com.anytypeio.anytype.domain.base.BaseUseCase
import com.anytypeio.anytype.domain.block.repo.BlockRepository
import com.anytypeio.anytype.domain.dataview.interactor.AddDataViewRelation.Params

class AddDataViewRelation(
    private val repo: BlockRepository
) : BaseUseCase<Pair<Id, Payload>, Params>() {

    override suspend fun run(params: Params) = safe {
        repo.addDataViewRelation(
            context = params.context,
            target = params.target,
            name = params.name,
            format = params.format
        )
    }

    /**
     * @property [context] operation context
     * @property [target] data view's block id
     * @property [name] name for the new relation
     * @property [format] relation's format
     */
    class Params(
        val context: Id,
        val target: Id,
        val name: String,
        val format: Relation.Format
    )
}