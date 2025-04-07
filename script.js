// Fonction pour copier le code dans le presse-papiers
function copyToClipboard(button) {
    const codeBlock = button.parentElement;
    const code = codeBlock.querySelector('code').innerText;
    
    navigator.clipboard.writeText(code).then(() => {
        // Changer temporairement l'icône pour indiquer le succès
        const originalHTML = button.innerHTML;
        button.innerHTML = '<i class="fas fa-check"></i>';
        
        setTimeout(() => {
            button.innerHTML = originalHTML;
        }, 2000);
    }).catch(err => {
        console.error('Erreur lors de la copie:', err);
    });
}

// Animation au défilement
document.addEventListener('DOMContentLoaded', function() {
    // Fonction pour vérifier si un élément est visible à l'écran
    function isElementInViewport(el) {
        const rect = el.getBoundingClientRect();
        return (
            rect.top >= 0 &&
            rect.left >= 0 &&
            rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) &&
            rect.right <= (window.innerWidth || document.documentElement.clientWidth)
        );
    }
    
    // Fonction pour animer les éléments lorsqu'ils entrent dans la vue
    function handleScrollAnimation() {
        const animatedElements = document.querySelectorAll('.feature-card, .tech-item, .roadmap-item, .step');
        
        animatedElements.forEach(element => {
            if (isElementInViewport(element) && !element.classList.contains('animated')) {
                element.classList.add('animated');
                element.style.opacity = '1';
                element.style.transform = 'translateY(0)';
            }
        });
    }
    
    // Initialiser les éléments qui seront animés
    const animatedElements = document.querySelectorAll('.feature-card, .tech-item, .roadmap-item, .step');
    animatedElements.forEach(element => {
        element.style.opacity = '0';
        element.style.transform = 'translateY(20px)';
        element.style.transition = 'opacity 0.5s ease, transform 0.5s ease';
    });
    
    // Exécuter la fonction d'animation au chargement et au défilement
    handleScrollAnimation();
    window.addEventListener('scroll', handleScrollAnimation);
    
    // Navigation fluide pour les ancres
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function(e) {
            e.preventDefault();
            
            const targetId = this.getAttribute('href');
            const targetElement = document.querySelector(targetId);
            
            if (targetElement) {
                window.scrollTo({
                    top: targetElement.offsetTop - 80,
                    behavior: 'smooth'
                });
            }
        });
    });
});