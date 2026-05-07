package org.example

import kotlin.random.*

class BC(
    val char: Char,
    var px: Double = 0.0,
    var py: Double = 0.0,
) {
    private val restitution = Random.nextDouble(0.6, 0.9)
    private var vx = Random.nextDouble(-drift, drift)
    private var vy = 0.0
    var bounces: Int = 0

    companion object {
        var limitx = 1.0
        var limity = 1.0
        var gravity = 0.1
        var drift = 0.6
    }

    fun update() {
        vy += gravity
        px += vx
        py += vy
        if (px < 0) {
            px = 0.0
            vx *= -restitution
        }
        if (px > limitx) {
            px = limitx
            vx *= -restitution
        }
        if (py > limity) {
            py = limity
            vy *= -restitution
            vx *= 0.9
            bounces++
        }
    }
}
