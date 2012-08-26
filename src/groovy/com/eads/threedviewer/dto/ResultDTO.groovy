package com.eads.threedviewer.dto

class ResultDTO {
    String name;
    List<Float> resultValues = [];

    List<Float> getCalculatedResults() {
        Float min = minValue
        Float max = maxValue
        return resultValues.collect {(it - min) / (max - min)}
    }

    Float getMinValue() {
        resultValues.min {it}
    }

    Float getMaxValue() {
        resultValues.max {it}
    }
}
