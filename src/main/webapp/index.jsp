<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Inicio</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body {
                background-color: #f8f9fa;
                display: flex;
                flex-direction: column;
                justify-content: center;
                align-items: center;
                margin: 0;
            }
            .logo {
                max-width: 250px;
                margin-bottom: 30px;
                margin-top: 15px;
            }
            .options-container {
                display: flex;
                justify-content: center;
                gap: 20px;
            }
            .option {
                text-align: center;
                background-color: white;
                padding: 20px;
                border-radius: 10px;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                transition: transform 0.3s, box-shadow 0.3s;
            }
            .option:hover {
                transform: translateY(-5px);
                box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
            }
            .option img {
                max-width: 100px;
                margin-bottom: 15px;
            }
            .option h3 {
                font-size: 1.25rem;
                color: #343a40;
            }
            .option a {
                text-decoration: none;
                color: inherit;
            }
            .option a:focus {
                outline: none; /* Elimina la línea azul */
            }

            /* Estilos responsivos */
            @media (max-width: 768px) {
                .logo {
                    max-width: 150px;
                }
                .options-container {
                    flex-direction: column;
                }
                .option {
                    margin-bottom: 20px;
                }
                h1 {
                    font-size: 1.5rem;
                }
            }

            @media (min-width: 769px) and (max-width: 1024px) {
                .options-container {
                    flex-direction: row;
                    flex-wrap: wrap;
                }
                .option {
                    width: 50%;
                }
            }

            @media (min-width: 1025px) {
                .options-container {
                    flex-direction: row;
                }
                .option {
                    width: 33.33%;
                }
            }
        </style>
    </head>
    <body>    
        <jsp:include page="/nav/navbar.jsp"></jsp:include>
            <div class="text-center">
                <!-- Logo centrado -->
                <img src="https://firebasestorage.googleapis.com/v0/b/chatgram-4c6da.appspot.com/o/LogotransIndex.png?alt=media&token=b9c0500b-d054-4399-83b0-24080d296073" alt="Logo DevConta" class="logo">

                <!-- Mensaje de bienvenida -->
                <h1 class="mb-5">Bienvenido a DevConta</h1>

                <!-- Opciones en horizontal -->
                <div class="options-container">
                    <div class="option">
                        <a href="${pageContext.request.contextPath}/ControladorActivo">
                        <img src="https://firebasestorage.googleapis.com/v0/b/chatgram-4c6da.appspot.com/o/depreciacion.png?alt=media&token=e95bf579-2331-44d3-be47-af4ad101c581" alt="Depreciación">
                        <h3>Depreciación</h3>
                    </a>
                </div>
                <div class="option">
                    <a href="#" onclick="showAmortizationMessage(); return false;">
                        <img src="https://firebasestorage.googleapis.com/v0/b/chatgram-4c6da.appspot.com/o/amortizacion.png?alt=media&token=f2e96077-1446-471c-a008-87f98a671fc5" alt="Amortización">
                        <h3>Amortización</h3>
                    </a>
                </div>
                <div class="option">
                    <a href="${pageContext.request.contextPath}/Vistas/asientoForm.jsp">
                        <img src="https://firebasestorage.googleapis.com/v0/b/chatgram-4c6da.appspot.com/o/partida_doble.png?alt=media&token=12f39c70-23bb-4f10-9603-3ef52410f266" alt="Partida Doble">
                        <h3>Partida Doble</h3>
                    </a>
                </div>
            </div>
        </div>
        <hr>

        <script>
            function showAmortizationMessage() {
                alert("El cálculo de amortización no se ha realizado porque la catedrática indicó que ya no es necesario. ¡Gracias por su comprensión!");
            }
        </script>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
