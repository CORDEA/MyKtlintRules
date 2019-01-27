package jp.cordea.ktlint.rules

import com.github.shyiko.ktlint.core.LintError
import com.github.shyiko.ktlint.test.lint
import com.google.common.truth.Truth
import org.junit.Test

class ReturnTypeRuleTest {
    private val rule = ReturnTypeRule()

    @Test
    fun test() {
        Truth.assertThat(
            rule.lint(
                """
                val a: String = "a"
                """.trimIndent()
            ).isEmpty()
        )

        Truth.assertThat(
            rule.lint(
                """
                fun a(): String = "a"
                """.trimIndent()
            ).isEmpty()
        )

        Truth.assertThat(
            rule.lint(
                """
                val a: String get() = "a"
                """.trimIndent()
            ).isEmpty()
        )

        Truth.assertThat(
            rule.lint(
                """
                fun a() {
                    val a = "a"
                }
                """.trimIndent()
            ).isEmpty()
        )

        Truth.assertThat(
            rule.lint(
                """
                class A {
                    fun a() {
                        val a = ""
                    }
                }
                """.trimIndent()
            ).isEmpty()
        )

        Truth.assertThat(
            rule.lint(
                """
                val a = ""
                """.trimIndent()
            )
        ).isEqualTo(
            listOf(
                LintError(1, 6, "return-type", "Missing return type")
            )
        )

        Truth.assertThat(
            rule.lint(
                """
                val a get() {
                    ""
                }
                """.trimIndent()
            )
        ).isEqualTo(
            listOf(
                LintError(1, 6, "return-type", "Missing return type")
            )
        )

        Truth.assertThat(
            rule.lint(
                """
                fun a(a: String) = ""
                """.trimIndent()
            )
        ).isEqualTo(
            listOf(
                LintError(1, 17, "return-type", "Missing return type")
            )
        )

        Truth.assertThat(
            rule.lint(
                """
                class A() {
                    val a = ""
                    fun a(a: String) = ""

                    fun b(a: String) {
                        val c = a
                        return ""
                    }
                }
                """.trimIndent()
            )
        ).isEqualTo(
            listOf(
                LintError(2, 10, "return-type", "Missing return type"),
                LintError(3, 21, "return-type", "Missing return type"),
                LintError(5, 21, "return-type", "Missing return type")
            )
        )
    }
}
