package com.egkhan.bucketdrops.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.egkhan.bucketdrops.AppBucketDrops;
import com.egkhan.bucketdrops.R;
import com.egkhan.bucketdrops.beans.Drop;
import com.egkhan.bucketdrops.extras.Util;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by EgK on 6/19/2017.
 */

public class AdapterDrops extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SwipeListener {

    ResetListener resetListener;
    LayoutInflater layoutInflater;
    RealmResults<Drop> realmResults;
    AddListener addListener;
    MarkListener markListener;
    Realm realm;
    int filterOption;
    Context context;

    public static final int COUNT_FOOTER = 1;
    public static final int COUNT_NO_ITEMS = 1;
    public static final int ITEM = 0;
    public static final int NO_ITEM = 1;
    public static final int FOOTER = 2;

//    public void setAddListener(AddListener listener)
//    {
//        this.addListener = listener;
//    }

    public AdapterDrops(Context context, Realm realm, RealmResults<Drop> results) {
        layoutInflater = LayoutInflater.from(context);
        this.realm = realm;
        Update(results);
    }

    public AdapterDrops(Context context, Realm realm, RealmResults<Drop> results, AddListener addListener, MarkListener markListener,ResetListener resetListener) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.realm = realm;
        Update(results);
        this.addListener = addListener;
        this.markListener = markListener;
        this.resetListener = resetListener;
    }

    public void Update(RealmResults<Drop> results) {
        realmResults = results;
        filterOption = AppBucketDrops.loadFilter(context);
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        if(position < realmResults.size())
        {
            return realmResults.get(position).getAdded();
        }
        return RecyclerView.NO_ID;
    }

    @Override
    public int getItemViewType(int position) {
//        if (realmResults == null || position < realmResults.size()) {
//            return ITEM;
//        } else
//            return FOOTER;
        if(!realmResults.isEmpty())
        {
            if(position<realmResults.size())
                return ITEM;
            else
                return FOOTER;
        }
        else{
            if(filterOption == Filter.COMPLETE || filterOption == Filter.INCOMPLETE)
                if(position == 0)
                    return NO_ITEM;
                else
                    return FOOTER;
            else
                return ITEM;
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FOOTER) {
            View rowDropView = layoutInflater.inflate(R.layout.footer, parent, false);
            return new FooterHolder(rowDropView, addListener);
        } if (viewType == NO_ITEM) {
            View rowDropView = layoutInflater.inflate(R.layout.no_item, parent, false);
            return new NoItemsHolder(rowDropView);
        } else {
            View rowDropView = layoutInflater.inflate(R.layout.row_drop, parent, false);
            return new DropHolder(rowDropView, markListener);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DropHolder) {
            DropHolder dropHolder = (DropHolder) holder;
            Drop drop = realmResults.get(position);
            dropHolder.setWhat(drop.getWhat());
            dropHolder.setWhen(drop.getWhen());
            dropHolder.setBackground(drop.isCompleted());
        }
    }

    @Override
    public int getItemCount() {
//        if (realmResults == null || realmResults.isEmpty())
//            return 0;
//        else
//            return realmResults.size() + 1;
        // +1 footer için
        if(!realmResults.isEmpty())
        {
            return realmResults.size()+COUNT_FOOTER;
        }
        else{
            if(filterOption == Filter.LEAST_TIME_LEFT || filterOption == Filter.MOST_TIME_LEFT || filterOption == Filter.NONE)
                return 0;
            else
            {
                return COUNT_NO_ITEMS + COUNT_FOOTER;
            }
        }
    }

    @Override
    public void onSwipe(int position) {
        if (position < realmResults.size()) {
            realm.beginTransaction();
            realmResults.get(position).deleteFromRealm();
            realm.commitTransaction();
            notifyItemRemoved(position);
        }
        resetFilterIfEmpty();
    }

    private void resetFilterIfEmpty() {
        if(realmResults.isEmpty() && (filterOption == Filter.COMPLETE|| filterOption == Filter.INCOMPLETE))
        {
            resetListener.onReset();
        }
    }

    public void markComplete(int position) {
        if (position < realmResults.size()) {
            realm.beginTransaction();
            realmResults.get(position).setCompleted(true);
            realm.commitTransaction();
            notifyItemChanged(position);
        }
    }

    public static class DropHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textWhat;
        TextView textWhen;
        MarkListener markListener;
        Context context;
        View itemView;

        public DropHolder(View itemView, MarkListener markListener) {
            super(itemView);
            this.itemView = itemView;
            context = itemView.getContext();
            itemView.setOnClickListener(this);
            textWhat = (TextView) itemView.findViewById(R.id.tv_what);
            textWhen = (TextView) itemView.findViewById(R.id.tv_when);
            this.markListener = markListener;
        }

        public void setWhat(String what) {
            textWhat.setText(what);
        }
        public void setWhen(long when) {
            textWhen.setText(DateUtils.getRelativeTimeSpanString(when, System.currentTimeMillis(),DateUtils.DAY_IN_MILLIS,0));
        }

        @Override
        public void onClick(View v) {
            markListener.onMark(getAdapterPosition());
        }

        public void setBackground(boolean completed) {
            Drawable drawable;
            if (completed) {
                drawable = ContextCompat.getDrawable(context, R.color.bg_drop_complete);
            } else {
                drawable = ContextCompat.getDrawable(context, R.drawable.bg_row_drop);
            }
            Util.setBackground(itemView,drawable);


        }


    }
    public  static class NoItemsHolder extends RecyclerView.ViewHolder{

        public NoItemsHolder(View itemView) {
            super(itemView);
        }
    }

    public static class FooterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button btnAdd_Footer;
        AddListener addListener;

        public FooterHolder(View itemView) {
            super(itemView);
            btnAdd_Footer = (Button) itemView.findViewById(R.id.btn_add_it_footer);
            btnAdd_Footer.setOnClickListener(this);
        }

        public FooterHolder(View itemView, AddListener listener) {
            super(itemView);
            btnAdd_Footer = (Button) itemView.findViewById(R.id.btn_add_it_footer);
            btnAdd_Footer.setOnClickListener(this);
            addListener = listener;
        }

        @Override
        public void onClick(View v) {
            addListener.Add();
        }
    }
}
