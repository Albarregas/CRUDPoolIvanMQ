<%-- 
    Document   : finBorrar
    Created on : 22-oct-2018, 17:32:00
    Author     : Iván
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="beans.Ave"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Pantalla Borrar</h1>
        <h2>Estos son los datos borrados.</h2>
        <table>
            <tr>
                <td style="border: 1px solid black">Anilla</td>
                <td style="border: 1px solid black">Especie</td>
                <td style="border: 1px solid black">Lugar</td>
                <td style="border: 1px solid black">Fecha</td>
            </tr>
        <%

            ArrayList<Ave> lista = (ArrayList<Ave>) request.getAttribute("listadoBorrado");
            for (Ave elem : lista) {
        %><tr><%
    %><td style="border: 1px solid black"><%=elem.getAnilla()%></td><%
    %><td style="border: 1px solid black"><%=elem.getEspecie()%></td><%
    %><td style="border: 1px solid black"><%=elem.getLugar()%></td><%
    %><td style="border: 1px solid black"><%=elem.getFecha()%></td><%
    %></tr><%
        }

    %>
            
            
        
</table>
    <form method="POST" action="Final">
        <%%>
        <input type="hidden" value="Eliminar" name="nombre">
            <input type="submit" name="boton" value="Cancelar">
            <input type="submit" name="boton" value="Confirmar">
        </form>
</body>
</html>
