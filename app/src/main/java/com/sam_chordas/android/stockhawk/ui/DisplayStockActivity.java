package com.sam_chordas.android.stockhawk.ui;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sam_chordas.android.stockhawk.R;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.communication.IOnPointFocusedListener;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DisplayStockActivity extends AppCompatActivity {

    private ArrayList<String> x;
    private ArrayList<Float> y;
    ValueLineChart lineChartView;
    TextView date;
    TextView close;
    TextView high;
    TextView low;
    TextView open;
    TextView volume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_stock);
        Intent i = getIntent();
        String stock = i.getStringExtra("stock");
        boolean isConnected;
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.detail_stock_texts);
        TextView emptyText = (TextView)findViewById(R.id.empty_stock_detail_view);
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        lineChartView = (ValueLineChart) findViewById(R.id.linechart);
        if(isConnected){
            downloadData(stock);
        }else{
            linearLayout.setVisibility(View.GONE);
            lineChartView.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
            emptyText.setText("Please connect to the internet to know more about this stock!");
        }



    }

    void downloadData(String stock){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().
                url("http://chartapi.finance.yahoo.com/instrument/1.0/" + stock + "/chartdata;type=quote;range=5y/json").
                build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(response.code()==200){
                    try{
                        String data = response.body().string();
                        if (data.startsWith("finance_charts_json_callback( ")) {
                         data = data.substring(29, data.length() - 2);
                        }
                        JSONObject jsonObject = new JSONObject(data);
                        final String name = jsonObject.getJSONObject("meta").getString("Company-Name");
                        final ValueLineSeries lineSet = new ValueLineSeries();
                        final JSONArray jsonArray = jsonObject.getJSONArray("series");
                        Log.d("length",String.valueOf(jsonArray.length()));
                        for(int i =0;i<jsonArray.length();i++){
                            JSONObject datapoint = jsonArray.getJSONObject(i);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
                            String date = android.text.format.DateFormat.
                                    getMediumDateFormat(getApplicationContext()).
                                    format(simpleDateFormat.parse(datapoint.getString("Date")));
                            lineSet.addPoint(new ValueLinePoint(date,Float.parseFloat(datapoint.getString("close"))));
                            lineSet.setColor(0xFF56B7F1);


                        }
                        DisplayStockActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DisplayStockActivity.this.setTitle(name);
                                //lineChartView = (ValueLineChart) findViewById(R.id.linechart);
                                lineChartView.addSeries(lineSet);
                                lineChartView.startAnimation();
                                lineChartView.setIndicatorTextColor(Color.WHITE);
                                date = (TextView)findViewById(R.id.stock_date_value);
                                close = (TextView)findViewById(R.id.stock_close_value);
                                high = (TextView) findViewById(R.id.stock_high_value);
                                low = (TextView) findViewById(R.id.stock_low_value);
                                open = (TextView)findViewById(R.id.stock_open_value);
                                volume = (TextView)findViewById(R.id.stock_volume_value);;
                                try{
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
                                    String date_value =jsonArray.getJSONObject((jsonArray.length()+1)/2).getString("Date");
                                    try{
                                        date.setText(android.text.format.DateFormat.
                                                getMediumDateFormat(getApplicationContext()).
                                                format(simpleDateFormat.parse(date_value)));
                                    }catch (ParseException p){}

                                    close.setText(jsonArray.getJSONObject((jsonArray.length()+1)/2).getString("close"));
                                    high.setText(jsonArray.getJSONObject((jsonArray.length()+1)/2).getString("high"));
                                    low.setText(jsonArray.getJSONObject((jsonArray.length()+1)/2).getString("low"));
                                    open.setText(jsonArray.getJSONObject((jsonArray.length()+1)/2).getString("open"));
                                    volume.setText(jsonArray.getJSONObject((jsonArray.length()+1)/2).getString("volume"));
                                }catch (JSONException e){}


                                lineChartView.setOnPointFocusedListener(new IOnPointFocusedListener() {
                                    @Override
                                    public void onPointFocused(int _PointPos) {


                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
                                        try{
                                            String date_value =jsonArray.getJSONObject(_PointPos).getString("Date");
                                            try{
                                                date.setText(android.text.format.DateFormat.
                                                        getMediumDateFormat(getApplicationContext()).
                                                        format(simpleDateFormat.parse(date_value)));
                                            }catch (ParseException p){}

                                            close.setText(jsonArray.getJSONObject(_PointPos).getString("close"));
                                            high.setText(jsonArray.getJSONObject(_PointPos).getString("high"));
                                            low.setText(jsonArray.getJSONObject(_PointPos).getString("low"));
                                            open.setText(jsonArray.getJSONObject(_PointPos).getString("open"));
                                            volume.setText(jsonArray.getJSONObject(_PointPos).getString("volume"));
                                        }catch (JSONException e){e.printStackTrace();}


                                    }
                                });

                            }
                        });
               }catch (Exception e){}
                }



            }
        });

    }
}
