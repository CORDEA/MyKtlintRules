package jp.cordea.ktlint.rules

import com.github.shyiko.ktlint.core.Rule
import org.jetbrains.kotlin.KtNodeTypes
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.lexer.KtTokens

class IfBraceRule : Rule("if-brace") {
    override fun visit(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit
    ) {
        if (node.elementType != KtNodeTypes.THEN && node.elementType != KtNodeTypes.ELSE) {
            return
        }
        if (!node.treePrev.text.contains("\n")) {
            return
        }
        if (node.findChildByType(KtTokens.LBRACE) == null) {
            emit(node.treePrev.startOffset, "Brace is required for multi-line if statement", false)
        }
    }
}
