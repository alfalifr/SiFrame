package sidev.lib.universal.tool.util;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class Cob {
    public static void main(String[] args) {
        Method[] mets= Cob.class.getDeclaredMethods();
        for(Method met : mets){
            System.out.println("Method= " +met.getName());
            Parameter[] params= met.getParameters();

            for(Parameter param: params){
                System.out.println("  param= " +param.getType().getSimpleName());
            }
        }
    }
}
