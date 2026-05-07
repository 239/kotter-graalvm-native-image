package org.example

import com.varabyte.kotter.foundation.*
import com.varabyte.kotter.foundation.input.*
import com.varabyte.kotter.foundation.text.*
import com.varabyte.kotter.foundation.timer.*
import com.varabyte.kotter.terminal.system.*
import kotlin.time.*

const val chars = "♔♕♖♗♘♙♚♛♜♝♞♟"
const val hello = "Hello, press any key to continue! (Esc to exit)"

fun main() = session { //while developing
//fun main() = session(SystemTerminal()) { //excluding VirtualTerminal from native image
    val list = mutableListOf<BC>()
    val duration = 10.toDuration(DurationUnit.MILLISECONDS)
    var column = hello.length
    var hue = 180
    var cycle by liveVarOf(0)
    hello.forEachIndexed { i, c -> list.add(BC(c, i.toDouble(), 1.0)) }
    section {
        BC.limitx = width - 1.0
        BC.limity = height - 1.0
        hue = (hue + 1) % 360
        hsv(hue, 1.0f, 1.0f)
        for (row in 1 until height) {
            val line = CharArray(width) { ' ' } //TODO reuse?
            list.filter { it.py.toInt() == row }.forEach {
                if (it.px.toInt() in line.indices) //window resizing!
                    line[it.px.toInt()] = it.char
            }
            textLine(String(line))
        }
        invert()
        text("${list.size}•${list.joinToString("") { "${it.char}" }}"
            .take(width).let { it + " ".repeat(width - it.length) })
    }.runUntilKeyPressed(Keys.Escape) {
        onKeyPressed {
            if (key == Keys.Space) list.add(BC(chars.random(), column++.toDouble(), 1.0))
            else "$key".forEach { list.add(BC(it, column++.toDouble(), 1.0)) }
            column %= BC.limitx.toInt()
            if (cycle < 1) cycle = 1 //kick off
        }
        addTimer(duration, true) {
            if (cycle > 0) {
                if (list.isNotEmpty()) cycle++
                list.removeIf { it.bounces > 39 }
                list.forEach { it.update() }
            }
        }
    }
}
