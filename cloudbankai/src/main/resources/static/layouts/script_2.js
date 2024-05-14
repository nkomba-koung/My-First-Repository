// Sélection de la balise div avec l'ID "vac"
var vacDiv = document.getElementById("chat-messages-container");

// Variable determinant quel pdf a ete charger
var charger = -1;
var nomPdf = "";

// Capture du nom d'utilisateur
const queryPar = new URLSearchParams(window.location.search);
const username = queryPar.get("name");

// Manipulation du textarea
const textarea = document.getElementById('chat-input');
const form = document.getElementById('form');

/* fonction pour permettre a la barre de saisie de s'ajuster en fonction du contenu*/
textarea.addEventListener('input', function() {
  this.style.height = 'auto';
  this.style.height = (this.scrollHeight) + 'px';
});

/* Changer la fonction du bouton entrer dans le textarea */
textarea.addEventListener('keydown', function(event) {
    if (event.key === 'Enter' && !event.shiftKey) {
        event.preventDefault(); // Empêche le comportement par défaut de la touche "Enter"
        //form.submit(); // Envoie le formulaire
        sendMessage();
    }
});


// Fonction pour faire défiler vers le bas le contenu de la div
function scrollDown() {
    vacDiv.scrollTop = vacDiv.scrollHeight;
}


// Fonction pour l'affichage de la barre de chargement
function showLoading() {
	$(".loading").show();
}

// Function to hide loading animation
function hideLoading() {
	$(".loading").hide();
}

// Fonction pour afficher le document correspondant
function afficherDoc() {

    var pdfViewer = document.getElementById("pdfViewer");

    $.ajax({
       url: "pdfContent",
       type: "GET",
       data: {action: "the document to preview", pdf: nomPdf},
       success: function(data) {
            // Mettre à jour la source de la balise iframe avec la nouvelle valeur
            var src = "/Cloudbank_Doc/" + data.message + ".html#page" + data.num;
            pdfViewer.setAttribute("src", src);
       },
       error: function() {
            console.log("Erreur lors de la requête AJAX");
       }
    });
}

// Fonction pour charger le document choisi par l'utilisateur
function chargerPdf(docNumber) {
    var pdfViewer = document.getElementById("pdfViewer");

    var mesPdf= {
        1: "ADMINISTRATION",
        2: "BACK-OFFICE",
        0: "EDITION",
        3: "FRONT_OFFICE",
        4: "GENERALITES",
        6: "Manuel-Administrateur",
        5: "MANUEL_DE_SECURITE",
        7: "procedure_save_back_up"
    };

    charger = docNumber;
    nomPdf = mesPdf[charger];

    afficherDoc();
}

// Fonctions realisant les requetes ajax sur le serveur de servlet
function loadMessages(messagesToLoad, charge) {
	$.ajax({
		type: "GET",
		url: "chat",
		data: { action: "getMessages", name: username },
		success: function(messages) {
			// Vider l'espace de chat
			if(messagesToLoad == 0)
			    $(".chat-messages").empty();

	        // Afficher les messages dans la zone de chat
	        $.each(messages.slice(messagesToLoad), function(index, message) {

	            if(message.sender === "null"){
	                window.location.href = "/inscription";
	            }

	        	if (message.sender === "Utilisateur") {
                    var messageClass = "message-user";
	        	} else {
	        	    var messageClass = "message-cloudbank";
	        	}

	        	$(".chat-messages").append(
                    " <div class=\" " + messageClass + "\"> "
                	+ " <div class=\"message-header\"> " + message.sender + " </div> "
                	+ " <div class=\"message-body\"> " + message.content + " </div> "
                	+ " </div> "
                );
	        });

            afficherDoc();
	        scrollDown();
	        hideLoading();
	     },
	     error: function() {
	        alert("Erreur lors du chargement des messages.");
	        hideLoading();
	     }
	});
}


// Fonction pour envoyer un message via AJAX
function sendMessage() {
	var messageText = $("#chat-input").val();
	showLoading();
	$.ajax({
		type: "POST",
	    url: "chat",
	    data: { action: "sendMessage", name: username, message: messageText, num: charger },
	    success: function() {
	          loadMessages(-2, charger);
	          $("#chat-input").val("");
	    },
	    error: function() {
	          alert("Erreur lors de l'envoi du message.");
	          hideLoading();
	    }
	});

    return false;
}

// Supprimer tous les messages
function deleteMessages() {
	showLoading();
	$.ajax({
	    type: "DELETE",
	    url: "reset",
	    data: {action: "delete Messages", name: username},
	    success: function() {
	        $(".chat-messages").empty();
	        hideLoading();
	    },
	    error: function() {
	          alert("Erreur lors de la suppression des messages.");
	          hideLoading();
	    }
	 });
}

// afficher tous les messages a chaque actualisation de la page
loadMessages(0, charger);


