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

    List<Float> getBarNumbers() {
        Float maxValue = maxValue
        Float minValue = minValue
        Float midValue = (maxValue && minValue) ? (maxValue + minValue) / 2 : null
        return midValue ? [maxValue, (maxValue + midValue) / 2, midValue, (midValue + minValue) / 2, minValue] : []
    }
}
