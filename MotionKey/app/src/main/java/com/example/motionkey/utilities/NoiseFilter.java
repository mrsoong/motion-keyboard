package com.example.motionkey.utilities;

import java.security.InvalidParameterException;
import java.util.Arrays;

/**
 * Copyright (C) 2017 MotionKey
 * File Created on 3/17/17
 */

public class NoiseFilter {

    private int historyLength;
    private int oldestHistoryIndex;
    private float[][] measurementHistory;
    // Determines how sensitive the keyboard is
    private float sensitivity;

    //0=Azimuth 1=Pitch 2=Roll
    private int dimensions;

    public NoiseFilter(int historyLength, float sensitivity, int dimensions) {
        this.historyLength = historyLength;
        this.sensitivity = sensitivity;
        this.dimensions = dimensions;
        this.measurementHistory = new float[this.dimensions][this.historyLength];
        //initialize history arrays in all dimensions
        for (int i=0; i<this.dimensions; i++) {
            Arrays.fill(this.measurementHistory[i], 0);
        }
    }

    public void addMeasurement(int axis, float value) {

    }

    public float[] getOldestMeasurement() {
        float[] oldestMeasurement = new float[this.dimensions];
        for (int i=0; i<this.dimensions; i++) {
            oldestMeasurement[i] = this.measurementHistory[i][this.oldestHistoryIndex];
        }
        return oldestMeasurement;
    }

    public void setOldestMeasurement(float[] value) throws InvalidParameterException {
        //input must be of same dimension
        if (value.length != this.dimensions) {
            throw new InvalidParameterException();
        }
        // Replace the oldest measurement with the newest mesasurement
        for (int i=0; i<this.dimensions; i++){
            this.measurementHistory[i][this.oldestHistoryIndex] = value[i];
        }

        this.oldestHistoryIndex++;
        this.oldestHistoryIndex = this.oldestHistoryIndex % this.historyLength;
    }

    public float[] getFilteredMeasurement() {
        float[] filteredMeasurement = new float[this.dimensions];
        Arrays.fill(filteredMeasurement, 0);

        // Compute the sum of the measurements for each dimension
        for (int j=0; j<this.dimensions; j++) {
            for (int i=0; i<this.historyLength; i++) {
                filteredMeasurement[j] += this.measurementHistory[j][i];
            }
        }

        // Compute the average measurement (for every dimension) using the sums, and multiply them with the
        // sensitivity to get the filtered measurement
        for (int i=0; i<this.dimensions; i++) {
            filteredMeasurement[i] = filteredMeasurement[i] / (float) this.historyLength * this.sensitivity;
        }

        return filteredMeasurement;
    }

}
