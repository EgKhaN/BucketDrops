package com.egkhan.bucketdrops.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.egkhan.bucketdrops.R;
import com.egkhan.bucketdrops.beans.Drop;

import io.realm.RealmResults;

/**
 * Created by EgK on 6/19/2017.
 */

public class AdapterDrops extends RecyclerView.Adapter<AdapterDrops.DropHolder>{
    LayoutInflater layoutInflater;
    RealmResults<Drop> realmResults;
    public AdapterDrops(Context context,RealmResults<Drop> results)
    {
        layoutInflater = LayoutInflater.from(context);
        Update(results);
    }
    public void Update(RealmResults<Drop> results)
    {
        realmResults = results;
        notifyDataSetChanged();
    }

    @Override
    public DropHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowDropView = layoutInflater.inflate(R.layout.row_drop,parent,false);
        DropHolder holder = new DropHolder(rowDropView);
        return holder;
    }

    @Override
    public void onBindViewHolder(DropHolder holder, int position) {
        Drop drop = realmResults.get(position);
        holder.textWhat.setText(drop.getWhat());
    }

    @Override
    public int getItemCount() {
        return realmResults.size();
    }

    public static class DropHolder extends RecyclerView.ViewHolder{
TextView textWhat ;
        public DropHolder(View itemView) {
            super(itemView);
            textWhat = (TextView) itemView.findViewById(R.id.tv_what);
        }
    }
}
