<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Información de Inventario</title>
    <!-- Bootstrap CSS -->
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
    <!-- Custom Styles -->
    <style>
        body {
            background-color: #f8f9fa;
        }
        h1 {
            text-align: center;
            margin-top: 20px;
            margin-bottom: 40px;
        }
        .table-container {
            margin: 0 auto;
            max-width: 90%;
            background-color: #fff;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0px 0px 15px rgba(0, 0, 0, 0.1);
        }
        table {
            margin-bottom: 0;
        }
        th {
            background-color: #007bff;
            color: #fff;
            text-align: center;
        }
        td {
            text-align: center;
        }
        .table thead th {
            position: sticky;
            top: 0;
            z-index: 1;
        }
    </style>
</head>
<body>

    <div class="container">
        <h1>Información Detallada Inventario</h1>

        <div class="table-container">
            <table class="table table-hover table-bordered table-striped">
                <thead>
                    <tr>
                        <th>ID Compra</th>
                        <th>Nombre Producto</th>
                        <th>ID Lote</th>
                        <th>Cantidad</th>
                        <th>Costo Unitario</th>
                        <th>Costo Total</th>
                        <th>Cantidad Disponible</th>
                        <th>Fecha de Compra</th>
                        <th>Tipo de Movimiento</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="compra" items="${listaInventarioInfo}">
                        <tr>
                            <td>${compra.id_compra}</td>
                            <td>${compra.nombre}</td>
                            <td>${compra.id_lote}</td>
                            <td>${compra.cantidad}</td>
                            <td>${compra.costo_unitario}</td>
                            <td>${compra.costo_total}</td>
                            <td>${compra.cantidad_disponible}</td>
                            <td>${compra.fecha_compra}</td>
                            <td>${compra.tipo_movimiento}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <!-- Bootstrap JS, Popper.js, and jQuery -->
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>

</body>
</html>
