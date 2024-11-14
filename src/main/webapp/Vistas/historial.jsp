<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Historial de Movimientos | DevConta</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        :root {
            --primary-color: #2c3e50;
            --secondary-color: #34495e;
            --accent-color: #3498db;
        }

        body {
            background-color: #f8f9fa;
            color: var(--primary-color);
        }

        .page-header {
            background: linear-gradient(45deg, var(--primary-color), var(--secondary-color));
            color: white;
            padding: 2rem 0;
            margin-bottom: 2rem;
            box-shadow: 0 2px 15px rgba(0,0,0,0.1);
        }

        .filters-card {
            background: white;
            border-radius: 10px;
            padding: 1.5rem;
            box-shadow: 0 2px 15px rgba(0,0,0,0.05);
            margin-bottom: 2rem;
        }

        .filters-card label {
            font-weight: 500;
            color: var(--primary-color);
            margin-bottom: 0.5rem;
        }

        .form-select, .form-control {
            border: 1px solid #e0e0e0;
            padding: 0.5rem;
            border-radius: 6px;
            transition: all 0.3s ease;
        }

        .form-select:focus, .form-control:focus {
            border-color: var(--accent-color);
            box-shadow: 0 0 0 0.2rem rgba(52, 152, 219, 0.25);
        }

        /* Badges mejorados */
        .badge {
            color: white !important;
            font-weight: 500;
            padding: 8px 16px !important;
            font-size: 0.85rem !important;
            border-radius: 20px;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            transition: all 0.3s ease;
        }

        .badge-COMPRA, .badge-compra {
            background: linear-gradient(45deg, #0d6efd, #0a58ca) !important;
        }

        .badge-VENTA, .badge-venta {
            background: linear-gradient(45deg, #198754, #146c43) !important;
        }

        .badge-DEVOLUCIÓN-VENTA, .badge-devolución-venta, .badge-devolucion-venta {
            background: linear-gradient(45deg, #212529, #1a1e21) !important;
        }

        .badge-DEVOLUCIÓN-COMPRA, .badge-devolución-compra, .badge-devolucion-compra {
            background: linear-gradient(45deg, #fd7e14, #ca6510) !important;
        }

        .badge-DEVOLUCIÓN-DEFECTUOSO-VENTA, .badge-devolución-defectuoso-venta, .badge-devolucion-defectuoso-venta {
            background: linear-gradient(45deg, #dc3545, #b02a37) !important;
        }

        /* Tabla mejorada */
        .table-container {
            background: white;
            border-radius: 10px;
            padding: 1rem;
            box-shadow: 0 2px 15px rgba(0,0,0,0.05);
        }

        .table {
            margin-bottom: 0;
        }

        .table thead th {
            background-color: #f8f9fa;
            border-bottom: 2px solid #e9ecef;
            color: var(--primary-color);
            font-weight: 600;
            text-transform: uppercase;
            font-size: 0.85rem;
            letter-spacing: 0.5px;
        }

        .table tbody tr {
            transition: all 0.3s ease;
        }

        .table tbody tr:hover {
            background-color: #f8f9fa;
        }

        /* Botón mejorado */
        .btn-info {
            background-color: var(--accent-color);
            border: none;
            color: white;
            padding: 0.5rem 1rem;
            border-radius: 6px;
            transition: all 0.3s ease;
        }

        .btn-info:hover {
            background-color: #2980b9;
            transform: translateY(-1px);
        }

        /* Modal mejorado */
        .modal-content {
            border-radius: 10px;
            border: none;
        }

        .modal-header {
            background: linear-gradient(45deg, var(--primary-color), var(--secondary-color));
            color: white;
            border-radius: 10px 10px 0 0;
            border-bottom: none;
        }

        .modal-body {
            padding: 2rem;
        }

        /* Animaciones */
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(20px); }
            to { opacity: 1; transform: translateY(0); }
        }

        .table-container {
            animation: fadeIn 0.5s ease-out;
        }

        /* Responsive */
        @media (max-width: 768px) {
            .page-header {
                padding: 1.5rem 0;
            }
            
            .filters-card {
                padding: 1rem;
            }
            
            .table-container {
                padding: 0.5rem;
            }
        }
    </style>
</head>
<body>
    <jsp:include page="/nav/navbar.jsp"></jsp:include>

    <!-- Header -->
    <div class="page-header">
        <div class="container">
            <h2 class="text-center m-0">
                <i class="fas fa-history me-2"></i>
                Historial de Movimientos de Inventario
            </h2>
        </div>
    </div>

    <div class="container">
        <!-- Filtros -->
        <div class="filters-card">
            <div class="row g-3">
                <div class="col-md-4">
                    <label for="filtroTipo" class="form-label">
                        <i class="fas fa-filter me-2"></i>Tipo de Operación
                    </label>
                    <select class="form-select" id="filtroTipo" onchange="filtrarHistorial()">
                        <option value="">Todos los tipos</option>
                        <option value="COMPRA">Compras</option>
                        <option value="VENTA">Ventas</option>
                        <option value="DEVOLUCIÓN">Devoluciones</option>
                    </select>
                </div>
                <div class="col-md-4">
                    <label for="fechaDesde" class="form-label">
                        <i class="fas fa-calendar-alt me-2"></i>Desde
                    </label>
                    <input type="date" class="form-control" id="fechaDesde" onchange="filtrarHistorial()">
                </div>
                <div class="col-md-4">
                    <label for="fechaHasta" class="form-label">
                        <i class="fas fa-calendar-alt me-2"></i>Hasta
                    </label>
                    <input type="date" class="form-control" id="fechaHasta" onchange="filtrarHistorial()">
                </div>
            </div>
        </div>

        <!-- Tabla -->
        <div class="table-container">
            <div class="table-responsive">
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th><i class="fas fa-calendar me-2"></i>Fecha</th>
                            <th><i class="fas fa-tag me-2"></i>Tipo</th>
                            <th><i class="fas fa-hashtag me-2"></i>ID</th>
                            <th><i class="fas fa-box me-2"></i>Producto</th>
                            <th><i class="fas fa-barcode me-2"></i>Lote</th>
                            <th><i class="fas fa-sort-amount-up me-2"></i>Cantidad</th>
                            <th><i class="fas fa-dollar-sign me-2"></i>Costo Unitario</th>
                            <th><i class="fas fa-percent me-2"></i>IVA</th>
                            <th><i class="fas fa-coins me-2"></i>Total</th>
                            <th><i class="fas fa-comment me-2"></i>Razón</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="movimiento" items="${historialCompleto}">
                            <tr>
                                <td>
                                    <fmt:formatDate value="${movimiento.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/>
                                </td>
                                <td>
                                    <span class="badge badge-${fn:toLowerCase(fn:replace(movimiento.tipoOperacion, ' ', '-'))}">
                                        ${movimiento.tipoOperacion}
                                    </span>
                                </td>
                                <td>${movimiento.idOperacion}</td>
                                <td>${movimiento.nombreProducto}</td>
                                <td>${movimiento.numeroLote}</td>
                                <td>${movimiento.cantidad}</td>
                                <td>
                                    <fmt:formatNumber value="${movimiento.costoUnitario}" type="currency" currencySymbol="$"/>
                                </td>
                                <td>
                                    <fmt:formatNumber value="${movimiento.iva}" type="currency" currencySymbol="$"/>
                                </td>
                                <td>
                                    <fmt:formatNumber value="${movimiento.costoTotal}" type="currency" currencySymbol="$"/>
                                </td>
                                <td>
                                    <c:if test="${not empty movimiento.razon}">
                                        <button class="btn btn-info btn-sm" onclick="mostrarRazon('${movimiento.razon}')">
                                            <i class="fas fa-eye me-1"></i>Ver razón
                                        </button>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- Modal -->
    <div class="modal fade" id="razonModal" tabindex="-1">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">
                        <i class="fas fa-info-circle me-2"></i>
                        Razón de la devolución
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <p id="razonTexto" class="mb-0"></p>
                </div>
            </div>
        </div>
    </div>
    <br>

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

        // Animación para filas de la tabla
        document.querySelectorAll('tbody tr').forEach((tr, index) => {
            tr.style.animation = `fadeIn 0.3s ease-out ${index * 0.1}s forwards`;
            tr.style.opacity = '0';
        });
    </script>
</body>
</html>