package com.example.collapserecycler;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;


public class NotifService extends Service {

    public static final String CHANNEL_ID = "ForegroundServiceChannel";

    Socket socketIO;

    @Override
    public void onCreate() {
        super.onCreate();

        startSocket();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        return START_NOT_STICKY;



    }

    private void createNotificationChannel() {


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel =  new NotificationChannel(CHANNEL_ID , "Foreground Service Channel", NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);


        }else {
            stopSelf();
        }


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public void startSocket(){
        try {
            socketIO = IO.socket("http://192.168.43.196:3000");
            socketIO.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d("service","connectado ao servidor");

                socketIO.on("teste1", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {

                        try {
                            JSONObject data ;
                            data = new JSONObject(String.valueOf(args[0]));
                            String name  = data.getString("name");
                            String msg  = data.getString("msg");
                            notifyBroadCast(name,msg);

                            Log.d("output: " ,data.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
                }
            });

            socketIO.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d("service","Servidor offline");
                }
            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socketIO.connect();
    }

    public  void notifyBroadCast(String titulo ,  String msg){

        createNotificationChannel();
        Intent intentNotification =  new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intentNotification,0);
        Notification notification =  new NotificationCompat.Builder(this , CHANNEL_ID)
                .setContentTitle(titulo)
                .setContentText(msg)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1,notification); // Nao acontece nada, mesmo depois de chamares o serviço? yes
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, notification);
    }
}
