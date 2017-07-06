package com.egkhan.bucketdrops.services;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;

import com.egkhan.bucketdrops.ActivityMain;
import com.egkhan.bucketdrops.R;
import com.egkhan.bucketdrops.beans.Drop;

import br.com.goncalves.pugnotification.notification.PugNotification;
import io.realm.Realm;
import io.realm.RealmResults;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class NotificationService extends IntentService {
    
    
    public NotificationService() {
        super("NotificationService");
    }
    
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Realm realm = null;
            try {
                realm = Realm.getDefaultInstance();
                RealmResults<Drop> realmResults = realm.where(Drop.class).equalTo("completed", false).findAll();
    
                for (Drop currentDrop : realmResults) {
                    if (isNotificationNeeded(currentDrop.getAdded(),currentDrop.getWhen()))
                    {
                        fireNotification(currentDrop);
                    }
                }
            } finally {
                if (realm != null) {
                    realm.close();
                }
            }
        }
    }
    
    private void fireNotification(Drop drop) {
        PugNotification.with(this)
                .load()
                .title("Acthivement")
                .message("You are nearing your goal" +drop.getWhat())
                .bigTextStyle("Congratulations,Your are on the verge of accomplishing your goal")
                .smallIcon(R.drawable.ic_drop)
                .largeIcon(R.drawable.pugnotification_ic_launcher)
                .flags(Notification.DEFAULT_ALL)
                .autoCancel(true)
                .click(ActivityMain.class)
                .simple()
                .build();
    }
    
    private boolean isNotificationNeeded(long added, long when) {
        long now = System.currentTimeMillis();
        if (now > when)
            return false;
        else {
            long difference90 = (long) (0.9 * (when - added));
            return (now>(added+difference90));
        }
    }
    
}
