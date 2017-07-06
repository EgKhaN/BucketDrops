package com.egkhan.bucketdrops;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.egkhan.bucketdrops.adapters.AdapterDrops;
import com.egkhan.bucketdrops.adapters.AddListener;
import com.egkhan.bucketdrops.adapters.CompleteListener;
import com.egkhan.bucketdrops.adapters.Divider;
import com.egkhan.bucketdrops.adapters.Filter;
import com.egkhan.bucketdrops.adapters.MarkListener;
import com.egkhan.bucketdrops.adapters.ResetListener;
import com.egkhan.bucketdrops.adapters.SimpleTouchCallback;
import com.egkhan.bucketdrops.beans.Drop;
import com.egkhan.bucketdrops.extras.Util;
import com.egkhan.bucketdrops.widgets.BucketRecyclerView;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

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

    AddListener addListener = new AddListener() {
        @Override
        public void Add() {
            showDialogAdd();
        }
    };
    private RealmChangeListener realmChangeListener = new RealmChangeListener() {
        @Override
        public void onChange(Object o) {
            adapterDrops.Update(realmResults);
        }
    };

    MarkListener markListener = new MarkListener() {
        @Override
        public void onMark(int position) {
            showDialogMark(position);
        }
    };
    CompleteListener completeListener = new CompleteListener() {
        @Override
        public void onComplete(int position) {
            adapterDrops.markComplete(position);

        }
    };
    ResetListener resetListener = new ResetListener() {

        @Override
        public void onReset() {
            AppBucketDrops.saveFilter(ActivityMain.this,Filter.NONE);
            loadResults(Filter.NONE);
        }
    };

    private void showDialogAdd() {
        DialogAdd dialogAdd = new DialogAdd();
        dialogAdd.show(getSupportFragmentManager(), "Add");
    }

    private void showDialogMark(int position) {
        DialogMark dialogMark = new DialogMark();
        Bundle bundle = new Bundle();
        bundle.putInt("POSITION", position);
        dialogMark.setArguments(bundle);
        dialogMark.setCompleteListener(completeListener);
        dialogMark.show(getSupportFragmentManager(), "Mark");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        realm = Realm.getDefaultInstance();
        int filterOption = AppBucketDrops.loadFilter(this);
        loadResults(filterOption);

        adapterDrops = new AdapterDrops(this, realm, realmResults, addListener, markListener,resetListener);
        //adapterDrops.setAddListener(addListener);
        adapterDrops.setHasStableIds(true);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(btnAddListener);
        emptyDropsView = findViewById(R.id.empty_drops);

        recyclerView = (BucketRecyclerView) findViewById(R.id.rv_drops);
        recyclerView.addItemDecoration(new Divider(this, LinearLayoutManager.VERTICAL));

//        LinearLayoutManager manager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapterDrops);
        recyclerView.hideIfEmpty(toolbar);
        recyclerView.showIfEmpty(emptyDropsView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        SimpleTouchCallback simpleTouchCallback = new SimpleTouchCallback(adapterDrops);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        initBackgroundImage();

        Util.scheduleAlarm(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        realmResults.addChangeListener(realmChangeListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        boolean handled = true;
        int filterOption = Filter.NONE;
        switch (id) {
            case R.id.action_add:
                showDialogAdd();
                break;
            case R.id.action_none:
               filterOption = Filter.NONE;
                break;case R.id.action_sort_ascending_date:
               filterOption = Filter.LEAST_TIME_LEFT;
                break;
            case R.id.action_sort_descending_date:
                filterOption = Filter.MOST_TIME_LEFT;
                break;
            case R.id.action_show_complete:
                filterOption = Filter.COMPLETE;
                break;
            case R.id.action_show_incomplete:
                filterOption = Filter.INCOMPLETE;
                break;
            default:
                handled = false;
                break;
        }
        AppBucketDrops.saveFilter(this,filterOption);
        loadResults(filterOption);
        return handled;
    }
    void loadResults(int filterOption)
    {
        switch (filterOption)
        {
            case Filter.NONE:
                realmResults = realm.where(Drop.class).findAllAsync();
                break;
            case Filter.LEAST_TIME_LEFT:
                realmResults = realm.where(Drop.class).findAllSortedAsync("when");
                break;
            case Filter.MOST_TIME_LEFT:
                realmResults = realm.where(Drop.class).findAllSortedAsync("when", Sort.DESCENDING);
                break;
            case Filter.COMPLETE:
                realmResults = realm.where(Drop.class).equalTo("completed", true).findAllAsync();
                break;
            case Filter.INCOMPLETE:
                realmResults = realm.where(Drop.class).equalTo("completed", false).findAllAsync();
                break;
        }
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
