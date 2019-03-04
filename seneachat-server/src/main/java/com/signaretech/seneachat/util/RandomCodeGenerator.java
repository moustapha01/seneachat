package com.signaretech.seneachat.util;

import java.util.Random;

public class RandomCodeGenerator {

    private static final char[] characters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};


    public static String generateCode(){

        Random rnd = new Random();

        StringBuffer code = new StringBuffer();
        code.append(characters[rnd.nextInt(26)])
                .append(characters[rnd.nextInt(26)])
                .append(rnd.nextInt(10))
                .append(characters[rnd.nextInt(26)])
                .append(rnd.nextInt(10))
                .append(characters[rnd.nextInt(26)]);

        return code.toString();

    }
}
