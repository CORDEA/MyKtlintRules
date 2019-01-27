package jp.cordea.ktlint.rules

import com.github.shyiko.ktlint.core.Rule
import org.jetbrains.kotlin.KtNodeTypes
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.psi.tree.TokenSet
import org.jetbrains.kotlin.lexer.KtTokens

class ArgumentListParenthesisRule : Rule("argument-list-parenthesis") {
    override fun visit(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit
    ) {
        if (node.elementType != KtNodeTypes.CALL_EXPRESSION) {
            return
        }
        val args = node.findChildByType(KtNodeTypes.VALUE_ARGUMENT_LIST) ?: return
        if (!args.text.contains("\n")) {
            return
        }
        val lpar = args.findChildByType(KtTokens.LPAR) ?: return
        val rpar = args.findChildByType(KtTokens.RPAR) ?: return
        if (!lpar.treeNext.text.contains("\n")) {
            emit(lpar.startOffset, """Missing newline after "("""", false)
        }
        if (!rpar.treePrev.text.contains("\n")) {
            emit(rpar.startOffset, """Missing newline before ")"""", false)
        }
        val children = args.getChildren(TokenSet.create(KtTokens.COMMA))
        children.forEach {
            if (!it.treeNext.text.contains("\n")) {
                emit(it.startOffset, """Missing newline after ","""", false)
            }
        }
    }
}
