<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro de Compras | DevConta</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        :root {
            --primary-color: #2c3e50;
            --secondary-color: #34495e;
            --accent-color: #3498db;
            --success-color: #2ecc71;
            --warning-color: #f1c40f;
            --danger-color: #e74c3c;
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

        .form-container {
            background: white;
            border-radius: 15px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.05);
            padding: 2rem;
            margin-bottom: 2rem;
        }

        .form-label {
            color: var(--primary-color);
            font-weight: 600;
            margin-bottom: 0.5rem;
        }

        .form-control, .form-select {
            border-radius: 8px;
            border: 1px solid #dee2e6;
            padding: 0.75rem;
            transition: all 0.3s ease;
        }

        .form-control:focus, .form-select:focus {
            border-color: var(--accent-color);
            box-shadow: 0 0 0 0.2rem rgba(52, 152, 219, 0.25);
        }

        .table-container {
            background: white;
            border-radius: 15px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.05);
            padding: 1.5rem;
            margin-top: 2rem;
        }

        .table {
            margin-bottom: 0;
        }

        .table thead th {
            background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
            color: white;
            padding: 1rem;
            font-weight: 500;
            border: none;
        }

        .table tbody tr {
            transition: all 0.3s ease;
        }

        .table tbody tr:hover {
            background-color: rgba(52, 152, 219, 0.05);
        }

        .badge-status {
            padding: 0.5rem 1rem;
            border-radius: 20px;
            font-weight: 500;
        }

        .btn {
            padding: 0.6rem 1.5rem;
            border-radius: 8px;
            font-weight: 500;
            transition: all 0.3s ease;
        }

        .btn-primary {
            background: var(--accent-color);
            border: none;
        }

        .btn-primary:hover {
            background: #2980b9;
            transform: translateY(-2px);
        }

        .btn-danger {
            background: var(--danger-color);
            border: none;
        }

        .btn-danger:hover {
            background: #c0392b;
            transform: translateY(-2px);
        }

        .alert {
            border-radius: 10px;
            border: none;
            padding: 1rem;
        }

        .alert-success {
            background: rgba(46, 204, 113, 0.1);
            color: #27ae60;
        }

        .alert-danger {
            background: rgba(231, 76, 60, 0.1);
            color: #c0392b;
        }

        .form-check-input:checked {
            background-color: var(--accent-color);
            border-color: var(--accent-color);
        }

        .price-info {
            background: #f8f9fa;
            border-radius: 8px;
            padding: 1rem;
            margin-top: 1rem;
        }

        /* Animaciones */
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(20px); }
            to { opacity: 1; transform: translateY(0); }
        }

        .form-container, .table-container {
            animation: fadeIn 0.5s ease-out forwards;
        }
    </style>
</head>
<body>
    <jsp:include page="/nav/navbar.jsp"></jsp:include>

    <!-- Header -->
    <div class="page-header">
        <div class="container">
            <h2 class="text-center m-0">
                <i class="fas fa-shopping-cart me-2"></i>
                Registro de Compras
            </h2>
        </div>
    </div>

    <div class="container">
        <!-- Mensajes -->
        <c:if test="${not empty mensaje}">
            <c:choose>
                <c:when test="${fn:contains(mensaje, 'Error') || fn:contains(mensaje, 'No se puede') || fn:contains(mensaje, 'Ya existe')}">
                    <div class="alert alert-danger" role="alert">
                        <i class="fas fa-exclamation-circle me-2"></i>
                        ${mensaje}
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="alert alert-success" role="alert">
                        <i class="fas fa-check-circle me-2"></i>
                        ${mensaje}
                    </div>
                </c:otherwise>
            </c:choose>
        </c:if>

        <!-- Formulario de Registro -->
        <div class="form-container">
            <form action="ComprasServlet" method="post">
                <input type="hidden" name="action" value="create">
                
                <div class="row mb-4">
                    <div class="col-md-6">
                        <label class="form-label">
                            <i class="fas fa-box me-2"></i>Seleccionar Producto
                        </label>
                        <select name="id_producto" id="producto" class="form-select">
                            <c:forEach var="producto" items="${listaProductos}">
                                <option value="${producto.id_producto}">${producto.nombre}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">
                            <i class="fas fa-sort-amount-up me-2"></i>Cantidad
                        </label>
                        <input type="number" name="cantidad" id="cantidad" class="form-control" required>
                    </div>
                </div>

                <div class="row mb-4">
                    <div class="col-md-6">
                        <label class="form-label">
                            <i class="fas fa-tag me-2"></i>Costo Unitario
                        </label>
                        <input type="text" name="costo_unitario1" id="costo_unitario1" 
                               class="form-control" required oninput="calcularPrecio()">
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Tipo de Precio</label>
                        <div class="mt-2">
                            <div class="form-check form-check-inline">
                                <input type="radio" id="precioNormal" name="tipoPrecio" 
                                       value="normal" checked onclick="calcularPrecio()" 
                                       class="form-check-input">
                                <label for="precioNormal" class="form-check-label">Precio Normal</label>
                            </div>
                            <div class="form-check form-check-inline">
                                <input type="radio" id="ivaIncluido" name="tipoPrecio" 
                                       value="iva" onclick="calcularPrecio()" 
                                       class="form-check-input">
                                <label for="ivaIncluido" class="form-check-label">IVA Incluido</label>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="price-info mb-4">
                    <div class="row">
                        <div class="col-md-6">
                            <label class="form-label">
                                <i class="fas fa-dollar-sign me-2"></i>Precio Sin IVA
                            </label>
                            <input type="text" id="costo_unitario" name="costo_unitario" 
                                   class="form-control" readonly>
                        </div>
                    </div>
                </div>

                <div class="text-end">
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-save me-2"></i>Registrar Compra
                    </button>
                </div>
            </form>
        </div>

        <!-- Tabla de Compras -->
        <div class="table-container">
            <h3 class="mb-4">
                <i class="fas fa-list me-2"></i>
                Listado de Compras
            </h3>
            <div class="table-responsive">
                <table class="table">
                    <thead>
                        <tr>
                            <th><i class="fas fa-hashtag me-2"></i>ID</th>
                            <th><i class="fas fa-box me-2"></i>Producto</th>
                            <th><i class="fas fa-barcode me-2"></i>Lote</th>
                            <th><i class="fas fa-sort-amount-up me-2"></i>Cantidad</th>
                            <th><i class="fas fa-tag me-2"></i>Costo Unit.</th>
                            <th><i class="fas fa-dollar-sign me-2"></i>Total</th>
                            <th><i class="fas fa-calendar me-2"></i>Fecha</th>
                            <th><i class="fas fa-cog me-2"></i>Acciones</th>
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
                                    <fmt:formatNumber value="${compra.costo_unitario}" 
                                                    type="currency" 
                                                    currencySymbol="$" 
                                                    minFractionDigits="2" 
                                                    maxFractionDigits="2"/>
                                </td>
                                <td>
                                    <fmt:formatNumber value="${compra.costo_total}" 
                                                    type="currency" 
                                                    currencySymbol="$" 
                                                    minFractionDigits="2" 
                                                    maxFractionDigits="2"/>
                                </td>
                                <td>
                                    <fmt:formatDate value="${compra.fecha_compra}" 
                                                  pattern="d'/'MMMM'/'yyyy '-' HH:mm:ss" />
                                </td>
                                <td>
                                    <form method="post" action="ComprasServlet" 
                                          style="display:inline;" 
                                          onsubmit="return confirmarEliminacion()">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id_compra" value="${compra.id_compra}">
                                        <input type="hidden" name="id_lote" value="${compra.id_lote}">
                                        <button type="submit" class="btn btn-danger btn-sm">
                                            <i class="fas fa-trash-alt"></i>
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <br>

    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
        function calcularPrecio() {
            const costoUnitario1 = parseFloat(document.getElementById('costo_unitario1').value) || 0;
            const costo_unitario = document.getElementById('costo_unitario');
            const ivaIncluido = document.getElementById('ivaIncluido').checked;
            const ivaRate = 0.13;

            if (ivaIncluido) {
                costo_unitario.value = (costoUnitario1 / (1 + ivaRate)).toFixed(2);
            } else {
                costo_unitario.value = costoUnitario1.toFixed(2);
            }
        }

        function confirmarEliminacion() {
            return confirm('¿Está seguro que desea eliminar esta compra?');
        }

        // Animaciones para la tabla
        document.addEventListener('DOMContentLoaded', function() {
            document.querySelectorAll('tbody tr').forEach((tr, index) => {
                tr.style.opacity = '0';
                tr.style.animation = `fadeIn 0.3s ease-out ${index * 0.1}s forwards`;
            });
        });
    </script>
</body>
</html>