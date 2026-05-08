package org.example

import com.varabyte.kotter.terminal.virtual.VirtualTerminal
import kotlin.test.Test

class Test {
    @Test
    fun run() = start(VirtualTerminal.create(hideVerticalScrollbar = true))
}
