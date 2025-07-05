/*<![CDATA[*/
let comprasChart = null;

// Agrupa y filtra por mes
    function procesarDatos(mesSeleccionado) {
        const gananciasPorFecha = {};

        compras.forEach(compra => {
            const fechaObj = new Date(compra.fechaCompra);
            const fecha = fechaObj.toLocaleDateString('es-PE');
            const mes = String(fechaObj.getMonth() + 1).padStart(2, '0');

            if (mesSeleccionado === 'todos' || mes === mesSeleccionado) {
                if (!gananciasPorFecha[fecha]) {
                    gananciasPorFecha[fecha] = 0;
                }
                gananciasPorFecha[fecha] += compra.total;
            }
        });

        const labels = Object.keys(gananciasPorFecha);
        const data = Object.values(gananciasPorFecha);

        return { labels, data };
    }

    // Renderiza el gráfico
    function renderChart(mes = 'todos') {
        const ctx = document.getElementById('comprasChart').getContext('2d');
        const { labels, data } = procesarDatos(mes);

        if (comprasChart) {
            comprasChart.destroy();
        }

        comprasChart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Ganancias por Fecha (S/.)',
                    data: data,
                    borderColor: 'rgba(75, 192, 192, 1)',
                    backgroundColor: 'rgba(75, 192, 192, 0.2)',
                    tension: 0.3,
                    fill: true,
                    pointRadius: 5,
                    pointHoverRadius: 7
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true,
                        title: { display: true, text: 'Soles (S/.)' }
                    },
                    x: {
                        title: { display: true, text: 'Fecha' }
                    }
                }
            }
        });
    }

    // Evento para filtrar
    document.getElementById("mesSelect").addEventListener("change", function() {
        renderChart(this.value);
    });

    // Exportar como imagen
    function exportarGrafico() {
        const canvas = document.getElementById("comprasChart");
        const link = document.createElement('a');
        link.download = "grafico_compras.png";
        link.href = canvas.toDataURL();
        link.click();
    }

    // Inicializa
    renderChart();
/*]]>*/