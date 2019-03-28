package com.example.population;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.LruCache;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    BeanClass beanClass;
    Thread thread;
    private Handler handler;
    ArrayList<BeanClass> countries;
    String url;
    ImageView imageFlag;
    int count = 0;
    TextView txtRank, txtName, txtPopulation;
    Button nxtButton, prevButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        url = "http://www.androidbegin.com/tutorial/jsonparsetutorial.txt";
        imageFlag = findViewById(R.id.imageView);

        txtRank = findViewById(R.id.rankTextView);
        txtName = findViewById(R.id.nameTextView);
        txtPopulation = findViewById(R.id.populationTextView);

        nxtButton = findViewById(R.id.nextButton);
        prevButton = findViewById(R.id.PreviousButton);

        countries = new ArrayList<>();


        beanClass = new BeanClass();
        thread = new Thread(new HandlerClass());
        thread.start();
        //handler = new Handler();

    }


    private void showCountry(int page) {

        txtRank.setText(String.valueOf(countries.get(page).getRank()));
        txtName.setText(countries.get(page).getCountry());
        txtPopulation.setText(countries.get(page).getPopulation());

        new MyAsyncTask().execute(countries.get(page).getFlag());

    }

    @Override
    protected void onResume() {
        super.onResume();
        handler = new Handler() {
            public void handleMessage(Message msg) {
                showCountry(count);

                nxtButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        count++;
                        if (count > 9) {
                            count = 0;
                        }
                        showCountry(count);
                    }
                });


                prevButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        count--;
                        if (count < 0) {
                            count = 9;
                        }
                        showCountry(count);
                    }
                });

            }


        };
    }


    private void download(String urlText) throws IOException, JSONException {
        String response;
        URL url = new URL(urlText);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        InputStream inputStream = new BufferedInputStream(connection.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append('\n');
        }
        response = sb.toString();

        JSONObject jsonObj = new JSONObject(response);
        JSONArray worldPopulation = jsonObj.getJSONArray("worldpopulation");
        for (int i = 0; i < worldPopulation.length(); i++) {
            JSONObject jsonObject = worldPopulation.getJSONObject(i);
            BeanClass countryModel = new BeanClass();
            countryModel.setRank(jsonObject.getInt("rank"));
            countryModel.setCountry(jsonObject.getString("country"));
            countryModel.setPopulation(jsonObject.getString("population"));
            countryModel.setFlag(jsonObject.getString("flag"));
            countries.add(countryModel);
        }

    }

    private class MyAsyncTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        public Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream input = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        public void onPostExecute(Bitmap result) {
            imageFlag.setImageBitmap(result);
        }
    }


    class HandlerClass implements Runnable {
        @Override
        public void run() {
            try {
                download(url);
                handler.sendEmptyMessage(0);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}


