package com.cloudbankai.cloudbankai.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

@Service
public class ChatService {

    private static final String API_KEY = "sec_8kXbybf7z1ydAaxGrvGKYfk03xFFS4l6";
    private static final ArrayList<String> sources_ID = new ArrayList<String>();

    // URL de l'endpoint de l'API Chatpdf
    private static final String endpoint = "https://api.chatpdf.com/v1/chats/message";

    // Initialisation des sources
    public ChatService() {
        sources_ID.add("src_Vy1TRTvpu4MaBfNRyTI4h"); // EDITION
        sources_ID.add("src_0F08QvEuRxmOoaS78ccxI");   // ADMINISTRATION
        sources_ID.add("src_8jYTH8vPThDsIyvtPb4q2");   // BACK-OFFICE
        sources_ID.add("src_cZMOwbrLeTiNd3oyFsk7i");  // FRONT_OFFICE
        sources_ID.add("src_hCsdww9nzs3hisZPFNS8m");  // GENERALITES
        sources_ID.add("src_u0WDZK5BTPayANMbdFt66");  // MANUEL_DE_SECURITE
        sources_ID.add("src_MOVQyZniGjXrAn1BeGtZD");  // Manuel-Administrateur
        sources_ID.add("src_skEbguz1ImvRfr0nZqomH"); // procedure_save_back_up
    }


    // Methode pour déterminer la meilleur reponse
    public ArrayList<Object> bestAnswer(String question) throws IOException {
        try {
            int i = 0;
            String intermediate = null;
            ArrayList<Object> response = new ArrayList<Object>();
            if (question.toLowerCase().contains("back up") ||
                    question.toLowerCase().contains("back ups") ||
                    (question.toLowerCase().contains("sauvegarde") &&
                            question.toLowerCase().contains("back up"))){
                response.add(answerPDFQuestions(question, 7));
                response.add(7);
            }
            else if(question.toLowerCase().contains("erreur")){
                response.add(answerPDFQuestions(question, 6));
                response.add(6);
            }
            else if(question.toLowerCase().contains("securite") ||
                    question.toLowerCase().contains("menace") ||
                    question.toLowerCase().contains("architecture")){
                response.add(answerPDFQuestions(question, 5));
                response.add(5);
            }
            else if(question.toLowerCase().contains("general") ||
                    question.toLowerCase().contains("generalement") ||
                    question.toLowerCase().contains("generalites")){
                response.add(answerPDFQuestions(question, 4));
                response.add(4);
            }
            else
                do {
                    intermediate = answerPDFQuestions(question, i);
                    i++;
                } while ((intermediate.toLowerCase().contains("malheureusement") ||
                        intermediate.toLowerCase().contains("je suis désolé") ||
                        intermediate.toLowerCase().contains("je ne peux pas") ||
                        intermediate.toLowerCase().contains("il n'y a pas de mention spécifique") ||
                        intermediate.toLowerCase().contains("je n'ai pas trouvé") ||
                        intermediate.toLowerCase().contains("je ne trouve pas") )
                        && (i <= 4));

                response.add(intermediate);
                response.add(i-1);

            return response;
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    // Surcharge de la methode bestAnswer
    public ArrayList<Object> bestAnswer(String question, int num) throws IOException {
        try{
            ArrayList<Object> response = new ArrayList<Object>();

            response.add(answerPDFQuestions(question, num));
            response.add(num);

            return response;
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    // Methode pour repondre aux questions de l'user
    public String answerPDFQuestions(String question, int pdf) throws IOException {

        URL url = new URL(endpoint);

        // Traitement de la question
        question = question.replaceAll("[\\\\/'\"]", " ");

        try{
            // Ouverture de la connexion HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            // Définition des en-têtes HTTP
            connection.setRequestProperty("x-api-key", API_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Corps de la requête POST
            String postData = "{" +
                    "\"sourceId\": \"" + sources_ID.get(pdf) + "\","
                    + "\"messages\": [ "
                    + "{ \"role\": \"assistant\", \"content\": \" t'es un expert " +
                    "sur le document pdf que tu possede et tu inclus toujours les " +
                    "pages de reference de tes reponses, ces numeros de pages sont " +
                    "dans les crochets et précédé par le terme page \" }," +
                    "{ \"role\": \"user\", \"content\": \" " + question + " \" }"
                    + "]}";

            System.out.println(postData);

            // Envoi des données de la requête
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = postData.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Lecture de la réponse de l'API
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }

            // Enregistrement du resultat
            // return extractScript(extractContentFromResponse(response.toString()));
            return extractContentFromResponse(response.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Fonction charge d'extraire le resultat de chatpdf
    public String extractContentFromResponse(String response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonObject = objectMapper.readTree(response);

            // Récuperer la valeur du champ content de la réponse
            return jsonObject.get("content").asText();
        } catch (IOException e) {
            return "error";
        }
    }
}
