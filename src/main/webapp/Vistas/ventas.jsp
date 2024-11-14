<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Registro de Ventas | DevConta</title>
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

            .info-box {
                background: linear-gradient(135deg, #26c6da, #00acc1);
                color: white;
                padding: 1rem;
                border-radius: 10px;
                margin-bottom: 2rem;
                box-shadow: 0 4px 15px rgba(0,0,0,0.05);
            }

            .info-box i {
                font-size: 1.5rem;
                margin-right: 0.5rem;
            }

            .info-text {
                font-size: 0.9rem;
                margin: 0;
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
                padding: 1rem;
                margin: 1rem 0;
                border: none;
            }

            .alert-success {
                background: rgba(46, 204, 113, 0.1);
                color: #27ae60;
            }

            .alert-danger {
                background: rgba(231, 76, 60, 0.1);
                color: #c0392b;
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
        </style>
    </head>
    <body>
        <jsp:include page="/nav/navbar.jsp"></jsp:include>

            <!-- Header -->
            <div class="page-header">
                <div class="container">
                    <h2 class="text-center m-0">
                        <i class="fas fa-cash-register me-2"></i>
                        Registro de Ventas
                    </h2>
                </div>
            </div>

            <div class="container">
                <!-- Info Box -->
                <div class="info-box">
                    <div class="d-flex align-items-center">
                        <i class="fas fa-info-circle"></i>
                        <p class="info-text">
                            Registre las ventas de productos. El sistema automáticamente actualizará el inventario 
                            siguiendo el método PEPS (Primero en Entrar, Primero en Salir).
                        </p>
                    </div>
                </div>

                <!-- Mensajes -->
            <c:if test="${not empty mensaje}">
                <div class="alert alert-${tipoMensaje}" role="alert">
                    <i class="fas fa-${tipoMensaje == 'success' ? 'check-circle' : 'exclamation-circle'} me-2"></i>
                    ${mensaje}
                </div>
            </c:if>

            <!-- Formulario -->
            <div class="form-container">
                <form action="VentasServlet" method="post">
                    <input type="hidden" name="action" value="create">

                    <div class="row mb-4">
                        <div class="col-12">
                            <label class="form-label">
                                <i class="fas fa-box me-2"></i>Seleccionar Producto
                            </label>
                            <select name="id_producto" id="producto" class="form-select" required>
                                <c:forEach var="producto" items="${listaProductos}">
                                    <option value="${producto.id_producto}">${producto.nombre}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="row mb-4">
                        <div class="col-md-6">
                            <label class="form-label">
                                <i class="fas fa-sort-amount-up me-2"></i>Cantidad
                            </label>
                            <input type="number" name="cantidad" id="cantidad" min="1" 
                                   class="form-control" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">
                                <i class="fas fa-tag me-2"></i>Precio de Venta Unitario
                            </label>
                            <input type="number" step="0.01" name="precio_venta_unitario" 
                                   id="precio_venta_unitario" class="form-control" required>
                        </div>
                    </div>

                    <div class="text-end">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save me-2"></i>Registrar Venta
                        </button>
                    </div>
                </form>
            </div>

            <!-- Tabla -->
            <div class="table-container">
                <h3 class="mb-4">
                    <i class="fas fa-list me-2"></i>
                    Historial de Ventas
                </h3>
                <div class="table-responsive">
                    <table class="table">
                        <thead>
                            <tr>
                                <th><i class="fas fa-hashtag me-2"></i>ID</th>
                                <th><i class="fas fa-box me-2"></i>Producto</th>
                                <th><i class="fas fa-barcode me-2"></i>Lote</th>
                                <th><i class="fas fa-sort-amount-up me-2"></i>Cantidad</th>
                                <th><i class="fas fa-tag me-2"></i>Precio Unit.</th>
                                <th><i class="fas fa-dollar-sign me-2"></i>Total</th>
                                <th><i class="fas fa-calendar me-2"></i>Fecha</th>
                                <th><i class="fas fa-cog me-2"></i>Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="venta" items="${listaVentas}">
                                <tr>
                                    <td>${venta.id_venta}</td>
                                    <td>${venta.nombre}</td>
                                    <td>${venta.id_lote}</td>
                                    <td>${venta.cantidad}</td>
                                    <td>
                                        <fmt:formatNumber value="${venta.precio_venta_unitario}" 
                                                          type="currency" currencySymbol="$" 
                                                          minFractionDigits="2" maxFractionDigits="2"/>
                                    </td>
                                    <td>
                                        <fmt:formatNumber value="${venta.cantidad * venta.precio_venta_unitario}" 
                                                          type="currency" currencySymbol="$" 
                                                          minFractionDigits="2" maxFractionDigits="2"/>
                                    </td>
                                    <td>
                                        <fmt:formatDate value="${venta.fecha_venta}" 
                                                        pattern="d/MMMM/yyyy - HH:mm:ss"/>
                                    </td>
                                    <td>
                                        <form method="post" action="VentasServlet" 
                                              style="display:inline;" 
                                              onsubmit="return confirmarEliminacion()">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="id_venta" value="${venta.id_venta}">
                                            <button type="submit" class="btn btn-danger btn-sm">
                                                <i class="fas fa-trash-alt"></i>
                                            </button>
                                        </form>
                                        <a href="VentasServlet?action=generarBoleta&id=${venta.id_venta}" 
                                           class="btn btn-success btn-sm ms-1"
                                           data-tooltip="Generar Boleta">
                                            <i class="fas fa-file-pdf"></i>
                                        </a>
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
                                                  // Validación del formulario
                                                  document.querySelector('form[action="VentasServlet"]').addEventListener('submit', function (e) {
                                                      if (this.querySelector('input[name="action"]').value === 'create') {
                                                          const cantidad = parseInt(document.getElementById('cantidad').value);
                                                          const precio = parseFloat(document.getElementById('precio_venta_unitario').value);

                                                          if (cantidad <= 0) {
                                                              e.preventDefault();
                                                              alert('La cantidad debe ser mayor a 0');
                                                              return;
                                                          }

                                                          if (precio <= 0) {
                                                              e.preventDefault();
                                                              alert('El precio debe ser mayor a 0');
                                                              return;
                                                          }
                                                      }
                                                  });

                                                  function confirmarEliminacion() {
                                                      return confirm('¿Está seguro que desea eliminar esta venta? Se restaurará el inventario.');
                                                  }

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