package com.rp.agrocast.predictions;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rp.agrocast.API.JSONPlaceholder;
import com.rp.agrocast.DTO.tomato_price.Root;
import com.rp.agrocast.R;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PricePrediction extends AppCompatActivity {
    JSONPlaceholder jsonPlaceholder_potato, jsonPlaceholder_tomato;
    DatePickerDialog picker_potato, picker_tomato;

    EditText et_temperature_potato, et_temperature_tomato;
    TextView tv_potatoPrice, calender_potato, tv_tomatoPrice, calender_tomato;
    Button potato_btn, tomato_btn;

    double para1 = 0.025;
    double para2 = 0.927;
    double temperature = 0.0;
    String date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_prediction);

        et_temperature_potato = findViewById(R.id.et_tempurature2);
        tv_potatoPrice = findViewById(R.id.tv_predict_value);
        potato_btn = findViewById(R.id.btn_predict_potato);
        calender_potato = findViewById(R.id.calendarView2);
        et_temperature_tomato = findViewById(R.id.et_tempurature);
        tv_tomatoPrice = findViewById(R.id.tv_predict_value2);
        calender_tomato = findViewById(R.id.calendarView);
        tomato_btn = findViewById(R.id.btn_predict_tomato);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = df.format(c);

        calender_potato.setText(formattedDate);
        calender_tomato.setText(formattedDate);

        Retrofit potato = new Retrofit.Builder()
                .baseUrl("http://002aa57e-d9c5-4750-bf8e-24214124ed47.eastus2.azurecontainer.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceholder_potato = potato.create(JSONPlaceholder.class);

        Retrofit tomato = new Retrofit.Builder()
                .baseUrl("http://a6509e6c-680c-460a-a803-00b041915ac9.eastus2.azurecontainer.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceholder_tomato = tomato.create(JSONPlaceholder.class);

        getDatePotato();
        getDateTomato();

        potato_btn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              String temp = et_temperature_potato.getText().toString().trim();

              if (temp.isEmpty()){
                  temperature = 0.0;
              }else{
                  temperature = Double.parseDouble(temp);
              }

              PotatoForecast();
          }
      });

        tomato_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = et_temperature_tomato.getText().toString().trim();

                if (temp.isEmpty()){
                    temperature = 0.0;
                }else{
                    temperature = Double.parseDouble(temp);
                }

                TomatoForecast();
            }
        });
        
    }

    private void TomatoForecast() {
        String json = "{\"Inputs\":{\"data\":[{\"Days\":\""+date+"\",\"Tempreture\":"+temperature+"}]},\"GlobalParameters\":{\"quantiles\":[0.025,0.975]}}";

        JsonParser jsonParser = new JsonParser();
        JsonObject obj = (JsonObject)jsonParser.parse(json.toString());

        System.out.println("Json Object : "+obj);

        Call<Root> call = jsonPlaceholder_tomato.createTomatoForecast(obj);
        call.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()){
//                    Toast.makeText(PricePrediction.this, "Success : " + response, Toast.LENGTH_SHORT).show();
                    String price = response.body().getResults().getForecast().toString();
                    price = price.replaceAll("\\[", "").replaceAll("\\]", "");
                    float p = Float.parseFloat(price);
                    tv_tomatoPrice.setText("RS : "+round(p,2));
                }else{
                    Toast.makeText(PricePrediction.this, "Fail : " + "Please select a Future Date", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(PricePrediction.this, "Error : " + t, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getDateTomato() {
        calender_tomato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker_tomato = new DatePickerDialog(PricePrediction.this,R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                calender_tomato.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                date = year+"-"+(monthOfYear+1)+"-"+dayOfMonth+"T00:00:00.000Z";
                            }

                        }, year, month, day);
                picker_tomato.show();
            }
        });
    }

    private void getDatePotato() {
        calender_potato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker_potato = new DatePickerDialog(PricePrediction.this,R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                calender_potato.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                date = year+"-"+(monthOfYear+1)+"-"+dayOfMonth+"T00:00:00.000Z";
                            }

                        }, year, month, day);
                picker_potato.show();
            }
    });
    }

    private void PotatoForecast(){
        String json = "{\"Inputs\":{\"data\":[{\"Days\":\""+date+"\",\"Tempreture\":"+temperature+"}]},\"GlobalParameters\":{\"quantiles\":[0.025,0.975]}}";

        JsonParser jsonParser = new JsonParser();
        JsonObject obj = (JsonObject)jsonParser.parse(json.toString());

        Call<com.rp.agrocast.DTO.potato_price.Root> call = jsonPlaceholder_potato.createPotatoForecast(obj);
        call.enqueue(new Callback<com.rp.agrocast.DTO.potato_price.Root>() {
            @Override
            public void onResponse(Call<com.rp.agrocast.DTO.potato_price.Root> call, Response<com.rp.agrocast.DTO.potato_price.Root> response) {
                if (response.isSuccessful()){
//                    Toast.makeText(PricePrediction.this, "Success : " + response, Toast.LENGTH_SHORT).show();
                    String price = response.body().getResults().getForecast().toString();
                    price = price.replaceAll("\\[", "").replaceAll("\\]", "");
                    float p = Float.parseFloat(price);
                    tv_potatoPrice.setText("RS : "+round(p,2));
                }else{
                    Toast.makeText(PricePrediction.this, "Fail : " + "Please Select a Future Date", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<com.rp.agrocast.DTO.potato_price.Root> call, Throwable t) {
                Toast.makeText(PricePrediction.this, "Error : " + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

}