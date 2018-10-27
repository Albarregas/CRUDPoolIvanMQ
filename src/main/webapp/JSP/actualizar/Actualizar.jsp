<%-- 
    Document   : Actualizar
    Created on : 22-oct-2018, 17:33:10
    Author     : Iván
--%>

<%@page import="beans.Ave"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Actualizando</h1>
        <%if (request.getAttribute("error") != null) {%>
        <h3><%=request.getAttribute("error")%></h3>
        <%}%>
        <%if (request.getAttribute("Ave") != null) {
                Ave ave = (Ave) request.getAttribute("Ave");
        %>
        <form action="Concluir" method="POST">
            <input type="hidden" value="Insertar" name="nombre">
            Anilla:<br>
            <input type="text" name="Anilla" value="<%=ave.getAnilla()%>"><br>
            Especie:<br>
            <input type="text" name="Especie" value="<%=ave.getEspecie()%>"><br>
            Lugar:<br>
            <input type="text" name="Lugar" value="<%=ave.getLugar()%>"><br>
            Fecha:<br>
            <input type="text" name="Fecha" value="<%=ave.getFecha()%>"><br>
            <input type="submit" name="boton" value="Cancelar">
            <input type="submit" name="boton" value="Confirmar">

            <%}%> 
</body>
</html>
