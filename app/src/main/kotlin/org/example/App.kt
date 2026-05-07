package org.example

import com.varabyte.kotter.foundation.*
import com.varabyte.kotter.foundation.collections.*
import com.varabyte.kotter.foundation.input.*
import com.varabyte.kotter.foundation.text.*
import com.varabyte.kotter.foundation.timer.*
import com.varabyte.kotter.terminal.system.*
import kotlin.time.*

const val chars = "♔♕♖♗♘♙♚♛♜♝♞♟"
const val hello = "Hello, press any key to continue! (Esc to exit)"

//fun main() = session { //while developing
fun main() = session(SystemTerminal()) { //excluding VirtualTerminal from native image
    val list = liveListOf(hello.mapIndexed { i, c -> BC(c, i.toDouble(), 1.0) }) //CopyOnWriteArrayList?
    val duration = 10.toDuration(DurationUnit.MILLISECONDS)
    var column = hello.length
    var hue = 180
    var active = false
    section {
        BC.limitx = width.coerceAtLeast(2) - 1.0
        BC.limity = height.coerceAtLeast(2) - 1.0
        hue = (hue + 1) % 360
        hsv(hue, 1.0f, 1.0f)
        val rows = list.groupBy { it.py.toInt() }
        for (row in 1 until height) {
            val line = CharArray(width) { ' ' }
            rows[row]?.forEach {
                if (it.px.toInt() in line.indices)
                    line[it.px.toInt()] = it.char
            }
            textLine(String(line))
        }
        invert()
        text("${list.size}•${list.joinToString("") { "${it.char}" }}"
            .take(width).let { it + " ".repeat(width - it.length) })
    }.runUntilKeyPressed(Keys.Escape) {
        onKeyPressed {
            list.withWriteLock {
                if (key == Keys.Space) add(BC(chars.random(), column++.toDouble(), 1.0))
                else addAll("$key".map { BC(it, column++.toDouble(), 1.0) })
            }
            column %= BC.limitx.toInt()
            active = true
        }
        addTimer(duration, true) {
            if (active && list.isNotEmpty()) list.withWriteLock {
//                removeAll { it.bounces > 39 } //TODO does not work?!
                removeAll(list.filter { it.bounces > 39 })
                forEach { it.update() }
            }
        }
    }
}
