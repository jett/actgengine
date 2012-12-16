package com.incuventure.accounting.event;

/**
 * Created with IntelliJ IDEA.
 * User: Jett
 * Date: 12/15/12
 * Time: 4:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class Implem implements TestInterface {

    @Override
    public void say(String message) {
        System.out.println(message);
    }
}
