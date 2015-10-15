package classes.rest.com.testpro;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;

public class Service_Data extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_data);

        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            putParameter();
        }
    };

    public void putParameter( ){

        EditText editText = (EditText)findViewById(R.id.paramData);

        if (editText.getText().toString() == ""){
            Toast toast = Toast.makeText(getApplicationContext(), "Please write something: " , Toast.LENGTH_LONG);
            toast.show();
        } else {
            String param = editText.getText().toString();

            new Consumer().execute(param);
        }
    }

    private class Consumer extends AsyncTask<String, Void, Void>{

        TextView vista = (TextView)findViewById(R.id.dataView);
        private String totalizer = "";
        @Override
        protected Void doInBackground(String... params) {
            try{

                HttpClient httpClient = HttpClientBuilder.create().build();

                HttpGet getRequest = new HttpGet("http://seifernet.com:8080/skye/api/service" + "/" + params[0]);

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
            JSONObject jsonResponse;
            try{
                jsonResponse = new JSONObject(totalizer);

                outPutData += "Hash code: " + jsonResponse.get("hash") + "\n";
                outPutData += "Name: " + jsonResponse.get("name") + "\n";
                outPutData += "Data: \n";
                //Create array
                JSONArray jsonArray = jsonResponse.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++){
                    outPutData += "\"" + jsonArray.getString(i) + "\"\n";
                }

                vista.setText(outPutData);
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
