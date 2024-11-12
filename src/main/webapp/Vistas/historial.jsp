<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Historial de Movimientos</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            /* Estilos básicos para todos los badges */
            .badge {
                color: white !important;
                font-weight: 500;
                padding: 8px 12px !important;
                font-size: 0.9em !important;
            }

            /* Operaciones básicas */
            .badge-COMPRA,
            .badge-compra {
                background-color: #0d6efd !important;  /* Azul */
            }

            .badge-VENTA,
            .badge-venta {
                background-color: #198754 !important;  /* Verde */
            }

            /* Devoluciones */
            .badge-DEVOLUCIÓN-VENTA,
            .badge-devolución-venta,
            .badge-devolucion-venta {
                background-color: #212529 !important;  /* Negro */
            }

            .badge-DEVOLUCIÓN-COMPRA,
            .badge-devolución-compra,
            .badge-devolucion-compra {
                background-color: #fd7e14 !important;  /* Naranja */
            }

            .badge-DEVOLUCIÓN-DEFECTUOSO-VENTA,
            .badge-devolución-defectuoso-venta,
            .badge-devolucion-defectuoso-venta {
                background-color: #dc3545 !important;  /* Rojo */
            }

            /* Hover effect */
            .badge:hover {
                opacity: 0.9;
            }
        </style>
    </head>
    <body>
        <jsp:include page="/nav/navbar.jsp"></jsp:include>
        <div class="container mt-4">
            <h2 class="text-center mb-4">Historial de Movimientos</h2>

            <!-- Filtros -->
            <div class="row mb-4">
                <div class="col-md-3">
                    <select class="form-select" id="filtroTipo" onchange="filtrarHistorial()">
                        <option value="">Todos los tipos</option>
                        <option value="COMPRA">Compras</option>
                        <option value="VENTA">Ventas</option>
                        <option value="DEVOLUCIÓN">Devoluciones</option>
                    </select>
                </div>
                <div class="col-md-3">
                    <input type="date" class="form-control" id="fechaDesde" onchange="filtrarHistorial()">
                </div>
                <div class="col-md-3">
                    <input type="date" class="form-control" id="fechaHasta" onchange="filtrarHistorial()">
                </div>
            </div>

            <!-- Tabla de Historial -->
            <div class="table-responsive">
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>Fecha</th>
                            <th>Tipo</th>
                            <th>ID</th>
                            <th>Producto</th>
                            <th>Cantidad</th>
                            <th>Costo Unitario</th>
                            <th>Total</th>
                            <th>Razón</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="movimiento" items="${historialCompleto}">
                            <tr>
                                <td>
                                    <fmt:formatDate value="${movimiento.fecha}" 
                                                    pattern="dd/MM/yyyy HH:mm:ss"/>
                                </td>
                                <td>
                                    <span class="badge badge-${fn:toLowerCase(fn:replace(movimiento.tipoOperacion, ' ', '-'))}">
                                        ${movimiento.tipoOperacion}
                                    </span>
                                </td>
                                <td>${movimiento.idOperacion}</td>
                                <td>${movimiento.nombreProducto}</td>
                                <td>${movimiento.cantidad}</td>
                                <td>
                                    <fmt:formatNumber value="${movimiento.costoUnitario}" 
                                                      type="currency" currencySymbol="$"/>
                                </td>
                                <td>
                                    <fmt:formatNumber value="${movimiento.costoTotal}" 
                                                      type="currency" currencySymbol="$"/>
                                </td>
                                <td>
                                    <c:if test="${not empty movimiento.razon}">
                                        <button class="btn btn-sm btn-info" 
                                                onclick="mostrarRazon('${movimiento.razon}')">
                                            Ver razón
                                        </button>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Modal para mostrar razón -->
        <div class="modal fade" id="razonModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Razón de la devolución</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <p id="razonTexto"></p>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>

        <script>
                                                    const modal = new bootstrap.Modal(document.getElementById('razonModal'));

                                                    function mostrarRazon(razon) {
                                                        document.getElementById('razonTexto').textContent = razon;
                                                        modal.show();
                                                    }

                                                    function filtrarHistorial() {
                                                        const tipo = document.getElementById('filtroTipo').value;
                                                        const desde = document.getElementById('fechaDesde').value;
                                                        const hasta = document.getElementById('fechaHasta').value;

                                                        // Implementar filtrado de tabla
                                                        const tabla = document.querySelector('table tbody');
                                                        const filas = tabla.getElementsByTagName('tr');

                                                        for (let fila of filas) {
                                                            let mostrar = true;
                                                            const tipoCell = fila.cells[1].textContent.trim();
                                                            const fecha = new Date(fila.cells[0].textContent);

                                                            if (tipo && !tipoCell.includes(tipo))
                                                                mostrar = false;
                                                            if (desde && fecha < new Date(desde))
                                                                mostrar = false;
                                                            if (hasta && fecha > new Date(hasta))
                                                                mostrar = false;

                                                            fila.style.display = mostrar ? '' : 'none';
                                                        }
                                                    }
        </script>
    </body>
</html>