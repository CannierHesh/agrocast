package com.rp.agrocast.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.rp.agrocast.R;

import org.jsoup.Jsoup;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MarketFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MarketFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    int lowest;
    String lowestSupermarket,lfsp,lp,kp;
    TextView lowestprice,laugfsp,lassanap,kaprukap;
    ImageView lowestImage;



    ConstraintLayout bestCultivation, favorableCondition;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MarketFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MarketFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MarketFragment newInstance(String param1, String param2) {
        MarketFragment fragment = new MarketFragment();
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
        View v = inflater.inflate(R.layout.fragment_market, container, false);

        Spinner dropdown = v.findViewById(R.id.spinner);

        lowestprice = v.findViewById(R.id.lowestPrice);
        lowestImage = (ImageView)v.findViewById(R.id.lowestPriceImg);

        laugfsp = v.findViewById(R.id.laugfsprice);
        lassanap = v.findViewById(R.id.lassanaprice);
        kaprukap = v.findViewById(R.id.kaprukaprice);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.vegetables, android.R.layout.simple_spinner_item);

        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);

        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String choice = adapterView.getItemAtPosition(i).toString();

        if (choice.equals("Potato")){
            webscrapePotato ws = new webscrapePotato();
            ws.execute();
        }else if(choice.equals("Tomato")){
            webscrapeTomato wst = new webscrapeTomato();
            wst.execute();
        }else{

        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }



    private class webscrapePotato extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids){
            org.jsoup.nodes.Document document = null;
            org.jsoup.nodes.Document document2 = null;
            org.jsoup.nodes.Document document3 = null;

            //laugfs
            try {
                document = Jsoup.connect("https://www.laugfssuper.com/index.php/categories/vegetables/local-potatoes.html").get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            org.jsoup.select.Elements elements = document.select("span[class=regular-price],del[class=price]");

            String price = elements.text();
            String pricea = price.replaceAll("[^0-9]","");
            int laugfs = Integer.parseInt(pricea);
            laugfs = laugfs/100;
            lfsp = String.valueOf(laugfs);

            //System.out.println("Laugfs Price: "+ laugfs);

            //kapruka
            try {
                document2 = Jsoup.connect("https://www.kapruka.com/buyonline/1-kg-potatoes/kid/grocery001450").get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            org.jsoup.select.Elements elements2 = document2.getElementsByClass("price");

            String price2 = elements2.text();
            String price2a = price2.replaceAll("[^0-9]","");
            int kapruka = Integer.parseInt(price2a);
            kp = String.valueOf(kapruka);
            //System.out.println("Kapruka Price: "+ kapruka);

            //lassana
            try {
                document3 = Jsoup.connect("https://lassana.com/potato-%25E0%25B6%2585%25E0%25B6%25BD---500g/p/1931").get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            org.jsoup.select.Elements elements3 = document3.getElementsByClass("orig_price");
            String price3 = elements3.text();
            String price3a = price3.replaceAll("[^0-9]","");
            int lassana = Integer.parseInt(price3a);
            lassana = (lassana/100)*2;
            lp = String.valueOf(lassana);
        //System.out.println("Lassana Price: "+ lassana);

            if (laugfs < lassana) {
                if (laugfs<kapruka){
                    lowest = laugfs;
                    lowestSupermarket = "Laugfs";

                }
            }
            if (lassana < kapruka){
                if (lassana < laugfs){
                    lowest = lassana;
                    lowestSupermarket = "Lassana";

                }
            }
            if (kapruka < lassana){
                if (kapruka < laugfs){
                    lowest = kapruka;
                    lowestSupermarket = "Kapruka";

                }
            }
            return null;


        }


        @Override
        protected void onPostExecute(Void aVoid){

            laugfsp.setText("Rs."+lfsp+".00");
            lassanap.setText("Rs."+lp+".00");
            kaprukap.setText("Rs."+kp+".00");

            lowestprice.setText("Rs."+lowest+".00 (1KG)");
            if (lowestSupermarket.equals("Laugfs")){
                lowestImage.setImageResource(R.drawable.laugfs);
            }
            if (lowestSupermarket.equals("Lassana")){
                lowestImage.setImageResource(R.drawable.lassana);
            }
            if (lowestSupermarket.equals("Kapruka")){
                lowestImage.setImageResource(R.drawable.kapruka);
            }
        }


    }

    private class webscrapeTomato extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids){
            org.jsoup.nodes.Document document = null;
            org.jsoup.nodes.Document document2 = null;
            org.jsoup.nodes.Document document3 = null;

            //laugfs
            try {
                document = Jsoup.connect("https://www.laugfssuper.com/index.php/categories/vegetables/up-country-vegetables/tomatoes-3/tomatoes.html").get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            org.jsoup.select.Elements elements = document.select("span[class=regular-price],del[class=price]");

            String price = elements.text();
            String pricea = price.replaceAll("[^0-9]","");
            int laugfs = Integer.parseInt(pricea);
            laugfs = laugfs/100;
            lfsp = String.valueOf(laugfs);
            //System.out.println("Laugfs Price: "+ laugfs);

            //kapruka
            try {
                document2 = Jsoup.connect("https://www.kapruka.com/buyonline/tomato-500g-fresh-vegetables/kid/vegibox00144").get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            org.jsoup.select.Elements elements2 = document2.getElementsByClass("price");

            String price2 = elements2.text();
            String price2a = price2.replaceAll("[^0-9]","");
            int kapruka = Integer.parseInt(price2a);
            kapruka = kapruka*2;
            kp = String.valueOf(kapruka);
            //System.out.println("Kapruka Price: "+ kapruka);

            //lassana
            try {
                document3 = Jsoup.connect("https://lassana.com/TOMATO%20(%E0%B6%AD%E0%B6%9A%E0%B7%8A%E0%B6%9A%E0%B7%8F%E0%B6%BD%E0%B7%92)%20-%20250g/p/1934").get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            org.jsoup.select.Elements elements3 = document3.getElementsByClass("orig_price");
            String price3 = elements3.text();
            String price3a = price3.replaceAll("[^0-9]","");
            int lassana = Integer.parseInt(price3a);
            lassana = (lassana/100)*2;
            lp = String.valueOf(lassana);
            //System.out.println("Lassana Price: "+ lassana);


            if (laugfs < lassana) {
                if (laugfs<kapruka){
                    lowest = laugfs;
                    lowestSupermarket = "Laugfs";

                }
            }
            if (lassana < kapruka){
                if (lassana < laugfs){
                    lowest = lassana;
                    lowestSupermarket = "Lassana";

                }
            }
            if (kapruka < lassana){
                if (kapruka < laugfs){
                    lowest = kapruka;
                    lowestSupermarket = "Kapruka";

                }
            }
            return null;


        }

        @Override
        protected void onPostExecute(Void aVoid){

            laugfsp.setText("Rs."+lfsp+".00");
            lassanap.setText("Rs."+lp+".00");
            kaprukap.setText("Rs."+kp+".00");

            lowestprice.setText("Rs."+lowest+".00 (1KG)");
            if (lowestSupermarket.equals("Laugfs")){
                lowestImage.setImageResource(R.drawable.laugfs);
            }
            if (lowestSupermarket.equals("Lassana")){
                lowestImage.setImageResource(R.drawable.lassana);
            }
            if (lowestSupermarket.equals("Kapruka")){
                lowestImage.setImageResource(R.drawable.kapruka);
            }

        }

    }

}