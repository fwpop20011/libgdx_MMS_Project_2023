package com.mygdx.tools;

public class KeyGen {
    static int key = -4;

    public static int getKey(){
        key += 1;
        return key;
    }
    
}
