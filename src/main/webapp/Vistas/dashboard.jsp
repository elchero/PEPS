<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Resumen de Inventario</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        <style>
            .dashboard-card {
                border: none;
                border-radius: 10px;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                transition: transform 0.2s;
            }

            .dashboard-card:hover {
                transform: translateY(-5px);
            }

            .metric-card {
                background: linear-gradient(45deg, #4158D0, #C850C0);
                color: white;
            }

            .inventory-card {
                background: linear-gradient(45deg, #0093E9, #80D0C7);
                color: white;
            }

            .returns-card {
                background: linear-gradient(45deg, #FF3CAC, #784BA0);
                color: white;
            }

            .metric-icon {
                font-size: 2.5rem;
                margin-bottom: 1rem;
            }

            .metric-value {
                font-size: 2rem;
                font-weight: bold;
            }

            .metric-label {
                font-size: 0.9rem;
                opacity: 0.9;
            }

            .table-container {
                background: white;
                border-radius: 10px;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                padding: 1.5rem;
            }

            .custom-table {
                margin: 0;
            }

            .custom-table thead th {
                background-color: #f8f9fa;
                border-bottom: 2px solid #dee2e6;
                color: #495057;
                font-weight: 600;
            }

            .badge-status {
                padding: 0.5rem 1rem;
                border-radius: 20px;
                font-weight: 500;
            }

            .progress-bar-container {
                height: 8px;
                background-color: #e9ecef;
                border-radius: 4px;
                margin-top: 5px;
            }

            .progress-bar {
                height: 100%;
                border-radius: 4px;
                transition: width 0.3s ease;
            }

            body {
                background-color: #f8f9fa;
            }

            .section-title {
                font-size: 1.5rem;
                font-weight: 600;
                margin-bottom: 1.5rem;
                color: #344767;
            }

            .trend-indicator {
                padding: 4px 8px;
                border-radius: 4px;
                font-size: 0.8rem;
                margin-left: 8px;
            }

            .trend-up {
                background-color: rgba(40, 199, 111, 0.2);
                color: #28c76f;
            }

            .trend-down {
                background-color: rgba(234, 84, 85, 0.2);
                color: #ea5455;
            }
        </style>
    </head>
    <body>
        <jsp:include page="/nav/navbar.jsp"></jsp:include>

            <div class="container mt-4">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h2 class="section-title mb-0">Resumen de Inventario</h2>
                    <div class="btn-group">
                        <button class="btn btn-outline-primary">
                            <i class="fas fa-download me-2"></i>Exportar
                        </button>
                    </div>
                </div>

                <!-- Tarjetas de Métricas -->
                <div class="row mb-4 g-3">
                    <!-- Ventas -->
                    <div class="col-md-3">
                        <div class="card dashboard-card metric-card">
                            <div class="card-body p-4">
                                <div class="d-flex justify-content-between">
                                    <div>
                                        <div class="metric-label">Ventas Totales</div>
                                        <div class="metric-value">
                                            $<fmt:formatNumber value="${ventasNetas}" pattern="#,##0.00"/>
                                    </div>
                                    <div class="metric-label">
                                        ${unidadesVendidas} unidades
                                        <span class="trend-indicator trend-up">
                                            <fmt:formatNumber value="${porcentajeMargen}" pattern="#,##0.0"/>% margen
                                        </span>
                                    </div>
                                </div>
                                <div class="metric-icon">
                                    <i class="fas fa-shopping-cart"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card dashboard-card inventory-card">
                        <div class="card-body p-4">
                            <div class="d-flex justify-content-between">
                                <div>
                                    <div class="metric-label">Costo de Ventas</div>
                                    <div class="metric-value">
                                        $<fmt:formatNumber value="${costoVentasNeto}" pattern="#,##0.00"/>
                                    </div>
                                    <div class="metric-label">
                                        Margen: <fmt:formatNumber value="${((ventasNetas - costoVentasNeto) / ventasNetas) * 100}" pattern="#,##0.0"/>%
                                    </div>
                                </div>
                                <div class="metric-icon">
                                    <i class="fas fa-dollar-sign"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Inventario -->
                <div class="col-md-3">
                    <div class="card dashboard-card inventory-card">
                        <div class="card-body p-4">
                            <div class="d-flex justify-content-between">
                                <div>
                                    <div class="metric-label">Inventario Actual</div>
                                    <div class="metric-value">
                                        ${totalInventarioActual}
                                    </div>
                                    <div class="metric-label">
                                        En ${totalLotes} lotes activos
                                    </div>
                                </div>
                                <div class="metric-icon">
                                    <i class="fas fa-box"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Devoluciones sobre Ventas -->
                <div class="col-md-3">
                    <div class="card dashboard-card returns-card">
                        <div class="card-body p-4">
                            <div>
                                <div class="metric-label">Devoluciones sobre Ventas</div>
                                <div class="metric-value">
                                    ${cantidadDevolucionesVenta}
                                </div>
                                <div class="metric-label">
                                    <fmt:formatNumber value="${porcentajeDevolucionesVenta}" pattern="#,##0.0"/>% del total vendido
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Devoluciones sobre Compras -->
                <div class="col-md-3">
                    <div class="card dashboard-card">
                        <div class="card-body p-4">
                            <div>
                                <div class="metric-label">Devoluciones sobre Compras</div>
                                <div class="metric-value">
                                    ${cantidadDevolucionesCompra}
                                </div>
                                <div class="metric-label">
                                    $<fmt:formatNumber value="${costoDevolucionesCompra}" pattern="#,##0.00"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Tabla de Detalle -->
            <div class="table-container">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h4 class="section-title mb-0">Análisis por Producto</h4>
                    <div class="d-flex gap-2">
                        <div class="input-group" style="width: 300px;">
                            <span class="input-group-text bg-white border-end-0">
                                <i class="fas fa-search text-muted"></i>
                            </span>
                            <input type="text" class="form-control border-start-0" 
                                   id="searchInput" placeholder="Buscar producto...">
                        </div>
                    </div>
                </div>

                <div class="table-responsive">
                    <table class="table custom-table">
                        <thead>
                            <tr>
                                <th>Producto</th>
                                <th class="text-end">Inv. Inicial</th>
                                <th class="text-end">Inv. Actual</th>
                                <th class="text-end">Vendido</th>
                                <th class="text-end">Ventas Brutas</th>
                                <th class="text-end">Devoluciones</th>
                                <th class="text-end">Ventas Netas</th>
                                <th class="text-end">Margen</th>
                                <th class="text-end">Estado</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${resumenDashboard}">
                                <tr>
                                    <td>
                                        <div class="d-flex align-items-center">
                                            <div class="ms-2">
                                                <div class="fw-bold">${item.producto}</div>
                                                <div class="text-muted small">Lotes: ${item.totalLotes}</div>
                                            </div>
                                        </div>
                                    </td>
                                    <td class="text-end">${item.inventarioInicial}</td>
                                    <td class="text-end">
                                        ${item.inventarioActual}
                                        <div class="progress-bar-container">
                                            <div class="progress-bar bg-primary" 
                                                 style="width: ${(item.inventarioActual / item.inventarioInicial) * 100}%">
                                            </div>
                                        </div>
                                    </td>
                                    <td class="text-end">${item.unidadesVendidas}</td>
                                    <td class="text-end">$<fmt:formatNumber value="${item.ventasBrutas}" pattern="#,##0.00"/></td>
                                    <td class="text-end">$<fmt:formatNumber value="${item.costoDevolucionesVenta}" pattern="#,##0.00"/></td>
                                    <td class="text-end">$<fmt:formatNumber value="${item.ventasNetas}" pattern="#,##0.00"/></td>
                                    <td class="text-end">
                                        <span class="${item.ventasNetas - item.costoVentasNeto >= 0 ? 'text-success' : 'text-danger'}">
                                            $<fmt:formatNumber value="${item.ventasNetas - item.costoVentasNeto}" pattern="#,##0.00"/>
                                        </span>
                                    </td>
                                    <td class="text-end">
                                        <c:choose>
                                            <c:when test="${item.inventarioActual > 20}">
                                                <span class="badge bg-success badge-status">Stock OK</span>
                                            </c:when>
                                            <c:when test="${item.inventarioActual > 5}">
                                                <span class="badge bg-warning badge-status">Stock Bajo</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-danger badge-status">Crítico</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            // Búsqueda en tabla
            document.getElementById('searchInput').addEventListener('keyup', function () {
                const searchText = this.value.toLowerCase();
                const table = document.querySelector('.custom-table tbody');
                const rows = table.getElementsByTagName('tr');

                for (let row of rows) {
                    const productCell = row.cells[0];
                    const productName = productCell.textContent || productCell.innerText;
                    if (productName.toLowerCase().indexOf(searchText) > -1) {
                        row.style.display = '';
                    } else {
                        row.style.display = 'none';
                    }
                }
            });
        </script>
    </body>
</html>