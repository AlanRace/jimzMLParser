/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.py4j;

import py4j.GatewayServer;

/**
 *
 * @author alan.race
 */
public class Py4JGateway {
    public static void main(String[] args) {
        Py4JGateway gateway = new Py4JGateway();
        
        GatewayServer gatewayServer = new GatewayServer(gateway);
        
        gatewayServer.start();
    }
}
