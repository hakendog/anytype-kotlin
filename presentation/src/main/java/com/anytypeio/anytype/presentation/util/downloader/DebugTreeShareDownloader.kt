package com.anytypeio.anytype.presentation.util.downloader

import android.content.Context
import com.anytypeio.anytype.domain.block.repo.BlockRepository

class DebugTreeShareDownloader(
    private val repo: BlockRepository,
    context: Context,
    uriFileProvider: UriFileProvider
) : MiddlewareShareDownloader(context, uriFileProvider) {

    override suspend fun downloadFile(hash: String, path: String) =
        repo.debugTree(objectId = hash, path = path)
}