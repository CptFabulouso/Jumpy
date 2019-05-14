package com.jerry.framework.impl;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class CompassHandler implements SensorEventListener {
	float yaw;
	float pitch;
	float roll;

	/*
	 * The constructor takes a Context, from which it gets a SensorManager
	 * instance to set up the event listening
	 */
	public CompassHandler(Context context) {
		SensorManager manager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		//if no accelerometer is installed, the handler will return zero acceleration on all axes
		if (manager.getSensorList(Sensor.TYPE_ORIENTATION).size() != 0) {
			Sensor compass = manager.getSensorList(
					Sensor.TYPE_ORIENTATION).get(0);
			manager.registerListener(this, compass,
					SensorManager.SENSOR_DELAY_GAME);
		}
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		yaw = event.values[0];
		pitch= event.values[1];
		roll = event.values[2];
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// nothing to do
	}

	public float getYaw() {
		return yaw;
	}

	public float getPitch() {
		return pitch;
	}

	public float getRoll() {
		return roll;
	}

}
