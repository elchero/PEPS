<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Registro de Compras y Lotes</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <script>
            function calcularPrecio() {
                const costoUnitario1 = parseFloat(document.getElementById('costo_unitario1').value) || 0;
                const costo_unitario = document.getElementById('costo_unitario');//este el precio sin IVA
                const ivaIncluido = document.getElementById('ivaIncluido').checked;
                const ivaRate = 0.13; // Suponiendo un IVA del 13%

                if (ivaIncluido) {
                    // Si el precio incluye IVA, calcular el precio sin IVA
                    costo_unitario.value = (costoUnitario1 / (1 + ivaRate)).toFixed(2);
                } else {
                    // Si es precio normal, mostrar el mismo costo unitario
                    costo_unitario.value = costoUnitario1.toFixed(2);
                }
            }
        </script>
    </head>
    <body>
        <div class="container mt-4">

            <h2 class="text-center mb-4">Registro de Compra</h2>

            <!-- Formulario de Registro de Compra -->
            <form action="ComprasServlet" method="post" class="p-5 border rounded shadow-sm bg-light">
                <input type="hidden" name="action" value="create">
                <!-- Selección de Producto -->
                <div class="row mb-3">
                    <div class="col">
                        <label for="producto" class="form-label fw-bold">Producto:</label>
                        <select name="id_producto" id="producto" class="form-select">
                            <c:forEach var="producto" items="${listaProductos}">
                                <option value="${producto.id_producto}">${producto.nombre}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <!-- Cantidad y Costo Unitario -->
                <div class="row mb-3">
                    <div class="col-md-6">
                        <label for="cantidad" class="form-label fw-bold">Cantidad:</label>
                        <input type="number" name="cantidad" id="cantidad" class="form-control" required>
                    </div>
                    <div class="col-md-6">
                        <label for="costo_unitario1" class="form-label fw-bold">Costo Unitario:</label>
                        <input type="text" name="costo_unitario1" id="costo_unitario1" class="form-control" required oninput="calcularPrecio()">
                    </div>
                </div>

                <!-- Tipo de Precio -->
                <div class="mb-3">
                    <label class="form-label fw-bold">Tipo de Precio:</label>
                    <div class="form-check form-check-inline ms-3">
                        <input type="radio" id="precioNormal" name="tipoPrecio" value="normal" checked onclick="calcularPrecio()" class="form-check-input">
                        <label for="precioNormal" class="form-check-label">Precio Normal</label>
                    </div>
                    <div class="form-check form-check-inline">
                        <input type="radio" id="ivaIncluido" name="tipoPrecio" value="iva" onclick="calcularPrecio()" class="form-check-input">
                        <label for="ivaIncluido" class="form-check-label">IVA Incluido</label>
                    </div>
                </div>

                <!-- Precio Sin IVA -->
                <div class="row mb-3">
                    <div class="col-md-6">
                        <label for="costo_unitario" class="form-label fw-bold">Precio Sin IVA:</label>
                        <input type="text" id="costo_unitario" name="costo_unitario" class="form-control">
                    </div>
                </div>

                <!-- Botón de Envío -->
                <div class="text-end">
                    <button type="submit" class="btn btn-primary px-4 py-2">Registrar Compra</button>
                </div>
            </form>
            <!-- Mensaje de éxito o error -->
            <c:if test="${not empty mensaje}">
                <c:choose>
                    <c:when test="${fn:contains(mensaje, 'Error') || fn:contains(mensaje, 'No se puede') || fn:contains(mensaje, 'Ya existe')}">
                        <div class="alert alert-danger mt-3" role="alert">${mensaje}</div>
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-success mt-3" role="alert">${mensaje}</div>
                    </c:otherwise>
                </c:choose>
            </c:if>
            <!-- Tabla de Compras -->
            <h3 class="mt-4">Listado de Compras</h3>
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>ID Compra</th>
                        <th>Producto</th>
                        <th>ID lote</th>
                        <th>Cantidad</th>
                        <th>Costo Unitario</th>
                        <th>Costo Total</th>
                        <th>Fecha</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="compra" items="${listaCompras}">
                        <tr>
                            <td>${compra.id_compra}</td>
                            <td>${compra.nombre}</td>
                            <td>${compra.id_lote}</td>
                            <td>${compra.cantidad}</td>
                            <td>
                                <fmt:formatNumber 
                                    value="${compra.costo_unitario}" 
                                    type="currency" 
                                    currencySymbol="$" 
                                    minFractionDigits="2" 
                                    maxFractionDigits="2"
                                    />
                            </td>
                            <td>
                                <fmt:formatNumber 
                                    value="${compra.costo_total}" 
                                    type="currency" 
                                    currencySymbol="$" 
                                    minFractionDigits="2" 
                                    maxFractionDigits="2"
                                    />
                            </td>
                            <td>
                                <fmt:formatDate value="${compra.fecha_compra}" pattern="d'/'MMMM'/'yyyy '-' HH:mm:ss" />
                            </td>
                            <td>                           
                                <form method="post" action="ComprasServlet" style="display:inline;" onsubmit="return confirmarEliminacion()">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="id_compra" value="${compra.id_compra}">
                                    <input type="hidden" name="id_lote" value="${compra.id_lote}">
                                    <button type="submit" class="btn btn-danger btn-sm">Eliminar</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <div class="modal fade" id="editModal" tabindex="-1" aria-labelledby="editModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <form method="post" action="ComprasServlet">
                            <input type="hidden" name="action" value="update">
                            <input type="hidden" id="id_compra" name="id_compra">
                            <input type="hidden" id="id_producto" name="id_producto">

                            <div class="modal-header">
                                <h5 class="modal-title" id="editModalLabel">Editar Compra</h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>

                            <div class="modal-body">
                                <div class="form-group">
                                    <label for="cantidad">Cantidad</label>
                                    <input type="number" class="form-control" id="cantidad" name="cantidad" required>
                                </div>
                                <div class="form-group">
                                    <label for="costo_unitario">Costo Unitario</label>
                                    <input type="number" class="form-control" id="costo_unitario" name="costo_unitario" step="0.01" required>
                                </div>
                            </div>

                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
                                <button type="submit" class="btn btn-primary">Guardar cambios</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
            <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"></script>
            <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

            <script>

                                    function confirmarEliminacion() {
                                        return confirm('¿Está seguro que desea eliminar esta compra?');
                                    }
            </script>
    </body>
</html>
