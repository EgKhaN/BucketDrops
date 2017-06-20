package com.egkhan.bucketdrops.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.egkhan.bucketdrops.R;
import com.egkhan.bucketdrops.beans.Drop;

import io.realm.RealmResults;

/**
 * Created by EgK on 6/19/2017.
 */

public class AdapterDrops extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    LayoutInflater layoutInflater;
    RealmResults<Drop> realmResults;

    public static final int ITEM = 0;
    public static final int FOOTER = 1;

    public AdapterDrops(Context context, RealmResults<Drop> results) {
        layoutInflater = LayoutInflater.from(context);
        Update(results);
    }

    public void Update(RealmResults<Drop> results) {
        realmResults = results;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (realmResults == null || position < realmResults.size()) {
            return ITEM;
        } else
            return FOOTER;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FOOTER) {
            View rowDropView = layoutInflater.inflate(R.layout.footer, parent, false);
            return new FooterHolder(rowDropView);
        } else {
            View rowDropView = layoutInflater.inflate(R.layout.row_drop, parent, false);
            return new DropHolder(rowDropView);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DropHolder) {
            DropHolder dropHolder = (DropHolder) holder;
            Drop drop = realmResults.get(position);
            dropHolder.textWhat.setText(drop.getWhat());
        }
    }

    @Override
    public int getItemCount() {
        // +1 footer için
        return realmResults.size() + 1;
    }

    public static class DropHolder extends RecyclerView.ViewHolder {
        TextView textWhat;

        public DropHolder(View itemView) {
            super(itemView);
            textWhat = (TextView) itemView.findViewById(R.id.tv_what);
        }
    }

    public static class FooterHolder extends RecyclerView.ViewHolder {
        Button btnAdd_Footer;

        public FooterHolder(View itemView) {
            super(itemView);
            btnAdd_Footer = (Button) itemView.findViewById(R.id.btn_add_it_footer);
        }
    }
}
