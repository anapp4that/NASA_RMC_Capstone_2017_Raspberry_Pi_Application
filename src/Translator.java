import com.pi4j.io.serial.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;


public class Translator{


	public static final int NUM_BYTES_TO_BE_SENT_X = 5;
	public static final int NUM_BYTES_TO_BE_SENT_Y = 3;
	final Serial serial;
	byte[][] previousSend;
	private BitSet bitArray;
	private SerialConfig config;
	
	public Translator()
	{

		bitArray = new BitSet(21);
		serial = SerialFactory.createInstance();
		previousSend = new byte[NUM_BYTES_TO_BE_SENT_X][NUM_BYTES_TO_BE_SENT_Y];
		serial.addListener(new SerialDataEventListener() {
			public void dataReceived(SerialDataEvent event) {
				try {
					System.out.print("[HEX DATA] " + event.getHexByteString());
					System.out.print("[ASCII DATA] " + event.getAsciiString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		try {
			config = new SerialConfig();
			config.device(SerialPort.getDefaultPort())
					.baud(Baud._9600)
					.dataBits(DataBits._7)
					.parity(Parity.NONE)
					.stopBits(StopBits._1)
					.flowControl(FlowControl.NONE);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	
	private static BitSet fromString(final String s) {
		return BitSet.valueOf(new long[]{Long.parseLong(s, 2)});
	}

	public void sendArray(BitSet incbitArray)
	{
		bitArray = incbitArray;
		byte[][] currentByteArray = translateArray(incbitArray);

		System.out.print(Arrays.toString(currentByteArray) + "\n");

		//Compare bitArray's after translation.
		List needsSending = new LinkedList();
		for (int i = 0; i < NUM_BYTES_TO_BE_SENT_X; i++) {
			if (!(Arrays.equals(currentByteArray[i], previousSend[i]))) {
				needsSending.add(currentByteArray[i]);
			}
		}

		if (needsSending.size() <= 0) {
			return;
		}


		//Send the byteArray after we have verified if things are different or not.
		byte[] sendingArray = new byte[needsSending.size()];
		for (int i = 0; i < needsSending.size(); i++)
			sendingArray[i] = (byte) needsSending.get(i);
		previousSend = currentByteArray;
		try {
			serial.open(config);
			serial.write(sendingArray);
			serial.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private byte[][] translateArray(BitSet incBitArray) {
		//BitSet leftArray = incBitArray.get(0, 3 + 1);
		//BitSet rightArray = incBitArray.get(4, 7 + 1);
		//BitSet armArray = incBitArray.get(8, 9 + 1);

		//incoming bits
		//0 is direction for left side, motors 0 and 1
		//1-8 left side speed, motors 0 and 1
		//9 is direction for right side, motors 2 and 3
		//10-17 right side speed, motors 2 and 3
		//18-19 drum control, motor 4
		//20-21 arm control
		
		byte[][] translatedByteArray = new byte[NUM_BYTES_TO_BE_SENT_X][NUM_BYTES_TO_BE_SENT_Y];

		BitSet translationBitSet;
		
		//x + y + (index % 8) allows the x position of translationBitSet
		//to increase to 2 and 3 even though x + y will only equal 1 and 2
		//when addressing the left side wheels, index will = 0, so the % op will = 0
		//when addressing the right side wheels, index will = 9 and the % op will = 1
		
		
		//set wheel motors
		for(int x = 0; x < 2; x++)
		{
			for(int y = 0; y < 2; y++)
			{
				translationBitSet = new BitSet(8);
				
				int index = x * 9;
				//set address
				translationBitSet.set(0, y == 1);
				translationBitSet.set(1, x == 1);
				if (translationBitSet.toByteArray().length > 0)
					translatedByteArray[x + y + (index % 8)][0] = translationBitSet.toByteArray()[0];
				
				//set direction
				translationBitSet.set(0, incBitArray.get(index));
				if (translationBitSet.toByteArray().length > 0)
					translatedByteArray[x + y + (index % 8)][1] = translationBitSet.toByteArray()[0];
				
				//set speed
				translationBitSet.set(0, incBitArray.get(index + 1));
				translationBitSet.set(1, incBitArray.get(index + 2));
				translationBitSet.set(2, incBitArray.get(index + 3));
				translationBitSet.set(3, incBitArray.get(index + 4));
				translationBitSet.set(4, incBitArray.get(index + 5));
				translationBitSet.set(5, incBitArray.get(index + 6));
				translationBitSet.set(6, incBitArray.get(index + 7));
				translationBitSet.set(7, incBitArray.get(index + 8));
				
				if (translationBitSet.toByteArray().length > 0)
					translatedByteArray[x + y + (index % 8)][2] = translationBitSet.toByteArray()[0];
			}
		}
		
		//translation should result in an array of bytes. return these bytes to the sending function.
		return translatedByteArray;
	}
	
	public void killTranslator() throws IllegalStateException,
    IOException
	{
		try
		{
			serial.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	
}
