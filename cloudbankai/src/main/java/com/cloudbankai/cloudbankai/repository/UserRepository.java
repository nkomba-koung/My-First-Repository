package com.cloudbankai.cloudbankai.repository;

import com.cloudbankai.cloudbankai.domain.Message;
import com.cloudbankai.cloudbankai.domain.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Classe pour enregistrer les utilisateurs
@Service
public class UserRepository {

    // Liste des utilisateurs avec leurs messages de l'app
    private static Map<User, ArrayList<Message>> users = new HashMap<User, ArrayList<Message>>();


    // Fonction pour ajouter un utilisateur a la BD
    public void addUser(User user){

        users.put(user, new ArrayList<Message>());
    }

    // Verifier qu'un utilisateur existe deja en BD
    public boolean checkUser(User user) {

        return users.containsKey(user);
    }

    public boolean checkUser(String username) {

        for(User user: users.keySet())
            if(user.getUsername().equals(username))
                return true;

        return false;
    }

    // recuperer tous les messages lies a un utilisateur
    public ArrayList<Message> getMessages(String username){

        ArrayList<Message> ms = new ArrayList<>();
        ms.add(new Message("null", "null"));

        for(User key : users.keySet()){
            if(key.getUsername().equals(username))
                return users.get(key);
        }

        return ms;
    }

    // Ajouter des messages dans la boite de dialogue d'un utilisateur
    public void addMessage(String username, Message message){

        users.forEach((user, messages) -> {
            if(user.getUsername().equals(username)){
                messages.add(message);
            }
        });
    }

    // Supprimer les messages d'un utilisateur
    public void clearMessage(String username){

        users.forEach((user, messages) -> {
            if(user.getUsername().equals(username))
                messages.clear();
        });
    }
}
