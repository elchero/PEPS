<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Gestión de Devoluciones | DevConta</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
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

            .warning-box {
                background: linear-gradient(135deg, #f1c40f, #f39c12);
                color: #2c3e50;
                padding: 1rem;
                border-radius: 10px;
                margin-bottom: 2rem;
                box-shadow: 0 4px 15px rgba(0,0,0,0.05);
            }

            .warning-box i {
                font-size: 1.5rem;
                margin-right: 0.5rem;
                color: #e67e22;
            }

            .form-section {
                background: #f8f9fa;
                border-radius: 8px;
                padding: 1.5rem;
                margin-bottom: 1.5rem;
            }

            .form-label {
                color: var(--primary-color);
                font-weight: 600;
                margin-bottom: 0.5rem;
                display: flex;
                align-items: center;
            }

            .form-label i {
                margin-right: 0.5rem;
            }

            /* Select2 personalización */
            .select2-container--default .select2-selection--single {
                height: 38px;
                border: 1px solid #dee2e6;
                border-radius: 8px;
            }

            .select2-container--default .select2-selection--single .select2-selection__rendered {
                line-height: 38px;
                padding-left: 12px;
                color: var(--primary-color);
            }

            .select2-container--default .select2-selection--single .select2-selection__arrow {
                height: 36px;
            }

            .select2-container--default .select2-results__option--highlighted[aria-selected] {
                background-color: var(--accent-color);
            }

            .select2-dropdown {
                border: 1px solid #dee2e6;
                border-radius: 8px;
                box-shadow: 0 4px 15px rgba(0,0,0,0.05);
            }

            /* Badges personalizados */
            .badge {
                padding: 0.5rem 1rem;
                border-radius: 20px;
                font-weight: 500;
                text-transform: uppercase;
                letter-spacing: 0.5px;
            }

            .badge.bg-info {
                background: linear-gradient(135deg, #3498db, #2980b9) !important;
            }

            .badge.bg-primary {
                background: linear-gradient(135deg, #9b59b6, #8e44ad) !important;
            }

            .badge.bg-danger {
                background: linear-gradient(135deg, #e74c3c, #c0392b) !important;
            }

            .badge.bg-success {
                background: linear-gradient(135deg, #2ecc71, #27ae60) !important;
            }

            .badge.bg-warning {
                background: linear-gradient(135deg, #f1c40f, #f39c12) !important;
                color: #2c3e50;
            }

            .table-container {
                background: white;
                border-radius: 15px;
                box-shadow: 0 4px 15px rgba(0,0,0,0.05);
                padding: 1.5rem;
                margin-top: 2rem;
            }

            .table thead th {
                background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
                color: white;
                padding: 1rem;
                font-weight: 500;
                border: none;
            }

            .btn-revert {
                background: linear-gradient(135deg, #f1c40f, #f39c12);
                color: #2c3e50;
                border: none;
                padding: 0.6rem 1.5rem;
                border-radius: 8px;
                font-weight: 500;
                transition: all 0.3s ease;
            }

            .btn-revert:hover {
                transform: translateY(-2px);
                box-shadow: 0 4px 15px rgba(243, 156, 18, 0.3);
            }

            /* Animaciones */
            @keyframes fadeIn {
                from {
                    opacity: 0;
                    transform: translateY(20px);
                }
                to {
                    opacity: 1;
                    transform: translateY(0);
                }
            }

            .form-container, .table-container {
                animation: fadeIn 0.5s ease-out forwards;
            }

            /* Tooltips */
            [data-tooltip] {
                position: relative;
                cursor: help;
            }

            [data-tooltip]:before {
                content: attr(data-tooltip);
                position: absolute;
                bottom: 100%;
                left: 50%;
                transform: translateX(-50%);
                padding: 0.5rem 1rem;
                background: rgba(44, 62, 80, 0.9);
                color: white;
                border-radius: 4px;
                font-size: 0.8rem;
                white-space: nowrap;
                visibility: hidden;
                opacity: 0;
                transition: all 0.3s ease;
            }

            [data-tooltip]:hover:before {
                visibility: visible;
                opacity: 1;
                bottom: calc(100% + 5px);
            }
        </style>
    </head>
    <body>
        <jsp:include page="/nav/navbar.jsp"></jsp:include>

            <!-- Header -->
            <div class="page-header">
                <div class="container">
                    <h2 class="text-center m-0">
                        <i class="fas fa-exchange-alt me-2"></i>
                        Gestión de Devoluciones
                    </h2>
                </div>
            </div>

            <div class="container">
                <!-- Warning Box -->
                <div class="warning-box">
                    <div class="d-flex align-items-center">
                        <i class="fas fa-exclamation-triangle"></i>
                        <div>
                            <strong>Importante:</strong>
                            <p class="mb-0">
                                Las devoluciones afectarán directamente al inventario. Asegúrese de verificar 
                                todos los datos antes de procesar una devolución.
                            </p>
                        </div>
                    </div>
                </div>

                <!-- Mensajes -->
            <c:if test="${not empty mensaje}">
                <div class="alert alert-${tipoMensaje} alert-dismissible fade show" role="alert">
                    <i class="fas fa-${tipoMensaje == 'success' ? 'check-circle' : 'exclamation-circle'} me-2"></i>
                    ${mensaje}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>

            <!-- Formulario -->
            <div class="form-container">
                <form action="DevolucionesServlet" method="post" id="devolucionForm">
                    <input type="hidden" name="action" value="create">

                    <!-- Tipo de Devolución -->
                    <div class="form-section">
                        <div class="row">
                            <div class="col-md-6">
                                <label class="form-label">
                                    <i class="fas fa-exchange-alt me-2"></i>Tipo de Devolución
                                </label>
                                <select id="tipoSelect" name="tipo_operacion" 
                                        class="form-select" onchange="cambiarTipoDevolucion()">
                                    <option value="venta">Devolución de Venta</option>
                                    <option value="compra">Devolución de Compra</option>
                                </select>
                            </div>
                        </div>
                    </div>

                    <!-- Sección de Ventas -->
                    <div id="ventasSection" class="form-section">
                        <div class="row">
                            <div class="col-12">
                                <label class="form-label">
                                    <i class="fas fa-receipt me-2"></i>Seleccionar Venta
                                </label>
                                <select name="venta" id="ventaSelect" class="select2 form-select" 
                                        onchange="cargarDatosVenta()">
                                    <option value="">Seleccione una venta</option>
                                    <c:forEach var="venta" items="${ventasDisponibles}">
                                        <option value="${venta.id_venta}" 
                                                data-producto="${venta.id_producto}"
                                                data-lote="${venta.id_lote}"
                                                data-cantidad-original="${venta.cantidad_original}"
                                                data-cantidad-disponible="${venta.cantidad_disponible}"
                                                data-nombre="${venta.nombre_producto}"
                                                ${venta.cantidad_disponible <= 0 ? 'disabled' : ''}>
                                            Venta #${venta.id_venta} - ${venta.nombre_producto} - 
                                            Cantidad Original: ${venta.cantidad_original} - 
                                            Disponible: ${venta.cantidad_disponible}
                                            (<fmt:formatDate value="${venta.fecha_venta}" 
                                                            pattern="dd/MM/yyyy HH:mm:ss"/>)
                                            ${venta.cantidad_disponible <= 0 ? '- No disponible' : ''}
                                        </option>
                                    </c:forEach>
                                </select>
                                <small class="text-muted d-block mt-1">
                                    <i class="fas fa-info-circle me-1"></i>
                                    Las ventas se muestran ordenadas por fecha (PEPS)
                                </small>
                            </div>
                        </div>
                    </div>

                    <!-- Sección de Compras -->
                    <div id="comprasSection" class="form-section" style="display:none;">
                        <div class="row">
                            <div class="col-12">
                                <label class="form-label">
                                    <i class="fas fa-shopping-cart me-2"></i>Seleccionar Compra
                                </label>
                                <select name="compra" id="compraSelect" class="select2 form-select" 
                                        onchange="cargarDatosCompra()">
                                    <option value="">Seleccione una compra</option>
                                    <c:forEach var="compra" items="${comprasDisponibles}">
                                        <option value="${compra.id_compra}" 
                                                data-producto="${compra.id_producto}"
                                                data-lote="${compra.id_lote}"
                                                data-cantidad-original="${compra.cantidad_original}"
                                                data-cantidad-disponible="${compra.cantidad_disponible}"
                                                data-nombre="${compra.nombre_producto}"
                                                ${compra.cantidad_disponible <= 0 ? 'disabled' : ''}>
                                            Compra #${compra.id_compra} - ${compra.nombre_producto} - 
                                            Cantidad Original: ${compra.cantidad_original} - 
                                            Disponible: ${compra.cantidad_disponible}
                                            (<fmt:formatDate value="${compra.fecha_compra}" 
                                                            pattern="dd/MM/yyyy HH:mm:ss"/>)
                                        </option>
                                    </c:forEach>
                                </select>
                                <small class="text-muted d-block mt-1">
                                    <i class="fas fa-info-circle me-1"></i>
                                    Las compras se muestran ordenadas por fecha
                                </small>
                            </div>
                        </div>
                    </div>

                    <input type="hidden" name="id_producto" id="id_producto">
                    <input type="hidden" name="id_lote" id="id_lote">

                    <!-- Detalles de la devolución -->
                    <div class="form-section">
                        <div class="row">
                            <div class="col-md-4">
                                <label class="form-label">
                                    <i class="fas fa-sort-amount-up me-2"></i>Cantidad a Devolver
                                </label>
                                <input type="number" name="cantidad" id="cantidad" 
                                       class="form-control" required min="1">
                                <small class="text-muted d-block mt-1" id="cantidadHelper"></small>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label">
                                    <i class="fas fa-tag me-2"></i>Estado del Producto
                                </label>
                                <select name="tipo_devolucion" id="tipo_devolucion" 
                                        class="form-select" required>
                                    <option value="venta">En buen estado</option>
                                    <option value="defectuoso">Defectuoso</option>
                                </select>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label">
                                    <i class="fas fa-comment me-2"></i>Razón
                                </label>
                                <textarea name="razon" id="razon" class="form-control" 
                                          required rows="2" 
                                          placeholder="Especifique la razón de la devolución..."></textarea>
                            </div>
                        </div>
                    </div>

                    <div class="text-end mt-4">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save me-2"></i>Registrar Devolución
                        </button>
                    </div>
                </form>
            </div>

            <!-- Botón de Revertir -->
            <div class="mb-4 d-flex justify-content-end">
                <form action="DevolucionesServlet" method="post" onsubmit="return confirmarReversion()">
                    <input type="hidden" name="action" value="revertir">
                    <button type="submit" class="btn btn-revert">
                        <i class="fas fa-history me-2"></i>
                        Revertir Devoluciones
                    </button>
                </form>
            </div>

            <!-- Tabla de Devoluciones -->
            <div class="table-container">
                <h3 class="mb-4">
                    <i class="fas fa-history me-2"></i>
                    Historial de Devoluciones
                </h3>
                <div class="table-responsive">
                    <table class="table">
                        <thead>
                            <tr>
                                <th><i class="fas fa-hashtag me-2"></i>ID</th>
                                <th><i class="fas fa-box me-2"></i>Producto</th>
                                <th><i class="fas fa-barcode me-2"></i>Lote</th>
                                <th><i class="fas fa-sort-amount-up me-2"></i>Cantidad</th>
                                <th><i class="fas fa-exchange-alt me-2"></i>Tipo</th>
                                <th><i class="fas fa-info-circle me-2"></i>Estado</th>
                                <th><i class="fas fa-comment me-2"></i>Razón</th>
                                <th><i class="fas fa-calendar me-2"></i>Fecha</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="devolucion" items="${listaDevoluciones}">
                                <tr>
                                    <td>${devolucion.id_devolucion}</td>
                                    <td>${devolucion.nombre_producto}</td>
                                    <td>${devolucion.id_lote}</td>
                                    <td>${devolucion.cantidad}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${devolucion.tipo_operacion eq 'compra'}">
                                                <span class="badge bg-info">
                                                    <i class="fas fa-undo me-1"></i>
                                                    Devolución Compra
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-primary">
                                                    <i class="fas fa-undo me-1"></i>
                                                    Devolución Venta
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${devolucion.tipo_devolucion eq 'defectuoso'}">
                                                <span class="badge bg-danger">
                                                    <i class="fas fa-times me-1"></i>
                                                    Defectuoso
                                                </span>
                                            </c:when>
                                            <c:when test="${devolucion.tipo_devolucion eq 'normal'}">
                                                <span class="badge bg-success">
                                                    <i class="fas fa-check me-1"></i>
                                                    Buen estado
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-secondary">
                                                    <i class="fas fa-question me-1"></i>
                                                    Estado no definido
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <span class="text-truncate d-inline-block" 
                                              style="max-width: 200px;" 
                                              data-tooltip="${devolucion.razon}">
                                            ${devolucion.razon}
                                        </span>
                                    </td>
                                    <td>
                                        <fmt:formatDate value="${devolucion.fecha_devolucion}" 
                                                        pattern="d/MMMM/yyyy - HH:mm:ss"/>
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
        <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>

        <script>
                    // Inicialización de Select2
                    $(document).ready(function () {
                        $('.select2').select2({
                            placeholder: "Buscar...",
                            allowClear: true,
                            width: '100%',
                            language: {
                                noResults: function () {
                                    return "No se encontraron resultados";
                                }
                            }
                        });
                    });

                    // Cambio de tipo de devolución
                    function cambiarTipoDevolucion() {
                        const tipoSelect = document.getElementById('tipoSelect');
                        const ventasSection = document.getElementById('ventasSection');
                        const comprasSection = document.getElementById('comprasSection');

                        // Limpiar campos
                        document.getElementById('id_producto').value = '';
                        document.getElementById('id_lote').value = '';
                        document.getElementById('cantidad').value = '';
                        document.getElementById('cantidad').max = '';
                        document.getElementById('cantidad').placeholder = '';
                        document.getElementById('cantidadHelper').textContent = '';

                        // Resetear Select2
                        $('#ventaSelect').val(null).trigger('change');
                        $('#compraSelect').val(null).trigger('change');

                        if (tipoSelect.value === 'compra') {
                            ventasSection.style.display = 'none';
                            comprasSection.style.display = 'block';
                            document.getElementById('compraSelect').required = true;
                            document.getElementById('ventaSelect').required = false;
                        } else {
                            ventasSection.style.display = 'block';
                            comprasSection.style.display = 'none';
                            document.getElementById('compraSelect').required = false;
                            document.getElementById('ventaSelect').required = true;
                        }
                    }

                    // Cargar datos de venta
                    function cargarDatosVenta() {
                        const select = document.getElementById('ventaSelect');
                        const option = select.options[select.selectedIndex];

                        if (option.value) {
                            document.getElementById('id_producto').value = option.dataset.producto;
                            document.getElementById('id_lote').value = option.dataset.lote;
                            document.getElementById('cantidad').value = '';

                            const cantidadInput = document.getElementById('cantidad');
                            cantidadInput.max = option.dataset.cantidadDisponible;
                            cantidadInput.placeholder = `Máximo: ${option.dataset.cantidadDisponible}`;

                            document.getElementById('cantidadHelper').textContent =
                                    `Disponible para devolución: ${option.dataset.cantidadDisponible} de ${option.dataset.cantidadOriginal}`;
                        }
                    }

                    // Cargar datos de compra
                    function cargarDatosCompra() {
                        const select = document.getElementById('compraSelect');
                        const option = select.options[select.selectedIndex];

                        if (option.value) {
                            document.getElementById('id_producto').value = option.dataset.producto;
                            document.getElementById('id_lote').value = option.dataset.lote;

                            const cantidadInput = document.getElementById('cantidad');
                            cantidadInput.max = option.dataset.cantidadDisponible;
                            cantidadInput.placeholder = `Máximo: ${option.dataset.cantidadDisponible}`;

                            document.getElementById('cantidadHelper').textContent =
                                    `Disponible para devolución: ${option.dataset.cantidadDisponible} de ${option.dataset.cantidadOriginal}`;
                        }
                    }

                    // Confirmar reversión
                    function confirmarReversion() {
                        return confirm(
                                '¡ADVERTENCIA!\n\n' +
                                'Esta acción:\n' +
                                '1. Restaurará el inventario al estado antes de las devoluciones\n' +
                                '2. Eliminará todos los registros de devoluciones\n' +
                                '3. Eliminará los movimientos relacionados con devoluciones\n\n' +
                                'Las ventas y compras originales se mantendrán intactas.\n\n' +
                                '¿Está seguro de que desea continuar?'
                                );
                    }

                    // Validación del formulario
                    document.querySelector('form#devolucionForm').addEventListener('submit', function (e) {
                        const tipoOperacion = document.getElementById('tipoSelect').value;
                        const cantidad = parseInt(document.getElementById('cantidad').value);
                        const maxCantidad = parseInt(document.getElementById('cantidad').max);
                        
                        if (!document.getElementById('id_producto').value) {
                            e.preventDefault();
                            alert('Debe seleccionar una ' + (tipoOperacion === 'venta' ? 'venta' : 'compra'));
                            return;
                        }

                        if (!maxCantidad) {
                            e.preventDefault();
                            alert('Debe seleccionar un registro válido');
                            return;
                        }

                        if (cantidad <= 0) {
                            e.preventDefault();
                            alert('La cantidad debe ser mayor a 0');
                            return;
                        }

                        if (cantidad > maxCantidad) {
                            e.preventDefault();
                            alert(`La cantidad no puede ser mayor a ${maxCantidad}`);
                            return;
                        }

                        const razon = document.getElementById('razon').value.trim();
                        if (!razon) {
                            e.preventDefault();
                            alert('Debe especificar una razón para la devolución');
                            return;
                        }

                        if (!confirm(`¿Está seguro de registrar esta devolución de ${tipoOperacion}?`)) {
                            e.preventDefault();
                        }
                    });

                    // Animaciones para la tabla
                    document.addEventListener('DOMContentLoaded', function () {
                        document.querySelectorAll('tbody tr').forEach((tr, index) => {
                            tr.style.opacity = '0';
                            tr.style.animation = `fadeIn 0.3s ease-out ${index * 0.1}s forwards`;
                        });
                    });
        </script>
    </body>
</html>         
