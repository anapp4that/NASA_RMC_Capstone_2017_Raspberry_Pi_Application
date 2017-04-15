import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.BitSet;

public class Receiver {

    public static final int SERVER_PORT = 6001;
    public static final String SERVER_IP_ADDRESS = "uaf135300.ddns.uark.edu";
    Socket raspberryPi;
    Translator translator;

    public Receiver() throws IOException {
        translator = new Translator();
        while (true) {
            try {
                raspberryPi = new Socket(SERVER_IP_ADDRESS, SERVER_PORT);
            } catch (ConnectException cex) {
                continue;
            }
            break;
        }
        DataOutputStream out = new DataOutputStream(raspberryPi.getOutputStream());
        out.writeUTF("rasp");
        raspberryPi.setKeepAlive(true);
    }

    public static void main(String args[]) {
        try {
            Receiver receiver = new Receiver();
            receiver.run();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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
