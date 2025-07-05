const datos = {
    labels: ['Hemoglobina (g/dL)', 'Glucosa (mg/dL)', 'Colesterol (mg/dL)'],
    datasets: [
        {
            label: 'Valores Saludables',
            data: [hemoglobinaPromedio, glucosaPromedio, colesterolPromedio],
            backgroundColor: 'rgba(75, 192, 192, 0.5)',
            borderColor: 'rgba(75, 192, 192, 1)',
            borderWidth: 1,
            borderRadius: 5
        },
        {
            label: 'Paciente',
            data: [hemoglobinaPaciente, glucosaPaciente, colesterolPaciente],
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
                    text: 'Nivel',
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

new Chart(document.getElementById('graficoSangre'), config);

// Diagnóstico automático
document.addEventListener("DOMContentLoaded", function () {
    let diagnostico = "";

    if (hemoglobinaPaciente < 12) {
        diagnostico += "Posible anemia (hemoglobina baja). ";
    }

    if (glucosaPaciente >= 126) {
        diagnostico += "Posible diabetes (glucosa alta). ";
    } else if (glucosaPaciente < 70) {
        diagnostico += "Posible hipoglucemia (glucosa baja). ";
    }

    if (colesterolPaciente >= 240) {
        diagnostico += "Colesterol alto. ";
    } else if (colesterolPaciente < 125) {
        diagnostico += "Colesterol bajo. ";
    }

    if (diagnostico === "") {
        diagnostico = "Resultados normales o dentro de rangos saludables.";
    }

    const resultadoEl = document.getElementById("resultadoDiagnostico");
    resultadoEl.textContent = diagnostico;
    resultadoEl.classList.add("animate__animated", "animate__fadeIn");
});
