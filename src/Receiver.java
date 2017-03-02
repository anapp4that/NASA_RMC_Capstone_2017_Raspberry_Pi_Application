import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.BitSet;

public class Receiver extends Thread {

    Socket raspberryPi;
    Translator translator;

    public Receiver(String serverName, int port) {
        try {
            System.out.print("Connecting to " + serverName + " on port " + port + "\n");
            raspberryPi = new Socket(serverName, port);
            raspberryPi.setKeepAlive(true);
            translator = new Translator();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        String serverName = args[0];
        int port = Integer.parseInt(args[1]);
        Thread receiverThread = new Receiver(serverName, port);
        receiverThread.start();
    }

    public void run() {
        while (true) {
            try {
                DataInputStream in = new DataInputStream(raspberryPi.getInputStream());
                while (true) {
                    int length = in.readInt();
                    if (length >= 0 && length < 3) {
                        byte[] transmission = new byte[length];
                        in.readFully(transmission, 0, transmission.length);
                        if (length > 0) {
                            BitSet bitArray = BitSet.valueOf(transmission);
                            translator.sendArray(bitArray);
                        } else {
                            translator.sendArray(new BitSet(9));
                        }
                    } else {
                        Thread.sleep(200);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
