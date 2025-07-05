async function descargarGraficoPDF() {
    const graficoContainer = document.querySelector('.chart-container');
    const canvas = await html2canvas(graficoContainer, { scale: 2 });

    const imgData = canvas.toDataURL('image/png');
    const { jsPDF } = window.jspdf;
    const pdf = new jsPDF();

    // Título
    pdf.setFontSize(16);
    pdf.setTextColor(40);
    pdf.text("Informe de Examen de Sangre", 105, 15, null, null, "center");

    // Datos del paciente
    const nombre = document.querySelector('h5 span')?.textContent.trim() || "Paciente";
    const fecha = document.querySelector('p span')?.textContent.trim() || "Fecha";
    pdf.setFontSize(12);
    pdf.setFont("helvetica", "normal");
    pdf.text(nombre, 10, 30);
    pdf.text("Fecha del examen: " + fecha, 10, 40);

    // Gráfico
    const imgProps = pdf.getImageProperties(imgData);
    const pdfWidth = 190;
    const imgHeight = (imgProps.height * pdfWidth) / imgProps.width;
    pdf.addImage(imgData, 'PNG', 10, 50, pdfWidth, imgHeight);

    // Observaciones
    const observaciones = document.querySelector(".alert-info p")?.textContent.trim() || "Sin observaciones.";
    pdf.setFont("helvetica", "normal");
    pdf.setFontSize(12);
    pdf.text("Observaciones:", 10, 60 + imgHeight);
    pdf.setFontSize(11);
    const obsLines = pdf.splitTextToSize(observaciones, 180);
    pdf.text(obsLines, 10, 70 + imgHeight);

    // Diagnóstico
    const diagnostico = document.getElementById("resultadoDiagnostico")?.textContent.trim() || "Sin diagnóstico.";
    pdf.setFontSize(12);
    pdf.setFont("helvetica", "normal");
    pdf.text("Diagnóstico automático:", 10, 80 + imgHeight + obsLines.length * 5);
    pdf.setFont("helvetica", "bold");
    const diagLines = pdf.splitTextToSize(diagnostico, 180);
    pdf.text(diagLines, 10, 90 + imgHeight + obsLines.length * 5);

    // Descargar
    pdf.save("informe_examen_sangre.pdf");
}