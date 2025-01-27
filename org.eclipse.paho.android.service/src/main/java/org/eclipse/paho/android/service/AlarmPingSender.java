/*******************************************************************************
 * Copyright (c) 2014 IBM Corp.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution. 
 *
 * The Eclipse Public License is available at 
 *    http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at 
 *   http://www.eclipse.org/org/documents/edl-v10.php.
 */
package org.eclipse.paho.android.service;

import static android.content.Context.RECEIVER_NOT_EXPORTED;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.Settings;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttPingSender;
import org.eclipse.paho.client.mqttv3.internal.ClientComms;

/**
 * Default ping sender implementation on Android. It is based on AlarmManager.
 *
 * <p>This class implements the {@link MqttPingSender} pinger interface
 * allowing applications to send ping packet to server every keep alive interval.
 * </p>
 *
 * @see MqttPingSender
 */
class AlarmPingSender implements MqttPingSender {
    // Identifier for Intents, log messages, etc..
    static final String TAG = "AlarmPingSender";

    // TODO: Add log.
    private ClientComms comms;
    private MqttService service;
    private BroadcastReceiver alarmReceiver;
    private AlarmPingSender that;
    private PendingIntent pendingIntent;
    private volatile boolean hasStarted = false;

    public AlarmPingSender(MqttService service) {
        if (service == null) {
            throw new IllegalArgumentException(
                    "Neither service nor client can be null.");
        }
        this.service = service;
        that = this;
    }

    @Override
    public void init(ClientComms comms) {
        this.comms = comms;
        this.alarmReceiver = new AlarmReceiver();
    }

    @Override
    public void start() {
        String action = MqttServiceConstants.PING_SENDER
                + comms.getClient().getClientId();
        Log.d(TAG, "Register alarmreceiver to MqttService" + action);
        service.registerReceiver(alarmReceiver, new IntentFilter(action), RECEIVER_NOT_EXPORTED);

        pendingIntent = PendingIntent.getBroadcast(service, 0, new Intent(
                action), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        schedule(comms.getKeepAlive());
        hasStarted = true;
    }

    @Override
    public void stop() {
        // Cancel Alarm.
        AlarmManager alarmManager = (AlarmManager) service
                .getSystemService(Service.ALARM_SERVICE);
        if (alarmManager != null && pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
        }

        Log.d(TAG, "Unregister alarmreceiver to MqttService" + comms.getClient().getClientId());
        if (hasStarted) {
            hasStarted = false;
            try {
                service.unregisterReceiver(alarmReceiver);
            } catch (IllegalArgumentException e) {
                //Ignore unregister errors.
            }
        }
    }

    @Override
    public void schedule(long delayInMilliseconds) {
        long nextAlarmInMilliseconds = System.currentTimeMillis()
                + delayInMilliseconds;
        Log.d(TAG, "Schedule next alarm at " + nextAlarmInMilliseconds);
        AlarmManager alarmManager = (AlarmManager) service
                .getSystemService(Service.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                Log.i(TAG, "Cannot schedule exact alarms");
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                intent.setData(Uri.parse("package:" + service.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                service.startActivity(intent);
                return;
            }

        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextAlarmInMilliseconds,
                pendingIntent);
    }

    /*
     * This class sends PingReq packet to MQTT broker
     */
    class AlarmReceiver extends BroadcastReceiver {
        private WakeLock wakelock;
        private String wakeLockTag = MqttServiceConstants.PING_WAKELOCK
                + that.comms.getClient().getClientId();

        @Override
        public void onReceive(Context context, Intent intent) {
            // According to the docs, "Alarm Manager holds a CPU wake lock as
            // long as the alarm receiver's onReceive() method is executing.
            // This guarantees that the phone will not sleep until you have
            // finished handling the broadcast.", but this class still get
            // a wake lock to wait for ping finished.
            int count;
            try {
                count = intent.getIntExtra(Intent.EXTRA_ALARM_COUNT, -1);
            } catch (ClassCastException ex) {
                // This is a Motorola Phone (Probably a Moto G or X)
                // And so Intent.EXTRA_ALARM_COUNT is actually a Long!
                Long longCount = intent.getLongExtra(Intent.EXTRA_ALARM_COUNT, -1);
                count = longCount.intValue();
            }

            Log.d(TAG, "Ping " + count + " times.");

            Log.d(TAG, "Check time :" + System.currentTimeMillis());

            PowerManager pm = (PowerManager) service
                    .getSystemService(Service.POWER_SERVICE);
            wakelock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, wakeLockTag);
            wakelock.acquire();

            // Assign new callback to token to execute code after PingResq
            // arrives. Get another wakelock even receiver already has one,
            // release it until ping response returns.
            IMqttToken token = comms.checkForActivity(new IMqttActionListener() {

                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG, "Success. Release lock(" + wakeLockTag + "):"
                            + System.currentTimeMillis());
                    //Release wakelock when it is done.
                    wakelock.release();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    Log.d(TAG, "Failure. Release lock(" + wakeLockTag + "):"
                            + System.currentTimeMillis());
                    //Release wakelock when it is done.
                    wakelock.release();
                }
            });

            if (token == null && wakelock.isHeld()) {
                wakelock.release();
            }
        }
    }
}
