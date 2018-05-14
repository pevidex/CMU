package com.cmu.group22.hoponcmu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cmu.group22.hoponcmu.Task.UserHistoryTask;

import java.util.ArrayList;
import java.util.List;

import classes.Location;

public class MyQuizActivity extends AppCompatActivity {

    ListView listView;
    GlobalContext globalContext;

    List<Location> locations;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myquiz);
        locations= new ArrayList<Location>();
        globalContext = (GlobalContext) getApplicationContext();
        listView = (ListView) findViewById(R.id.listView_myquiz);
        new UserHistoryTask(MyQuizActivity.this).execute(globalContext.getUserName());
        setMyquiz(locations);

    }
    public void test(){

    }

    private void setMyquiz(List< Location > locations){
        //this.locations = locations;

        //for debug

        /*locations.add(new Location("L1","res/123.jpg"));

        locations.add(new Location("L1","res/123.jpg"));

        locations.add(new Location("L2","res/123.jpg"));
        locations.add(new Location("L3","res/123.jpg"));
        locations.add(new Location("L4","res/123.jpg"));*/

        listView.setAdapter(new InitLocations(this,locations));
        listView.setOnItemClickListener(quizClickListener);

    }

    private AdapterView.OnItemClickListener quizClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(view.getContext(), QuizResultActivity.class);
            //String name_of_quiz = locations.get(i).getName();
            //Log.d("ATLAS",locations.size()+"");

            intent.putExtra("name_of_quiz",locations.get(i).getName());

            startActivity(intent);
        }
    };
    public void updateInterface(String reply) {
    }
    public void updateLocations(ArrayList<Location> locations){

        this.locations=locations;
        //init the list of locations
        listView.setAdapter(new InitLocations(this, locations));
        Log.d("updateLocations", Integer.toString(locations.size()));
    }
}

