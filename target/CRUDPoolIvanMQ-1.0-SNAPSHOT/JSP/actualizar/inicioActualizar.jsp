<%-- 
    Document   : inicioActualizar
    Created on : 22-oct-2018, 17:32:51
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
        <h1>Hello World!</h1>
        <form action="Operacion" method="GET">
            <input type="hidden" value="Borrar" name="nombre">
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
                    %><input type="radio" name="valores" value="<%=elem.getAnilla()%>"><%
                %><td style="border: 1px solid black"><%=elem.getEspecie()%></td><%
                %><td style="border: 1px solid black"><%=elem.getLugar()%></td><%
                %><td style="border: 1px solid black"><%=elem.getFecha()%></td><%
                %></tr><%
                    }
                %>
            </table>
            <input type="submit" name="boton" value="Cancelar">
            <input type="submit" name="boton" value="Aceptar">
        </form>
    </body>
</html>
