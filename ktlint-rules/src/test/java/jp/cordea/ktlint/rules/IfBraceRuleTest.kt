package jp.cordea.ktlint.rules

import com.github.shyiko.ktlint.core.LintError
import com.github.shyiko.ktlint.test.lint
import com.google.common.truth.Truth
import org.junit.Test

class IfBraceRuleTest {
    private val rule = IfBraceRule()

    @Test
    fun test() {
        Truth.assertThat(
            rule.lint(
                """
                val a = if (true) "a" else "b"
                """.trimIndent()
            )
        ).isEmpty()

        Truth.assertThat(
            rule.lint(
                """
                val a = if (true) {
                    "a"
                } else {
                    "b"
                }
                """.trimIndent()
            )
        ).isEmpty()

        Truth.assertThat(
            rule.lint(
                """
                val a = if (true) {
                    "a"
                }
                """.trimIndent()
            )
        ).isEmpty()

        Truth.assertThat(
            rule.lint(
                """
                val a = if (true)
                    "a"
                """.trimIndent()
            )
        ).isEqualTo(
            listOf(
                LintError(1, 18, "if-brace", "Brace is required for multi-line if statement")
            )
        )

        Truth.assertThat(
            rule.lint(
                """
                val a = if (true)
                    "a"
                else
                    "b"
                """.trimIndent()
            )
        ).isEqualTo(
            listOf(
                LintError(1, 18, "if-brace", "Brace is required for multi-line if statement"),
                LintError(3, 5, "if-brace", "Brace is required for multi-line if statement")
            )
        )

        Truth.assertThat(
            rule.lint(
                """
                val a = if (true)
                    "a"
                else "b"
                """.trimIndent()
            )
        ).isEqualTo(
            listOf(
                LintError(1, 18, "if-brace", "Brace is required for multi-line if statement")
            )
        )

        Truth.assertThat(
            rule.lint(
                """
                val a = if (true) "a" else
                    "b"
                """.trimIndent()
            )
        ).isEqualTo(
            listOf(
                LintError(1, 27, "if-brace", "Brace is required for multi-line if statement")
            )
        )
    }
}
