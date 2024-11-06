<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Registro de Compras y Lotes</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container mt-4">
            <h2>Registro de Compra</h2>

            <!-- Formulario de Registro de Compra -->
            <form action="ComprasServlet" method="post" class="p-4 border rounded shadow-sm">
                <div class="mb-3">
                    <label for="producto" class="form-label">Producto:</label>
                    <select name="id_producto" id="producto" class="form-select">
                        <c:forEach var="producto" items="${listaProductos}">
                            <option value="${producto.id_producto}">${producto.nombre}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="mb-3">
                    <label for="cantidad" class="form-label">Cantidad:</label>
                    <input type="number" name="cantidad" id="cantidad" class="form-control" required>
                </div>
                <div class="mb-3">
                    <label for="costo_unitario" class="form-label">Costo Unitario:</label>
                    <input type="text" name="costo_unitario" id="costo_unitario" class="form-control" required>
                </div>

                <div class="text-end">
                    <button type="submit" class="btn btn-primary">Registrar Compra</button>
                </div>
            </form>

            <!-- Mensaje de éxito o error -->
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
