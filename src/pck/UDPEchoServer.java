package pck;

/**
 * UDP Server. Invia messaggi all' IP IPAddress alla porta @port_write_to. In parallelo
 * lancia un thread che si mette in ascolto sulla porta @port_lister_to. I pacchetti ricevuti
 * vengono salvati sul file presente nella cartella del progetto "Packets_received.txt".
 * I comandi che vengono inviati vengono presi da riga di comando (stringhe), convertiti
 * in byte[] e inviati al destinatario.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Random;

class UDPEchoServer {

    //porta dove ascolta per pacchetti in arrivo
    private int port_listen_to = 55555;

    //porta dove inviare pacchetti
    private int port_write_to = 55555;

    //socket per l'ascolto dei pacchetti alla porta port_listen_to
    private DatagramSocket serverSocket = new DatagramSocket(port_listen_to);

    //buffer usato per prendere da riga di comando il messaggio da inviare
    private BufferedReader bufferedReader =
            new BufferedReader(new InputStreamReader(System.in));
    private InetAddress IPAddress;

    private Random rand = new Random();
    private String hex_speed, hex_position, string_to_send = new String();


    //costruttore
    public UDPEchoServer() throws UnknownHostException, SocketException {

        //..........::::::::::: CHANGE THIS LINE :::::::::............ | Destination address
        IPAddress = InetAddress.getByName("192.168.0.101");
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

            System.out.println("....:::: Inserire comando tra i seguenti:\n- stress\t\tDura circa 50 secondi, invia un messaggio diverso ogni 50ms\n- yaw\t\t\tInvia un messaggio di rotazione YAW di un angolo casuale\n- quit o q\t\tExit\n\n");
            Message message;
            try {
                //acquisisce da CLI il messaggio da inviare
                command = bufferedReader.readLine();

                switch (command) {
                    case "stress": {
                        stress();
                        exit = true;
                        break;
                    }
                    case "test": {
                        test();
                        break;
                    }
                    case "pitch": {
                        pitch();
                        break;
                    }
                    case "roll": {
                        roll();
                        break;
                    }
                    case "yaw": {
                        yaw();
                        break;
                    }
                    case "quit":
                    case "q":
                        exit = true;


                    default:
                        System.out.println("..:: Sorry, wrong command ::..");
                }

                if (exit)
                    break;

                message = new Message(string_to_send);
                //chiama metodo per inviare messaggio
                sendMessage(message);

                System.out.println("....:::: Pacchetto inviato ::::....");
                System.out.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void test() {


    }

    private void stress() {
        try {
            for (int i = 0; i < 100; i++) {
                sendMessage(new Message("#0200000000000003845000000496\r"));
                System.out.println("....:::: Pacchetto inviato ::::....");
                Thread.sleep(50);
                sendMessage(new Message("#0200965000000000000000000491\r"));
                System.out.println("....:::: Pacchetto inviato ::::....");
                Thread.sleep(50);
                sendMessage(new Message("#02000000000000004B5000000496\r"));
                System.out.println("....:::: Pacchetto inviato ::::....");
                Thread.sleep(50);
                sendMessage(new Message("#02000000FF9C50000000000004CF\r"));
                System.out.println("....:::: Pacchetto inviato ::::....");
                Thread.sleep(50);
                sendMessage(new Message("#0200005000005000005000000491\r"));
                System.out.println("....:::: Pacchetto inviato ::::....");
                Thread.sleep(50);
                sendMessage(new Message("#0200000000645000000000000491\r"));
                System.out.println("....:::: Pacchetto inviato ::::....");
                Thread.sleep(50);
                sendMessage(new Message("#0200000000000003841400000496\r"));
                System.out.println("....:::: Pacchetto inviato ::::....");
                Thread.sleep(50);
                sendMessage(new Message("#020000000000008708500000049E\r"));
                System.out.println("....:::: Pacchetto inviato ::::....");
                Thread.sleep(50);
            }
        } catch (Exception e) {
        }

    }

    private void pitch() {
    }

    private void roll() {

    }

    private void yaw() {

        int speed = rand.nextInt(80) + 10;

        int position = rand.nextInt(3599) + 1;

        hex_speed = decToHex(speed);
        hex_position = decToHex(position);

        string_to_send = "#020000000000000" + hex_position + hex_speed + "0000049E\r";

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


    private String decToHex(int decimal) {
        return Integer.toHexString(decimal);
    }

}