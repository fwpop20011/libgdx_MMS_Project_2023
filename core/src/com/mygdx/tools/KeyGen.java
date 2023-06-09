package com.mygdx.tools;

public class KeyGen {
    private static int key = 0;

    public static int getKey(){
        key += 1;
        return key;
    }
    
    public static void reset(int resetValue){
        key = resetValue;
    }
}
