const datos = {
        labels: ['Presión Sistólica', 'Presión Diastólica'],
        datasets: [
            {
                label: 'Saludable',
                data: [sistolicaPromedio, diastolicaPromedio],
                backgroundColor: 'rgba(75, 192, 192, 0.5)',
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 1,
                borderRadius: 5
            },
            {
                label: 'Paciente',
                data: [sistolicaPaciente, diastolicaPaciente],
                backgroundColor: 'rgba(255, 99, 132, 0.5)',
                borderColor: 'rgba(255, 99, 132, 1)',
                borderWidth: 1,
                borderRadius: 5
            }
        ]
    };

    const config = {
        type: 'bar',
        data: datos,
        options: {
            responsive: true,
            animation: {
                duration: 1000,
                easing: 'easeOutBounce'
            },
            plugins: {
                legend: {
                    position: 'top'
                },
                tooltip: {
                    backgroundColor: '#f0f0f0',
                    titleColor: '#000',
                    bodyColor: '#000'
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: 'Presión (mmHg)',
                        font: {
                            weight: 'bold'
                        }
                    },
                    grid: {
                        color: '#ddd'
                    }
                }
            }
        }
    };

    new Chart(document.getElementById('graficoPresion'), config);

    document.addEventListener("DOMContentLoaded", function () {
            let diagnostico = "";
            if (sistolicaPaciente >= 140 || diastolicaPaciente >= 90) {
                diagnostico = "Presión arterial alta (hipertensión)";
            } else if (sistolicaPaciente < 90 || diastolicaPaciente < 60) {
                diagnostico = "Presión arterial baja (hipotensión)";
            } else {
                diagnostico = "Presión arterial normal";
            }

            const resultadoEl = document.getElementById("resultadoDiagnostico");
            resultadoEl.textContent = diagnostico;
            resultadoEl.classList.add("animate__animated", "animate__fadeIn");
    });