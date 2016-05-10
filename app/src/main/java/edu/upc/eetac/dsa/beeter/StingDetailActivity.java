package edu.upc.eetac.dsa.beeter;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;

import edu.upc.eetac.dsa.beeter.client.BeeterClient;
import edu.upc.eetac.dsa.beeter.client.BeeterClientException;
import edu.upc.eetac.dsa.beeter.client.entity.Sting;

public class StingDetailActivity extends AppCompatActivity {


    private final static String TAG = StingDetailActivity.class.toString();
    private GetStingDetailTask mGetStingDetailTask = null;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sting_detail);
    }

    class GetStingDetailTask extends AsyncTask<Void, Void, String> {
        private String uri;
        public GetStingDetailTask(String uri) {
            this.uri = uri;

        }

        @Override
        protected String doInBackground(Void... params) {
            String jsonSting = null;
            try{
                jsonSting = BeeterClient.getInstance().getSting(uri);
            }catch(BeeterClientException e){
                // TODO: Handle gracefully
                Log.d(TAG, e.getMessage());
            }
            return jsonSting;
        }

        @Override
        protected void onPostExecute(String jsonSting) {
            TextView tvSubject = (TextView)findViewById(R.id.textsubjectdetail);
            TextView tvContent = (TextView)findViewById(R.id.textcontentdetail);
            TextView tvCreator = (TextView)findViewById(R.id.textcreatordetail);
            TextView tvCreationTime = (TextView)findViewById(R.id.textcreationtimestampdetail);
            TextView tvLastModifTime = (TextView)findViewById(R.id.textlastModifieddetail);

            // json -> sting
            Sting sting = (new Gson()).fromJson(jsonSting, Sting.class);
            tvSubject.setText(sting.getSubject());
            tvContent.setText(sting.getContent());
            tvCreator.setText(sting.getCreator());
            tvCreationTime.setText(sdf.format(sting.getCreationTimestamp()));
            tvLastModifTime.setText(sdf.format(sting.getLastModified()));
        }
    }
}
