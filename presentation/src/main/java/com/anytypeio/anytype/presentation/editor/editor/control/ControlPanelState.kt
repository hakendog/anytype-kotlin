package com.anytypeio.anytype.presentation.editor.editor.control

import com.anytypeio.anytype.core_models.TextStyle
import com.anytypeio.anytype.presentation.editor.editor.Markup
import com.anytypeio.anytype.presentation.editor.editor.model.Alignment
import com.anytypeio.anytype.presentation.editor.editor.slash.SlashWidgetState
import com.anytypeio.anytype.presentation.editor.editor.styling.StyleConfig
import com.anytypeio.anytype.presentation.editor.editor.styling.StylingMode
import com.anytypeio.anytype.presentation.editor.markup.MarkupStyleDescriptor
import com.anytypeio.anytype.presentation.navigation.DefaultObjectView
import com.anytypeio.anytype.presentation.objects.ObjectTypeView

/**
 * Control panels are UI-elements that allow user to interact with blocks on a page.
 * Each panel is currently represented as a toolbar.
 * @property mainToolbar block-toolbar state (main toolbar state)
 * @property stylingToolbar styling toolbar state
 */
data class ControlPanelState(
    val navigationToolbar: Toolbar.Navigation = Toolbar.Navigation.reset(),
    val mainToolbar: Toolbar.Main = Toolbar.Main.reset(),
    val stylingToolbar: Toolbar.Styling = Toolbar.Styling.reset(),
    val styleExtraToolbar: Toolbar.Styling.Other = Toolbar.Styling.Other.reset(),
    val styleColorToolbar: Toolbar.Styling.Color = Toolbar.Styling.Color.reset(),
    val styleBackgroundToolbar: Toolbar.Styling.Background = Toolbar.Styling.Background.reset(),
    val markupMainToolbar: Toolbar.MarkupMainToolbar = Toolbar.MarkupMainToolbar.reset(),
    val markupColorToolbar: Toolbar.MarkupColorToolbar = Toolbar.MarkupColorToolbar.reset(),
    val multiSelect: Toolbar.MultiSelect = Toolbar.MultiSelect.reset(),
    val mentionToolbar: Toolbar.MentionToolbar = Toolbar.MentionToolbar.reset(),
    val slashWidget: Toolbar.SlashWidget = Toolbar.SlashWidget.reset(),
    val searchToolbar: Toolbar.SearchToolbar = Toolbar.SearchToolbar.reset(),
    val objectTypesToolbar: Toolbar.ObjectTypes = Toolbar.ObjectTypes.reset()
) {

    sealed class Toolbar {

        /**
         * General property that defines whether this toolbar is visible or not.
         */
        abstract val isVisible: Boolean

        /**
         * Navigation bottom toolbar allowing user open Search, Naviagation Screens
         * or add new Document on Dashboard
         *
         * @property isVisible
         */
        data class Navigation(
            override val isVisible: Boolean
        ) : Toolbar() {
            companion object {
                fun reset(): Navigation = Navigation(false)
            }
        }

        /**
         * Main toolbar allowing user-interface for CRUD-operations on block/page content.
         * @property isVisible defines whether the toolbar is visible or not
         */
        data class Main(
            override val isVisible: Boolean = false
        ) : Toolbar() {
            companion object {
                fun reset(): Main = Main(false)
            }
        }

        /**
         * Main toolbar allowing user-interface for markup operations.
         * @property isVisible defines whether the toolbar is visible or not
         */
        data class MarkupMainToolbar(
            override val isVisible: Boolean,
            val style: MarkupStyleDescriptor? = null,
            val supportedTypes: List<Markup.Type> = emptyList(),
            val isTextColorSelected: Boolean = false,
            val isBackgroundColorSelected: Boolean = false
        ) : Toolbar() {
            companion object {
                fun reset(): MarkupMainToolbar = MarkupMainToolbar(
                    isVisible = false,
                    style = null,
                    isTextColorSelected = false,
                    isBackgroundColorSelected = false
                )
            }
        }

        /**
         * Main toolbar allowing user-interface for markup operations.
         * @property isVisible defines whether the toolbar is visible or not
         */
        data class MarkupColorToolbar(
            override val isVisible: Boolean = false
        ) : Toolbar() {
            companion object {
                fun reset(): MarkupColorToolbar = MarkupColorToolbar(false)
            }
        }

        /**
         * Basic color toolbar state.
         * @property isVisible defines whether the toolbar is visible or not
         */
        data class Styling(
            override val isVisible: Boolean,
            val target: Target? = null,
            val config: StyleConfig? = null,
            val props: Props? = null,
            val mode: StylingMode? = null,
            val style: TextStyle? = TextStyle.P
        ) : Toolbar() {

            companion object {
                fun reset() = Styling(
                    isVisible = false,
                    target = null,
                    config = null,
                    props = null,
                    mode = null
                )
            }

            data class Other(override val isVisible: Boolean) : Toolbar() {
                companion object {
                    fun reset() = Other(false)
                }
            }

            data class Color(override val isVisible: Boolean = false) : Toolbar() {
                companion object {
                    fun reset() = Color(false)
                }
            }

            data class Background(
                override val isVisible: Boolean,
                val selectedBackground: String?
            ) : Toolbar() {
                companion object {
                    fun reset() = Background(isVisible = false, selectedBackground = null)
                }
            }

            /**
             * Target's properties corresponding to current selection or styling mode.
             */
            data class Props(
                val isBold: Boolean = false,
                val isItalic: Boolean = false,
                val isStrikethrough: Boolean = false,
                val isCode: Boolean = false,
                val isLinked: Boolean = false,
                val color: String? = null,
                val background: String? = null,
                val alignment: Alignment? = null
            )

            /**
             * Target block associated with this toolbar.
             */
            data class Target(
                val id: String,
                val text: String,
                val color: String?,
                val background: String?,
                val alignment: Alignment?,
                val marks: List<Markup.Mark>
            ) {

                val isBold: Boolean = marks.any { mark ->
                    mark is Markup.Mark.Bold && mark.from == 0 && mark.to == text.length
                }

                val isItalic: Boolean = marks.any { mark ->
                    mark is Markup.Mark.Italic && mark.from == 0 && mark.to == text.length
                }

                val isStrikethrough: Boolean = marks.any { mark ->
                    mark is Markup.Mark.Strikethrough && mark.from == 0 && mark.to == text.length
                }

                val isCode: Boolean = marks.any { mark ->
                    mark is Markup.Mark.Keyboard && mark.from == 0 && mark.to == text.length
                }

                val isLinked: Boolean = marks.any { mark ->
                    mark is Markup.Mark.Link && mark.from == 0 && mark.to == text.length
                }
            }
        }

        /**
         * Basic multi select mode toolbar state.
         * @property isVisible defines whether we are in multi select mode or not
         * @property count number of selected blocks
         */
        data class MultiSelect(
            override val isVisible: Boolean,
            val isScrollAndMoveEnabled: Boolean = false,
            val isQuickScrollAndMoveMode: Boolean = false,
            val count: Int = 0
        ) : Toolbar() {
            companion object {
                fun reset(): MultiSelect = MultiSelect(
                    isVisible = false
                )
            }
        }

        /**
         * Toolbar with list of mentions and add new page item.
         * @property isVisible defines whether the toolbar is visible or not
         * @property mentionFrom first position of the mentionFilter in text
         * @property mentionFilter sequence of symbol @ and characters, using for filtering mentions
         * @property cursorCoordinate y coordinate bottom of the cursor, using for define top border of the toolbar
         * @property mentions list of all mentions
         */
        data class MentionToolbar(
            override val isVisible: Boolean,
            val mentionFrom: Int?,
            val mentionFilter: String?,
            val cursorCoordinate: Int?,
            val updateList: Boolean = false,
            val mentions: List<DefaultObjectView> = emptyList()
        ) : Toolbar() {
            companion object {
                fun reset(): MentionToolbar = MentionToolbar(
                    isVisible = false,
                    mentionFilter = null,
                    mentionFrom = null,
                    cursorCoordinate = null,
                    mentions = emptyList(),
                    updateList = false
                )
            }
        }

        /**
         * Search toolbar.
         */
        data class SearchToolbar(
            override val isVisible: Boolean
        ) : Toolbar() {
            companion object {
                fun reset(): SearchToolbar = SearchToolbar(false)
            }
        }

        data class SlashWidget(
            override val isVisible: Boolean,
            val from: Int? = null,
            val filter: String? = null,
            val cursorCoordinate: Int? = null,
            val updateList: Boolean = false,
            val items: List<String> = emptyList(),
            val widgetState: SlashWidgetState? = null
        ) : Toolbar() {
            companion object {
                fun reset(): SlashWidget = SlashWidget(
                    isVisible = false,
                    filter = null,
                    from = null,
                    cursorCoordinate = null,
                    items = emptyList(),
                    updateList = false,
                    widgetState = null
                )
            }
        }

        data class ObjectTypes(
            override val isVisible: Boolean,
            val data: List<ObjectTypeView> = emptyList()
        ) : Toolbar() {
            companion object {
                fun reset(): ObjectTypes = ObjectTypes(
                    isVisible = false,
                    data = listOf()
                )
            }
        }
    }

    /**
     * Block currently associated with this panel.
     * @property id id of the focused block
     */
    data class Focus(
        val id: String,
        val type: Type
    ) {
        enum class Type {
            P, H1, H2, H3, H4, TITLE, QUOTE, CODE_SNIPPET, BULLET, NUMBERED, TOGGLE, CHECKBOX, BOOKMARK
        }
    }

    companion object {

        /**
         * Factory function for creating initial state.
         */
        fun init(): ControlPanelState = ControlPanelState(
            navigationToolbar = Toolbar.Navigation(
                isVisible = true
            ),
            mainToolbar = Toolbar.Main(
                isVisible = false
            ),
            markupMainToolbar = Toolbar.MarkupMainToolbar(
                isVisible = false
            ),
            markupColorToolbar = Toolbar.MarkupColorToolbar(),
            multiSelect = Toolbar.MultiSelect(
                isVisible = false,
                count = 0
            ),
            stylingToolbar = Toolbar.Styling(
                isVisible = false,
                mode = null
            ),
            mentionToolbar = Toolbar.MentionToolbar(
                isVisible = false,
                cursorCoordinate = null,
                mentionFilter = null,
                updateList = false,
                mentionFrom = null,
                mentions = emptyList()
            ),
            searchToolbar = Toolbar.SearchToolbar(
                isVisible = false
            ),
            slashWidget = Toolbar.SlashWidget.reset(),
            objectTypesToolbar = Toolbar.ObjectTypes.reset()
        )
    }
}