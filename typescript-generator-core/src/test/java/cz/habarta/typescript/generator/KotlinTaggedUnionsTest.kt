package cz.habarta.typescript.generator

import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class KotlinTaggedUnionsTest {
    @JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS)
    sealed interface MinimalEffort {
        data class A(
            val a: String
        ) : MinimalEffort

        data object B : MinimalEffort
    }

    @Test
    fun testMinimalEffortUnions() {
        val settings = TestUtils.settings()
        settings.quotes = "'"
        val output = TypeScriptGenerator(settings)
            .generateTypeScript(
                Input.from(
                    MinimalEffort::class.java,
                    MinimalEffort.A::class.java,
                    MinimalEffort.B::class.java
                )
            )
        val expected = """
        interface MinimalEffort {
            '@c': 'cz.habarta.typescript.generator.KotlinTaggedUnionsTest${'$'}MinimalEffort${'$'}A' | 'cz.habarta.typescript.generator.KotlinTaggedUnionsTest${'$'}MinimalEffort${'$'}B';
        }

        interface A extends MinimalEffort {
            '@c': 'cz.habarta.typescript.generator.KotlinTaggedUnionsTest${'$'}MinimalEffort${'$'}A';
            a: string;
        }

        interface B extends MinimalEffort {
            '@c': 'cz.habarta.typescript.generator.KotlinTaggedUnionsTest${'$'}MinimalEffort${'$'}B';
        }
        """.trimIndent()
        Assertions.assertEquals(expected.trim(), output.trim())
    }
}
