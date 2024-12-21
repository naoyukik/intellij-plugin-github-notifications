package com.github.naoyukik.intellijplugingithubnotifications.utility

object CommandExecutor {
    fun execute(command: List<String>): String? {
        val processBuilder = ProcessBuilder(command)
        val process = processBuilder.redirectErrorStream(true).start()
        val output = process.inputStream.bufferedReader().readText()
        process.waitFor()
        return output.ifBlank { null }
    }
}
