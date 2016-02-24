package in.incognitech.smartcanvas;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class PlaySound extends Service {

    private MediaPlayer mediaPlayer;

    public PlaySound() {
        super();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            if(mediaPlayer==null || !mediaPlayer.isPlaying()) {
                mediaPlayer = MediaPlayer.create(this, R.raw.eraser);
                mediaPlayer.start();
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }
}
