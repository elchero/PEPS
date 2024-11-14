<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Nav</title>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        <style>
            .navbar-custom {
                background: linear-gradient(to right, #1a1a1a, #2c3e50);
                padding: 0.8rem 1rem;
                box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            }

            .navbar-custom .navbar-brand {
                font-weight: 600;
                color: #fff;
                display: flex;
                align-items: center;
                font-size: 1.4rem;
                transition: all 0.3s ease;
            }

            .navbar-custom .navbar-brand:hover {
                color: #3498db;
                transform: translateY(-1px);
            }

            .navbar-custom .navbar-brand img {
                height: 40px;
                width: auto;
                margin-right: 12px;
                transition: transform 0.3s ease;
            }

            .navbar-custom .navbar-brand:hover img {
                transform: scale(1.05);
            }

            .navbar-custom .nav-link {
                color: rgba(255, 255, 255, 0.9) !important;
                padding: 0.8rem 1.2rem;
                font-weight: 500;
                position: relative;
                transition: all 0.3s ease;
            }

            .navbar-custom .nav-link:before {
                content: '';
                position: absolute;
                bottom: 0;
                left: 50%;
                width: 0;
                height: 2px;
                background: #3498db;
                transform: translateX(-50%);
                transition: width 0.3s ease;
            }

            .navbar-custom .nav-link:hover {
                color: #3498db !important;
            }

            .navbar-custom .nav-link:hover:before {
                width: 80%;
            }

            .navbar-custom .dropdown-menu {
                background: #fff;
                border: none;
                border-radius: 8px;
                box-shadow: 0 4px 15px rgba(0,0,0,0.1);
                padding: 0.5rem;
                margin-top: 0.5rem;
                animation: dropdownFade 0.3s ease;
            }

            .navbar-custom .dropdown-item {
                color: #2c3e50;
                padding: 0.7rem 1.2rem;
                border-radius: 6px;
                transition: all 0.2s ease;
            }

            .navbar-custom .dropdown-item:hover {
                background: #f8f9fa;
                color: #3498db;
                transform: translateX(5px);
            }

            .navbar-custom .navbar-toggler {
                border: none;
                padding: 0.5rem;
                transition: transform 0.2s ease;
            }

            .navbar-custom .navbar-toggler:focus {
                box-shadow: none;
            }

            .navbar-custom .navbar-toggler:hover {
                transform: scale(1.1);
            }

            .navbar-custom .navbar-toggler-icon {
                background-image: url("data:image/svg+xml;charset=utf8,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 30 30'%3E%3Cpath stroke='rgba%28255, 255, 255, 0.9%29' stroke-width='2' linecap='round' linejoin='round' d='M4 7h22M4 15h22M4 23h22'/%3E%3C/svg%3E");
            }

            /* Animaciones */
            @keyframes dropdownFade {
                from {
                    opacity: 0;
                    transform: translateY(-10px);
                }
                to {
                    opacity: 1;
                    transform: translateY(0);
                }
            }

            /* Estilos responsivos */
            @media (max-width: 991.98px) {
                .navbar-custom .navbar-collapse {
                    background: rgba(26, 26, 26, 0.95);
                    padding: 1rem;
                    border-radius: 8px;
                    margin-top: 0.5rem;
                }

                .navbar-custom .nav-link:before {
                    display: none;
                }

                .navbar-custom .dropdown-menu {
                    background: rgba(255, 255, 255, 0.05);
                }

                .navbar-custom .dropdown-item {
                    color: rgba(255, 255, 255, 0.9);
                }

                .navbar-custom .dropdown-item:hover {
                    background: rgba(255, 255, 255, 0.1);
                    color: #3498db;
                }
            }
        </style>
    </head>
    <body>
        <nav class="navbar navbar-expand-lg navbar-custom">
            <div class="container-fluid">
                <a class="navbar-brand" href="">
                    <img src="https://firebasestorage.googleapis.com/v0/b/chatgram-4c6da.appspot.com/o/logotrans3.png?alt=media&token=da1e3e81-28a6-4c90-9224-ffa062775208" 
                         alt="Logo DevConta" 
                         class="img-fluid">
                    DevConta
                </a>

                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" 
                        data-bs-target="#navbarSupportedContent" 
                        aria-controls="navbarSupportedContent" 
                        aria-expanded="false" 
                        aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>

                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/index.jsp">
                                <i class="fas fa-home me-1"></i> Inicio
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/ProductosServlet">
                                <i class="fas fa-box me-1"></i> Producto
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/inventarioInfoServlet">
                                <i class="fas fa-warehouse me-1"></i> Ver inventario
                            </a>
                        </li>
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" 
                               href="#" 
                               id="pepsDropdown" 
                               role="button" 
                               data-bs-toggle="dropdown" 
                               aria-expanded="false"
                               aria-haspopup="true">
                                <i class="fas fa-layer-group me-1"></i> PEPS
                            </a>
                            <ul class="dropdown-menu" aria-labelledby="pepsDropdown">
                                <li>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/InventarioInicialServlet">
                                        <i class="fas fa-clipboard-list me-2"></i> Inventario Inicial
                                    </a>
                                </li>
                                <li>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/ComprasServlet">
                                        <i class="fas fa-shopping-cart me-2"></i> Compra
                                    </a>
                                </li>
                                <li>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/VentasServlet">
                                        <i class="fas fa-cash-register me-2"></i> Venta
                                    </a>
                                </li>
                                <li>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/DevolucionesServlet">
                                        <i class="fas fa-undo-alt me-2"></i> Devoluciones
                                    </a>
                                </li>
                            </ul>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/HistorialServlet">
                                <i class="fas fa-history me-1"></i> Historial de movimientos
                            </a>
                        </li>
                    </ul>

                    <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/DashboardController">
                                <i class="fas fa-chart-pie me-1"></i> Resumen de Inventario
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/Vistas/nosotros.jsp">
                                <i class="fas fa-users me-1"></i> Integrantes
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
        <script>
            document.addEventListener('DOMContentLoaded', function () {
                var dropdownElementList = [].slice.call(document.querySelectorAll('.dropdown-toggle'));
                var dropdownList = dropdownElementList.map(function (dropdownToggleEl) {
                    return new bootstrap.Dropdown(dropdownToggleEl);
                });
            });
        </script>
    </body>
</html>