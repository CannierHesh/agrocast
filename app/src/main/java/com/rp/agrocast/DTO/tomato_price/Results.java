package com.rp.agrocast.DTO.tomato_price;

import java.util.List;

public class Results {
    private List<Double> forecast;

    private List<String> prediction_interval;

    private List<Index> index;

    public List<Double> getForecast() {
        return forecast;
    }

    public void setForecast(List<Double> forecast) {
        this.forecast = forecast;
    }

    public List<String> getPrediction_interval() {
        return prediction_interval;
    }

    public void setPrediction_interval(List<String> prediction_interval) {
        this.prediction_interval = prediction_interval;
    }

    public List<Index> getIndex() {
        return index;
    }

    public void setIndex(List<Index> index) {
        this.index = index;
    }
}
