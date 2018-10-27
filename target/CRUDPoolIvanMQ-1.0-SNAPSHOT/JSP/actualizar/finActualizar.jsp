<%-- 
    Document   : finActualizar
    Created on : 22-oct-2018, 17:33:40
    Author     : Iván
--%>

<%@page import="beans.Ave"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Actualizar</title>
    </head>
    <body>
        <h1>Actualizado</h1>
        <h3>Este es el ave actualizada</h3>
        <table>
            <tr>
                <td style="border: 1px solid black">Anilla</td>
                <td style="border: 1px solid black">Especie</td>
                <td style="border: 1px solid black">Lugar</td>
                <td style="border: 1px solid black">Fecha</td>
            </tr>
        <%Ave ave = (Ave) request.getAttribute("Ave");%><tr>
            <td style="border: 1px solid black"><%=ave.getAnilla()%></td>
            <td style="border: 1px solid black"><%=ave.getEspecie()%></td>
            <td style="border: 1px solid black"><%=ave.getLugar()%></td>
            <td style="border: 1px solid black"><%=ave.getFecha()%></td>
        </tr>
        
</table>
    <form method="post" action="Final">
            <input type="submit" name="Principal" value="Principal"/>
        </form>
</body>
</html>
