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


$T_USUARIOS="t_usuarios"
# Se obtiene el parametro, que dira que funcion se va a ejecutar
$parametro = $_POST["parametro"]



# Identificacion de usuarios
if ($parametro == "identificacion") {
	$email = $_POST["email"]
	$password = $_POST["password"]

	$consulta = "SELECT email FROM '$T_USUARIOS' WHERE email='$email' AND password='$password'"
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
		echo "Bien"
	}
	# Si no, no se identifica
	else{
		echo "Mal"
	}
}

# Registro de usuarios
# Identificacion de usuarios
if ($parametro == "registro") {
	$email = $_POST["email"]
	$password = $_POST["password"]

	$consulta = "INSERT INTO '$T_USUARIOS' VALUES ('$email', '$password')"

	# Ejecutar la sentencia SQL
	$resultado = mysqli_query($con, $consulta);
	# Comprobar si se ha ejecutado correctamente
	if (!$resultado) {
		echo 'Ha ocurrido algún error: ' . mysqli_error($con);
	}
	else {
		echo "Se ha registrado el usuario"
	}
}

# Existe ya el email???
if ($parametro == "existeEmail") {
	$email = $_POST["email"]

	$consulta = "SELECT email FROM '$T_USUARIOS' WHERE email='$email'"
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
		echo "Ya existe el email"
	}
	# Si no, no se identifica
	else{
		echo "No existe el email"
	}
}

?>