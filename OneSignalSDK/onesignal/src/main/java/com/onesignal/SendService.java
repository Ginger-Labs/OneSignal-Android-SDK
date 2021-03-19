package com.onesignal;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.RemoteInput;
import android.util.Log;

public class SendService extends Service {

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        if (intent != null) {
            Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);

            if (remoteInput != null) {
                final CharSequence replySeq = remoteInput.getCharSequence("key_reply");
                if (replySeq != null) {
                    if (OneSignal.onInstantReplyListener != null) {
                        Thread thread = new Thread() {
                            public void run() {
                                OneSignal.onInstantReplyListener.onReply(replySeq.toString(), intent);
                            }
                        };
                        thread.start();
                    } else {
                        Log.e(this.getClass().getSimpleName(), "onReceive: Got instant reply but no onInstantReplyListener to fire.");
                    }
                } else {
                    Log.e(this.getClass().getSimpleName(), "onReceive: Instant reply CharSequence is null");
                }
            }
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}