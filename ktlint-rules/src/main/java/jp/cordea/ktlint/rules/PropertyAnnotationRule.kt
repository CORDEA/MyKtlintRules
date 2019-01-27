package jp.cordea.ktlint.rules

import com.github.shyiko.ktlint.core.KtLint
import com.github.shyiko.ktlint.core.Rule
import com.google.common.annotations.VisibleForTesting
import org.jetbrains.kotlin.KtNodeTypes
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.lang.FileASTNode
import org.jetbrains.kotlin.psi.stubs.elements.KtStubElementTypes

class PropertyAnnotationRule : Rule("property-annotation") {
    @VisibleForTesting internal var maxLineLength: Int = 0

    override fun visit(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit
    ) {
        if (node.elementType == KtStubElementTypes.FILE) {
            val editorConfig = (node as FileASTNode).getUserData(KtLint.EDITOR_CONFIG_USER_DATA_KEY)!!
            editorConfig.get("max_line_length")?.toIntOrNull()?.let {
                maxLineLength = it
            }
            return
        }
        if (maxLineLength == 0) {
            return
        }
        if (node.elementType != KtNodeTypes.PROPERTY) {
            return
        }
        val modifier = node.findChildByType(KtNodeTypes.MODIFIER_LIST) ?: return
        if (modifier.findChildByType(KtNodeTypes.ANNOTATION_ENTRY) == null) {
            return
        }
        if (modifier.treeNext.text.contains("\n") && node.text.length <= maxLineLength) {
            emit(modifier.treeNext.startOffset, "Newline is not needed between annotation and property", false)
        }
    }
}
