package pck;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;

/**
 * Nuovo tipo di thread che si mette in ascolto di pacchetti in arrivo sulla porta di ascolto
 */
public class ListenServerThread extends Thread {

    private UDPEchoServer UDPEchoServer = null;

    public ListenServerThread (UDPEchoServer udps){
        this.UDPEchoServer = udps;
    }

    public void run(){

        boolean neverEnd = true;
        byte[] receiveData = new byte[1024];
        File f = new File("Packets_received.txt");
        FileWriter fileWriter = null;
        PrintWriter printWriter;


        while (neverEnd) {

            try {

                fileWriter = new FileWriter(f,true);
                printWriter = new PrintWriter(fileWriter);
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                UDPEchoServer.getServerSocket().receive(receivePacket);
                printWriter.write(new String(receivePacket.getData()));
                printWriter.flush();

                //System.out.println("RECEIVED " + packetReceived + " packets");

            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
