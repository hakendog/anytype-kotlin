package com.agileburo.anytype.data.auth.repo.block

import com.agileburo.anytype.data.auth.model.BlockEntity
import com.agileburo.anytype.data.auth.model.CommandEntity
import com.agileburo.anytype.data.auth.model.ConfigEntity
import com.agileburo.anytype.data.auth.model.EventEntity
import kotlinx.coroutines.flow.Flow

interface BlockRemote {
    suspend fun create(command: CommandEntity.Create)
    suspend fun update(update: CommandEntity.Update)
    suspend fun dnd(command: CommandEntity.Dnd)
    suspend fun getConfig(): ConfigEntity
    suspend fun createPage(parentId: String): String
    suspend fun openPage(id: String)
    suspend fun closePage(id: String)

    fun observeBlocks(): Flow<List<BlockEntity>>
    fun observeEvents(): Flow<EventEntity>
    fun observePages(): Flow<List<BlockEntity>>

    suspend fun openDashboard(contextId: String, id: String)
    suspend fun closeDashboard(id: String)
}