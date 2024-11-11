<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Información del Grupo</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            .page-content {
                background-color: #f8f9fa;
                padding: 30px;
                border-radius: 15px;
            }
            .university-logo {
                display: block;
                margin: 0 auto;
                width: 200px;
            }
            .header-info {
                text-align: center;
                margin-bottom: 50px;
            }
            .card {
                margin-bottom: 20px;
            }
            .card-body {
                display: flex;
                align-items: center;
            }
            .card img {
                width: 100px;
                height: 100px;
                border-radius: 50%;
                margin-right: 20px;
            }
        </style>
    </head>
    <body>
        <jsp:include page="/nav/navbar.jsp"></jsp:include>
        <div class="container mt-5">
            <div class="page-content shadow-sm">
                <!-- Logotipo de la Universidad -->
                <div class="header-info">
                    <img src="https://firebasestorage.googleapis.com/v0/b/chatgram-4c6da.appspot.com/o/UNICAES_Logo.png?alt=media&token=5add912a-b762-4179-b127-a5673a45a4b7" alt="Logo Universidad" class="university-logo">
                    <h1 class="mt-4">Universidad Católica de El Salvador</h1>
                    <h2>Facultad de Ingeniería y Arquitectura</h2>
                    <h3>Carrera: Ingeniería en Desarrollo de Software</h3>
                    <h4>Materia: Aplicación de Técnicas Contables - Sección "A"</h4>
                    <h5>Catedrática: Ma. Patricia Carolina Rivas de Molina</h5>
                </div>
                <hr>
                <br>
                <!-- Información de los Integrantes -->
                <div class="row">
                    <!-- Integrante 1 -->
                    <div class="col-md-4">
                        <div class="card">
                            <div class="card-body">
                                <img src="https://firebasestorage.googleapis.com/v0/b/chatgram-4c6da.appspot.com/o/David.png?alt=media&token=e3189db8-8a75-4af9-b354-2b1c64bfc385" alt="David Oswaldo Magaña Pacheco">
                                <h5 class="card-title">David Oswaldo Magaña Pacheco</h5>
                            </div>
                        </div>
                    </div>
                    <!-- Integrante 2 -->
                    <div class="col-md-4">
                        <div class="card">
                            <div class="card-body">
                                <img src="https://firebasestorage.googleapis.com/v0/b/chatgram-4c6da.appspot.com/o/Barrientos.png?alt=media&token=7b72d039-cc8a-42f1-9f39-e4bebff9a907" alt="Jonathan Alexis Barrientos Barrientos">
                                <h5 class="card-title">Jonathan Alexis Barrientos Barrientos</h5>
                            </div>
                        </div>
                    </div>
                    <!-- Integrante 3 -->
                    <div class="col-md-4">
                        <div class="card">
                            <div class="card-body">
                                <img src="https://firebasestorage.googleapis.com/v0/b/chatgram-4c6da.appspot.com/o/Alex.png?alt=media&token=40461dc1-1ec2-4143-b266-c55002ab69f3" alt="Alexander Vladimir Carrillos">
                                <h5 class="card-title">Alexander Vladimir Carrillos Gaitán</h5>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
