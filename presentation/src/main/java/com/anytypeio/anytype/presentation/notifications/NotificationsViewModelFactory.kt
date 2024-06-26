package com.anytypeio.anytype.presentation.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anytypeio.anytype.analytics.base.Analytics
import com.anytypeio.anytype.domain.multiplayer.GetSpaceMemberByIdentity
import com.anytypeio.anytype.domain.notifications.ReplyNotifications
import com.anytypeio.anytype.domain.spaces.SaveCurrentSpace
import com.anytypeio.anytype.domain.workspace.SpaceManager
import javax.inject.Inject

class NotificationsViewModelFactory @Inject constructor(
    private val analytics: Analytics,
    private val notificationsProvider: NotificationsProvider,
    private val spaceManager: SpaceManager,
    private val saveCurrentSpace: SaveCurrentSpace,
    private val getSpaceMemberByIdentity: GetSpaceMemberByIdentity,
    private val replyNotifications: ReplyNotifications,
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NotificationsViewModel(
            analytics = analytics,
            notificationsProvider = notificationsProvider,
            spaceManager = spaceManager,
            saveCurrentSpace = saveCurrentSpace,
            getSpaceMemberByIdentity = getSpaceMemberByIdentity,
            replyNotifications = replyNotifications,
        ) as T
    }
}