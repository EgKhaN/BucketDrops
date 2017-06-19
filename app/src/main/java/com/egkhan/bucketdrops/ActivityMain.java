package com.egkhan.bucketdrops;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.egkhan.bucketdrops.adapters.AdapterDrops;
import com.egkhan.bucketdrops.beans.Drop;
import com.egkhan.bucketdrops.widgets.BucketRecyclerView;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class ActivityMain extends AppCompatActivity {
    final String TAG = "EgK";
    Toolbar toolbar;
    Button btnAdd;
    BucketRecyclerView recyclerView;
    Realm realm;
    RealmResults<Drop> realmResults;
    AdapterDrops adapterDrops;
    View emptyDropsView;
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
        emptyDropsView = findViewById(R.id.empty_drops);

        recyclerView =  (BucketRecyclerView) findViewById(R.id.rv_drops);

//        LinearLayoutManager manager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapterDrops);
        recyclerView.hideIfEmpty(toolbar);
        recyclerView.showIfEmpty(emptyDropsView);

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
