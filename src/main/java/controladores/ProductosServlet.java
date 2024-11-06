package controladores;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelos.Productos;
import modelosDAO.ProductosDAO;

@WebServlet(name = "ProductosServlet", urlPatterns = {"/ProductosServlet"})
public class ProductosServlet extends HttpServlet {

    private ProductosDAO productosDAO;

    @Override
    public void init() throws ServletException {
        try {
            productosDAO = new ProductosDAO();
        } catch (ClassNotFoundException e) {
            throw new ServletException("Error al inicializar DAO: " + e.getMessage(), e);
        }
    }

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
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ProductosServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ProductosServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //processRequest(request, response);
        String action = request.getParameter("action");
        if (action == null) {
            action = "listar";
        }

        switch (action) {
            case "listar":
                listarProductos(request, response);
                break;
            case "obtener":
                obtenerProducto(request, response);
                break;
            case "desactivar":
                desactivarProducto(request, response);
                break;
            default:
                response.sendRedirect("index.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //processRequest(request, response);
        String action = request.getParameter("action");
        if ("agregar".equals(action)) {
            agregarProducto(request, response);
        } else if ("editar".equals(action)) {
            editarProducto(request, response);
        } else {
            response.sendRedirect("index.jsp");
        }
    }

    private void listarProductos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Productos> listaProductos = productosDAO.listarProductos();
        request.setAttribute("listaProductos", listaProductos);
        request.getRequestDispatcher("productos.jsp").forward(request, response);
    }

    private void agregarProducto(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        String proveedor = request.getParameter("proveedor");
        String precioStr = request.getParameter("precio");
        String estado = "activo";

        if (nombre == null || nombre.trim().isEmpty() || descripcion == null || descripcion.trim().isEmpty()
                || proveedor == null || proveedor.trim().isEmpty() || precioStr == null || precioStr.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Todos los campos son obligatorios.");
            request.getRequestDispatcher("productos.jsp").forward(request, response);
            return;
        }

        double precio;
        try {
            precio = Double.parseDouble(precioStr);
            if (precio <= 0) {
                throw new NumberFormatException("El precio debe ser mayor que cero.");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Precio inválido: " + e.getMessage());
            request.getRequestDispatcher("productos.jsp").forward(request, response);
            return;
        }

        Productos producto = new Productos(nombre, descripcion, proveedor, precio, estado);
        boolean resultado = productosDAO.insertarProductos(producto);
        if (resultado) {
            response.sendRedirect("ProductosServlet?action=listar");
        } else {
            request.setAttribute("errorMessage", "Error al agregar producto.");
            request.getRequestDispatcher("productos.jsp").forward(request, response);
        }
    }

    private void obtenerProducto(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    try {
        // Obtener el parámetro ID del producto
        String idParam = request.getParameter("id_producto");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de producto es requerido");
            return;
        }

        // Convertir el parámetro a un número
        int id_producto = Integer.parseInt(idParam);
        // Obtener el producto desde el DAO
        Productos producto = productosDAO.obtenerProducto(id_producto);

        try (PrintWriter out = response.getWriter()) {
            if (producto != null) {
                // Convertir el objeto producto a JSON y escribirlo en la respuesta
                Gson gson = new Gson();
                out.print(gson.toJson(producto));
            } else {
                // En caso de no encontrar el producto
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Producto no encontrado");
            }
        }
    } catch (NumberFormatException e) {
        // Manejar el caso donde el ID no es un número válido
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de producto inválido");
    } catch (Exception e) {
        // Manejo de excepciones generales (puedes registrar esto también)
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error en el servidor");
    }
}
    private void editarProducto(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id_producto = Integer.parseInt(request.getParameter("id_producto"));
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        String proveedor = request.getParameter("proveedor");
        double precio = Double.parseDouble(request.getParameter("precio"));
        String estado = request.getParameter("estado");

        Productos producto = new Productos(id_producto, nombre, descripcion, proveedor, precio, estado);
        boolean resultado = productosDAO.actualizarProducto(producto);

        if (resultado) {
            response.sendRedirect("ProductosServlet?action=listar");
        } else {
            request.setAttribute("errorMessage", "Error al actualizar producto.");
            request.getRequestDispatcher("productos.jsp").forward(request, response);
        }
    }

    private void desactivarProducto(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int idProducto = Integer.parseInt(request.getParameter("id"));
        boolean resultado = productosDAO.desactivarProducto(idProducto);
        if (resultado) {
            response.sendRedirect("ProductosServlet?action=listar");
        } else {
            response.getWriter().println("Error al desactivar producto");
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
