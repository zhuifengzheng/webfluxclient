package com.yp.proxy;

import java.util.stream.IntStream;

/**
 * @author fengzheng
 * @create 2018-09-17 11:05
 * @desc
 **/
public class Test {

    public static void main(String[] args) {
        System.out.println( print());
        StringBuffer str = new StringBuffer("abcdefg");
        StringBuffer reverse = str.reverse();

        String str1 = "abcdefghijk";
        for(int i=str1.length()-1; i>-1; i--){
            System.out.print(str1.charAt(i));
        }
    }

    public static String print(){
        try{
            return "hi";
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return "final";
//            System.out.println("final");
        }
//            return "end";
    }
}