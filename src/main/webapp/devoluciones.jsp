<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Gestión de Devoluciones</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container mt-4">
            <h2 class="text-center mb-4">Gestión de Devoluciones</h2>

            <!-- Formulario de Registro de Devolución -->
            <form action="DevolucionesServlet" method="post" class="p-5 border rounded shadow-sm bg-light mb-5">
                <input type="hidden" name="action" value="create">

                <!-- Selector de tipo de devolución -->
                <div class="row mb-3">
                    <div class="col-md-6">
                        <label class="form-label fw-bold">Tipo de Devolución:</label>
                        <select id="tipoSelect" name="tipo_operacion" class="form-select" onchange="cambiarTipoDevolucion()">
                            <option value="venta">Devolución de Venta</option>
                            <option value="compra">Devolución de Compra</option>
                        </select>
                    </div>
                </div>

                <!-- Sección de Ventas -->
                <div id="ventasSection">
                    <div class="row mb-3">
                        <div class="col-md-12">
                            <label class="form-label fw-bold">Seleccionar Venta:</label>
                            <select name="venta" id="ventaSelect" class="form-select" onchange="cargarDatosVenta()">
                                <option value="">Seleccione una venta</option>
                                <c:forEach var="venta" items="${ventasDisponibles}">
                                    <option value="${venta.id_venta}" 
                                            data-producto="${venta.id_producto}"
                                            data-lote="${venta.id_lote}"
                                            data-cantidad="${venta.cantidad}"
                                            data-nombre="${venta.nombre_producto}">
                                        Venta #${venta.id_venta} - ${venta.nombre_producto} - 
                                        Disponible para devolución: ${venta.cantidad}
                                        (<fmt:formatDate value="${venta.fecha_venta}" pattern="dd/MM/yyyy"/>)
                                    </option>
                                </c:forEach>
                            </select>
                            <small class="text-muted">Las ventas se muestran ordenadas por fecha de lote (PEPS)</small>
                        </div>
                    </div>
                </div>

                <!-- Sección de Compras -->
                <div id="comprasSection" style="display:none;">
                    <div class="row mb-3">
                        <div class="col-md-12">
                            <label class="form-label fw-bold">Seleccionar Compra:</label>
                            <select name="compra" id="compraSelect" class="form-select" onchange="cargarDatosCompra()">
                                <option value="">Seleccione una compra</option>
                                <c:forEach var="compra" items="${comprasDisponibles}">
                                    <option value="${compra.id_compra}" 
                                            data-producto="${compra.id_producto}"
                                            data-lote="${compra.id_lote}"
                                            data-disponible="${compra.cantidad_disponible}"
                                            data-nombre="${compra.nombre_producto}">
                                        Compra #${compra.id_compra} - ${compra.nombre_producto} - 
                                        Disponible: ${compra.cantidad_disponible} - 
                                        Fecha: <fmt:formatDate value="${compra.fecha_compra}" pattern="dd/MM/yyyy"/>
                                        - Por orden PEPS
                                    </option>
                                </c:forEach>
                            </select>
                            <small class="text-muted">Las compras se muestran ordenadas por fecha de compra (PEPS)</small>
                        </div>
                    </div>
                </div>
                <input type="hidden" name="id_producto" id="id_producto">
                <input type="hidden" name="id_lote" id="id_lote">

                <!-- Detalles de la devolución -->
                <div class="row mb-3">
                    <div class="col-md-4">
                        <label for="cantidad" class="form-label fw-bold">Cantidad a Devolver:</label>
                        <input type="number" name="cantidad" id="cantidad" class="form-control" required min="1">
                    </div>
                    <div class="col-md-4">
                        <label for="tipo_devolucion" class="form-label fw-bold">Estado del Producto:</label>
                        <select name="tipo_devolucion" id="tipo_devolucion" class="form-select" required>
                            <option value="normal">En buen estado</option>
                            <option value="defectuoso">Defectuoso</option>
                        </select>
                    </div>
                    <div class="col-md-4">
                        <label for="razon" class="form-label fw-bold">Razón:</label>
                        <textarea name="razon" id="razon" class="form-control" required rows="2"></textarea>
                    </div>
                </div>

                <div class="text-end">
                    <button type="submit" class="btn btn-primary px-4 py-2">Registrar Devolución</button>
                </div>
            </form>

            <!-- Mensaje de éxito o error -->
            <c:if test="${not empty mensaje}">
                <div class="alert alert-${tipoMensaje} mt-3" role="alert">
                    ${mensaje}
                </div>
            </c:if>

            <!-- Tabla de Devoluciones -->
            <h3 class="mt-4">Historial de Devoluciones</h3>
            <div class="table-responsive">
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Producto</th>
                            <th>Lote</th>
                            <th>Cantidad</th>
                            <th>Tipo Operación</th>
                            <th>Estado</th>
                            <th>Razón</th>
                            <th>Fecha</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="devolucion" items="${listaDevoluciones}">
                            <tr>
                                <td>${devolucion.id_devolucion}</td>
                                <td>${devolucion.nombre_producto}</td>
                                <td>${devolucion.id_lote}</td>
                                <td>${devolucion.cantidad}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${devolucion.tipo_operacion eq 'compra'}">
                                            <span class="badge bg-info">Devolución Compra</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-primary">Devolución Venta</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${devolucion.tipo_devolucion eq 'defectuoso'}">
                                            <span class="badge bg-danger">Defectuoso</span>
                                        </c:when>
                                        <c:when test="${devolucion.tipo_devolucion eq 'normal'}">
                                            <span class="badge bg-success">Buen estado</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-secondary">Estado no definido</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${devolucion.razon}</td>
                                <td>
                                    <fmt:formatDate value="${devolucion.fecha_devolucion}" 
                                                    pattern="d/MMMM/yyyy - HH:mm:ss"/>
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
                                function cambiarTipoDevolucion() {
                                    const tipoSelect = document.getElementById('tipoSelect');
                                    const ventasSection = document.getElementById('ventasSection');
                                    const comprasSection = document.getElementById('comprasSection');

                                    // Limpiar campos
                                    document.getElementById('id_producto').value = '';
                                    document.getElementById('id_lote').value = '';
                                    document.getElementById('cantidad').value = '';
                                    document.getElementById('cantidad').max = '';
                                    document.getElementById('cantidad').placeholder = '';

                                    if (tipoSelect.value === 'compra') {
                                        ventasSection.style.display = 'none';
                                        comprasSection.style.display = 'block';
                                        document.getElementById('compraSelect').required = true;
                                        document.getElementById('ventaSelect').required = false;
                                    } else {
                                        ventasSection.style.display = 'block';
                                        comprasSection.style.display = 'none';
                                        document.getElementById('compraSelect').required = false;
                                        document.getElementById('ventaSelect').required = true;
                                    }
                                }

                                function cargarDatosVenta() {
                                    const select = document.getElementById('ventaSelect');
                                    const option = select.options[select.selectedIndex];

                                    if (option.value) {
                                        document.getElementById('id_producto').value = option.dataset.producto;
                                        document.getElementById('id_lote').value = option.dataset.lote;

                                        const cantidadInput = document.getElementById('cantidad');
                                        cantidadInput.max = option.dataset.cantidad;
                                        cantidadInput.placeholder = `Máximo: ${option.dataset.cantidad}`;
                                    }
                                }

                                function cargarDatosCompra() {
                                    const select = document.getElementById('compraSelect');
                                    const option = select.options[select.selectedIndex];

                                    if (option.value) {
                                        document.getElementById('id_producto').value = option.dataset.producto;
                                        document.getElementById('id_lote').value = option.dataset.lote;

                                        const cantidadInput = document.getElementById('cantidad');
                                        cantidadInput.max = option.dataset.disponible;
                                        cantidadInput.placeholder = `Máximo: ${option.dataset.disponible}`;
                                    }
                                }

                                // Validación del formulario
                                document.querySelector('form').addEventListener('submit', function (e) {
                                    const tipoOperacion = document.getElementById('tipoSelect').value;
                                    const cantidad = parseInt(document.getElementById('cantidad').value);
                                    const maxCantidad = parseInt(document.getElementById('cantidad').max);

                                    if (!document.getElementById('id_producto').value) {
                                        e.preventDefault();
                                        alert('Debe seleccionar una ' + (tipoOperacion === 'venta' ? 'venta' : 'compra'));
                                        return;
                                    }

                                    if (!maxCantidad) {
                                        e.preventDefault();
                                        alert('Debe seleccionar un registro válido');
                                        return;
                                    }

                                    if (cantidad <= 0) {
                                        e.preventDefault();
                                        alert('La cantidad debe ser mayor a 0');
                                        return;
                                    }

                                    if (cantidad > maxCantidad) {
                                        e.preventDefault();
                                        alert('La cantidad no puede ser mayor a ' + maxCantidad);
                                        return;
                                    }

                                    const razon = document.getElementById('razon').value.trim();
                                    if (!razon) {
                                        e.preventDefault();
                                        alert('Debe especificar una razón para la devolución');
                                        return;
                                    }

                                    // Confirmar la operación
                                    if (!confirm('¿Está seguro de registrar esta devolución de ' + tipoOperacion + '?')) {
                                        e.preventDefault();
                                        return;
                                    }
                                });
        </script>
    </body>
</html>