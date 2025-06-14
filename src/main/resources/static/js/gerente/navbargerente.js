// static/js/administrador/navbaradmin.js
document.addEventListener("DOMContentLoaded", function () {
  const navbarHTML = `
  <nav class="navbar navbar-expand-lg navbar-custom">
    <div class="container-fluid">
      <a class="navbar-brand" href="#">Clinica Lolimsa</a>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainNavbar"
              aria-controls="mainNavbar" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"><i class="bi bi-list text-white fs-2"></i></span>
      </button>
      <div class="collapse navbar-collapse" id="mainNavbar">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item"><a class="nav-link" href="/gerente/gerentepanel"><i class="bi bi-bar-chart"></i> Panel</a></li>
          <li class="nav-item"><a class="nav-link" href="#"><i class="bi bi-calendar-check"></i> Gestión de Citas</a></li>
          <li class="nav-item"><a class="nav-link" href="/gerente/gerentemedicos"><i class="bi bi-person-badge"></i> Gestión de Medicos</a></li>
          <li class="nav-item"><a class="nav-link" href="/gerente/gerentemedicamentos"><i class="bi bi-bag-dash-fill"></i> Gestión de Medicamentos</a></li>
          <li class="nav-item"><a class="nav-link" href="/gerente/gerenteproveedores"><i class="bi bi-people"></i> Gestión de Proveedores</a></li>
          <li class="nav-item"><a class="nav-link" href="/gerente/gerentepaciente"><i class="bi bi-people"></i> Gestión de Pacientes</a></li>
        </ul>
        <a href="/" class="btn btn-danger logout-link"><i class="bi bi-box-arrow-right me-2"></i> Cerrar Sesión</a>
      </div>
    </div>
  </nav>
  `;
  document.getElementById("navbar-container").innerHTML = navbarHTML;
});
