package com.jasons.easyoffice;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.jasons.easyoffice.fragment.InteractScreenFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {
    EditText edtScreenID;
    Spinner building;
    Spinner floor;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("https://easy-office.herokuapp.com/");
        } catch (URISyntaxException e) {
            Log.e("error",e.getMessage());
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        edtScreenID=(EditText)findViewById(R.id.edtSreen);
        building=(Spinner) findViewById(R.id.buildings);
        floor=(Spinner)findViewById(R.id.floor);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mSocket.connect();
      // mSocket.emit("move_up","datablalbal" );
        if(mSocket.connected()){
            Log.i("youhouy","youhou");
        }
        populateSpinner();
    }

    private void populateSpinner() {
        final ArrayList<String> floorArray = new ArrayList<String>();
        for (int i = 0; i <= 15; i++) {
            floorArray.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapterfloor = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, floorArray);
        floor.setAdapter(adapterfloor);

        final ArrayList<String> buildingArray = new ArrayList<String>();
            buildingArray.add(getString(R.string.boreal));
        buildingArray.add(getString(R.string.marais));
        buildingArray.add(getString(R.string.manathan));
        buildingArray.add(getString(R.string.chancelerie));
        buildingArray.add(getString(R.string.botanique));

        ArrayAdapter<String> adapterBuilding = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, buildingArray);
        building.setAdapter(adapterBuilding);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment newFragment = new InteractScreenFragment();
        Bundle bundle=new Bundle();
       if (id == R.id.nav_boreal) {


           bundle.putInt("floor",Integer.valueOf(getString(R.string.boreal_floor)));
           bundle.putString("building",getString(R.string.boreal));


        } else if (id == R.id.nav_marais) {
           bundle.putInt("floor",Integer.valueOf(getString(R.string.marais_floor)));
           bundle.putString("building",getString(R.string.marais));
        }else if(id==R.id.nav_manathan){
           bundle.putInt("floor",Integer.valueOf(getString(R.string.manathan_floor)));
           bundle.putString("building",getString(R.string.manathan));
       }else if(id==R.id.nav_botanique){

           bundle.putInt("floor",Integer.valueOf(getString(R.string.botanique_floor)));
           bundle.putString("building",getString(R.string.botanique));
       }
        newFragment.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.mainLayout, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public  Socket getinstanceSocket(){
        return mSocket;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnUp){

            try {
                mSocket.emit("move_up",createjsonobject(building.getSelectedItem().toString(),Integer.valueOf(floor.getSelectedItem().toString()),edtScreenID.getText().toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else  if(v.getId()==R.id.btnAll){
            try {
                mSocket.emit("all",createjsonobject(building.getSelectedItem().toString(),Integer.valueOf(floor.getSelectedItem().toString()),edtScreenID.getText().toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else  if(v.getId()==R.id.btnDown){
            try {
                mSocket.emit("move_down",createjsonobject(building.getSelectedItem().toString(),Integer.valueOf(floor.getSelectedItem().toString()),edtScreenID.getText().toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else  if(v.getId()==R.id.btnfloor) {
            try {
                mSocket.emit("floor", createjsonobject(building.getSelectedItem().toString(), Integer.valueOf(floor.getSelectedItem().toString()), edtScreenID.getText().toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private JSONObject createjsonobject(String building,int floor,String screenid) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("Building", building);
        json.put("Floor", floor);
        json.put("ScreenId", screenid);
        return json;
    }
}
