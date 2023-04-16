package com.example.primerproyecto;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class ServicioFirebase extends FirebaseMessagingService {
    public ServicioFirebase() {
    }

    // Qué hacer cada vez que se genere un token para el dispositivo
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

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
