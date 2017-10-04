package com.example.amitdasgupta.storeintolaptopusingxampp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    EditText etID, etName,etAddress;
    Button btn;
    String result = "", text;
    String name, id , address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etID = (EditText) findViewById(R.id.editText);
        etName = (EditText) findViewById(R.id.editText2);
        etAddress = (EditText) findViewById(R.id.editText3);
        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=etName.getText().toString();
                id = etID.getText().toString();
                address = etAddress.getText().toString();
               // phone=ephone.getText().toString();
                new back().execute();
            }
        });
    }

    class back extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                postData();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Login");
            pd.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            pd.cancel();
            //lv.setAdapter(adp);
            Toast.makeText(MainActivity.this,text,Toast.LENGTH_LONG).show();
        }
    }

    public void postData() throws JSONException
    {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://192.168.43.97/amitxampp/insertii.php");
        JSONObject jObject = new JSONObject();

        try{
            jObject.put("id",id);
            jObject.put("name",name);
            jObject.put("add",address);


            JSONArray postjson = new JSONArray();
            postjson.put(jObject);
            httpPost.setHeader("json",jObject.toString());
            httpPost.getParams().setParameter("jsonpost",postjson);

            HttpResponse response = httpClient.execute(httpPost);
            if(response!=null)
            {
                InputStream is = response.getEntity().getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line = null;
                try{
                    while((line=reader.readLine())!=null)
                    {
                        sb.append(line+"\n");
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                finally {
                    try{
                        is.close();
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                text = sb.toString();
            }

        }
        catch(ClientProtocolException e)
        {
        }
        catch (IOException e)
        {
        }
    }
}








