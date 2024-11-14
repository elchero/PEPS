<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Información de Inventario | DevConta</title>
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

        .header-title {
            font-size: 2rem;
            font-weight: 600;
            margin: 0;
        }

        .stats-container {
            margin-bottom: 2rem;
        }

        .stat-card {
            background: white;
            border-radius: 10px;
            padding: 1.5rem;
            box-shadow: 0 2px 15px rgba(0,0,0,0.05);
            transition: transform 0.3s ease;
        }

        .stat-card:hover {
            transform: translateY(-5px);
        }

        .stat-value {
            font-size: 1.8rem;
            font-weight: 600;
            color: var(--accent-color);
        }

        .stat-label {
            color: var(--secondary-color);
            font-size: 0.9rem;
            margin-top: 0.5rem;
        }

        .table-container {
            background: white;
            border-radius: 12px;
            padding: 1.5rem;
            box-shadow: 0 2px 15px rgba(0,0,0,0.05);
            margin-bottom: 2rem;
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
            text-transform: uppercase;
            font-size: 0.85rem;
            letter-spacing: 0.5px;
        }

        .table tbody tr {
            transition: all 0.3s ease;
        }

        .table tbody tr:hover {
            background-color: rgba(52, 152, 219, 0.05) !important;
        }

        .table tbody td {
            padding: 1rem;
            border-bottom: 1px solid #eee;
            font-size: 0.9rem;
        }

        .badge {
            padding: 0.5rem 1rem;
            font-weight: 500;
            border-radius: 20px;
        }

        .badge-cantidad {
            background-color: var(--accent-color);
        }

        .badge-disponible {
            background-color: var(--success-color);
        }

        .precio {
            font-weight: 600;
            color: var(--primary-color);
        }

        .fecha {
            color: #666;
            font-size: 0.85rem;
        }

        .tipo-movimiento {
            text-transform: uppercase;
            font-size: 0.8rem;
            font-weight: 600;
        }

        /* Filtros y búsqueda */
        .filters-section {
            margin-bottom: 2rem;
        }

        .search-box {
            position: relative;
        }

        .search-box input {
            padding-left: 2.5rem;
            border-radius: 20px;
            border: 1px solid #ddd;
        }

        .search-box i {
            position: absolute;
            left: 1rem;
            top: 50%;
            transform: translateY(-50%);
            color: #666;
        }

        /* Animaciones */
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(20px); }
            to { opacity: 1; transform: translateY(0); }
        }

        .table-container {
            animation: fadeIn 0.5s ease-out;
        }

        /* Responsividad */
        @media (max-width: 768px) {
            .table-container {
                padding: 1rem;
            }

            .header-title {
                font-size: 1.5rem;
            }

            .stat-card {
                margin-bottom: 1rem;
            }
        }
    </style>
</head>
<body>
    <jsp:include page="/nav/navbar.jsp"></jsp:include>

    <!-- Header -->
    <div class="page-header">
        <div class="container">
            <div class="d-flex justify-content-between align-items-center">
                <h1 class="header-title">
                    <i class="fas fa-warehouse me-2"></i>
                    Información del Inventario
                </h1>
            </div>
        </div>
    </div>

    <div class="container">
        <!-- Estadísticas -->
        <div class="row stats-container">
            <div class="col-md-3">
                <div class="stat-card">
                    <div class="stat-value" id="totalProductos">0</div>
                    <div class="stat-label">Total de Productos</div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-card">
                    <div class="stat-value" id="valorInventario">$0</div>
                    <div class="stat-label">Valor Total del Inventario</div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-card">
                    <div class="stat-value" id="totalLotes">0</div>
                    <div class="stat-label">Total de Lotes</div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-card">
                    <div class="stat-value" id="productosAgotados">0</div>
                    <div class="stat-label">Lotes agotados</div>
                </div>
            </div>
        </div>

        <!-- Filtros y Búsqueda -->
        <div class="row filters-section">
            <div class="col-md-4">
                <div class="search-box">
                    <i class="fas fa-search"></i>
                    <input type="text" class="form-control" id="searchInput" placeholder="Buscar producto...">
                </div>
            </div>
        </div>

        <!-- Tabla -->
        <div class="table-container">
            <div class="table-responsive">
                <table class="table">
                    <thead>
                        <tr>
                            <th><i class="fas fa-hashtag me-2"></i>ID</th>
                            <th><i class="fas fa-box me-2"></i>Producto</th>
                            <th><i class="fas fa-barcode me-2"></i>Lote</th>
                            <th><i class="fas fa-sort-amount-up me-2"></i>Cantidad</th>
                            <th><i class="fas fa-tag me-2"></i>Costo Unit.</th>
                            <th><i class="fas fa-dollar-sign me-2"></i>Costo Total</th>
                            <th><i class="fas fa-boxes me-2"></i>Disponible</th>
                            <th><i class="fas fa-calendar me-2"></i>Fecha</th>
                            <th><i class="fas fa-exchange-alt me-2"></i>Movimiento</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="compra" items="${listaCompras}">
                            <tr>
                                <td>${compra.id_compra}</td>
                                <td>
                                    <strong>${compra.nombre}</strong>
                                </td>
                                <td>${compra.id_lote}</td>
                                <td>
                                    <span class="badge badge-cantidad">
                                        ${compra.cantidad}
                                    </span>
                                </td>
                                <td class="precio">
                                    <fmt:formatNumber value="${compra.costo_unitario}" 
                                                    type="currency" 
                                                    currencySymbol="$" 
                                                    minFractionDigits="2" 
                                                    maxFractionDigits="2" 
                                                    groupingUsed="true" />
                                </td>
                                <td class="precio">
                                    <fmt:formatNumber value="${compra.costo_total}" 
                                                    type="currency" 
                                                    currencySymbol="$" 
                                                    minFractionDigits="2" 
                                                    maxFractionDigits="2" 
                                                    groupingUsed="true" />
                                </td>
                                <td>
                                    <span class="badge badge-disponible">
                                        ${compra.cantidad_disponible}
                                    </span>
                                </td>
                                <td class="fecha">
                                    <fmt:formatDate value="${compra.fecha_compra}" 
                                                  pattern="d'/'MMMM'/'yyyy '-' HH:mm:ss" />
                                </td>
                                <td>
                                    <span class="tipo-movimiento">
                                        ${compra.tipo_movimiento}
                                    </span>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Función para calcular estadísticas
        function calcularEstadisticas() {
            const rows = document.querySelectorAll('tbody tr');
            let totalProductos = rows.length;
            let valorTotal = 0;
            let lotesUnicos = new Set();
            let productosAgotados = 0;

            rows.forEach(row => {
                const costoTotal = parseFloat(row.cells[5].textContent.replace(/[$,]/g, ''));
                const disponible = parseInt(row.cells[6].textContent);
                const lote = row.cells[2].textContent;

                valorTotal += costoTotal;
                lotesUnicos.add(lote);
                if (disponible <= 5) productosAgotados++;
            });

            document.getElementById('totalProductos').textContent = totalProductos;
            document.getElementById('valorInventario').textContent = '$' + valorTotal.toFixed(2);
            document.getElementById('totalLotes').textContent = lotesUnicos.size;
            document.getElementById('productosAgotados').textContent = productosAgotados;
        }

        // Función para filtrar la tabla
        document.getElementById('searchInput').addEventListener('keyup', function() {
            const searchTerm = this.value.toLowerCase();
            const rows = document.querySelectorAll('tbody tr');

            rows.forEach(row => {
                const text = row.textContent.toLowerCase();
                row.style.display = text.includes(searchTerm) ? '' : 'none';
            });
        });

        // Calcular estadísticas al cargar la página
        document.addEventListener('DOMContentLoaded', calcularEstadisticas);
    </script>
</body>
</html>