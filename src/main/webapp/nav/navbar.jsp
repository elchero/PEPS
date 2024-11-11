<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Nav</title>
       
        <style>
            /* Custom CSS for Navbar */
            .navbar-custom {
                background-color: #000; /* Black background */
                color: #fff; /* White text */
                width: 100%; /* Ensure navbar spans the full width */
            }
            .navbar-custom .navbar-brand {
                font-weight: bold;
                color: #fff; /* White text */
            }
            .navbar-custom .nav-link {
                color: #fff; /* White text */
            }
            .navbar-custom .nav-link:hover {
                color: #007bff; /* Blue on hover */
            }
            .navbar-custom .navbar-toggler {
                border-color: rgba(255, 255, 255, 0.1); /* Light border for toggler */
            }
            .navbar-custom .navbar-toggler-icon {
                background-image: url("data:image/svg+xml;charset=utf8,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 30 30'%3E%3Cpath stroke='rgba%28255, 255, 255, 0.5%29' stroke-width='2' linecap='round' linejoin='round' d='M4 7h22M4 15h22M4 23h22'/%3E%3C/svg%3E");
            }
            .navbar-custom .navbar-brand img {
                max-height: 50px;
                width: auto; /* Mantén la proporción de la imagen */
                margin-right: 15px; /* Añade un margen a la derecha de la imagen */
            }
        </style>
    </head>
    <body>
        <!-- Navbar -->
        <nav class="navbar navbar-expand-lg navbar-custom">
            <div class="container-fluid">
                <a class="navbar-brand" href="${pageContext.request.contextPath}/ControladorEmpresa">
                    <img src="https://firebasestorage.googleapis.com/v0/b/chatgram-4c6da.appspot.com/o/logotrans3.png?alt=media&token=da1e3e81-28a6-4c90-9224-ffa062775208" alt="Logo Empresa" class="img-fluid">
                    DevConta
                </a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/index.jsp">Inicio</a>
                        </li>
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                Depreciación
                            </a>
                            <ul class="dropdown-menu">
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/ControladorActivoCategoria">Activo Categoria</a></li>
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/ControladorActivoFamilia">Familia Activo</a></li>
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/ControladorActivo">Activo</a></li>
                            </ul>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/Vistas/asientoForm.jsp">Partida Doble</a>
                        </li>
                    </ul>
                    <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/Vistas/preguntas.jsp">Preguntas Frecuentes</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/Vistas/nosotros.jsp">Integrantes</a>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
      
    </body>
</html>
