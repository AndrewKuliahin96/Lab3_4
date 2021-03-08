package com.example.lab3

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Инициализируем наши TextView
    private val tvX by lazy { findViewById<TextView>(R.id.tvX) }
    private val tvY by lazy { findViewById<TextView>(R.id.tvY) }
    private val tvZ by lazy { findViewById<TextView>(R.id.tvZ) }

    // Обьявляем слушатель событий датчика
    private val listener: SensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, acc: Int) = Unit

        // Данный метод передает аргумент с событием датчика
        override fun onSensorChanged(event: SensorEvent) {
            // SensorEvent содержит массив со значениями датчика
            // Для Sensor.TYPE_GYROSCOPE это диапазон индексов 0-2
            // Вот описание:

            // SensorEvent.values[0] - Rate of rotation around the x axis (rad/s)
            // SensorEvent.values[1] - Rate of rotation around the y axis (rad/s)
            // SensorEvent.values[2] - Rate of rotation around the z axis (rad/s)
            // https://developer.android.com/guide/topics/sensors/sensors_motion
            tvX.text = event.values[0].format(1)
            tvY.text = event.values[1].format(1)
            tvZ.text = event.values[2].format(1)
        }
    }

    // Оьявляем переменные типа SensorManager и Sensor
    private var sensorManager: SensorManager? = null
    private var sensor: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Вызываем функцию инициализации сенсора
        initSensor()
    }

    // В этой функции подписываем слушатель
    override fun onResume() {
        super.onResume()
        sensorManager?.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    // В этой функции отменяем подписку слушателя
    override fun onPause() {
        sensorManager?.unregisterListener(listener)
        super.onPause()
    }

    private fun initSensor() {
        // Инициализируем SensorManager получая системный сервис
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // Инициализируем sensor при помощи нашего SensorManager и передаем тип нашего датчика
        // Sensor.*
        sensor = sensorManager?.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    }

    // Функция расширения для типа Float, которая возвращает
    // отформатированную строку
    // Аргумент digits - количество символов после запятой
    private fun Float.format(digits: Int) = "%.${digits}f".format(this)
}
