package com.example.primerproyecto;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class ServicioFirebase extends FirebaseMessagingService {
    public ServicioFirebase() {
    }

    // Qué hacer cada vez que se genere un token para el dispositivo
    @Override
    public void onNewToken(@NonNull String token) {
        Log.d("fcm", "Refreshed token: " + token);
    }
    // Si la aplicación está en background, no se ejecuta este método
    // Este metodo solo ejecuta cuando esta en Primer Plano y no muestra notificacion
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Si el mensaje viene con datos
        if (remoteMessage.getData().size() > 0) {
            Log.d("fcm", "Message data payload: " + remoteMessage.getData());
        }
        // Si el mensaje es una notificacion
        if (remoteMessage.getNotification() != null) {
            Log.d("fcm", "Message Notification Body: " + remoteMessage.getNotification().getBody());


            NotificationManager elManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder elBuilder = new NotificationCompat.Builder(this, "IdCanal");

            // Necesario para API mayor o igual que la de la version Oreo
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                NotificationChannel channel = new NotificationChannel("IdCanal", "canal", NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription("esElCanal");
                // Register the channel with the system. You can't change the importance
                // or other notification behaviors after this.
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);

                elBuilder.setSmallIcon(android.R.drawable.stat_sys_warning)
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setVibrate(new long[]{0, 1000, 500, 1000})
                        .setAutoCancel(true);


                elManager.notify(1, elBuilder.build());
            }
        }
    }


}


