/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientpranawa;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author PRANAWA
 */
public class ClientPranawa {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("10.151.34.155", 6666);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            boolean flag=false; 
            
            while (true) {
                byte[] buf = new byte[1];
                is.read(buf);
                String inputan = new String(buf);
                while (!inputan.endsWith("\n")) {
                    is.read(buf);
                    inputan = inputan + new String(buf);
                }
                System.out.print(inputan);
                
                if(inputan.equals("Hash:\n")) {
                    byte[] dum = new byte[1];
                    is.read(dum);
                    String arguments = new String(dum);
                    while (!arguments.endsWith("\n")) {
                        is.read(dum);
                        arguments =arguments + new String(dum);
                    }
                    System.out.print(arguments);
                    arguments = arguments.trim();
                    
                    String[] arg = arguments.split(":");
                    int len = Integer.parseInt(arg[1]);
                    
                    String respond = "Hash:";
                    for(int i=0;i<len;i++) {
                        is.read(dum);
                        respond = respond + new String(dum);
                    }
                    
                    respond = respond + "\n";
                    System.out.print("respond: " + respond);
                    os.write(respond.getBytes());
                    os.flush();
                    
                    is.read(dum);
                    is.read(dum);
                    arguments = new String(dum);
                    while (!arguments.endsWith("\n")) {
                        is.read(dum);
                        arguments += new String(dum);
                    }
                    
                    String[] status = arguments.trim().split(" ");
                    
                    if(status[0].equals("666")){
                        System.out.print(arguments);
                        break;
                    } else {
                        System.out.println(status[0] + "\n");
                    }
                } else if(inputan.endsWith("?\n")) {
                    inputan = inputan.trim();
                    String[] inputs = inputan.split(" ");
                    int bil1 = Integer.parseInt(inputs[0]);
                    int bil2 = Integer.parseInt(inputs[2]);
                    int res;
                    
                    switch (inputs[1]) {
                        case "+":
                            res = bil1+bil2;
                            break;
                        case "-":
                            res = bil1-bil2;
                            break;
                        case "x":
                            res = bil1*bil2;
                            break;
                        default:
                            res = bil1%bil2;
                            break;
                    }
                    
                    String respond = "result:"+ Integer.toString(res) + "\n";
                    System.out.print("respond: " + respond);
                    os.write(respond.getBytes());
                    os.flush();
                } else if(inputan.endsWith("NRP\\n\n")) {
                    Scanner text = new Scanner(System.in);
                    String username = text.nextLine();
                    username = username + "\n";
                    os.write(username.getBytes());
                    os.flush();
                }
            }
            
            os.close();
            is.close();
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientPranawa.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
