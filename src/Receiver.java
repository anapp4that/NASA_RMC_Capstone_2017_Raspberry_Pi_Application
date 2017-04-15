import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.BitSet;

public class Receiver extends Thread {

    public static final int SERVER_PORT_1 = 6001;
    public static final int SERVER_PORT_2 = 6000;
    public static final String SERVER_IP_ADDRESS = "uaf135300.ddns.uark.edu";
    Socket raspberryPi;
    Translator translator;

    public Receiver() {
        try {
            raspberryPi = new Socket(SERVER_IP_ADDRESS, SERVER_PORT_1);
            DataOutputStream out = new DataOutputStream(raspberryPi.getOutputStream());
            out.writeUTF("rasp");
            raspberryPi.setKeepAlive(true);
            translator = new Translator();
        } catch (ConnectException cex) {
            try {
                raspberryPi = new Socket(SERVER_IP_ADDRESS, SERVER_PORT_2);
                DataOutputStream out = new DataOutputStream(raspberryPi.getOutputStream());
                out.writeUTF("rasp");
                raspberryPi.setKeepAlive(true);
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.print("Server is not online.");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        Thread receiverThread = new Receiver();
        receiverThread.start();
    }

    public void run() {
        while (true) {
            try {
                DataInputStream in = new DataInputStream(raspberryPi.getInputStream());

                String transmission = in.readUTF();
                System.out.print(transmission);

                BitSet bitArray = new BitSet(22);

                for (int x = 21; x >= 0; x--) {
                    if (transmission.charAt(21 - x) == '1') {
                        bitArray.set(x);
                    }
                }

                //if (length > 0) {
                //BitSet bitArray = BitSet.valueOf(transmission);
                System.out.print(bitArray.toString() + "\n");

                translator.sendArray(bitArray);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
