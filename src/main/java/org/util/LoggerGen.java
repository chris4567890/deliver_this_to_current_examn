package org.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoggerGen {

    private static LoggerGen instance;


    private LoggerGen(){

    }

    public static LoggerGen getInstance(){
        if(instance == null){
            instance = new LoggerGen();
        }
        return instance;
    }


    public void logToConsole(String msg){

        System.out.println("{insert time: }" + msg);
    }

}
