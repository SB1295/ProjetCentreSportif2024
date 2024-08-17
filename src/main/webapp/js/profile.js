$(document).ready(function() {
    console.log("Document ready. Initialisation de Select2");

    // Initialisation de Select2 pour le menu déroulant
    initializeSelect2();

    // Gestionnaire d'événement pour le changement de localité
    $('#locality').on('change', function() {
        console.log("Changement de localité détecté");
        handleLocalityChange($(this).val());
    });

    // Gestionnaire d'événement pour la soumission du formulaire utilisateur
    $('#userForm').on('submit', function() {
        console.log("Soumission du formulaire utilisateur détectée. Réinitialisation de Select2.");
        setTimeout(function() {
            initializeSelect2();
        }, 500);
    });
});

function initializeSelect2() {
    console.log("Initialisation de Select2 pour .locality-select");
    $('.locality-select').select2({
        placeholder: 'Recherchez une localité...',
        allowClear: true
    });
}

function handleLocalityChange(localityId) {
    console.log("Localité sélectionnée : " + localityId);
    if (localityId) {
        console.log("Envoi de la requête AJAX pour l'ID de localité : " + localityId);
        $.ajax({
            url: 'AddressServlet',
            method: 'GET',
            data: { localityId: localityId },
            success: function(data) {
                console.log("Réponse reçue pour la localité : ", data);
                $('#town').val(data.town);
                $('#province').val(data.province);
                $('#maintown').val(data.maintown); // Remplir le champ maintown
            },
            error: function() {
                console.error("Erreur lors de la récupération des détails de la localité.");
                alert('Erreur lors de la récupération des détails de la localité.');
            }
        });
    } else {
        console.log("Aucune localité sélectionnée.");
    }
}
