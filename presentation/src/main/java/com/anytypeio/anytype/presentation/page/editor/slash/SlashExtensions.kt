package com.anytypeio.anytype.presentation.page.editor.slash

import com.anytypeio.anytype.core_models.ObjectType
import com.anytypeio.anytype.presentation.page.editor.model.BlockView
import com.anytypeio.anytype.presentation.page.editor.model.UiBlock

fun List<ObjectType>.toView(): List<SlashItem.ObjectType> = map { oType ->
    SlashItem.ObjectType(
        url = oType.url,
        name = oType.name,
        emoji = oType.emoji,
        description = oType.description
    )
}

fun SlashItem.Style.Type.convertToUiBlock() = when (this) {
    SlashItem.Style.Type.Bulleted -> UiBlock.BULLETED
    SlashItem.Style.Type.Callout -> TODO()
    SlashItem.Style.Type.Checkbox -> UiBlock.CHECKBOX
    SlashItem.Style.Type.Heading -> UiBlock.HEADER_TWO
    SlashItem.Style.Type.Highlighted -> UiBlock.HIGHLIGHTED
    SlashItem.Style.Type.Numbered -> UiBlock.NUMBERED
    SlashItem.Style.Type.Subheading -> UiBlock.HEADER_THREE
    SlashItem.Style.Type.Text -> UiBlock.TEXT
    SlashItem.Style.Type.Title -> UiBlock.HEADER_ONE
    SlashItem.Style.Type.Toggle -> UiBlock.TOGGLE
}

object SlashExtensions {

    fun getSlashMainItems() = listOf(
        SlashItem.Main.Style,
        SlashItem.Main.Media,
        SlashItem.Main.Objects,
        SlashItem.Main.Relations,
        SlashItem.Main.Other,
        SlashItem.Main.Actions,
        SlashItem.Main.Alignment,
        SlashItem.Main.Color,
        SlashItem.Main.Background,
    )

    fun getSlashItems() = listOf(
        SlashItem.Style.Type.Text,
        SlashItem.Style.Type.Title,
        SlashItem.Style.Type.Heading,
        SlashItem.Style.Type.Subheading,
        SlashItem.Style.Type.Highlighted,
        SlashItem.Style.Type.Callout,
        SlashItem.Style.Type.Checkbox,
        SlashItem.Style.Type.Bulleted,
        SlashItem.Style.Type.Numbered,
        SlashItem.Style.Type.Toggle,
        SlashItem.Style.Markup.Bold,
        SlashItem.Style.Markup.Italic,
        SlashItem.Style.Markup.Breakthrough,
        SlashItem.Style.Markup.Code
    )

    fun getMediaItems() = listOf(
        SlashItem.Media.File,
        SlashItem.Media.Picture,
        SlashItem.Media.Video,
        SlashItem.Media.Bookmark,
        SlashItem.Media.Code
    )

    fun getOtherItems() = listOf(
        SlashItem.Other.Line,
        SlashItem.Other.Dots
    )
}