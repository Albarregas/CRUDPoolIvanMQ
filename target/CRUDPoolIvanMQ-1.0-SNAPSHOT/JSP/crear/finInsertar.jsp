<%-- 
    Document   : finInsertar
    Created on : 22-oct-2018, 17:30:05
    Author     : Iván
--%>

<%@page import="beans.Ave"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Insertar</title>
    </head>
    <body>
        <h1>Dato Insertado</h1>
        <h3>Este es el dato insertado.</h3>
        <table>
            <tr>
                <td style="border: 1px solid black">Anilla</td>
                <td style="border: 1px solid black">Especie</td>
                <td style="border: 1px solid black">Lugar</td>
                <td style="border: 1px solid black">Fecha</td>
            </tr>
        <%
            Ave elem =(Ave)request.getAttribute("Ave");    
        %><tr><%
    %><td style="border: 1px solid black"><%=elem.getAnilla()%></td><%
    %><td style="border: 1px solid black"><%=elem.getEspecie()%></td><%
    %><td style="border: 1px solid black"><%=elem.getLugar()%></td><%
    %><td style="border: 1px solid black"><%=elem.getFecha()%></td><%
    %></tr>
        
</table>
    <form method="post" action="Final">
            <input type="submit" name="Principal" value="Principal"/>
        </form>
</body>
</html>
