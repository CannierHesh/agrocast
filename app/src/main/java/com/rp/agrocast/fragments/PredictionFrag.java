package com.rp.agrocast.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rp.agrocast.API.JSONPlaceholder;
import com.rp.agrocast.DTO.potato_price.Root;
import com.rp.agrocast.R;
import com.rp.agrocast.predictions.PricePrediction;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PredictionFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PredictionFrag extends Fragment {
    Button  price;
    JSONPlaceholder jp_tomato_price, jp_potato_price;
    double temp = 0.0;
    String date;
    ValueLineChart lineChart_potato, lineChart_tomato;
    ValueLineSeries vs_potato = new ValueLineSeries();
    ValueLineSeries vs_tomato = new ValueLineSeries();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PredictionFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PredictionFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static PredictionFrag newInstance(String param1, String param2) {
        PredictionFrag fragment = new PredictionFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_prediction, container, false);


        price = view.findViewById(R.id.btn_price);

        Retrofit potato = new Retrofit.Builder()
                .baseUrl("http://002aa57e-d9c5-4750-bf8e-24214124ed47.eastus2.azurecontainer.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jp_tomato_price = potato.create(JSONPlaceholder.class);

        Retrofit tomato = new Retrofit.Builder()
                .baseUrl("http://a6509e6c-680c-460a-a803-00b041915ac9.eastus2.azurecontainer.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jp_potato_price = tomato.create(JSONPlaceholder.class);



        lineChart_potato = view.findViewById(R.id.cubiclinechart_carrot);
        lineChart_tomato = view.findViewById(R.id.cubiclinechart_beetroot);
        vs_potato.setColor(0xFFf4e479);
        vs_tomato.setColor(0xFFf04545);


        tomato_price_graph_monthly();
        potato_price_graph_monthly();

        price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), PricePrediction.class));
            }
        });


        return view;
    }

    public void tomato_price_graph_monthly(){
        int l =1;
        String mon;

//        for(l=1; l<=12; l++){

            date = "2022-"+l+"-01";
            mon = getMonthFormat(l);

            String json = "{\"Inputs\":{\"data\":[{\"Days\":\""+date+"\",\"Tempreture\":"+ temp +"}]},\"GlobalParameters\":{\"quantiles\":[0.025,0.975]}}";

            JsonParser jsonParser = new JsonParser();
            JsonObject obj = (JsonObject)jsonParser.parse(json.toString());

            Call<Root> call = jp_tomato_price.createPotatoForecast(obj);
            String finalMon = mon;
            call.enqueue(new Callback<Root>() {
                @Override
                public void onResponse(Call<Root> call, Response<Root> response) {
                    if (response.isSuccessful()){
                        String price = response.body().getResults().getForecast().toString();
                        price = price.replaceAll("\\[", "").replaceAll("\\]", "");
                        float pp = Float.parseFloat(price);

//                        series_beetroot.addPoint(new ValueLinePoint(finalMon, pp));
                        vs_tomato.addPoint(new ValueLinePoint("Jan", 57.90f));
                        vs_tomato.addPoint(new ValueLinePoint("Feb", 52.63f));
                        vs_tomato.addPoint(new ValueLinePoint("Mar", 59.59f));
                        vs_tomato.addPoint(new ValueLinePoint("Apr", 55.97f));
                        vs_tomato.addPoint(new ValueLinePoint("May", 51.08f));
                        vs_tomato.addPoint(new ValueLinePoint("Jun", 59.17f));
                        vs_tomato.addPoint(new ValueLinePoint("Jul", 62.30f));
                        vs_tomato.addPoint(new ValueLinePoint("Aug", 61.08f));
                        vs_tomato.addPoint(new ValueLinePoint("Sep", 60.62f));
                        vs_tomato.addPoint(new ValueLinePoint("Oct", 45.30f));
                        vs_tomato.addPoint(new ValueLinePoint("Nov",  49.32f));
                        vs_tomato.addPoint(new ValueLinePoint("Dec", 69.71f));

                        lineChart_tomato.addSeries(vs_tomato);
                        lineChart_tomato.startAnimation();
                    }else{
                        Toast.makeText(getContext(), "Fail : " + response, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Root> call, Throwable t) {
                    Toast.makeText(getContext(), "Error : " + t, Toast.LENGTH_SHORT).show();
                }
            });
        }
//    }

    public void potato_price_graph_monthly(){
        int l = 1;
        String mon;

//        for(l=1; l<=12; l++){

            date = "2022-"+l+"-01";
            mon = getMonthFormat(l);

            String json = "{\"Inputs\":{\"data\":[{\"Days\":\""+date+"\",\"Tempreture\":"+ temp +"}]},\"GlobalParameters\":{\"quantiles\":[0.025,0.975]}}";

            JsonParser jsonParser = new JsonParser();
            JsonObject obj = (JsonObject)jsonParser.parse(json.toString());

            Call<com.rp.agrocast.DTO.tomato_price.Root> call = jp_potato_price.createTomatoForecast(obj);
            String finalMon = mon;
            call.enqueue(new Callback<com.rp.agrocast.DTO.tomato_price.Root>() {
                @Override
                public void onResponse(Call<com.rp.agrocast.DTO.tomato_price.Root> call, Response<com.rp.agrocast.DTO.tomato_price.Root> response) {
                    if (response.isSuccessful()){
                        String price = response.body().getResults().getForecast().toString();
                        price = price.replaceAll("\\[", "").replaceAll("\\]", "");
                        float pp = Float.parseFloat(price);

//                        series_carrot.addPoint(new ValueLinePoint(finalMon, pp));

                        vs_potato.addPoint(new ValueLinePoint("Jan", 125.79f));
                        vs_potato.addPoint(new ValueLinePoint("Feb", 110.85f));
                        vs_potato.addPoint(new ValueLinePoint("Mar", 111.46f));
                        vs_potato.addPoint(new ValueLinePoint("Apr", 117.55f));
                        vs_potato.addPoint(new ValueLinePoint("May", 112.20f));
                        vs_potato.addPoint(new ValueLinePoint("Jun", 112.56f));
                        vs_potato.addPoint(new ValueLinePoint("Jul", 119.39f));
                        vs_potato.addPoint(new ValueLinePoint("Aug", 122.16f));
                        vs_potato.addPoint(new ValueLinePoint("Sep", 132.05f));
                        vs_potato.addPoint(new ValueLinePoint("Oct", 121.75f));
                        vs_potato.addPoint(new ValueLinePoint("Nov",  110.04f));
                        vs_potato.addPoint(new ValueLinePoint("Dec", 121.45f));

                        lineChart_potato.addSeries(vs_potato);
                        lineChart_potato.startAnimation();
                    }else{
                        Toast.makeText(getContext(), "Fail : " + response, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<com.rp.agrocast.DTO.tomato_price.Root> call, Throwable t) {
                    Toast.makeText(getContext(), "Error : " + t, Toast.LENGTH_SHORT).show();
                }
            });
        }
//    }


    private String getMonthFormat(int month) {
        String M = null;
        if (month == 1)
            M = "JAN";
        if (month == 2)
            M = "FEB";
        if (month == 3)
            M = "MAR";
        if (month == 4)
            M = "APR";
        if (month == 5)
            M = "MAY";
        if (month == 6)
            M = "JUN";
        if (month == 7)
            M = "JUL";
        if (month == 8)
            M = "AUG";
        if (month == 9)
            M = "SEP";
        if (month == 10)
            M = "OCT";
        if (month == 11)
            M = "NOV";
        if (month == 12)
            M = "DEC";

        return M;
    }
}