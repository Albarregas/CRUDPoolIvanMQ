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
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Pantalla Leer</h1>
        <table>
            <tr>
        <%
            ArrayList<Ave> lista=(ArrayList<Ave>)request.getAttribute("lista");
            for (Ave elem : lista) {
                    %><td><%=elem.getAnilla()%></td><%
                    %><td><%=elem.getEspecie()%></td><%
                    %><td><%=elem.getLugar()%></td><%
                    %><td><%=elem.getFecha()%></td><%    
                }
        %>
            </tr>
        </table>
    </body>
</html>
