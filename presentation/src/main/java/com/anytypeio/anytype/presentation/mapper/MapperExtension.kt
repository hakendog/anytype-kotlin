package com.anytypeio.anytype.presentation.mapper

import com.anytypeio.anytype.core_models.*
import com.anytypeio.anytype.domain.config.DebugSettings
import com.anytypeio.anytype.domain.misc.UrlBuilder
import com.anytypeio.anytype.presentation.desktop.DashboardView
import com.anytypeio.anytype.presentation.navigation.PageLinkView
import com.anytypeio.anytype.presentation.page.editor.Markup
import com.anytypeio.anytype.presentation.page.editor.mention.Mention
import com.anytypeio.anytype.presentation.page.editor.model.Alignment
import com.anytypeio.anytype.presentation.page.editor.model.BlockView
import com.anytypeio.anytype.presentation.page.editor.model.UiBlock
import com.anytypeio.anytype.presentation.sets.buildGridRow
import com.anytypeio.anytype.presentation.sets.model.*
import com.anytypeio.anytype.presentation.settings.EditorSettings
import timber.log.Timber

fun Block.Content.File.toPictureView(
    id: String,
    urlBuilder: UrlBuilder,
    indent: Int,
    mode: BlockView.Mode
): BlockView = when (state) {
    Block.Content.File.State.EMPTY -> BlockView.MediaPlaceholder.Picture(
        id = id,
        indent = indent,
        mode = mode
    )
    Block.Content.File.State.UPLOADING -> BlockView.Upload.Picture(
        id = id,
        indent = indent,
        mode = mode
    )
    Block.Content.File.State.DONE -> BlockView.Media.Picture(
        id = id,
        size = size,
        name = name,
        mime = mime,
        hash = hash,
        url = urlBuilder.image(hash),
        indent = indent,
        mode = mode
    )
    Block.Content.File.State.ERROR -> BlockView.Error.Picture(
        id = id,
        indent = indent,
        mode = mode
    )
    else -> throw IllegalStateException("Unexpected state: $state")
}

fun Block.Content.File.toVideoView(
    id: String,
    urlBuilder: UrlBuilder,
    indent: Int,
    mode: BlockView.Mode
): BlockView = when (state) {
    Block.Content.File.State.EMPTY -> BlockView.MediaPlaceholder.Video(
        id = id,
        indent = indent,
        mode = mode
    )
    Block.Content.File.State.UPLOADING -> BlockView.Upload.Video(
        id = id,
        indent = indent,
        mode = mode
    )
    Block.Content.File.State.DONE -> BlockView.Media.Video(
        id = id,
        size = size,
        name = name,
        mime = mime,
        hash = hash,
        url = urlBuilder.video(hash),
        indent = indent,
        mode = mode
    )
    Block.Content.File.State.ERROR -> BlockView.Error.Video(
        id = id,
        indent = indent,
        mode = mode
    )
    else -> throw IllegalStateException("Unexpected state: $state")
}

fun Block.Content.File.toFileView(
    id: String,
    urlBuilder: UrlBuilder,
    indent: Int,
    mode: BlockView.Mode
): BlockView = when (state) {
    Block.Content.File.State.EMPTY -> BlockView.MediaPlaceholder.File(
        id = id,
        indent = indent,
        mode = mode
    )
    Block.Content.File.State.UPLOADING -> BlockView.Upload.File(
        id = id,
        indent = indent,
        mode = mode
    )
    Block.Content.File.State.DONE -> BlockView.Media.File(
        id = id,
        size = size,
        name = name,
        mime = mime,
        hash = hash,
        url = urlBuilder.video(hash),
        indent = indent,
        mode = mode
    )
    Block.Content.File.State.ERROR -> BlockView.Error.File(
        id = id,
        indent = indent,
        mode = mode
    )
    else -> throw IllegalStateException("Unexpected state: $state")
}

fun Block.Align.toView(): Alignment = when (this) {
    Block.Align.AlignCenter -> Alignment.CENTER
    Block.Align.AlignLeft -> Alignment.START
    Block.Align.AlignRight -> Alignment.END
    else -> TODO()
}

fun Block.Content.Text.marks(
    urlBuilder: UrlBuilder? = null,
    details: Block.Details? = null
): List<Markup.Mark> = marks
    .filterByRange(text.length)
    .mapNotNull { mark ->
        when (mark.type) {
            Block.Content.Text.Mark.Type.ITALIC -> {
                Markup.Mark(
                    from = mark.range.first,
                    to = mark.range.last,
                    type = Markup.Type.ITALIC
                )
            }
            Block.Content.Text.Mark.Type.BOLD -> {
                Markup.Mark(
                    from = mark.range.first,
                    to = mark.range.last,
                    type = Markup.Type.BOLD
                )
            }
            Block.Content.Text.Mark.Type.STRIKETHROUGH -> {
                Markup.Mark(
                    from = mark.range.first,
                    to = mark.range.last,
                    type = Markup.Type.STRIKETHROUGH
                )
            }
            Block.Content.Text.Mark.Type.TEXT_COLOR -> {
                try {
                    Markup.Mark(
                        from = mark.range.first,
                        to = mark.range.last,
                        type = Markup.Type.TEXT_COLOR,
                        param = checkNotNull(mark.param)
                    )
                } catch (e: Exception) {
                    Timber.e(e, "Could not parse param from ${mark.type}")
                    null
                }
            }
            Block.Content.Text.Mark.Type.LINK -> {
                try {
                    Markup.Mark(
                        from = mark.range.first,
                        to = mark.range.last,
                        type = Markup.Type.LINK,
                        param = checkNotNull(mark.param)
                    )
                } catch (e: Exception) {
                    Timber.e(e, "Could not parse param from ${mark.type}")
                    null
                }
            }
            Block.Content.Text.Mark.Type.BACKGROUND_COLOR -> {
                try {
                    Markup.Mark(
                        from = mark.range.first,
                        to = mark.range.last,
                        type = Markup.Type.BACKGROUND_COLOR,
                        param = checkNotNull(mark.param)
                    )
                } catch (e: Exception) {
                    Timber.e(e, "Could not parse param from ${mark.type}")
                    null
                }
            }
            Block.Content.Text.Mark.Type.KEYBOARD -> {
                Markup.Mark(
                    from = mark.range.first,
                    to = mark.range.last,
                    type = Markup.Type.KEYBOARD
                )
            }
            Block.Content.Text.Mark.Type.MENTION -> {

                val emoji: String?
                val image: String?

                var isLoading = Markup.Mark.IS_NOT_LOADING_VALUE

                if (details != null) {
                    emoji = details.details[mark.param]?.iconEmoji?.let { icon ->
                        if (icon.isEmpty()) null else icon
                    }
                    image = details.details[mark.param]?.iconImage?.let { icon ->
                        if (icon.isEmpty()) null else icon
                    }

                    if (!details.details.containsKey(mark.param))
                        isLoading = Markup.Mark.IS_LOADING_VALUE

                } else {
                    emoji = null
                    image = null
                    isLoading = Markup.Mark.IS_LOADING_VALUE
                }

                Markup.Mark(
                    from = mark.range.first,
                    to = mark.range.last,
                    type = Markup.Type.MENTION,
                    param = mark.param,
                    extras = mapOf(
                        "image" to image?.let { urlBuilder?.thumbnail(it) },
                        "emoji" to emoji,
                        "isLoading" to isLoading
                    )
                )
            }
            else -> null
        }
    }

fun List<Block.Content.Text.Mark>.filterByRange(textLength: Int): List<Block.Content.Text.Mark> {
    return this.mapNotNull { mark ->
        when {
            mark.range.first >= textLength -> null
            mark.range.first == mark.range.last -> null
            mark.range.last < 0 -> null
            else -> {
                var result = mark
                if (result.range.first < 0) {
                    result = result.copy(range = 0..result.range.last)
                }
                if (mark.range.last > textLength) {
                    result = result.copy(range = result.range.first..textLength)
                }
                if (result.range.first > mark.range.last) {
                    val from = result.range.first
                    val to = mark.range.last
                    result = result.copy(range = to..from)
                }
                return@mapNotNull result
            }
        }
    }
}

fun List<Block>.toDashboardViews(
    details: Block.Details = Block.Details(),
    builder: UrlBuilder,
    objectTypes: List<ObjectType> = emptyList(),
    objectTypePerObject: Map<String, String> = emptyMap()
): List<DashboardView> = this.mapNotNull { block ->
    when (val content = block.content) {
        is Block.Content.Smart -> {
            when (content.type) {
                SmartBlockType.PROFILE_PAGE -> {
                    DashboardView.Profile(
                        id = block.id,
                        name = details.details[block.id]?.name.orEmpty(),
                        avatar = details.details[block.id]?.iconImage.let {
                            if (it.isNullOrEmpty()) null
                            else builder.image(it)
                        }
                    )
                }
                else -> null
            }
        }
        is Block.Content.Link -> {
            val type = objectTypePerObject[content.target]
            if (type != null) {
                val value = objectTypes.find { it.url == type }
                if (value != null) {
                    when (value.layout) {
                        ObjectType.Layout.PAGE -> content.toPageView(block.id, details, builder)
                        ObjectType.Layout.SET -> content.toSetView(block.id, details)
                        else -> null
                    }
                } else {
                    when (content.type) {
                        Block.Content.Link.Type.PAGE -> content.toPageView(
                            block.id,
                            details,
                            builder
                        )
                        Block.Content.Link.Type.DATA_VIEW -> content.toSetView(block.id, details)
                        Block.Content.Link.Type.ARCHIVE -> content.toArchiveView(block.id, details)
                        else -> null
                    }
                }
            } else {
                when (content.type) {
                    Block.Content.Link.Type.PAGE -> content.toPageView(block.id, details, builder)
                    Block.Content.Link.Type.DATA_VIEW -> content.toSetView(block.id, details)
                    Block.Content.Link.Type.ARCHIVE -> content.toArchiveView(block.id, details)
                    else -> null
                }
            }
        }
        else -> null
    }
}

fun Block.Content.Link.toArchiveView(
    id: String,
    details: Block.Details
): DashboardView.Archive? {
    return DashboardView.Archive(
        id = id,
        target = target,
        title = details.details[target]?.name.orEmpty()
    )
}

fun Block.Content.Link.toPageView(
    id: String,
    details: Block.Details,
    builder: UrlBuilder
): DashboardView.Document {
    return DashboardView.Document(
        id = id,
        target = target,
        title = details.details[target]?.name,
        emoji = details.details[target]?.iconEmoji?.let { name ->
            if (name.isNotEmpty()) name else null
        },
        image = details.details[target]?.iconImage?.let { name ->
            if (name.isNotEmpty()) builder.image(name) else null
        },
        isArchived = details.details[target]?.isArchived ?: false,
        isLoading = !details.details.containsKey(target)
    )
}

fun Block.Content.Link.toSetView(
    id: String,
    details: Block.Details,
): DashboardView.ObjectSet {
    return DashboardView.ObjectSet(
        id = id,
        target = target,
        title = details.details[target]?.name,
        emoji = details.details[target]?.iconEmoji?.let { name ->
            if (name.isNotEmpty()) name else null
        },
        isArchived = details.details[target]?.isArchived ?: false
    )
}

fun UiBlock.style(): Block.Content.Text.Style = when (this) {
    UiBlock.TEXT -> Block.Content.Text.Style.P
    UiBlock.HEADER_ONE -> Block.Content.Text.Style.H1
    UiBlock.HEADER_TWO -> Block.Content.Text.Style.H2
    UiBlock.HEADER_THREE -> Block.Content.Text.Style.H3
    UiBlock.HIGHLIGHTED -> Block.Content.Text.Style.QUOTE
    UiBlock.CHECKBOX -> Block.Content.Text.Style.CHECKBOX
    UiBlock.BULLETED -> Block.Content.Text.Style.BULLET
    UiBlock.NUMBERED -> Block.Content.Text.Style.NUMBERED
    UiBlock.TOGGLE -> Block.Content.Text.Style.TOGGLE
    UiBlock.CODE -> Block.Content.Text.Style.CODE_SNIPPET
    else -> throw IllegalStateException("Could not extract style from block: $this")
}

fun DebugSettings.toView(): EditorSettings =
    EditorSettings(customContextMenu = this.isAnytypeContextMenuEnabled)

fun DocumentInfo.toView(urlBuilder: UrlBuilder): PageLinkView = PageLinkView(
    id = id,
    title = fields.name.orEmpty(),
    subtitle = snippet.orEmpty(),
    image = fields.toImageView(urlBuilder),
    emoji = fields.toEmojiView()
)

fun Block.Fields.toImageView(urlBuilder: UrlBuilder): String? = this.iconImage.let { url ->
    if (url.isNullOrBlank()) null else urlBuilder.image(url)
}

fun Block.Fields.toEmojiView(): String? = this.iconEmoji.let { emoji ->
    if (emoji.isNullOrBlank()) null else emoji
}

fun DocumentInfo.toMentionView(urlBuilder: UrlBuilder) = Mention(
    id = id,
    title = fields.getName(),
    image = fields.iconImage?.let { if (it.isNotEmpty()) urlBuilder.thumbnail(it) else null },
    emoji = fields.iconEmoji
)

fun Block.Fields.getName(): String =
    this.name.let { name ->
        if (name.isNullOrBlank()) "Untitled" else name
    }

fun Markup.Mark.mark(): Block.Content.Text.Mark = when (type) {
    Markup.Type.BOLD -> Block.Content.Text.Mark(
        range = from..to,
        type = Block.Content.Text.Mark.Type.BOLD
    )
    Markup.Type.ITALIC -> Block.Content.Text.Mark(
        range = from..to,
        type = Block.Content.Text.Mark.Type.ITALIC
    )
    Markup.Type.STRIKETHROUGH -> Block.Content.Text.Mark(
        range = from..to,
        type = Block.Content.Text.Mark.Type.STRIKETHROUGH
    )
    Markup.Type.TEXT_COLOR -> Block.Content.Text.Mark(
        range = from..to,
        type = Block.Content.Text.Mark.Type.TEXT_COLOR,
        param = param
    )
    Markup.Type.BACKGROUND_COLOR -> Block.Content.Text.Mark(
        range = from..to,
        type = Block.Content.Text.Mark.Type.BACKGROUND_COLOR,
        param = param
    )
    Markup.Type.LINK -> Block.Content.Text.Mark(
        range = from..to,
        type = Block.Content.Text.Mark.Type.LINK,
        param = param
    )
    Markup.Type.KEYBOARD -> Block.Content.Text.Mark(
        range = from..to,
        type = Block.Content.Text.Mark.Type.KEYBOARD
    )
    Markup.Type.MENTION -> Block.Content.Text.Mark(
        range = from..to,
        type = Block.Content.Text.Mark.Type.MENTION,
        param = param
    )
}

fun Block.Content.DataView.Sort.Type.toView(): Viewer.SortType = when (this) {
    Block.Content.DataView.Sort.Type.ASC -> Viewer.SortType.ASC
    Block.Content.DataView.Sort.Type.DESC -> Viewer.SortType.DESC
}

fun DVFilterOperator.toView(): Viewer.FilterOperator = when (this) {
    Block.Content.DataView.Filter.Operator.AND -> Viewer.FilterOperator.And
    Block.Content.DataView.Filter.Operator.OR -> Viewer.FilterOperator.Or
}

fun DVFilterCondition.toTextView(): Viewer.Filter.Condition.Text = when (this) {
    DVFilterCondition.EQUAL -> Viewer.Filter.Condition.Text.Equal()
    DVFilterCondition.NOT_EQUAL -> Viewer.Filter.Condition.Text.NotEqual()
    DVFilterCondition.LIKE -> Viewer.Filter.Condition.Text.Like()
    DVFilterCondition.NOT_LIKE -> Viewer.Filter.Condition.Text.NotLike()
    DVFilterCondition.EMPTY -> Viewer.Filter.Condition.Text.Empty()
    DVFilterCondition.NOT_EMPTY -> Viewer.Filter.Condition.Text.NotEmpty()
    else ->  throw IllegalStateException("Unexpected filter condition $this for Text relations")
}

fun DVFilterCondition.toNumberView(): Viewer.Filter.Condition.Number = when (this) {
    DVFilterCondition.EQUAL -> Viewer.Filter.Condition.Number.Equal()
    DVFilterCondition.NOT_EQUAL -> Viewer.Filter.Condition.Number.NotEqual()
    DVFilterCondition.GREATER -> Viewer.Filter.Condition.Number.Greater()
    DVFilterCondition.LESS -> Viewer.Filter.Condition.Number.Less()
    DVFilterCondition.GREATER_OR_EQUAL -> Viewer.Filter.Condition.Number.GreaterOrEqual()
    DVFilterCondition.LESS_OR_EQUAL -> Viewer.Filter.Condition.Number.LessOrEqual()
    else ->  throw IllegalStateException("Unexpected filter condition $this for Number or Date relations")
}

fun DVFilterCondition.toSelectedView(): Viewer.Filter.Condition.Selected = when (this) {
    DVFilterCondition.IN -> Viewer.Filter.Condition.Selected.In()
    DVFilterCondition.ALL_IN -> Viewer.Filter.Condition.Selected.AllIn()
    DVFilterCondition.EQUAL -> Viewer.Filter.Condition.Selected.Equal()
    DVFilterCondition.NOT_IN -> Viewer.Filter.Condition.Selected.NotIn()
    DVFilterCondition.EMPTY -> Viewer.Filter.Condition.Selected.Empty()
    DVFilterCondition.NOT_EMPTY -> Viewer.Filter.Condition.Selected.NotEmpty()
    else ->  throw IllegalStateException("Unexpected filter condition $this for Selected relations")
}

fun DVFilterCondition.toCheckboxView(): Viewer.Filter.Condition.Checkbox = when (this) {
    DVFilterCondition.EQUAL -> Viewer.Filter.Condition.Checkbox.Equal()
    DVFilterCondition.NOT_EQUAL -> Viewer.Filter.Condition.Checkbox.NotEqual()
    else ->  throw IllegalStateException("Unexpected filter condition $this for Checkbox relations")
}

fun SortingExpression.toDomain(): DVSort = DVSort(
    relationKey = key,
    type = when (type) {
        Viewer.SortType.ASC -> Block.Content.DataView.Sort.Type.ASC
        Viewer.SortType.DESC -> Block.Content.DataView.Sort.Type.DESC
    }
)

fun FilterExpression.toDomain(): DVFilter = DVFilter(
    relationKey = key,
    operator = operator.toDomain(),
    condition = condition.toDomain(),
    value = when (value) {
        is FilterValue.Number -> value.value
        is FilterValue.Status -> value.value
        is FilterValue.Tag -> value.value
        is FilterValue.Text -> value.value
        is FilterValue.Url -> value.value
        is FilterValue.Email -> value.value
        is FilterValue.Phone -> value.value
        is FilterValue.Date -> value.value
        is FilterValue.TextShort -> value.value
        is FilterValue.Check -> value.value
        is FilterValue.Object -> value.value
        null -> null
    }
)

fun Viewer.FilterOperator.toDomain(): DVFilterOperator = when (this) {
    Viewer.FilterOperator.And -> DVFilterOperator.AND
    Viewer.FilterOperator.Or -> DVFilterOperator.OR
}

fun Viewer.Filter.Condition.toDomain(): DVFilterCondition = when (this) {
    is Viewer.Filter.Condition.Checkbox.Equal -> DVFilterCondition.EQUAL
    is Viewer.Filter.Condition.Checkbox.NotEqual -> DVFilterCondition.NOT_EQUAL
    is Viewer.Filter.Condition.Number.Equal -> DVFilterCondition.EQUAL
    is Viewer.Filter.Condition.Number.Greater -> DVFilterCondition.GREATER
    is Viewer.Filter.Condition.Number.GreaterOrEqual -> DVFilterCondition.GREATER_OR_EQUAL
    is Viewer.Filter.Condition.Number.Less -> DVFilterCondition.LESS
    is Viewer.Filter.Condition.Number.LessOrEqual -> DVFilterCondition.LESS_OR_EQUAL
    is Viewer.Filter.Condition.Number.NotEqual -> DVFilterCondition.NOT_EQUAL
    is Viewer.Filter.Condition.Selected.AllIn -> DVFilterCondition.ALL_IN
    is Viewer.Filter.Condition.Selected.Empty -> DVFilterCondition.EMPTY
    is Viewer.Filter.Condition.Selected.Equal -> DVFilterCondition.EQUAL
    is Viewer.Filter.Condition.Selected.In -> DVFilterCondition.IN
    is Viewer.Filter.Condition.Selected.NotEmpty -> DVFilterCondition.NOT_EMPTY
    is Viewer.Filter.Condition.Selected.NotIn -> DVFilterCondition.NOT_IN
    is Viewer.Filter.Condition.Text.Empty -> DVFilterCondition.EMPTY
    is Viewer.Filter.Condition.Text.Equal -> DVFilterCondition.EQUAL
    is Viewer.Filter.Condition.Text.Like -> DVFilterCondition.LIKE
    is Viewer.Filter.Condition.Text.NotEmpty -> DVFilterCondition.NOT_EMPTY
    is Viewer.Filter.Condition.Text.NotEqual -> DVFilterCondition.NOT_EQUAL
    is Viewer.Filter.Condition.Text.NotLike -> DVFilterCondition.NOT_LIKE
}

fun List<Map<String, Any?>>.filterRecordsBy(filterBy: String): List<Map<String, Any?>> =
    filter { it.containsKey(filterBy) }

fun List<Map<String, Any?>>.toGridRecordRows(
    columns: List<ColumnView>,
    relations: List<Relation>,
    details: Map<Id, Block.Fields>,
    builder: UrlBuilder
): List<Viewer.GridView.Row> {
    val rows = mutableListOf<Viewer.GridView.Row>()
    forEach { record ->
        val row = columns.buildGridRow(record, relations, details, builder)
        rows.add(row)
    }
    return rows
}

// TODO maybe rename toViewerHeaders
fun List<Block.Content.DataView.Viewer.ViewerRelation>.toViewerColumns(
    relations: List<Relation>,
    filterBy: List<String>
): List<ColumnView> {
    val columns = mutableListOf<ColumnView>()
    this.filter { it.key !in filterBy }
        .forEach { viewerRelation ->
            relations
                .firstOrNull { it.key == viewerRelation.key }
                ?.let { relation ->
                    columns.add(
                        ColumnView(
                            key = relation.key,
                            text = relation.name,
                            format = relation.format.toView(),
                            width = viewerRelation.width ?: 0,
                            isVisible = viewerRelation.isVisible,
                            isHidden = relation.isHidden,
                            isReadOnly = relation.isReadOnly,
                            isDateIncludeTime = viewerRelation.isDateIncludeTime,
                            dateFormat = viewerRelation.dateFormat,
                            timeFormat = viewerRelation.timeFormat
                        )
                    )
                }
        }
    return columns
}

fun List<Block.Content.DataView.Viewer.ViewerRelation>.toSimpleRelations(
    relations: List<Relation>
): ArrayList<SimpleRelationView> {
    val result = arrayListOf<SimpleRelationView>()
    this.forEach { viewerRelation ->
        relations
            .firstOrNull { it.key == viewerRelation.key }
            ?.let { relation ->
                result.add(
                    SimpleRelationView(
                        key = relation.key,
                        title = relation.name,
                        format = relation.format.toView(),
                        isVisible = viewerRelation.isVisible,
                        isHidden = relation.isHidden
                    )
                )
            }
    }
    return result
}

fun Relation.Format.toView() = when (this) {
    Relation.Format.SHORT_TEXT -> ColumnView.Format.SHORT_TEXT
    Relation.Format.LONG_TEXT -> ColumnView.Format.LONG_TEXT
    Relation.Format.NUMBER -> ColumnView.Format.NUMBER
    Relation.Format.STATUS -> ColumnView.Format.STATUS
    Relation.Format.DATE -> ColumnView.Format.DATE
    Relation.Format.FILE -> ColumnView.Format.FILE
    Relation.Format.CHECKBOX -> ColumnView.Format.CHECKBOX
    Relation.Format.URL -> ColumnView.Format.URL
    Relation.Format.EMAIL -> ColumnView.Format.EMAIL
    Relation.Format.PHONE -> ColumnView.Format.PHONE
    Relation.Format.EMOJI -> ColumnView.Format.EMOJI
    Relation.Format.OBJECT -> ColumnView.Format.OBJECT
    Relation.Format.TAG -> ColumnView.Format.TAG
    Relation.Format.RELATIONS -> ColumnView.Format.RELATIONS
}