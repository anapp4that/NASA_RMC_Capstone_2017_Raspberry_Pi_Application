import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialFactory;

import java.io.IOException;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;



public class Translator{


	public static final int NUM_BYTES_TO_BE_SENT = 5;
	final Serial serial;
	byte[] previousSend;
	private BitSet bitArray;
	
	public Translator()
	{

		bitArray = new BitSet(9);
		serial = SerialFactory.createInstance();
		previousSend = new byte[NUM_BYTES_TO_BE_SENT];

	}

	private static BitSet fromString(final String s) {
		return BitSet.valueOf(new long[]{Long.parseLong(s, 2)});
	}

	public void sendArray(BitSet incbitArray)
	{
		bitArray = incbitArray;
		byte[] currentByteArray = translateArray(incbitArray);


		//Compare bitArray's after translation.
		List<byte> needsSending = new LinkedList<byte>();
		for (int i = 0; i < NUM_BYTES_TO_BE_SENT; i++) {
			if (currentByteArray[i] != previousSend[i]) {
				needsSending.add(currentByteArray[i]);
			}
		}

		if (needsSending.size() <= 0) {
			return;
		}


		//Send the byteArray after we have verified if things are different or not.
		byte[] sendingArray = new byte[needsSending.size()];
		for (int i = 0; i < needsSending.size(); i++)
			sendingArray[i] = needsSending.get(i);
		previousSend = currentByteArray;
	}

	private byte[] translateArray(BitSet incBitArray)
	{
		BitSet leftArray = incBitArray.get(0, 3 + 1);
		BitSet rightArray = incBitArray.get(4, 7 + 1);
		BitSet armArray = incBitArray.get(8, 9 + 1);

		byte[] translatedByteArray = new byte[NUM_BYTES_TO_BE_SENT];

		//translate here
		//translatedByteArray index meanings:
		//index 0: front left motor  000Direction3SpeedBits
		//index 1: back left motor   001Direction3SpeedBits
		//index 2: front right motor 010Direction3SpeedBits
		//index 3: back right motor  011Direction3SpeedBits
		//index 4: excavation motor  100Direction3SpeedBits
		//ANY FUTURE THINGS NEEDED TO BE DONE WILL HAVE TO BE APPENDED TO THIS BYTE ARRAY
		//These are all subject to change once the EE's complete their design


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
