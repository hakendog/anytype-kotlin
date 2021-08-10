package com.anytypeio.anytype.core_ui.features.editor

import android.content.Context
import android.text.Editable
import androidx.test.core.app.ApplicationProvider
import com.anytypeio.anytype.core_ui.tools.ClipboardInterceptor
import com.anytypeio.anytype.presentation.editor.editor.model.BlockView
import java.util.*

open class BlockAdapterTestSetup {

    val clipboardInterceptor: ClipboardInterceptor = object: ClipboardInterceptor {
        override fun onClipboardAction(action: ClipboardInterceptor.Action) {}
    }

    val context: Context = ApplicationProvider.getApplicationContext()

    fun buildAdapter(
        views: List<BlockView>,
        onFocusChanged: (String, Boolean) -> Unit = { _, _ -> },
        onTitleTextChanged: (Editable) -> Unit = {},
        onTextBlockTextChanged: (BlockView.Text) -> Unit = {},
        onTextInputClicked: (String) -> Unit = {},
        onSplitLineEnterClicked: (String, Editable, IntRange) -> Unit = { _, _, _ ->},
        onTextChanged: (String, Editable) -> Unit = { _, _ -> },
        onToggleClicked: (String) -> Unit = {},
        onEmptyBlockBackspaceClicked: (String) -> Unit = {},
        onNonEmptyBlockBackspaceClicked: (String, Editable) -> Unit = { _, _ -> },
        onCheckboxClicked: (BlockView.Text.Checkbox) -> Unit = {}
    ): BlockAdapter {
        return BlockAdapter(
            restore = LinkedList(),
            blocks = views,
            onNonEmptyBlockBackspaceClicked = onNonEmptyBlockBackspaceClicked,
            onEmptyBlockBackspaceClicked = onEmptyBlockBackspaceClicked,
            onSplitLineEnterClicked = onSplitLineEnterClicked,
            onSplitDescription = { _, _, _ -> },
            onTextChanged = onTextChanged,
            onCheckboxClicked = onCheckboxClicked,
            onFocusChanged = onFocusChanged,
            onSelectionChanged = { _, _ -> },
            onTextInputClicked = onTextInputClicked,
            onPageIconClicked = {},
            onProfileIconClicked = {},
            onTogglePlaceholderClicked = {},
            onToggleClicked = onToggleClicked,
            onTextBlockTextChanged = onTextBlockTextChanged,
            onTitleBlockTextChanged = {},
            onContextMenuStyleClick = {},
            onTitleTextInputClicked = {},
            onClickListener = {},
            clipboardInterceptor = clipboardInterceptor,
            onMentionEvent = {},
            onBackPressedCallback = { false },
            onCoverClicked = {},
            onSlashEvent = {}
        )
    }
}