package com.example.mycar

import androidx.compose.runtime.mutableStateOf
import androidx.core.util.rangeTo
import kotlinx.coroutines.*

class ViewModelCarCompose : InterfaceViewModelCar {
    private val scope = CoroutineScope(Dispatchers.Default)

    companion object {
        private const val DELAY = 10L
    }

    fun scopeCancel() { scope.cancel() }
    fun physicalCycleStart() {
        scope.launch {
            while (true) {
                delay(DELAY)
                physicalModel()
            }
        }
    }

    var positionX = mutableStateOf(500f)
    var speedStateX = mutableStateOf(0f)
    var angleCar = mutableStateOf(0f)

    private var speedVectorX = 0f

    private var boost = 0.03f
    private var reverseBoost = -0.015f

    private var brakeBoost = -0.1f
    private var resistance = -0.005f

    private var angleSpeedBrake = 0.12f
    private var angleSpeedGas = 0.03f

    private var gas = false
    private var brake = false
    private var reverseGear = false

    private fun physicalModel() {
        positionX.value += speedVectorX
        speedStateX.value = speedVectorX

        gasCar()
        reverseGearCar()
        resistanceGround()
        angleCar()

        brakeCar()
    }

    private fun gasCar() {
        if(gas) {
            speedVectorX += boost
        }
    }

    private fun reverseGearCar() {
        if(reverseGear) {
            speedVectorX += reverseBoost
        }
    }

    private fun brakeCar() {
        if(brake) {
            if(speedVectorX > 0.3) {
                speedVectorX += brakeBoost
            }
            if(speedVectorX < -0.3) {
                speedVectorX -= brakeBoost
            }
            if(speedVectorX > -0.3 && speedVectorX < 0.3){
                speedVectorX = 0f
            }
        }
    }

    private fun resistanceGround() {
        if(speedVectorX > 0.006) {
            speedVectorX += resistance
        }
        if(speedVectorX < -0.006) {
            speedVectorX -= resistance
        }
        if(speedVectorX > -0.006 && speedVectorX < 0.006){
            speedVectorX = 0f
        }
    }

    private fun angleCar() {
        if(gas) {
            if(angleCar.value > -3f) {
                angleCar.value -= angleSpeedGas
            }
        } else {
            if(angleCar.value < 0f) {
                angleCar.value += angleSpeedGas
            }
        }

        if(brake) {
            if(angleCar.value < 3f) {
                angleCar.value += angleSpeedBrake
            }
        } else {
            if(angleCar.value > 0f) {
                angleCar.value -= angleSpeedBrake
            }
        }
    }


    override fun onGas() { gas = true }
    override fun offGas() { gas = false }

    override fun onBrake() { brake = true }
    override fun offBrake() { brake = false }

    override fun onRevers() { reverseGear = true }
    override fun offRevers() { reverseGear = false }
}

