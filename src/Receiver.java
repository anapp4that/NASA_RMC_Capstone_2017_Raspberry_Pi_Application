import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.BitSet;

public class Receiver extends Thread {

    public static final int SERVER_PORT = 6001;
    public static final String SERVER_IP_ADDRESS = "uaf135300.ddns.uark.edu";
    Socket raspberryPi;
    Translator translator;

    public Receiver() {
        try {
            raspberryPi = new Socket(SERVER_IP_ADDRESS, SERVER_PORT);
            DataOutputStream out = new DataOutputStream(raspberryPi.getOutputStream());
            PrintWriter printWriter = new PrintWriter(out, true);
            out.writeUTF("rasp");
            raspberryPi.setKeepAlive(true);
            //translator = new Translator();
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

                byte[] transmission = new byte[3];
                in.read(transmission);
                //if (length > 0) {
                            BitSet bitArray = BitSet.valueOf(transmission);
                System.out.print(bitArray.toString());
                // translator.sendArray(bitArray);
                //} else {
                // translator.sendArray(new BitSet(9));
                //}
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
