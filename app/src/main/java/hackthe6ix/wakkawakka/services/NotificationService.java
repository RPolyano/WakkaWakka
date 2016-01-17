package hackthe6ix.wakkawakka.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import hackthe6ix.wakkawakka.MapsActivity;
import hackthe6ix.wakkawakka.PlayerType;
import hackthe6ix.wakkawakka.callbacks.NotificationEvent;
import hackthe6ix.wakkawakka.eventbus.EventBus;

public class NotificationService extends Service implements NotificationEvent {
    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        EventBus.NOTIFICATION_EVENTBUS.register(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        EventBus.NOTIFICATION_EVENTBUS.unregister(this);
        super.onDestroy();
    }

    @Override
    public void OnNotify(NotificationInfo info) {
        Log.i("Player", "Notify: " + info.text);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification.Builder mBuilder =
                new Notification.Builder(this)
                        .setSmallIcon(PlayerType.getDrawableID(info.iconType))
                        .setContentTitle("WakkaWakka Alert")
                        .setContentText(info.text)
                        .setSound(soundUri)
                        .setVibrate(new long[] {0, 200, 200, 200, 200});

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MapsActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MapsActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
    }
}
