package com.egkhan.bucketdrops;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.egkhan.bucketdrops.adapters.AdapterDrops;
import com.egkhan.bucketdrops.beans.Drop;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class ActivityMain extends AppCompatActivity {

    Toolbar toolbar;
    Button btnAdd;
    RecyclerView recyclerView;
    Realm realm;
    RealmResults<Drop> realmResults;
    final String TAG = "EgK";
    AdapterDrops adapterDrops;
    View.OnClickListener btnAddListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDialogAdd();
        }
    };
    private RealmChangeListener realmChangeListener = new RealmChangeListener() {
        @Override
        public void onChange(Object o) {
            adapterDrops.Update(realmResults);
        }
    };

    private void showDialogAdd() {
        DialogAdd dialogAdd = new DialogAdd();
        dialogAdd.show(getSupportFragmentManager(),"Add");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        realm = Realm.getDefaultInstance();
        realmResults = realm.where(Drop.class).findAllAsync();

        adapterDrops = new AdapterDrops(this,realmResults);

        toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(btnAddListener);
        recyclerView =  (RecyclerView) findViewById(R.id.rv_drops);
//        LinearLayoutManager manager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapterDrops);
        initBackgroundImage();

    }

    @Override
    protected void onStart() {
        super.onStart();
        realmResults.addChangeListener(realmChangeListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        realm.removeChangeListener(realmChangeListener);
    }

    private void initBackgroundImage() {
        ImageView background = (ImageView) findViewById(R.id.iv_background);
        Glide.with(this)
                .load(R.mipmap.background)
                .into(background);
    }
}
