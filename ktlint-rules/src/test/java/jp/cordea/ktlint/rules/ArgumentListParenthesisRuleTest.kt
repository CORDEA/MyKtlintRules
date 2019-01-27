package jp.cordea.ktlint.rules

import com.github.shyiko.ktlint.core.LintError
import com.github.shyiko.ktlint.test.lint
import com.google.common.truth.Truth
import org.junit.Test

class ArgumentListParenthesisRuleTest {
    private val rule = ArgumentListParenthesisRule()

    @Test
    fun test() {
        Truth.assertThat(
            rule.lint(
                """
                val a = arrayListOf<String>("a", "b")
                """.trimIndent()
            )
        ).isEmpty()

        Truth.assertThat(
            rule.lint(
                """
                val a = arrayListOf<String>(
                    "a",
                    "b"
                )
                """.trimIndent()
            )
        ).isEmpty()

        Truth.assertThat(
            rule.lint(
                """
                val a = arrayListOf<String>(
                    "a",
                     "b")
                """.trimIndent()
            )
        ).isEqualTo(
            listOf(
                LintError(3, 9, "argument-list-parenthesis", """Missing newline before ")"""")
            )
        )

        Truth.assertThat(
            rule.lint(
                """
                val a = arrayListOf<String>("a",
                     "b"
                )
                """.trimIndent()
            )
        ).isEqualTo(
            listOf(
                LintError(1, 28, "argument-list-parenthesis", """Missing newline after "("""")
            )
        )

        Truth.assertThat(
            rule.lint(
                """
                val a = arrayListOf<String>(
                    "a", "b"
                )
                """.trimIndent()
            )
        ).isEqualTo(
            listOf(
                LintError(2, 8, "argument-list-parenthesis", """Missing newline after ","""")
            )
        )

        Truth.assertThat(
            rule.lint(
                """
                class Test(a: String, b: String)

                val a = Test("a", "b"
                )
                """.trimIndent()
            )
        ).isEqualTo(
            listOf(
                LintError(3, 13, "argument-list-parenthesis", """Missing newline after "(""""),
                LintError(3, 17, "argument-list-parenthesis", """Missing newline after ","""")
            )
        )
    }
}
