package com.example.collapserecycler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        startService();
//        startSocketIO(); // quero ver se ele chega no log de connect ou se mostra pelo menos

//        runSocket();
    }

//    private void runSocket() {
//
//            try {
//              Socket  mSocket = IO.socket("http://chat.socket.io");
//            } catch (URISyntaxException e) {}
//        }

    private void startSocketIO() {
        try {
            final Socket socket = IO.socket("http://192.168.43.196:3000");
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.i("Sockets", "Connected");
                }
            }).on("teste1", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
//                    Log.i("Sockets", args[0]);
                    try {
                        JSONObject data ;
                        data = new JSONObject(String.valueOf(args[0]));
                        String name  = data.getString("name");
                        Log.i("Sockets", data.getString("Nome :" +name));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.e("Socket", "EVENT_CONNECT_ERROR"); // nice
                    // JSONObject obj = (JSONObject)args[0];
                    Exception err = (Exception) args[0];
                    Log.e("Socket", err.toString()); // nice vama ver se com server.ok continua parece

                }
            }).on(Socket.EVENT_ERROR, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.e("Socket", "error"); // vama ver se chega aqui
                }
            });
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    /*private void startSocketIO() {
        try {
            IO.Options opts = new IO.Options();
            opts.transports = new String[]{WebSocket.NAME}; //or Polling.NAME
            final Socket socket = IO.socket("http://192.168.43.196:3000", opts); // /application/v1/notify vamos testar se liga assim teu IP
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    Log.i("Noti","Connectou");
                    socket.emit("foo", "hi");
                    // socket.disconnect();
                }

            }).on("application", new Emitter.Listener() {

                @Override
                public void call(Object... args) {}

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {}

            }).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.e("Socket", "EVENT_CONNECT_ERROR"); // nice
                    // JSONObject obj = (JSONObject)args[0];
                    Exception err = (Exception) args[0];
                    Log.e("Socket", err.toString()); // nice vama ver se com server.ok continua parece
                    *//*for (Object obj : args) {
                        Log.v("SocketIO", "Errors :: " + obj);
                    }*//*
                }
            }).on(Socket.EVENT_ERROR, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.e("Socket", "error"); // vama ver se chega aqui
                }
            });
            socket.connect();
            Log.i("MainA", "connect...");
        } catch (URISyntaxException s) {
            s.printStackTrace();
        }
    }
*/

    public void stoptServiceListenner(View view) {

        Intent serviceIntent = new Intent(this, NotifService.class);
        stopService(serviceIntent);

    }

    public void startServiceListenner(View view) {
        Intent serviceIntent = new Intent(this, NotifService.class);
        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
        ContextCompat.startForegroundService(this, serviceIntent);
    }

  public void  startService(){
      Intent serviceIntent = new Intent(this, NotifService.class);
//      serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");

      ContextCompat.startForegroundService(this, serviceIntent);
    }
}
