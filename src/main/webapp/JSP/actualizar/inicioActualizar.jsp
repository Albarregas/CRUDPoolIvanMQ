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
        <title>Actualizar</title>
    </head>
    <body>
        <h1>Actualizacion</h1>
        <h3>Seleccione cual quiere actualizar.</h3>
        <%if(request.getAttribute("error")!=null){%>
        <h5 style="color: red"><%=request.getAttribute("error")%></h5>
        <%}%>
        <form action="Operacion" method="POST">
            <input type="hidden" value="Actualizar" name="nombre">
            <table>
                <tr>
                    <td>Selecciona uno:</td>
                    <td style="border: 1px solid black">Especie</td>
                    <td style="border: 1px solid black">Lugar</td>
                    <td style="border: 1px solid black">Fecha</td>
                </tr>
                <%
                    ArrayList<Ave> lista = (ArrayList<Ave>) request.getAttribute("lista");
                    for (Ave elem : lista) {
                %><tr>
                    <td><input type="radio" name="valores" value="<%=elem.getAnilla()%>"></td>
                <td style="border: 1px solid black"><%=elem.getEspecie()%></td>
                <td style="border: 1px solid black"><%=elem.getLugar()%></td>
                <td style="border: 1px solid black"><%=elem.getFecha()%></td>
            </tr>
            <%}%>
            </table>
            <input type="submit" name="boton" value="Cancelar">
            <input type="submit" name="boton" value="Aceptar">
        </form>
    </body>
</html>
