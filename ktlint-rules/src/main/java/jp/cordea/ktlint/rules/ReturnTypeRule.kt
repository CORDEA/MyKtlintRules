package jp.cordea.ktlint.rules

import com.github.shyiko.ktlint.core.Rule
import org.jetbrains.kotlin.KtNodeTypes
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.stubs.elements.KtStubElementTypes

class ReturnTypeRule : Rule("return-type") {
    override fun visit(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit
    ) {
        val child = when {
            node.elementType == KtNodeTypes.PROPERTY ->
                node.findChildByType(KtTokens.IDENTIFIER) ?: return
            node.elementType == KtNodeTypes.FUN ->
                node.findChildByType(KtNodeTypes.VALUE_PARAMETER_LIST) ?: return
            else -> return
        }
        val parent = node.treeParent
        if (parent.elementType != KtStubElementTypes.FILE && parent.elementType != KtNodeTypes.CLASS_BODY) {
            return
        }
        val next = child.treeNext
        if (next.elementType != KtTokens.COLON) {
            emit(next.startOffset, "Missing return type", false)
        }
    }
}
