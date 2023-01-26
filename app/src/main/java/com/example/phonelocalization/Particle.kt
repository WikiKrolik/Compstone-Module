package com.example.phonelocalization

class Particle(x: ParticleFilter.Point, y: Double, weight: Int) {
    var x = x
    var y = y
    var weight = weight
    var dx = 0f
    var dy = 0f
}