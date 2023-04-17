package com.example.primerproyecto;

import android.util.Log;

import androidx.annotation.NonNull;

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

        }
        // Si el mensaje es una notificacion
        if (remoteMessage.getNotification() != null) {

        }

    }
}
