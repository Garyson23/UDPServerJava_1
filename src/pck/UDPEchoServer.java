package pck;

/**
 * UDP Server. Invia messaggi all' IP IPAddress alla porta @port_write_to. In parallelo
 * lancia un thread che si mette in ascolto sulla porta @port_lister_to. I pacchetti ricevuti
 * vengono salvati sul file presente nella cartella del progetto "Packets_received.txt".
 * I comandi che vengono inviati vengono presi da riga di comando (stringhe), convertiti
 * in byte[] e inviati al destinatario.
 *
 */

import java.io.*;
import java.net.*;

class UDPEchoServer {

    //porta dove ascolta per pacchetti in arrivo
    private int port_listen_to = 34567;

    //porta dove inviare pacchetti
    private int port_write_to = 55555;

    //socket per l'ascolto dei pacchetti alla porta port_listen_to
    private DatagramSocket serverSocket = new DatagramSocket(port_listen_to);

    //buffer usato per prendere da riga di comando il messaggio da inviare
    private BufferedReader bufferedReader =
            new BufferedReader(new InputStreamReader(System.in));
    private InetAddress IPAddress;


    //costruttore
    public UDPEchoServer() throws UnknownHostException, SocketException {

        //..........::::::::::: CHANGE THIS LINE :::::::::............ | Destination address
        IPAddress = InetAddress.getByName("localhost");
    }

    //metodo specifico che invia un messaggio specifico @message
    private void sendMessage(Message message) {

        //pacchetto da inviare... conversione implicita con message.getBytes();
        DatagramPacket sendPacket =
                new DatagramPacket(message.getBytes(), message.getBytes().length, IPAddress, port_write_to);
        try {

            //invio pacchetto
            serverSocket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //funzione per inviare comandi ricevuti da CLI
    private void sendMessages() {

        boolean exit = false;
        String command;

        while (!exit) {

            System.out.println("....:::: Inserire stringa da inviare. Premere 'Q', 'q', o 'quit' per uscire ::::....");
            Message message;
            try {
                //acquisisce da CLI il messaggio da inviare
                command = bufferedReader.readLine();
                message = new Message(command);

                if ((command.equalsIgnoreCase("q"))||(command.equalsIgnoreCase("quit"))) {
                    exit = true;
                    break;
                }
                //chiama metodo per inviare messaggio
                sendMessage(message);

                System.out.println("....:::: Pacchetto inviato ::::....");
                System.out.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //funzione runnable che inizializza il server (UDPEchoServer) e lancia il thread che si mette
    //in ascolto di pacchetti in arrivo. Poi lancia il metodo senza fine che aspetta comandi da inviare
    public static void main(String args[]) throws Exception {

        UDPEchoServer UDPEchoServer = new UDPEchoServer();
        ListenServerThread listenServerThread = new ListenServerThread(UDPEchoServer);

        listenServerThread.start();
        System.out.println("Attendo pacchetti....");
        UDPEchoServer.sendMessages();

        System.out.println("....:::: Applicazione terminata ::::....");


    }


    public DatagramSocket getServerSocket() {
        return serverSocket;
    }

}

