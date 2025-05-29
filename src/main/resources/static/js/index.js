document.addEventListener("DOMContentLoaded", () => {
    const images = [
        '/img/imagencli1.jpg',
        '/img/imagencli2.jpg',
        '/img/imagencli3.jpg'
    ];

    let current = 0;
    const slider = document.querySelector('.background-slider');

    function changeBackground() {
        slider.style.backgroundImage = `url('${images[current]}')`;
        current = (current + 1) % images.length;
    }

    changeBackground();
    setInterval(changeBackground, 5000); // cambia cada 5 segundos
});

document.addEventListener("DOMContentLoaded", function () {
    const animatedElements = document.querySelectorAll('.animate-on-scroll');

    const observer = new IntersectionObserver(entries => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('visible');
            }
        });
    }, { threshold: 0.1 });

    animatedElements.forEach(el => observer.observe(el));
});
