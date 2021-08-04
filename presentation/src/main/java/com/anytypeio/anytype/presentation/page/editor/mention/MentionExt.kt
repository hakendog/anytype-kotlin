package com.anytypeio.anytype.presentation.page.editor.mention

import com.anytypeio.anytype.presentation.navigation.DefaultObjectView
import com.anytypeio.anytype.presentation.page.editor.mention.MentionConst.MENTION_PREFIX

fun String.getMentionName(untitled: String): String = if (this.isBlank()) untitled else this

/**
 * Filter all mentions by text without symbol @
 *
 */
fun List<DefaultObjectView>.filterMentionsBy(text: String): List<DefaultObjectView> {
    val filter = text.removePrefix(MENTION_PREFIX)
    return if (filter.isNotEmpty()) this.filter {
        it.name.contains(
            filter,
            ignoreCase = true
        )
    } else this
}

object MentionConst {
    const val MENTION_PREFIX = "@"
    const val MENTION_TITLE_EMPTY = "Untitled"
}