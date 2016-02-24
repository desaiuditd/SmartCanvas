package in.incognitech.smartcanvas;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;

import java.util.Random;

public class PhoneUnlockReceiver extends BroadcastReceiver {
    public PhoneUnlockReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent i = new Intent(context, DrawActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(DrawActivity.class);
        stackBuilder.addNextIntent(i);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // an Intent broadcast.
        int id = new Random().nextInt();
        Notification notification = new Notification.Builder(context)
                .setContentTitle("SmartCanvas")
                .setContentText("Hey!!! Create A Note on SmartCanvas")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, notification);

    }
}
