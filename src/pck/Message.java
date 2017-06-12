package pck;

/**
 * Messaggi inviati nella rete
 */
public class Message {

    private byte[] bytes = new byte[1024];

    public Message(String string){

        bytes = string.getBytes();

    }

    public byte[] getBytes(){

        return bytes;
    }
}
