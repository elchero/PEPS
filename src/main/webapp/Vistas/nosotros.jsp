<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Equipo DevConta | UNICAES</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
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
            background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
            color: white;
            padding: 3rem 0;
            margin-bottom: 3rem;
            position: relative;
            overflow: hidden;
        }

        .page-header::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><rect fill="rgba(255,255,255,0.1)" x="0" y="0" width="100" height="100"/></svg>') repeat;
            opacity: 0.1;
        }

        .university-logo {
            width: 180px;
            height: auto;
            margin-bottom: 2rem;
            transition: transform 0.3s ease;
        }

        .university-logo:hover {
            transform: scale(1.05);
        }

        .header-info {
            text-align: center;
            position: relative;
            z-index: 1;
        }

        .header-info h1 {
            font-size: 2.5rem;
            font-weight: 700;
            margin-bottom: 1rem;
        }

        .header-info h2 {
            font-size: 1.8rem;
            color: rgba(255, 255, 255, 0.9);
            margin-bottom: 0.8rem;
        }

        .header-info h3, .header-info h4, .header-info h5 {
            color: rgba(255, 255, 255, 0.8);
            margin-bottom: 0.5rem;
        }

        .team-section {
            padding: 2rem 0;
        }

        .team-card {
            background: white;
            border-radius: 15px;
            overflow: hidden;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            margin-bottom: 2rem;
        }

        .team-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 25px rgba(0,0,0,0.15);
        }

        .team-card-header {
            position: relative;
            padding-top: 100%;
            background: linear-gradient(45deg, var(--primary-color), var(--secondary-color));
        }

        .team-card-header img {
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            width: 150px;
            height: 150px;
            border-radius: 50%;
            border: 5px solid white;
            box-shadow: 0 5px 15px rgba(0,0,0,0.2);
            transition: transform 0.3s ease;
        }

        .team-card:hover .team-card-header img {
            transform: translate(-50%, -50%) scale(1.05);
        }

        .team-card-body {
            padding: 2rem;
            text-align: center;
        }

        .team-member-name {
            font-size: 1.4rem;
            color: var(--primary-color);
            margin-bottom: 0.5rem;
            font-weight: 600;
        }

        .team-member-role {
            color: var(--accent-color);
            font-size: 1rem;
            margin-bottom: 1rem;
        }

        .social-links {
            display: flex;
            justify-content: center;
            gap: 1rem;
            margin-top: 1rem;
        }

        .social-link {
            color: var(--primary-color);
            font-size: 1.2rem;
            transition: color 0.3s ease, transform 0.3s ease;
        }

        .social-link:hover {
            color: var(--accent-color);
            transform: translateY(-2px);
        }

        @media (max-width: 768px) {
            .page-header {
                padding: 2rem 0;
            }

            .header-info h1 {
                font-size: 2rem;
            }

            .header-info h2 {
                font-size: 1.5rem;
            }

            .university-logo {
                width: 150px;
            }
        }

        /* Animaciones */
        @keyframes fadeInUp {
            from {
                opacity: 0;
                transform: translateY(20px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .team-card {
            animation: fadeInUp 0.6s ease-out forwards;
            opacity: 0;
        }

        .team-card:nth-child(1) { animation-delay: 0.2s; }
        .team-card:nth-child(2) { animation-delay: 0.4s; }
        .team-card:nth-child(3) { animation-delay: 0.6s; }
    </style>
</head>
<body>
    <jsp:include page="/nav/navbar.jsp"></jsp:include>
    
    <!-- Header Section -->
    <header class="page-header">
        <div class="container">
            <div class="header-info">
                <img src="https://firebasestorage.googleapis.com/v0/b/chatgram-4c6da.appspot.com/o/UNICAES_Logo.png?alt=media&token=5add912a-b762-4179-b127-a5673a45a4b7" 
                     alt="Logo Universidad" 
                     class="university-logo">
                <h1>Universidad Católica de El Salvador</h1>
                <h2>Facultad de Ingeniería y Arquitectura</h2>
                <h3>Ingeniería en Desarrollo de Software</h3>
                <h4>Aplicación de Técnicas Contables - Sección "A"</h4>
                <h5>Docente: Ma. Patricia Carolina Rivas de Molina</h5>
            </div>
        </div>
    </header>

    <!-- Team Section -->
    <section class="team-section">
        <div class="container">
            <div class="row">
                <!-- Team Member 1 -->
                <div class="col-md-4">
                    <div class="team-card">
                        <div class="team-card-header">
                            <img src="https://firebasestorage.googleapis.com/v0/b/chatgram-4c6da.appspot.com/o/David.png?alt=media&token=e3189db8-8a75-4af9-b354-2b1c64bfc385" 
                                 alt="David Oswaldo Magaña Pacheco">
                        </div>
                        <div class="team-card-body">
                            <h3 class="team-member-name">David Oswaldo Magaña Pacheco</h3>
                            <p class="team-member-role">Desarrollador Frontend </p>
                            <div class="social-links">
                                <a href="#" class="social-link"><i class="fab fa-github"></i></a>
                                <a href="#" class="social-link"><i class="fab fa-linkedin"></i></a>
                                <a href="#" class="social-link"><i class="fas fa-envelope"></i></a>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Team Member 2 -->
                <div class="col-md-4">
                    <div class="team-card">
                        <div class="team-card-header">
                            <img src="https://firebasestorage.googleapis.com/v0/b/chatgram-4c6da.appspot.com/o/Barrientos.png?alt=media&token=7b72d039-cc8a-42f1-9f39-e4bebff9a907" 
                                 alt="Jonathan Alexis Barrientos Barrientos">
                        </div>
                        <div class="team-card-body">
                            <h3 class="team-member-name">Jonathan Alexis Barrientos</h3>
                            <p class="team-member-role">Desarrollador Backend</p>
                            <div class="social-links">
                                <a href="#" class="social-link"><i class="fab fa-github"></i></a>
                                <a href="#" class="social-link"><i class="fab fa-linkedin"></i></a>
                                <a href="#" class="social-link"><i class="fas fa-envelope"></i></a>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Team Member 3 -->
                <div class="col-md-4">
                    <div class="team-card">
                        <div class="team-card-header">
                            <img src="https://firebasestorage.googleapis.com/v0/b/chatgram-4c6da.appspot.com/o/Alex.png?alt=media&token=40461dc1-1ec2-4143-b266-c55002ab69f3" 
                                 alt="Alexander Vladimir Carrillos">
                        </div>
                        <div class="team-card-body">
                            <h3 class="team-member-name">Alexander Vladimir Carrillos</h3>
                            <p class="team-member-role">Desarrollador Full Stack</p>
                            <div class="social-links">
                                <a href="#" class="social-link"><i class="fab fa-github"></i></a>
                                <a href="#" class="social-link"><i class="fab fa-linkedin"></i></a>
                                <a href="#" class="social-link"><i class="fas fa-envelope"></i></a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>