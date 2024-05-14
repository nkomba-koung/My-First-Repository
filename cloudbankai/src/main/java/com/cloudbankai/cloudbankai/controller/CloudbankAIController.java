package com.cloudbankai.cloudbankai.controller;

import com.cloudbankai.cloudbankai.domain.Message;
import com.cloudbankai.cloudbankai.domain.User;
import com.cloudbankai.cloudbankai.repository.UserRepository;
import com.cloudbankai.cloudbankai.services.ChatService;
import com.pdfcrowd.Pdfcrowd;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.print.Doc;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class CloudbankAIController {

    private static ChatService chatService;
    private static UserRepository userRepository;


    // Enregistrer la reponse et le numero du pdf contenant la reponse
    ArrayList<Object> answer = null;

    // Make easy maintenance using dependency injection
    @Autowired
    public CloudbankAIController(ChatService chatService, UserRepository userRepository) {
        this.chatService = chatService;
        this.userRepository = userRepository;
    }

    // Methode pour afficher le fichier de la doc correspondant a la reponse
    @GetMapping("/pdfContent")
    public Data pdfDoc(@RequestParam String pdf) {

        //Map<String, Object> model = new HashMap<String, Object>();
        String doc = "GENERALITES";

        if(pdf != "")
            doc = pdf;

        // Numero de la page
        int numPage = 1;
        String result;

        if(answer != null){
            if(pdf == ""){
                // Determiner le fichier a renvoyer
                if(((int) answer.get(1)) == 0)
                    doc = "EDITION";
                else if (((int) answer.get(1)) == 1)
                    doc = "ADMINISTRATION";
                else if (((int) answer.get(1)) == 2)
                    doc = "BACK-OFFICE";
                else if (((int) answer.get(1)) == 3)
                    doc = "FRONT_OFFICE";
                else if (((int) answer.get(1)) == 4)
                    doc = "GENERALITES";
                else if(((int) answer.get(1)) == 5)
                    doc = "MANUEL_DE_SECURITE";
                else if(((int) answer.get(1)) == 6)
                    doc = "Manuel-Administrateur";
                else
                    doc = "procedure_save_back_up";
            }

            result = (String) answer.get(0);
            Matcher matcher = null;

            // Des que la presentation fini, modifie la regex par "page\\D*(\\d+)"
            if(result.toLowerCase().contains("page")){
                // Définir l'expression régulière pour trouver le premier numéro après "page"
                Pattern pattern = Pattern.compile("page\\D*(\\d+)", Pattern.CASE_INSENSITIVE);
                matcher = pattern.matcher(result.toLowerCase());
            }

            if(matcher != null){
                // Si un numéro est trouvé, extraire et retourner le premier numéro de page
                if (matcher.find()) {
                    String numeroPageStr = matcher.group(1); // Le groupe 1 contient le numéro de page
                    numPage = Integer.parseInt(numeroPageStr);
                } else {
                    // Si aucun numéro n'est trouvé, retourner -1 ou lever une exception selon le cas d'utilisation
                    numPage = 1;
                }
            }
        }


        Data data = new Data();
        data.setMessage(doc);
        data.setNum(numPage+1);

        return data;
    }

    // Methode pour retourner tous les messages du chat
    @GetMapping("/chat")
    public ArrayList<Message> discussion(@RequestParam String name){

        ArrayList<Message> messages = userRepository.getMessages(name);

        return messages;
    }

    // Nouvelle interface de chat
    @GetMapping("/goChat")
    public ModelAndView anotherDesign(@ModelAttribute(name = "name") String name){
        String viewName = "chat";

        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        Map<String, Object> map1 = new HashMap<>();
        map1.put("user", new User());

        if(userRepository.checkUser(name))
            return new ModelAndView(viewName, map);

        return new ModelAndView("inscription", map1);
    }

    @GetMapping("/inscription")
    public ModelAndView anotherD(){
        String viewName = "inscription";

        Map<String, Object> map = new HashMap<>();
        map.put("user", new User());

        return new ModelAndView(viewName, map);
    }


    // Page d'acceuil
    @GetMapping("/")
    public ModelAndView connect(){

        String viewName = "inscription";

        Map<String, Object> model = new HashMap<String, Object>();

        model.put("user", new User());

        return new ModelAndView(viewName, model);
    }


    // Interface de connexion
    @PostMapping("/connexion")
    public ModelAndView submitConnexionInfo(
            @Valid @ModelAttribute(name = "user") User user,
            BindingResult bindingResult)
    {
        String viewName = "connexion";

        Map<String, String> map = new HashMap<>();

        // Verifie si il y a un probleme
        if(!userRepository.checkUser(user)){
            map.put("problem", "L'utilisateur entrée n'existe pas");
            return new ModelAndView(viewName, map);
        }

        map.put("name", user.getUsername());

        // Redirection vers la page de chat si il n'y a pas d'erreurs
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/goChat");
        redirectView.setAttributesMap(map);

        return new ModelAndView(redirectView);
    }


    @PostMapping("/inscription")
    public ModelAndView newSubscriber(
            @Valid @ModelAttribute(name = "user") User user,
            BindingResult bindingResult)
    {

        Map<String, String> map = new HashMap<>();

        // Verifie si il y a un probleme
        if(userRepository.checkUser(user)){
            map.put("problem", "L'utilisateur entrée existe déjà");
            return new ModelAndView("inscription", map);
        }

        // Ajout de l'utilisateur
        userRepository.addUser(user);
        map.put("name", user.getUsername());

        // Redirection vers la page de chat si il n'y a pas d'erreurs
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/goChat");
        redirectView.setAttributesMap(map);

        return new ModelAndView(redirectView);

    }

    // Methode pour enregistrer de nouveaux messages
    @PostMapping("/chat")
    public void chat(
            @RequestParam String name, @RequestParam String message, @RequestParam int num)
    {

        // Ajout des messages de l'utilisateur et de chatpdf
        userRepository.addMessage(name, new Message("Utilisateur", message));

        try {
            if(num == -1)
                answer = chatService.bestAnswer(message);
            else
                answer = chatService.bestAnswer(message, num);

            // Ajout du message avec le numero du pdf ayant la reponse
            userRepository.addMessage(name,
                    new Message("Cloudbank AI", (String) answer.get(0)));
        } catch (IOException e) {
            userRepository.addMessage(name,
                    new Message("Cloudbank AI", e.toString()));
        }

    }


    // Methode pour supprimer tous les messages du chat
    @DeleteMapping("/reset")
    public void deleteMessages(@RequestParam String name){
        userRepository.clearMessage(name);
    }

    // Classe pour gerer le nom du document interroge et le numero de la page ayant la reponse
    public class Data {
        String message;
        int num;

        public Data() {
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getMessage() {
            return message;
        }

        public int getNum() {
            return num;
        }
    }

}
