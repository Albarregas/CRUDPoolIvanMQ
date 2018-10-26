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
        String anillo = request.getParameter("valores");
        request.setAttribute("listadoActualizar", obtenerDatos(anillo));
        request.getRequestDispatcher("JSP/actualizar/Actualizar.jsp").forward(request, response);
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
        if (request.getParameter("nombre").equals("Borrar")) {
            if (request.getParameter("boton").equals("Cancelar")) {
                request.getRequestDispatcher("/index.html").forward(request, response);
            } else if (request.getParameter("boton").equals("Aceptar")) {
                String[] valores = request.getParameterValues("valores");
                if (valores != null) {
                    ArrayList<Ave> listadoBorrado = null;
                    for (int i = 0; i < valores.length; i++) {
                        listadoBorrado = obtenerDatos(valores[i]);
                    }
                    request.setAttribute("listadoBorrado", listadoBorrado);
                    request.getRequestDispatcher("JSP/borrar/eliminar.jsp").forward(request, response);
                }else{
                    request.setAttribute("lista", visualizarContenido());
                    request.setAttribute("error","Por favor seleccione algun campo");
                    request.getRequestDispatcher("JSP/borrar/leerEliminar.jsp").forward(request, response);
                }
            }
        }
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
        if (request.getParameter("nombre").equals("Insertar")) {
            Ave ave = new Ave(
                    request.getParameter("Anilla"),
                    request.getParameter("Especie"),
                    request.getParameter("Lugar"),
                    request.getParameter("Fecha")
            );
            if (request.getParameter("boton").equals("Cancelar")) {
                request.getRequestDispatcher("/index.html").forward(request, response);
            } else if (request.getParameter("boton").equals("Aceptar")) {
                request.setAttribute("Ave", ave);
                if (ave.getAnilla() != null
                        && ave.getEspecie() != null
                        && ave.getLugar() != null
                        && ave.getFecha() != null) {
                    if (noExiste(ave.getAnilla())) {
                        insertarDB(ave);
                        request.getRequestDispatcher("JSP/crear/finInsertar.jsp").forward(request, response);
                    } else {
                        request.setAttribute("error","Ya existe la anilla en la base de datos");
                        
                        request.getRequestDispatcher("JSP/crear/inicioInsertar.jsp").forward(request, response);
                    }

                } else {
                    request.setAttribute("lista", visualizarContenido());
                    request.setAttribute("error","Por favor, rellene todos los campos para continuar");
                    request.getRequestDispatcher("JSP/crear/inicioInsertar.jsp").forward(request, response);
                }
            }
        }
        if (request.getParameter("nombre").equals("Actualizar")) {
            processRequest(request, response);
        }

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
            preparedStatement.setString(4, ave.getFecha());

            // se ejecuta la sentencia
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

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

    private ArrayList<Ave> obtenerDatos(String valor) {
        ArrayList<Ave> listado = new ArrayList<Ave>();
        try {

            Connection con = conex.iniciarConexion();
            String sql = "select * from aves where anilla=" + valor;
            Statement sentencia = con.createStatement();
            ResultSet resultado = sentencia.executeQuery(sql);
            Ave ave = null;
            while (resultado.next()) {
                ave = new Ave();
                ave.setAnilla(resultado.getString("anilla"));
                ave.setEspecie(resultado.getString("especie"));
                ave.setLugar(resultado.getString("lugar"));
                ave.setFecha(resultado.getString("fecha"));
                listado.add(ave);
            }
            conex.cerrarConexion();
        } catch (SQLException ex) {
            System.out.println("Error SQL en el metodo visualizar");
            Logger.getLogger(Realizar1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listado;
    }
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
                ave.setFecha(resultado.getString("fecha"));
                listado.add(ave);
            }
            conex.cerrarConexion();
        } catch (SQLException ex) {
            System.out.println("Error SQL en el metodo visualizar");
            Logger.getLogger(Realizar1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listado;
    }
}
