document.addEventListener("DOMContentLoaded", function () {
    const navbarHTML = `
    <nav class="navbar navbar-expand-lg navbar-custom">
        <div class="container-fluid">
            <a class="navbar-brand" href="/pacientes/pacientepanel">
                <i class="bi bi-hospital me-2"></i>Clinica Lolimsa
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainNavbar"
                    aria-controls="mainNavbar" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"><i class="bi bi-list text-white fs-2"></i></span>
            </button>
            <div class="collapse navbar-collapse" id="mainNavbar">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link" href="/pacientes/pacientepanel">
                            <i class="bi bi-speedometer2"></i> Inicio
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/empleados/vistaproductos">
                            <i class="bi bi-person-vcard"></i> Gestión de Pacientes
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/empleados/vistaproductos">
                            <i class="bi bi-calendar2-check"></i> Gestión de Citas
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/empleados/vistaproveedores">
                            <i class="bi bi-capsule"></i> Gestión de Medicamentos
                        </a>
                    </li>
                </ul>
                <a href="/" class="btn btn-danger logout-link">
                    <i class="bi bi-power me-2"></i> Cerrar Sesión
                </a>
            </div>
        </div>
    </nav>
    `;
    document.getElementById("navbar-container").innerHTML = navbarHTML;
});