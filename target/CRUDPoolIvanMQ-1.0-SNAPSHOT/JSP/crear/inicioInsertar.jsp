<%-- 
    Document   : inicioInsertar
    Created on : 22-oct-2018, 17:28:56
    Author     : Iván
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        
        <h1>Crear un ave</h1>
        <form action="Operacion" method="POST">
            <input type="hidden" value="Insertar" name="nombre">
            Anilla:<br>
            <input type="text" name="Anilla"><br>
            Especie:<br>
            <input type="text" name="Especie"><br>
            Lugar:<br>
            <input type="text" name="Lugar"><br>
            Fecha:<br>
            <input type="text" name="Fecha"><br>
            <input type="submit" name="boton" value="Cancelar">
            <input type="submit" name="boton" value="Aceptar">
        </form>
    </body>
</html>
