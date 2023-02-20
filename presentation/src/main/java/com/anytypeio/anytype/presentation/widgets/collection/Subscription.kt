package com.anytypeio.anytype.presentation.widgets.collection

import com.anytypeio.anytype.core_models.DVFilter
import com.anytypeio.anytype.core_models.DVSort
import com.anytypeio.anytype.core_models.Id
import com.anytypeio.anytype.core_models.Relations
import com.anytypeio.anytype.presentation.dashboard.DEFAULT_KEYS
import com.anytypeio.anytype.presentation.search.ObjectSearchConstants
import com.anytypeio.anytype.presentation.search.Subscriptions

sealed class Subscription(
    val id: Id,
    val keys: List<String>,
    val sorts: List<DVSort>,
    val limit: Int,
    val filters: (String) -> List<DVFilter>
) {
    object Recent : Subscription(
        Subscriptions.SUBSCRIPTION_RECENT,
        DEFAULT_KEYS + Relations.LAST_MODIFIED_DATE,
        ObjectSearchConstants.sortTabRecent,
        ObjectSearchConstants.limitTabRecent,
        filters = { workspaceId -> ObjectSearchConstants.filterTabRecent(workspaceId) }
    )

    object Bin : Subscription(
        Subscriptions.SUBSCRIPTION_ARCHIVED,
        DEFAULT_KEYS,
        ObjectSearchConstants.sortTabArchive,
        0,
        filters = { workspaceId -> ObjectSearchConstants.filterTabArchive(workspaceId) }
    )

    object Sets : Subscription(
        Subscriptions.SUBSCRIPTION_SETS,
        DEFAULT_KEYS,
        ObjectSearchConstants.sortTabSets,
        0,
        filters = { workspaceId -> ObjectSearchConstants.filterTabSets(workspaceId) }
    )

    object Favorites : Subscription(
        Subscriptions.SUBSCRIPTION_FAVORITES,
        DEFAULT_KEYS + Relations.LAST_MODIFIED_DATE,
        emptyList(),
        0,
        filters = { workspaceId -> ObjectSearchConstants.filterTabFavorites(workspaceId) }
    )

    object None : Subscription("", emptyList(), emptyList(), 0, filters = { emptyList() })
}