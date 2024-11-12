<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="modelos.Productos" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <title>Gestión de Productos</title>
    </head>
    <body>
        <jsp:include page="/nav/navbar.jsp"></jsp:include>
        <div class="container mt-4">
            <h1 class="text-center">Gestión de Productos</h1>

            <!-- Mensajes de éxito o error -->
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger">${errorMessage}</div>
            </c:if>
            <c:if test="${not empty successMessage}">
                <div class="alert alert-success">${successMessage}</div>
            </c:if>

            <!-- Botón para agregar producto -->
            <div class="mb-3">
                <button class="btn btn-success" data-bs-toggle="modal" data-bs-target="#agregarProductoModal" onclick="limpiarFormulario()">Agregar Producto</button>
            </div>

            <!-- Lista de Productos -->
            <table class="table table-bordered">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nombre</th>
                        <th>Descripción</th>
                        <th>Proveedor</th>
                        <th>Precio</th>                    
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="producto" items="${listaProductos}">
                        <tr>
                            <td>${producto.id_producto}</td>
                            <td>${producto.nombre}</td>
                            <td>${producto.descripcion}</td>
                            <td>${producto.proveedor}</td>
                            <td>${producto.precio}</td>
                            <td>
                                <!-- Botón para abrir el modal de edición -->
                                <button class="btn btn-primary" onclick="editarProducto(${producto.id_producto})">Editar</button>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <!-- Modal para agregar producto -->
        <div id="agregarProductoModal" class="modal fade" tabindex="-1" aria-labelledby="agregarProductoModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="agregarProductoModalLabel">Agregar Producto</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                    </div>
                    <div class="modal-body">
                        <form id="formAgregarProducto" action="ProductosServlet" method="post">
                            <input type="hidden" name="action" value="agregar">

                            <div class="mb-3">
                                <label for="nombre" class="form-label">Nombre:</label>
                                <input type="text" class="form-control" id="nombre" name="nombre" required>
                            </div>
                            <div class="mb-3">
                                <label for="descripcion" class="form-label">Descripción:</label>
                                <input type="text" class="form-control" id="descripcion" name="descripcion" required>
                            </div>
                            <div class="mb-3">
                                <label for="proveedor" class="form-label">Proveedor:</label>
                                <input type="text" class="form-control" id="proveedor" name="proveedor" required>
                            </div>
                            <div class="mb-3">
                                <label for="precio" class="form-label">Precio:</label>
                                <input type="number" step="0.01" class="form-control" id="precio" name="precio" required>
                            </div>

                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
                                <button type="submit" class="btn btn-primary">Agregar</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal para editar el producto -->
        <div id="editarProductoModal" class="modal fade" tabindex="-1" aria-labelledby="editarProductoModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="editarProductoModalLabel">Editar Producto</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                    </div>
                    <div class="modal-body">
                        <form id="formEditarProducto" action="ProductosServlet" method="post">
                            <input type="hidden" id="id_producto" name="id_producto">
                            <input type="hidden" name="action" value="editar">

                            <div class="mb-3">
                                <label for="nombre" class="form-label">Nombre:</label>
                                <input type="text" class="form-control" id="nombre_editar" name="nombre" required>
                            </div>
                            <div class="mb-3">
                                <label for="descripcion" class="form-label">Descripción:</label>
                                <input type="text" class="form-control" id="descripcion_editar" name="descripcion" required>
                            </div>
                            <div class="mb-3">
                                <label for="proveedor" class="form-label">Proveedor:</label>
                                <input type="text" class="form-control" id="proveedor_editar" name="proveedor" required>
                            </div>
                            <div class="mb-3">
                                <label for="precio" class="form-label">Precio:</label>
                                <input type="number" step="0.01" class="form-control" id="precio_editar" name="precio" required>
                            </div>

                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
                                <button type="submit" class="btn btn-primary">Guardar</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- jQuery -->
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>

        <script>
            // Función para limpiar el formulario del modal de agregar producto
            function limpiarFormulario() {
                // Limpiar todos los campos del formulario
                $('#formAgregarProducto')[0].reset();
            }

            // Función para abrir el modal de edición con los datos del producto
            function editarProducto(idProducto) {
                $.ajax({
                    url: 'ProductosServlet', 
                    type: 'GET',
                    data: { action: 'obtener', id_producto: idProducto },
                    success: function (producto) {
                        // Cargar los datos del producto en el modal de edición
                        $('#id_producto').val(producto.id_producto);
                        $('#nombre_editar').val(producto.nombre);
                        $('#descripcion_editar').val(producto.descripcion);
                        $('#proveedor_editar').val(producto.proveedor);
                        $('#precio_editar').val(producto.precio);
                        $('#editarProductoModal').modal('show');
                    },
                    error: function () {
                        alert('Error al obtener el producto.');
                    }
                });
            }
        </script>
    </body>
</html>