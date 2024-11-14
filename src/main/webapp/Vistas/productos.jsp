<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="modelos.Productos" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Productos | DevConta</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        :root {
            --primary-color: #2c3e50;
            --secondary-color: #34495e;
            --accent-color: #3498db;
            --success-color: #2ecc71;
        }

        body {
            background-color: #f8f9fa;
            color: var(--primary-color);
        }

        .page-header {
            background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
            color: white;
            padding: 2rem 0;
            margin-bottom: 2rem;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }

        .header-title {
            font-size: 2rem;
            font-weight: 600;
            margin: 0;
        }

        .producto-card {
            background: white;
            border-radius: 12px;
            box-shadow: 0 2px 15px rgba(0,0,0,0.05);
            transition: transform 0.3s ease;
            overflow: hidden;
            margin-bottom: 20px;
        }

        .producto-card:hover {
            transform: translateY(-5px);
        }

        .producto-imagen {
            width: 100%;
            height: 200px;
            object-fit: cover;
            border-bottom: 1px solid #eee;
        }

        .producto-detalles {
            padding: 1.5rem;
        }

        .producto-nombre {
            font-size: 1.2rem;
            font-weight: 600;
            margin-bottom: 0.5rem;
            color: var(--primary-color);
        }

        .producto-descripcion {
            color: #666;
            font-size: 0.9rem;
            margin-bottom: 1rem;
        }

        .producto-precio {
            font-size: 1.4rem;
            font-weight: 700;
            color: var(--accent-color);
            margin-bottom: 1rem;
        }

        .producto-proveedor {
            font-size: 0.85rem;
            color: #888;
            margin-bottom: 1rem;
        }

        .badge-proveedor {
            background: var(--accent-color);
            color: white;
            padding: 0.4rem 0.8rem;
            border-radius: 20px;
            font-size: 0.8rem;
        }

        .btn-agregar {
            background: var(--success-color);
            color: white;
            border: none;
            padding: 0.8rem 1.5rem;
            border-radius: 8px;
            font-weight: 500;
            transition: all 0.3s ease;
        }

        .btn-agregar:hover {
            background: #27ae60;
            transform: translateY(-2px);
            color: white;
        }

        .modal-content {
            border-radius: 12px;
            border: none;
        }

        .modal-header {
            background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
            color: white;
            border-radius: 12px 12px 0 0;
            border-bottom: none;
        }

        .modal-body {
            padding: 2rem;
        }

        .form-control {
            border-radius: 8px;
            padding: 0.8rem;
            border: 1px solid #dee2e6;
        }

        .form-control:focus {
            border-color: var(--accent-color);
            box-shadow: 0 0 0 0.2rem rgba(52, 152, 219, 0.25);
        }

        .alert {
            border-radius: 8px;
            padding: 1rem;
            margin-bottom: 1.5rem;
        }

        /* Animaciones */
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(20px); }
            to { opacity: 1; transform: translateY(0); }
        }

        .producto-card {
            animation: fadeIn 0.5s ease-out forwards;
        }
    </style>
</head>
<body>
    <jsp:include page="/nav/navbar.jsp"></jsp:include>

    <!-- Header -->
    <div class="page-header">
        <div class="container">
            <h1 class="header-title text-center">
                <i class="fas fa-box me-2"></i>
                Gestión de Productos
            </h1>
        </div>
    </div>

    <div class="container">
        <!-- Mensajes de éxito o error -->
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger alert-dismissible fade show">
                <i class="fas fa-exclamation-circle me-2"></i>
                ${errorMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success alert-dismissible fade show">
                <i class="fas fa-check-circle me-2"></i>
                ${successMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <!-- Botón para agregar producto -->
        <div class="text-end mb-4">
            <button class="btn btn-agregar" data-bs-toggle="modal" data-bs-target="#agregarProductoModal" onclick="limpiarFormulario()">
                <i class="fas fa-plus me-2"></i>
                Agregar Producto
            </button>
        </div>

        <!-- Grid de Productos -->
        <div class="row">
            <c:forEach var="producto" items="${listaProductos}">
                <div class="col-md-4">
                    <div class="producto-card">
                        <img src="https://firebasestorage.googleapis.com/v0/b/chatgram-4c6da.appspot.com/o/X.jpg?alt=media&token=e44884f1-612f-4140-8bb4-109e473e78c9" 
                             alt="${producto.nombre}" 
                             class="producto-imagen">
                        <div class="producto-detalles">
                            <h3 class="producto-nombre">${producto.nombre}</h3>
                            <p class="producto-descripcion">${producto.descripcion}</p>
                            <div class="producto-precio">
                                $${producto.precio}
                            </div>
                            <div class="producto-proveedor">
                                <span class="badge-proveedor">
                                    <i class="fas fa-building me-1"></i>
                                    ${producto.proveedor}
                                </span>
                            </div>
                            <button class="btn btn-primary w-100" onclick="editarProducto(${producto.id_producto})">
                                <i class="fas fa-edit me-2"></i>
                                Editar
                            </button>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

    <!-- Modal para agregar producto -->
    <div id="agregarProductoModal" class="modal fade" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">
                        <i class="fas fa-plus me-2"></i>
                        Agregar Producto
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form id="formAgregarProducto" action="ProductosServlet" method="post">
                        <input type="hidden" name="action" value="agregar">

                        <div class="mb-3">
                            <label for="nombre" class="form-label">
                                <i class="fas fa-box me-2"></i>Nombre:
                            </label>
                            <input type="text" class="form-control" id="nombre" name="nombre" required>
                        </div>
                        <div class="mb-3">
                            <label for="descripcion" class="form-label">
                                <i class="fas fa-align-left me-2"></i>Descripción:
                            </label>
                            <textarea class="form-control" id="descripcion" name="descripcion" required></textarea>
                        </div>
                        <div class="mb-3">
                            <label for="proveedor" class="form-label">
                                <i class="fas fa-building me-2"></i>Proveedor:
                            </label>
                            <input type="text" class="form-control" id="proveedor" name="proveedor" required>
                        </div>
                        <div class="mb-3">
                            <label for="precio" class="form-label">
                                <i class="fas fa-tag me-2"></i>Precio:
                            </label>
                            <input type="number" step="0.01" class="form-control" id="precio" name="precio" required>
                        </div>

                        <div class="text-end">
                            <button type="button" class="btn btn-secondary me-2" data-bs-dismiss="modal">
                                <i class="fas fa-times me-2"></i>Cancelar
                            </button>
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-save me-2"></i>Guardar
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal para editar producto -->
    <div id="editarProductoModal" class="modal fade" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">
                        <i class="fas fa-edit me-2"></i>
                        Editar Producto
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form id="formEditarProducto" action="ProductosServlet" method="post">
                        <input type="hidden" id="id_producto" name="id_producto">
                        <input type="hidden" name="action" value="editar">

                        <div class="mb-3">
                            <label for="nombre_editar" class="form-label">
                                <i class="fas fa-box me-2"></i>Nombre:
                            </label>
                            <input type="text" class="form-control" id="nombre_editar" name="nombre" required>
                        </div>
                        <div class="mb-3">
                            <label for="descripcion_editar" class="form-label">
                                <i class="fas fa-align-left me-2"></i>Descripción:
                            </label>
                            <textarea class="form-control" id="descripcion_editar" name="descripcion" required></textarea>
                        </div>
                        <div class="mb-3">
                            <label for="proveedor_editar" class="form-label">
                                <i class="fas fa-building me-2"></i>Proveedor:
                            </label>
                            <input type="text" class="form-control" id="proveedor_editar" name="proveedor" required>
                        </div>
                        <div class="mb-3">
                            <label for="precio_editar" class="form-label">
                                <i class="fas fa-tag me-2"></i>Precio:
                            </label>
                            <input type="number" step="0.01" class="form-control" id="precio_editar" name="precio" required>
                        </div>

                        <div class="text-end">
                            <button type="button" class="btn btn-secondary me-2" data-bs-dismiss="modal">
                                <i class="fas fa-times me-2"></i>Cancelar
                            </button>
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-save me-2"></i>Guardar Cambios
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>

    <script>
        function limpiarFormulario() {
            $('#formAgregarProducto')[0].reset();
        }

        function editarProducto(idProducto) {
            $.ajax({
                url: 'ProductosServlet',
                type: 'GET',
                data: { action: 'obtener', id_producto: idProducto },
                success: function(producto) {
                    $('#id_producto').val(producto.id_producto);
                    $('#nombre_editar').val(producto.nombre);
                    $('#descripcion_editar').val(producto.descripcion);
                    $('#proveedor_editar').val(producto.proveedor);
                    $('#precio_editar').val(producto.precio);
                    $('#editarProductoModal').modal('show');
                },
                error: function() {
                    alert('Error al obtener el producto.');
                }
            });
        }

        // Animación para las cards
        document.addEventListener('DOMContentLoaded', function() {
            document.querySelectorAll('.producto-card').forEach((card, index) => {
                card.style.opacity = '0';
                card.style.animation = `fadeIn 0.5s ease-out ${index * 0.1}s forwards`;
            });
        });
    </script>
</body>
</html>