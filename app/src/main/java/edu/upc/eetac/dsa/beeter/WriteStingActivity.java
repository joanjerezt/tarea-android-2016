package edu.upc.eetac.dsa.beeter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import java.net.URL;

import edu.upc.eetac.dsa.beeter.client.BeeterClient;
import edu.upc.eetac.dsa.beeter.client.BeeterClientException;

public class WriteStingActivity extends AppCompatActivity {

    private class PostStingTask extends AsyncTask<String, Void, String> {

        private String mSubject = null;
        private String mContent = null;

        PostStingTask(String subject, String content) throws BeeterClientException {
            mSubject = subject;
            mContent = content;
        }

        private ProgressDialog pd;


        @Override
        protected String doInBackground(String... params) {
            String jsonSting = null;
            try{
                jsonSting = BeeterClient.getInstance().createSting(mSubject, mContent);
            }
         catch (BeeterClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
            return jsonSting;
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(WriteStingActivity.this);

            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }

        @Override
        protected void onPostExecute(String result) {
            showStings();
            if (pd != null) {
                pd.dismiss();
            }
        }
    }

    String uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_sting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditText etSubject = (EditText) findViewById(R.id.etSubject);
        EditText etContent = (EditText) findViewById(R.id.etContent);

        String subject = etSubject.getText().toString();
        String content = etContent.getText().toString();

        try {
            (new PostStingTask(subject,content)).execute(subject, content);
        } catch (BeeterClientException e) {
            e.printStackTrace();
        }
    }

    private void showStings() {
        Intent intent = new Intent(this, StingsListActivity.class);
        startActivity(intent);
    }

}
