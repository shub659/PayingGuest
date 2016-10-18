package com.example.uddishverma.pg_app_beta;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.google.android.gms.analytics.internal.zzy.s;

/**
 * Class to Find PGs
 */

public class FindPGActivity extends AppCompatActivity {

    public static final String TAG = "FindPGActivity";

    RecyclerView mrecyclerView;
    RecyclerView.Adapter madapter;
    RecyclerView.LayoutManager layoutManager;
    private ArrayList<PgDetails_POJO.PgDetails> cardDetails;
    Button filterButton;
    Intent filterActivityIntent;

    Toolbar toolbar;

    boolean isInternetConnected = false;

    Intent checkActivityCallerIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_find_pg);

        isNetworkConnected();

        filterActivityIntent = new Intent(this, FilterActivity.class);


/**
 * To add Swipe Back Layout, extend the class to SwipeBackActivity
 * setDragEdge(SwipeBackLayout.DragEdge.LEFT);
 * compile 'com.github.liuguangqiang.swipeback:library:1.0.2@aar' in GRADLE
 */

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        filterButton = (Button) findViewById(R.id.filter);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(filterActivityIntent);
            }
        });


        mrecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        cardDetails = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
        mrecyclerView.setLayoutManager(layoutManager);
        mrecyclerView.setHasFixedSize(true);
        madapter = new PgDetailsAdapter(cardDetails, this);
        mrecyclerView.setAdapter(madapter);

        if (isInternetConnected) {
            final SweetAlertDialog mDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            mDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            mDialog.setTitleText("Please Wait");
            mDialog.show();

            Firebase.setAndroidContext(this);

            RegisterPG.firebaseRef = new Firebase("https://pgapp-c51ce.firebaseio.com/");

            Log.d(TAG, "onCreate: " + RegisterPG.firebaseRef.orderByChild("ac").equalTo("true"));


            /***************************************************************************************/

            /**************************************************************************************/

            RegisterPG.firebaseRef.child("PgDetails").addChildEventListener(new ChildEventListener() {


                @JsonIgnoreProperties
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    checkActivityCallerIntent = getIntent();
                    Bundle b = checkActivityCallerIntent.getExtras();
                    final String intentSource = (String) b.get("source");

              /*  if (intentSource.equals("FilterActivity"))
                {
                    String localityCheckCode= (String) b.get("localityCheckCode");
                    String collegeCheckCode= (String) b.get("collegeCheckCode");
                    String finalCheckCode=localityCheckCode+collegeCheckCode;
                    Log.d(TAG,"hello"+finalCheckCode);
                }*/


                    Log.d(TAG, intentSource);

                    /***********************************************************************************/
                    if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                        if (intentSource.equals("FilterActivity")) {
                            if (b != null) {

                                String localityCheckCode = (String) b.get("localityCheckCode");
                                String collegeCheckCode = (String) b.get("collegeCheckCode");
                                String finalCheckCode = localityCheckCode + collegeCheckCode;
                          /*  Log.d(TAG,localityCheckCode);
                            Log.d(TAG,collegeCheckCode);
                          */
                                Log.d(TAG, finalCheckCode);


                                if (finalCheckCode.equals("01")) {

                                    ArrayList<String> filteredLocalityList = new ArrayList<String>();
                                    filteredLocalityList = checkActivityCallerIntent.getStringArrayListExtra("filteredLocalityList");

                                    for (int i = 0; i < filteredLocalityList.size(); i++) {
                                        if (dataSnapshot.child("locality").getValue().equals(filteredLocalityList.get(i))) {
                                            PgDetails_POJO.PgDetails model = dataSnapshot
                                                    .getValue(PgDetails_POJO.PgDetails.class);
                                            cardDetails.add(model);
                                            madapter.notifyDataSetChanged();
                                            mDialog.dismiss();
                                        }
                                    }


                                    ArrayList<String> filteredCollegeList = new ArrayList<String>();
                                    filteredCollegeList = checkActivityCallerIntent.getStringArrayListExtra("filteredCollegesList");

                                    for (int i = 0; i < filteredCollegeList.size(); i++) {
                                        if (dataSnapshot.child("nearbyInstitute").getValue().equals(filteredCollegeList.get(i))) {
                                            PgDetails_POJO.PgDetails model = dataSnapshot
                                                    .getValue(PgDetails_POJO.PgDetails.class);
                                            cardDetails.add(model);
                                            madapter.notifyDataSetChanged();
                                            mDialog.dismiss();
                                        }
                                    }


                                } else if (localityCheckCode != null) {
                                    ArrayList<String> filteredLocalityList = new ArrayList<String>();
                                    filteredLocalityList = checkActivityCallerIntent.getStringArrayListExtra("filteredLocalityList");

                                    for (int i = 0; i < filteredLocalityList.size(); i++) {
                                        if (dataSnapshot.child("locality").getValue().equals(filteredLocalityList.get(i))) {
                                            PgDetails_POJO.PgDetails model = dataSnapshot
                                                    .getValue(PgDetails_POJO.PgDetails.class);
                                            cardDetails.add(model);
                                            madapter.notifyDataSetChanged();
                                            mDialog.dismiss();
                                        }
                                    }
                                } else if (collegeCheckCode != null) {
                                    ArrayList<String> filteredCollegeList = new ArrayList<String>();
                                    filteredCollegeList = checkActivityCallerIntent.getStringArrayListExtra("filteredCollegesList");

                                    for (int i = 0; i < filteredCollegeList.size(); i++) {
                                        if (dataSnapshot.child("nearbyInstitute").getValue().equals(filteredCollegeList.get(i))) {
                                            PgDetails_POJO.PgDetails model = dataSnapshot
                                                    .getValue(PgDetails_POJO.PgDetails.class);
                                            cardDetails.add(model);
                                            madapter.notifyDataSetChanged();
                                            mDialog.dismiss();
                                        }
                                    }
                                }


                           /* ArrayList<String> filteredLocalityList = new ArrayList<String>();
                            filteredLocalityList = checkActivityCallerIntent.getStringArrayListExtra("filteredLocalityList");

                            for (int i = 0; i < filteredLocalityList.size(); i++)
                            {
                                if (dataSnapshot.child("locality").getValue().equals(filteredLocalityList.get(i)))
                                {
                                    PgDetails_POJO.PgDetails model = dataSnapshot
                                            .getValue(PgDetails_POJO.PgDetails.class);
                                    cardDetails.add(model);
                                    madapter.notifyDataSetChanged();
                                    mDialog.dismiss();
                                }
                            }*/
                            }

                        }


                        /**********************************************************************************************/


                        else if (intentSource.equals("MainActivity")) {

                            Log.d(TAG, "onChildAdded: " + dataSnapshot.child("PgDetails").getValue());

                            /**
                             * This statement will be used to query from the firebase wrt to a particular POJO field
                             * The below log statement displays the city anit value
                             * Log.d(TAG, "onChildAdded: KEY VALUE : " + (dataSnapshot.child("city")));
                             *
                             * And this is used tp display cardViews where CITY = DELHI
                             * Log.d(TAG, "onChildAdded: KEY VALUE : " + (dataSnapshot.child("city").getValue().equals("delhi")));
                             */

                            Log.d(TAG, "onChildAdded: KEY VALUE : " + (dataSnapshot.child("city").getValue().equals("delhi")));


                            PgDetails_POJO.PgDetails model = dataSnapshot
                                    .getValue(PgDetails_POJO.PgDetails.class);
                            cardDetails.add(model);
                            madapter.notifyDataSetChanged();

                            //Stopping the progress dialogue

                            mDialog.dismiss();
                        }

                    }
                    mDialog.dismiss();

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
    }

    private void isNetworkConnected() {

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            isInternetConnected = true;
        } else {
            isInternetConnected = false;
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("No Internet")
                    .setContentText("Please Check Your Internet Connection!")
                    .show();
        }
    }
}