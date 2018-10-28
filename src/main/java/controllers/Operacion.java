/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import beans.Ave;
import connection.Conexion;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Iván
 */
@WebServlet(name = "Operacion", urlPatterns = {"/Operacion"})
public class Operacion extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init(); //To change body of generated methods, choose Tools | Templates.
        conex.iniciarPool();
    }

    public Conexion conex = new Conexion();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = null;
        //Comprobamos que existan valores.
        if (request.getParameter("valores") != null) {
            //Obtenemos el anillo para actualizarlo
            String anillo = request.getParameter("valores");
            //pasamos por peticion un ave que obtenemos con el metodo obtener dato
            //el ".get(0)" esta puesto por que el metodo devuelve un ArrayList.
            request.setAttribute("Ave", obtenerDatos("'" + anillo + "'").get(0));
            url = "JSP/actualizar/Actualizar.jsp";
        } else {
            //Si no selecciona ningun campo.
            request.setAttribute("error", "Seleccione algun campo.");
            request.setAttribute("lista", visualizarContenido());
            url = "JSP/actualizar/inicioActualizar.jsp";
        }
        request.getRequestDispatcher(url).forward(request, response);

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = null;
        //si estamos borrando
        if (request.getParameter("nombre").equals("Borrar")) {
            //Si pulsamos cancelar
            if (request.getParameter("boton").equals("Cancelar")) {
                url = "/index.html";
                //Si pulsamos aceptar
            } else if (request.getParameter("boton").equals("Aceptar")) {
                //Recogemos todos los valores marcados
                String[] valores = request.getParameterValues("valores");
                //Comprobamos que no esten vacios
                if (valores != null) {
                    ArrayList<Ave> listadoBorrado = null;
                    //metodo para poner comas y juntarlos en una misma cadena
                    String prefix = "", con_comas = "";
                    for (int i = 0; i < valores.length; i++) {
                        con_comas += prefix + "'" + valores[i] + "'";
                        prefix = ",";
                    }
                    //Cogemos todos los datos que queremos borrar y los mandamos por peticion
                    listadoBorrado = obtenerDatos(con_comas);
                    request.setAttribute("listadoBorrado", listadoBorrado);
                    url = "JSP/borrar/eliminar.jsp";
                } else {
                    //Si no pulsa ninguno le redireccionamos para atras.
                    request.setAttribute("lista", visualizarContenido());
                    request.setAttribute("error", "Por favor seleccione algun campo");
                    url = "JSP/borrar/leerEliminar.jsp";
                }
            }
        }
        request.getRequestDispatcher(url).forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Ave ave = new Ave();
        String url = null;
        //Si pulsa el boton cancelar
        if (request.getParameter("boton").equals("Cancelar")) {
            url = "/index.html";
        } else if (request.getParameter("nombre").equals("Insertar")) {
            try {

                //Recogemos el ave insertarda
                ave.setAnilla(request.getParameter("Anilla"));
                ave.setEspecie(request.getParameter("Especie"));
                ave.setLugar(request.getParameter("Lugar"));
                ave.setFecha(Date.valueOf(request.getParameter("Fecha")));
                //La pasamos por parametros
                request.setAttribute("Ave", ave);
                //Comprobamos que no este vacio
                if (!ave.getAnilla().equals("")
                        && !ave.getEspecie().equals("")
                        && !ave.getLugar().equals("")
                        && !ave.getFecha().equals("")) {
                    //Si no existe el ave en la base de datos
                    if (noExiste(ave.getAnilla())) {
                        insertarDB(ave);
                        url = "JSP/crear/finInsertar.jsp";
                    } else {
                        //si existe en la base de datos
                        request.setAttribute("error", "Ya existe la anilla en la base de datos");
                        url = "JSP/crear/inicioInsertar.jsp";
                    }
                } else {
                    //Si los campos no estan llenos
                    request.setAttribute("lista", visualizarContenido());
                    request.setAttribute("error", "Por favor, rellene todos los campos para continuar");
                    url = "JSP/crear/inicioInsertar.jsp";
                }
            } catch (IllegalArgumentException e) {
                //Todo esto lo hago para controlar si borra una fecha el usuario y devolverle todos los datos que ya tenia.
                request.setAttribute("error", "Por favor, no dejes la fecha en blanco.");
                request.setAttribute("Ave", ave);
                url = "JSP/crear/inicioInsertar.jsp";
            }
        } else if (request.getParameter("nombre").equals("Actualizar")) {
            processRequest(request, response);
        }

        request.getRequestDispatcher(url).forward(request, response);

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    //Inserta un ave en la base de datos

    private void insertarDB(Ave ave) {
        String insertTableSQL = "INSERT INTO aves"
                + "(anilla, especie, lugar, fecha) VALUES"
                + "(?,?,?,?)";

        try {

            Connection con = conex.iniciarConexion();
            PreparedStatement preparedStatement = con.prepareStatement(insertTableSQL);

            preparedStatement.setString(1, ave.getAnilla());
            preparedStatement.setString(2, ave.getEspecie());
            preparedStatement.setString(3, ave.getLugar());
            preparedStatement.setDate(4, ave.getFecha());

            // se ejecuta la sentencia
            preparedStatement.executeUpdate();
            conex.cerrarConexion();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //Comprueba si la anilla esta en la base de datos
    private boolean noExiste(String anilla) {
        try {
            Connection con = conex.iniciarConexion();
            String sql = "select * from aves where anilla = " + anilla;
            Statement sentencia = con.createStatement();
            ResultSet resultado = sentencia.executeQuery(sql);
            if (resultado.next()) {
                conex.cerrarConexion();
                return false;
            }
        } catch (SQLException ex) {
            conex.cerrarConexion();

        }
        return true;
    }

    //Obtiene una Lista de los datos de las anillas que les pase por parametro.
    private ArrayList<Ave> obtenerDatos(String valor) {
        ArrayList<Ave> listado = new ArrayList<Ave>();
        try {

            Connection con = conex.iniciarConexion();
            String sql = "select * from aves where anilla IN(";
            sql = sql + valor + ")";
            Statement sentencia = con.createStatement();
            ResultSet resultado = sentencia.executeQuery(sql);
            Ave ave = null;
            while (resultado.next()) {
                ave = new Ave();
                ave.setAnilla(resultado.getString("anilla"));
                ave.setEspecie(resultado.getString("especie"));
                ave.setLugar(resultado.getString("lugar"));
                ave.setFecha(resultado.getDate("fecha"));
                listado.add(ave);
            }
            conex.cerrarConexion();
        } catch (SQLException ex) {
            System.out.println("Error SQL en el metodo visualizar");
            Logger
                    .getLogger(Realizar.class
                            .getName()).log(Level.SEVERE, null, ex);
        }
        return listado;
    }

    //Este metodo te devuelve todas las aves de la base de datos
    private ArrayList<Ave> visualizarContenido() {
        ArrayList<Ave> listado = new ArrayList<Ave>();
        try {

            Connection con = conex.iniciarConexion();
            String sql = "select * from aves";
            Statement sentencia = con.createStatement();
            ResultSet resultado = sentencia.executeQuery(sql);
            Ave ave = null;
            while (resultado.next()) {
                ave = new Ave();
                ave.setAnilla(resultado.getString("anilla"));
                ave.setEspecie(resultado.getString("especie"));
                ave.setLugar(resultado.getString("lugar"));
                ave.setFecha(resultado.getDate("fecha"));
                listado.add(ave);
            }
            conex.cerrarConexion();
        } catch (SQLException ex) {
            System.out.println("Error SQL en el metodo visualizar");
            Logger
                    .getLogger(Realizar.class
                            .getName()).log(Level.SEVERE, null, ex);
        }
        return listado;
    }
}
