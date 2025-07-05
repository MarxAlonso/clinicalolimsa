async function descargarGraficoPDF() {
        const graficoContainer = document.querySelector('.chart-container');
        const canvas = await html2canvas(graficoContainer, { scale: 2 });

        const imgData = canvas.toDataURL('image/png');
        const { jsPDF } = window.jspdf;
        const pdf = new jsPDF();

        // Título
        pdf.setFontSize(16);
        pdf.setTextColor(40);
        pdf.text("Resultado de Examen Arterial", 105, 15, null, null, "center");

        // Datos del paciente (opcional: puedes agregarlos más dinámicamente)
        const nombre = document.querySelector('h5 span').textContent.trim();
        const fecha = document.querySelector('p span').textContent.trim();
        pdf.setFontSize(12);
        pdf.text(nombre, 10, 30);
        pdf.text("Fecha del examen: " + fecha, 10, 40);

        // Imagen del gráfico
        const imgProps = pdf.getImageProperties(imgData);
        const pdfWidth = 190;
        const imgHeight = (imgProps.height * pdfWidth) / imgProps.width;
        pdf.addImage(imgData, 'PNG', 10, 50, pdfWidth, imgHeight);

        // Observaciones
        const observaciones = document.querySelector(".alert-info p").textContent.trim();
        pdf.text("Observaciones:", 10, 60 + imgHeight);
        pdf.setFontSize(11);
        pdf.text(observaciones, 10, 70 + imgHeight);

        // Diagnóstico
        const diagnostico = document.getElementById("resultadoDiagnostico").textContent.trim();
        pdf.setFontSize(12);
        pdf.text("Diagnóstico automático:", 10, 80 + imgHeight);
        pdf.setFont("helvetica", "bold");
        pdf.text(diagnostico, 10, 90 + imgHeight);

        // Guardar PDF
        pdf.save("informe_presion_paciente.pdf");
    }