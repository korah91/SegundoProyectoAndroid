<?php


# Si se ha accedido con un post con el token del dispositivo hay que enviar 
if(isset($_POST['token'])){
    $token=$_POST['token'];
    
    # Se declara la cabecera
    $cabecera = array(
        'Authorization:key=AAAAd403vNY:APA91bH2GdpWoP6ySOXaNMQecknr82g0XZQafx1kI6tjCCh5Dff0KbxRC2BE1HkJAcI3k8VGb4Ymqyf-98UMEHq_hQeRHAVEjTCfYxCg8yhjxkBbzpIzmb4ileNvwHG0VL2CfzAYeWe_',
        'Content-Type:application/json'
    );

    $msg= array(
        'to'=>$token,
        'notification'=> array(
            'body' => 'Usaremos tu imagen de la universidad para ayudar a más gente',
            'title' => '¡Gracias por tu imagen!',
            'icon' => 'ic_stat_ic_notification',
            'click_action'=>"AVISO")
        );
    # Se convierte a JSON
    $msgJSON= json_encode($msg);

    $ch = curl_init(); #inicializar el handler de curl
    #indicar el destino de la petición, el servicio FCM de google
    curl_setopt( $ch, CURLOPT_URL, 'https://fcm.googleapis.com/fcm/send');
    #indicar que la conexión es de tipo POST
    curl_setopt( $ch, CURLOPT_POST, true );
    #agregar las cabeceras
    curl_setopt( $ch, CURLOPT_HTTPHEADER, $cabecera);
    #Indicar que se desea recibir la respuesta a la conexión en forma de string
    curl_setopt( $ch, CURLOPT_RETURNTRANSFER, true );
    #agregar los datos de la petición en formato JSON
    curl_setopt( $ch, CURLOPT_POSTFIELDS, $msgJSON );
    #ejecutar la llamada
    $resultado= curl_exec( $ch );
    #cerrar el handler de curl
    curl_close( $ch );

    if (curl_errno($ch)) {
        print curl_error($ch);
        }
        echo $resultado;}
    else{
        echo $resultado;
    }
    echo $cabecera[0];

?>