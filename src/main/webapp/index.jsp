<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>DevConta - Sistema de Gestión</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        <style>
            :root {
                --primary-color: #2c3e50;
                --secondary-color: #34495e;
                --accent-color: #3498db;
                --hover-color: #2980b9;
            }

            body {
                background-color: #f8f9fa;
                min-height: 100vh;
                padding-bottom: 2rem;
            }

            .main-container {
                max-width: 1200px;
                margin: 0 auto;
                padding: 2rem 1rem;
            }

            .logo {
                max-width: 200px;
                height: auto;
                margin: 1.5rem auto;
                display: block;
                transition: transform 0.3s ease;
            }

            .logo:hover {
                transform: scale(1.05);
            }

            .welcome-section {
                text-align: center;
                margin-bottom: 3rem;
                padding: 2rem 0;
            }

            .welcome-section h1 {
                color: var(--primary-color);
                font-size: 2.5rem;
                font-weight: 600;
                margin-bottom: 1rem;
            }

            .options-grid {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
                gap: 1.5rem;
                padding: 0 1rem;
            }

            .option-card {
                background: white;
                border-radius: 12px;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                padding: 1.5rem;
                text-align: center;
                transition: all 0.3s ease;
                border: 1px solid rgba(0, 0, 0, 0.05);
            }

            .option-card:hover {
                transform: translateY(-5px);
                box-shadow: 0 8px 15px rgba(0, 0, 0, 0.1);
                border-color: var(--accent-color);
            }

            .option-card img {
                width: 80px;
                height: 80px;
                object-fit: contain;
                margin-bottom: 1rem;
                transition: transform 0.3s ease;
            }

            .option-card:hover img {
                transform: scale(1.1);
            }

            .option-card h3 {
                color: var(--primary-color);
                font-size: 1.2rem;
                font-weight: 600;
                margin-bottom: 0.5rem;
            }

            .option-card a {
                text-decoration: none;
                color: inherit;
                display: block;
                height: 100%;
            }

            .option-card:hover h3 {
                color: var(--accent-color);
            }

            .divider {
                height: 1px;
                background: linear-gradient(to right, transparent, var(--accent-color), transparent);
                margin: 3rem 0;
            }

            @media (max-width: 768px) {
                .welcome-section h1 {
                    font-size: 2rem;
                }

                .options-grid {
                    grid-template-columns: 1fr;
                    gap: 1rem;
                }

                .option-card {
                    padding: 1rem;
                }
            }

            @media (min-width: 769px) and (max-width: 1024px) {
                .options-grid {
                    grid-template-columns: repeat(2, 1fr);
                }
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

            .option-card {
                animation: fadeIn 0.5s ease-out forwards;
            }

            .option-card:nth-child(n) {
                animation-delay: calc(n * 0.1s);
            }
        </style>
    </head>
    <body>    
        <jsp:include page="/nav/navbar.jsp"></jsp:include>

            <div class="main-container">
                <div class="welcome-section">
                    <img src="https://firebasestorage.googleapis.com/v0/b/chatgram-4c6da.appspot.com/o/LogotransIndex.png?alt=media&token=b9c0500b-d054-4399-83b0-24080d296073" 
                         alt="Logo DevConta" 
                         class="logo">
                    <h1>Bienvenido a DevConta</h1>
                    <p class="text-muted">Sistema de Gestión de Inventario y Contabilidad</p>
                </div>

                <div class="options-grid">
                    <div class="option-card">
                        <a href="${pageContext.request.contextPath}/ProductosServlet">
                        <img src="https://firebasestorage.googleapis.com/v0/b/chatgram-4c6da.appspot.com/o/zapatos.png?alt=media&token=9b4801c3-f1a6-4323-a9a8-63182aafc922" alt="Ver Productos">
                        <h3>Ver Productos</h3>
                        <p class="text-muted">Gestiona tu catálogo de productos</p>
                    </a>
                </div>

                <div class="option-card">
                    <a href="${pageContext.request.contextPath}/InventarioInicialServlet">
                        <img src="https://firebasestorage.googleapis.com/v0/b/chatgram-4c6da.appspot.com/o/agregarInventario.png?alt=media&token=0cb5c7e7-9335-49c3-a192-70b5f4f1d615" alt="Inventario Inicial">
                        <h3>Inventario Inicial</h3>
                        <p class="text-muted">Configura tu inventario base</p>
                    </a>
                </div>

                <div class="option-card">
                    <a href="${pageContext.request.contextPath}/inventarioInfoServlet">
                        <img src="https://firebasestorage.googleapis.com/v0/b/chatgram-4c6da.appspot.com/o/inventario.png?alt=media&token=cfb0c3c7-14bd-4342-8a06-6499f55d9a8f" alt="Ver Inventario">
                        <h3>Ver Inventario</h3>
                        <p class="text-muted">Consulta el estado actual del inventario</p>
                    </a>
                </div>

                <div class="option-card">
                    <a href="${pageContext.request.contextPath}/ComprasServlet">
                        <img src="https://firebasestorage.googleapis.com/v0/b/chatgram-4c6da.appspot.com/o/compra.png?alt=media&token=115d2ed3-53bf-4e2d-947a-a027f73f7d3b" alt="Compras">
                        <h3>Compras</h3>
                        <p class="text-muted">Registra nuevas adquisiciones</p>
                    </a>
                </div>

                <div class="option-card">
                    <a href="${pageContext.request.contextPath}/VentasServlet">
                        <img src="https://firebasestorage.googleapis.com/v0/b/chatgram-4c6da.appspot.com/o/Venta.png?alt=media&token=d50a08da-b35b-42cf-863e-5807d66a045b" alt="Ventas">
                        <h3>Ventas</h3>
                        <p class="text-muted">Gestiona tus transacciones de venta</p>
                    </a>
                </div>

                <div class="option-card">
                    <a href="${pageContext.request.contextPath}/DevolucionesServlet">
                        <img src="https://firebasestorage.googleapis.com/v0/b/chatgram-4c6da.appspot.com/o/devoluciones.png?alt=media&token=5cb3ecc9-4c6f-4db3-81d7-dc45c0f0f88c" alt="Devoluciones">
                        <h3>Devoluciones</h3>
                        <p class="text-muted">Procesa devoluciones de productos</p>
                    </a>
                </div>

                <div class="option-card">
                    <a href="${pageContext.request.contextPath}/HistorialServlet">
                        <img src="https://firebasestorage.googleapis.com/v0/b/chatgram-4c6da.appspot.com/o/historialPeps.png?alt=media&token=7eae2633-8247-4754-aa3b-4230746d840e" alt="Historial">
                        <h3>Historial de Movimientos</h3>
                        <p class="text-muted">Consulta el registro de operaciones</p>
                    </a>
                </div>
                <div class="option-card">
                    <a href="${pageContext.request.contextPath}/DashboardController">
                        <img src="https://firebasestorage.googleapis.com/v0/b/chatgram-4c6da.appspot.com/o/resumen.png?alt=media&token=f4227c9a-6860-4d8f-aefa-a95f27268abd" alt="Historial">
                        <h3>Resumen de inventario</h3>
                        <p class="text-muted">Consulta el resumen</p>
                    </a>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>