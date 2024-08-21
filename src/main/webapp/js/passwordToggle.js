// passwordToggle.js
document.addEventListener("DOMContentLoaded", function() {
    const togglePassword = document.querySelectorAll(".toggle-password");

    togglePassword.forEach(function(element) {
        element.addEventListener("click", function() {
            const input = document.querySelector(this.getAttribute("toggle"));
            if (input.getAttribute("type") === "password") {
                input.setAttribute("type", "text");
                this.classList.toggle("fa-eye-slash");
            } else {
                input.setAttribute("type", "password");
                this.classList.toggle("fa-eye-slash");
            }
        });
    });
});
