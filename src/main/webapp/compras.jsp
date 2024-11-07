<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
                        <input type="text" id="costo_unitario" name="costo_unitario" class="form-control" disabled>
                    </div>
                </div>

                <!-- Botón de Envío -->
                <div class="text-end">
                    <button type="submit" class="btn btn-primary px-4 py-2">Registrar Compra</button>
                </div>
            </form>
            <!-- Mensaje de éxito o error -->
            <c:if test="${not empty mensaje}">
                <div class="alert alert-info mt-3" role="alert">
                    ${mensaje}
                </div>
            </c:if>


            <!-- Tabla de Compras -->
            <h3 class="mt-4">Listado de Compras</h3>
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>ID Compra</th>
                        <th>Producto</th>
                        <th>ID Lote</th>
                        <th>Cantidad</th>
                        <th>Costo Total</th>
                        <th>Fecha de Compra</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="compra" items="${listaCompras}">
                        <tr>
                            <td>${compra.id_compra}</td>
                            <td>${compra.nombre}</td>
                            <td>${compra.id_lote}</td>
                            <td>${compra.cantidad}</td>
                            <td>${compra.costo_total}</td>
                            <td>
                                <fmt:formatDate value="${compra.fecha_compra}" pattern="d'/'MMMM'/'yyyy '-' HH:mm:ss" />
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <!-- Tabla de Lotes -->
            <h3 class="mt-4">Listado de Lotes</h3>
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>ID Lote</th>
                        <th>ID Producto</th>
                        <th>Costo Unitario</th>
                        <th>Fecha de Ingreso</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="lote" items="${listaLotes}">
                        <tr>
                            <td>${lote.id_lote}</td>
                            <td>${lote.nombre}</td>
                            <td>${lote.costo_unitario}</td>
                            <td>
                                <fmt:formatDate value="${lote.fecha_ingreso}" pattern="d'/'MMMM'/'yyyy '-' HH:mm:ss" />
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </body>
</html>
