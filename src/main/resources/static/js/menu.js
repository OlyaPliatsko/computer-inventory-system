const menuToggle = document.getElementById('menuToggle');
const menuOverlay = document.getElementById('menuOverlay');
const menuPanelLinks = document.querySelectorAll('.menu-panel-nav a');

menuToggle.addEventListener('click', () => {
    menuToggle.classList.toggle('active');
    menuOverlay.classList.toggle('show');
});

menuOverlay.addEventListener('click', (event) => {
    if (event.target === menuOverlay) {
        menuToggle.classList.remove('active');
        menuOverlay.classList.remove('show');
    }
});

menuPanelLinks.forEach(link => {
    link.addEventListener('click', () => {
        menuToggle.classList.remove('active');
        menuOverlay.classList.remove('show');
    });
});

window.addEventListener('scroll', () => {
    const navbar = document.querySelector('.navbar');

    if (window.scrollY > 50) {
        navbar.style.background = "rgba(1, 1, 1, 0.35)";
        navbar.style.boxShadow = "0 10px 30px rgba(0,0,0,0.35)";
    } else {
        navbar.style.background = "rgba(0, 0, 0, 0.25)";
        navbar.style.boxShadow = "none";
    }
});