package edu.upc.eetac.dsa.beeter;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;

import edu.upc.eetac.dsa.beeter.client.BeeterClient;
import edu.upc.eetac.dsa.beeter.client.BeeterClientException;
import edu.upc.eetac.dsa.beeter.client.StingCollectionAdapter;
import edu.upc.eetac.dsa.beeter.client.entity.Sting;
import edu.upc.eetac.dsa.beeter.client.entity.StingCollection;

public class StingsListActivity extends AppCompatActivity {

    private StingCollection stings = new StingCollection();

    private StingCollectionAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stings_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Execute AsyncTask
        mGetStingsTask = new GetStingsTask(null);
        mGetStingsTask.execute((Void) null);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // set list OnItemClick listener
        AdapterView list = null;

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, BeeterClient.getLink(stings.getStings().get(position).getLinks(), "self").getUri().toString());
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(StingsListActivity.this, StingDetailActivity.class);
                String uri = BeeterClient.getLink(stings.getStings().get(position).getLinks(), "self").getUri().toString();
                intent.putExtra("uri", uri);
                startActivity(intent);
            }
        });
    }

    private final static String TAG = StingsListActivity.class.toString();
    private GetStingsTask mGetStingsTask = null;

    class GetStingsTask extends AsyncTask<Void, Void, String> {
        private String uri;
        public GetStingsTask(String uri) {
            this.uri = uri;

        }

        @Override
        protected String doInBackground(Void... params) {
            String jsonStingCollection = null;
            try{
                jsonStingCollection = BeeterClient.getInstance().getStings(uri);
            }catch(BeeterClientException e){
                // TODO: Handle gracefully
                Log.d(TAG, e.getMessage());
            }
            return jsonStingCollection;
        }

        @Override
        protected void onPostExecute(String jsonStingCollection) {
                Log.d(TAG, jsonStingCollection);
                StingCollection stingCollection = (new Gson()).fromJson(jsonStingCollection, StingCollection.class);
                for(Sting sting : stingCollection.getStings()){
                    stings.getStings().add(stings.getStings().size(), sting);
                }
                adapter.notifyDataSetChanged();
            }
        }
    }
