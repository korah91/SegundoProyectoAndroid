<?php

$DB_SERVER="localhost"; #la dirección del servidor
$DB_USER="Xjgarcia424"; #el usuario para esa base de datos
$DB_PASS="XWK61WA2S"; #la clave para ese usuario
$DB_DATABASE="Xjgarcia424_universidades"; #la base de datos a la que hay que conectarse
# Se establece la conexión:
$con = mysqli_connect($DB_SERVER, $DB_USER, $DB_PASS, $DB_DATABASE);
#Comprobamos conexión
if (mysqli_connect_errno()) {
	echo 'Error de conexion: ' . mysqli_connect_error();
	exit();
}

# Se obtiene el parametro, que dira que funcion se va a ejecutar
$parametro = $_POST["parametro"];


# Identificacion de usuarios
if ($parametro == "identificacion") {
	
	$email = $_POST["email"];
	$password = $_POST["password"];
	
	
	# Primero se comprueba que existe el usuario para poder comprobar su clave cifrada con la que manda la aplicacion
		#$consulta = $con->prepare("SELECT * FROM t_usuarios WHERE email=:email");
		#$consulta->bindParam("email", $email, PDO::PARAM_STR);
	
	$resultado = $con->query("SELECT * FROM t_usuarios WHERE email='$email'");
	$fila = mysqli_fetch_assoc($resultado);
	$passwordHash = $fila['password'];

	# Comprobar si el email existe. No se informa al usuario de si lo que se ha introducido mal es el email o la contrasena por seguridad
	if (!$resultado) {
		echo "Mal";
	} else { 
		# Si la contraseña coincide con la contrasena guardada se devuelve Bien
		if (password_verify($password, $passwordHash)) {
			echo "Bien";
		} 
		# Si la contrasena no coincide, se devuelve Mal.
		else {
			echo "Mal";
		}
	}
}

# Registro de usuarios
# Identificacion de usuarios
if ($parametro == "registro") {
	$email = $_POST["email"];
	$password = password_hash($_POST["password"], PASSWORD_DEFAULT);
	
	$consulta = "INSERT INTO t_usuarios VALUES ('$email', '$password')";

	# Ejecutar la sentencia SQL
	$resultado = mysqli_query($con, $consulta);
	# Comprobar si se ha ejecutado correctamente
	if (!$resultado) {
		echo 'Mal';
	}
	else {
		echo "Bien";
	}		
}

# Existe ya el email???
if ($parametro == "existeEmail") {
	$email = $_POST["email"];
	
	$consulta = "SELECT email FROM t_usuarios WHERE email='$email'";
	# Ejecutar la sentencia SQL
	$resultado = mysqli_query($con, $consulta);
	# Comprobar si se ha ejecutado correctamente
	if (!$resultado) {
		echo 'Ha ocurrido algún error: ' . mysqli_error($con);
	}
	
	# Compruebo el numero de coincidencias
	$anything_found = mysqli_num_rows($resultado);
	# Si aparece la combinacion de email y password dadas se identifica correctamente
	if($anything_found > 0) {
		echo "Existe";
	}
	# Si no, no se identifica
	else{
		echo "NoExiste";
	}
}

?>
