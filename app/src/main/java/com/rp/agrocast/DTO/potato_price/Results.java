package com.rp.agrocast.DTO.potato_price;

import java.util.List;

public class Results {
    private List<Double> forecast;

    private List<String> prediction_interval;

    private List<Index> index;

    public void setForecast(List<Double> forecast){
        this.forecast = forecast;
    }
    public List<Double> getForecast(){
        return this.forecast;
    }
    public void setPrediction_interval(List<String> prediction_interval){
        this.prediction_interval = prediction_interval;
    }
    public List<String> getPrediction_interval(){
        return this.prediction_interval;
    }
    public void setIndex(List<Index> index){
        this.index = index;
    }
    public List<Index> getIndex(){
        return this.index;
    }
}
