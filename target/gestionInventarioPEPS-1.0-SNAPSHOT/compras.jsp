<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Registro de Compras y Lotes</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    </head>
    <body>
        <div class="container mt-4">
            <h2>Registro de Compra</h2>

            <!-- Formulario de Registro de Compra -->
            <form action="ComprasServlet" method="post">
                <div class="form-group">
                    <label for="id_producto">ID Producto:</label>
                    <input type="number" class="form-control" id="id_producto" name="id_producto" required>
                </div>
                <div class="form-group">
                    <label for="cantidad">Cantidad:</label>
                    <input type="number" class="form-control" id="cantidad" name="cantidad" required>
                </div>
                <div class="form-group">
                    <label for="costo_unitario">Costo Unitario:</label>
                    <input type="number" step="0.01" class="form-control" id="costo_unitario" name="costo_unitario" required>
                </div>
                <button type="submit" class="btn btn-primary">Registrar Compra</button>
            </form>

            <!-- Mensaje de éxito o error -->
            <c:if test="${not empty mensaje}">
                <div class="alert alert-info mt-3">${mensaje}</div>
            </c:if>

            <!-- Tabla de Compras -->
            <h3 class="mt-4">Listado de Compras</h3>
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>ID Compra</th>
                        <th>ID Producto</th>
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
                            <td>${compra.id_producto}</td>
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
                            <td>${lote.id_producto}</td>
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
