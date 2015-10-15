package classes.rest.com.testpro;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;

public class Service extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service);

        new Caller().execute("");
    }

    private class Caller extends AsyncTask<String, Void, Void> {

        TextView vista = (TextView)findViewById(R.id.vista);
        private String totalizer = "";
        @Override
        protected Void doInBackground(String... params) {
            try{

                HttpClient httpClient = HttpClientBuilder.create().build();

                //HttpGet getRequest = new HttpGet("http://androidexample.com/media/webservice/JsonReturn.php");
                HttpGet getRequest = new HttpGet("http://seifernet.com:8080/skye/api/service");

                getRequest.addHeader("accept", "application/json");

                HttpResponse response = httpClient.execute(getRequest);

                if (response.getStatusLine().getStatusCode() != 200){
                    throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
                }

                BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
                String output = "";

                while ((output = br.readLine()) != null){
                    totalizer += output;
                }

            } catch (Exception e){
                return null;
            }
            return null;
        }

        protected void onPostExecute(Void unused) {
            //Here we parse de JSON

            String outPutData = "";
            try{
                JSONArray jsonArray = new JSONArray(totalizer);
                JSONArray miniArray;

                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObjectChild = (JSONObject)jsonArray.getJSONObject(i);
                    outPutData += "Hash code: " + jsonObjectChild.get("hash") + "\n";
                    outPutData += "Name: " + jsonObjectChild.get("name") + "\n";
                    outPutData += "Data: \n";
                    miniArray = jsonObjectChild.getJSONArray("data");
                    for (int j = 0; j < miniArray.length(); j++){
                        outPutData += "\"" + miniArray.getString(i) + "\"\n";
                    }
                }

                vista.setText(outPutData);
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}