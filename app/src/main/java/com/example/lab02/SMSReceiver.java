package com.example.lab02;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;


public class SMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        for (int i = 0; i < messages.length; i++) {
            SmsMessage currentMessage = messages[i];
            String message = currentMessage.getDisplayMessageBody();
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();

            Intent smsReceivedIntent = new Intent("SMS_RECEIVED_ACTION");
            smsReceivedIntent.putExtra("message", message);
            context.sendBroadcast(smsReceivedIntent);
        }
    }
}
