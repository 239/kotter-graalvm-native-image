package org.example

import com.varabyte.kotter.foundation.*
import com.varabyte.kotter.foundation.input.*
import com.varabyte.kotter.foundation.text.*
import com.varabyte.kotter.foundation.timer.*
import com.varabyte.kotter.terminal.system.*

const val chars = "♔♕♖♗♘♙♚♛♜♝♞♟"

fun main() = session(SystemTerminal()) { //excluding VirtualTerminal from native image
    val list = mutableListOf<BC>()
    val duration = kotlin.time.Duration.parse("10ms")
    var column = 0
    var cycle by liveVarOf(0)
    section {
        BC.width = width
        BC.height = height
        for (row in 0..height) {
            val line = CharArray(width) { ' ' }
            list.filter { it.py.toInt() == row }.forEach {
                if (it.px.toInt() in line.indices)
                    line[it.px.toInt()] = it.char
            }
            textLine(String(line)) //TODO colors?
        }
        invert()
        text("$cycle•${list.size}:${list.joinToString("") { "${it.char}" }}"
            .take(width).let { it + " ".repeat(width - it.length) })
    }.runUntilKeyPressed(Keys.Escape) {
        onKeyPressed {
            if (key == Keys.Space) list.add(BC(chars.random(), column++.toDouble()))
            else "$key".forEach { list.add(BC(it, column++.toDouble())) }
            column = column.mod(BC.width)
        }
        addTimer(duration, true) {
            if (list.isNotEmpty()) cycle++
            list.removeIf { it.bounces > 33 }
            list.forEach { it.update() }
        }
    }
}
