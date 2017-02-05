import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.BitSet;

public class Receiver extends Thread {

    Socket raspberryPi;

    public Receiver(String serverName, int port) {
        try {
            System.out.print("Connecting to " + serverName + " on port " + port + "\n");
            raspberryPi = new Socket(serverName, port);
            raspberryPi.setKeepAlive(true);
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
                    if (length >= 0 && length < 20000) {
                        byte[] transmission = new byte[length];
                        in.readFully(transmission, 0, transmission.length);
                        BitSet bitArray = BitSet.valueOf(transmission);
                        System.out.print("ByteArray = " + bitArray.toString() + "\n");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
