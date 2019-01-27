package jp.cordea.ktlint.rules

import com.github.shyiko.ktlint.core.LintError
import com.github.shyiko.ktlint.test.lint
import com.google.common.truth.Truth
import org.junit.Test

class PropertyAnnotationRuleTest {
    private val rule = PropertyAnnotationRule()

    @Test
    fun test() {
        Truth.assertThat(
            rule.lint(
                """
                @A val a: String = "a"
                """.trimIndent()
            )
        ).isEmpty()

        Truth.assertThat(
            rule.apply {
                maxLineLength = 10
            }.lint(
                """
                @A
                val a: String = "a"
                """.trimIndent()
            )
        ).isEmpty()

        Truth.assertThat(
            rule.apply {
                maxLineLength = 100
            }.lint(
                """
                @A
                val a: String = "a"
                """.trimIndent()
            )
        ).isEqualTo(
            listOf(
                LintError(1, 3, "property-annotation", "Newline is not needed between annotation and property")
            )
        )

        Truth.assertThat(
            rule.apply {
                maxLineLength = 100
            }.lint(
                """
                @A @B
                val a: String = "a"
                """.trimIndent()
            )
        ).isEqualTo(
            listOf(
                LintError(1, 6, "property-annotation", "Newline is not needed between annotation and property")
            )
        )
    }
}
