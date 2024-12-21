package com.github.naoyukik.intellijplugingithubnotifications.utility

import io.kotest.core.Tag
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe

object Learn : Tag()

class CommandExecutorTest : StringSpec({
    "The gh cli version can be obtained.".config(tags = setOf(Learn)) {
        val commands: List<String> = listOf(
            "gh",
            "--version",
        )
        val executed = CommandExecutor.execute(commands)
        executed shouldNotBe null
    }
})
