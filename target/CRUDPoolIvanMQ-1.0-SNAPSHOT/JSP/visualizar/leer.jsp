<%-- 
    Document   : leer
    Created on : 22-oct-2018, 17:32:38
    Author     : Iván
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="beans.Ave"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Visualizar</title>
    </head>
    <body>
        <h1>Visualizar</h1>
        <h3>Estas son todas las aves de la base de datos.</h3>
        <table>
            <tr>
                <td style="border: 1px solid black">Anilla</td>
                <td style="border: 1px solid black">Especie</td>
                <td style="border: 1px solid black">Lugar</td>
                <td style="border: 1px solid black">Fecha</td>
            </tr>
        <%

            ArrayList<Ave> lista = (ArrayList<Ave>) request.getAttribute("lista");
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
    <form method="post" action="Final">
            <input type="submit" name="Principal" value="Principal"/>
        </form>
</body>
</html>
