<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inventario</title>
    <!-- Carga de Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        h1 {
            color: #343a40;
            text-align: center;
            margin-top: 20px;
        }
        .table {
            margin-top: 30px;
            border-radius: 8px;
            background-color: #fff;
        }
        .table th, .table td {
            vertical-align: middle;
        }
        .table th {
            background-color: #007bff;
            color: white;
        }
        .table tbody tr:nth-child(odd) {
            background-color: #f2f2f2;
        }
        .table-striped tbody tr:nth-child(odd) {
            background-color: #e9ecef;
        }
    </style>
</head>
<body>
    <div class="container mt-5">
        <h1>Información Detalla Inventario</h1>
        <table class="table table-striped table-bordered">
            <thead>
                <tr>
                    <th>ID Compra</th>
                    <th>Nombre Producto</th>
                    <th>ID Lote</th>
                    <th>Cantidad</th>
                    <th>Costo Unitario</th>
                    <th>Costo Total</th>
                    <th>Cantidad Disponible</th>
                    <th>Fecha Compra</th>
                    <th>Tipo Movimiento</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="compra" items="${listaCompras}">
                    <tr>
                        <td>${compra.id_compra}</td>
                        <td>${compra.nombre}</td>
                        <td>${compra.id_lote}</td>
                        <td>${compra.cantidad}</td>
                        <td><fmt:formatNumber value="${compra.costo_unitario}" type="currency" currencySymbol="$" minFractionDigits="2" maxFractionDigits="2" groupingUsed="true" /></td>
                        <td><fmt:formatNumber value="${compra.costo_total}" type="currency" currencySymbol="$" minFractionDigits="2" maxFractionDigits="2" groupingUsed="true" /></td>
                        <td>${compra.cantidad_disponible}</td>
                        <td><fmt:formatDate value="${compra.fecha_compra}" pattern="d'/'MMMM'/'yyyy" /></td>
                        <td>${compra.tipo_movimiento}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <!-- Script de Bootstrap -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
