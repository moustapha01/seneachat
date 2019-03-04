package com.signaretech.seneachat.controller;

import org.springframework.util.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationManager {

    private static final String phoneRegex = "\\d{9,10}";

    public static String validateInput(String password, String password2, String email, String phone){

        String result = "valid";
        if(password == null || password2 == null){
            result = "Completer votre mot de passe";
        }
        if(!password.equals(password2)){
            result = "Confirmer votre mot de passe.";
        }

        if(result.equals("valid") && StringUtils.isEmpty(email)){
            result = "Completer votre addresse email";
        }

        if(result.equals("valid")){
            result = validate(phone);
        }
        return result;
    }

    private static String validate(String phone) {
        String result = "valid";
        Pattern phonePattern = Pattern.compile(phoneRegex);
        Matcher matcher = phonePattern.matcher(phone);

        if(!matcher.find()){
            result = "Le numero de telephone inscrit est invalide.";
        }
        return result;
    }

//	public static String validatePrimaryPhotoSelection(List<Photo> photos){
//
//		String result = "valid";
//
//		Integer numPrimaryPhotos = 0;
//
//		for (Photo photo : photos) {
//			if(photo.getPrimaryInd()){
//				numPrimaryPhotos++;
//			}
//		}
//
//		if(numPrimaryPhotos != 1 && photos != null && photos.size() > 1){
//			result = "Un maximum d'une photo primaire doit etre selectionne avant de soumettre votre annonce";
//		}
//
//		return result;
//
//	}

}

