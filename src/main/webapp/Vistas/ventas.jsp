<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Registro de Ventas</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <jsp:include page="/nav/navbar.jsp"></jsp:include>
        <div class="container mt-4">
            <h2 class="text-center mb-4">Registro de Ventas</h2>

            <!-- Formulario de Registro de Venta -->
            <form action="VentasServlet" method="post" class="p-5 border rounded shadow-sm bg-light mb-5">
                <input type="hidden" name="action" value="create">

                <div class="row mb-3">
                    <div class="col">
                        <label for="producto" class="form-label fw-bold">Producto:</label>
                        <select name="id_producto" id="producto" class="form-select" required>
                            <c:forEach var="producto" items="${listaProductos}">
                                <option value="${producto.id_producto}">${producto.nombre}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="row mb-3">
                    <div class="col-md-6">
                        <label for="cantidad" class="form-label fw-bold">Cantidad:</label>
                        <input type="number" name="cantidad" id="cantidad" min="1" class="form-control" required>
                    </div>
                    <div class="col-md-6">
                        <label for="precio_venta_unitario" class="form-label fw-bold">Precio de Venta Unitario:</label>
                        <input type="number" step="0.01" name="precio_venta_unitario" id="precio_venta_unitario" 
                               class="form-control" required>
                    </div>
                </div>

                <div class="text-end">
                    <button type="submit" class="btn btn-primary px-4 py-2">Registrar Venta</button>
                </div>
            </form>

            <!-- Mensaje de éxito o error -->
            <c:if test="${not empty mensaje}">
                <div class="alert alert-${tipoMensaje} mt-3" role="alert">
                    ${mensaje}
                </div>
            </c:if>

            <!-- Tabla de Ventas -->
            <h3 class="mt-4">Listado de Ventas</h3>
            <div class="table-responsive">
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>ID Venta</th>
                            <th>Producto</th>
                            <th>Lote</th>
                            <th>Cantidad</th>
                            <th>Precio Unitario</th>
                            <th>Total</th>
                            <th>Fecha</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="venta" items="${listaVentas}">
                            <tr>
                                <td>${venta.id_venta}</td>
                                <td>${venta.nombre}</td>
                                <td>${venta.id_lote}</td>
                                <td>${venta.cantidad}</td>
                                <td>
                                    <fmt:formatNumber 
                                        value="${venta.precio_venta_unitario}" 
                                        type="currency" 
                                        currencySymbol="$" 
                                        minFractionDigits="2" 
                                        maxFractionDigits="2"
                                        />
                                </td>
                                <td>
                                    <fmt:formatNumber 
                                        value="${venta.cantidad * venta.precio_venta_unitario}" 
                                        type="currency" 
                                        currencySymbol="$" 
                                        minFractionDigits="2" 
                                        maxFractionDigits="2"
                                        />
                                </td>
                                <td>
                                    <fmt:formatDate 
                                        value="${venta.fecha_venta}" 
                                        pattern="d/MMMM/yyyy - HH:mm:ss"
                                        />
                                </td>
                                <td>
                                    <form method="post" action="VentasServlet" style="display:inline;" onsubmit="return confirmarEliminacion()">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id_venta" value="${venta.id_venta}">
                                        <button type="submit" class="btn btn-danger btn-sm">Eliminar</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.min.js"></script>

        <script>
                                        // Validación del formulario de creación
                                        document.querySelector('form[action="VentasServlet"]').addEventListener('submit', function (e) {
                                            if (this.querySelector('input[name="action"]').value === 'create') {
                                                const cantidad = parseInt(document.getElementById('cantidad').value);
                                                const precio = parseFloat(document.getElementById('precio_venta_unitario').value);

                                                if (cantidad <= 0) {
                                                    e.preventDefault();
                                                    alert('La cantidad debe ser mayor a 0');
                                                    return;
                                                }

                                                if (precio <= 0) {
                                                    e.preventDefault();
                                                    alert('El precio debe ser mayor a 0');
                                                    return;
                                                }
                                            }
                                        });

                                        // Función separada para confirmar eliminación
                                        function confirmarEliminacion() {
                                            return confirm('¿Está seguro que desea eliminar esta venta? Se restaurará el inventario.');
                                        }
        </script>
    </body>
</html>