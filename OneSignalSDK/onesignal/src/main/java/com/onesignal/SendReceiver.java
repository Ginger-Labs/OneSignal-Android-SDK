package com.onesignal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.RemoteInput;
import android.util.Log;

public class SendReceiver extends BroadcastReceiver {

    public static OnInstantReplyListener onInstantReplyListener;

    public interface OnInstantReplyListener {
        void onReply(String replyText);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);

        if (remoteInput != null) {
            final CharSequence replySeq = remoteInput.getCharSequence("key_reply");
            if (replySeq != null) {
                if (onInstantReplyListener != null) {
                    final PendingResult result = goAsync();
                    Thread thread = new Thread() {
                        public void run() {
                            onInstantReplyListener.onReply(replySeq.toString());
                            // Do processing
                            result.finish();
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

}