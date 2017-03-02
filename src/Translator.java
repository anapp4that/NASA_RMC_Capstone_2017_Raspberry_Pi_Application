import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.BitSet;



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

		System.out.print(Arrays.toString(currentByteArray) + "\n");

//		//Compare bitArray's after translation.
//		List<byte> needsSending = new LinkedList<byte>();
//		for (int i = 0; i < NUM_BYTES_TO_BE_SENT; i++) {
//			if (currentByteArray[i] != previousSend[i]) {
//				needsSending.add(currentByteArray[i]);
//			}
//		}
//
//		if (needsSending.size() <= 0) {
//			return;
//		}
//
//
//		//Send the byteArray after we have verified if things are different or not.
//		byte[] sendingArray = new byte[needsSending.size()];
//		for (int i = 0; i < needsSending.size(); i++)
//			sendingArray[i] = needsSending.get(i);
//		previousSend = currentByteArray;
	}

	private byte[] translateArray(BitSet incBitArray) {
		//BitSet leftArray = incBitArray.get(0, 3 + 1);
		//BitSet rightArray = incBitArray.get(4, 7 + 1);
		//BitSet armArray = incBitArray.get(8, 9 + 1);

		byte[] translatedByteArray = new byte[NUM_BYTES_TO_BE_SENT];

		//translate here
		//translatedByteArray index meanings:
		//index 0: front left motor  3SpeedBitsDirection000
		//index 1: back left motor   3SpeedBitsDirection001
		//index 2: front right motor 3SpeedBitsDirection010
		//index 3: back right motor  3SpeedBitsDirection011
		//index 4: excavation motor  3SpeedBitsDirection100
		//bytes are in little endian interpretation
		//each sent bit will only be 7 data bits. because that's all we need.
		//ANY FUTURE THINGS NEEDED TO BE DONE WILL HAVE TO BE APPENDED TO THIS BYTE ARRAY
		//These are all subject to change once the EE's complete their design


		//Index 0
		BitSet translationBitSet = new BitSet(8);
		translationBitSet.set(0, 2 + 1, false);
		translationBitSet.set(3, incBitArray.get(0));
		translationBitSet.set(4, incBitArray.get(1));
		translationBitSet.set(5, incBitArray.get(2));
		translationBitSet.set(6, incBitArray.get(3));
		if (translationBitSet.toByteArray().length > 0)
			translatedByteArray[0] = translationBitSet.toByteArray()[0];

		//index 1
		translationBitSet.set(2, true);
		if (translationBitSet.toByteArray().length > 0)
			translatedByteArray[1] = translationBitSet.toByteArray()[0];

		//Index 2
		translationBitSet.set(2, false);
		translationBitSet.set(1, true);
		translationBitSet.set(3, incBitArray.get(4));
		translationBitSet.set(4, incBitArray.get(5));
		translationBitSet.set(5, incBitArray.get(6));
		translationBitSet.set(6, incBitArray.get(7));
		if (translationBitSet.toByteArray().length > 0)
			translatedByteArray[2] = translationBitSet.toByteArray()[0];

		//Index 3
		translationBitSet.set(2, true);
		if (translationBitSet.toByteArray().length > 0)
			translatedByteArray[3] = translationBitSet.toByteArray()[0];

		//Index 4
		translationBitSet.set(1, 2 + 1, false);
		translationBitSet.set(0, true);
		translationBitSet.set(3, incBitArray.get(8));
		translationBitSet.set(4, 6 + 1, true);
		if (translationBitSet.toByteArray().length > 0)
			translatedByteArray[4] = translationBitSet.toByteArray()[0];
		// PROBABLY SHOULD ASK EE's IF THIS IS AN OKAY SPEED. MOST LIKELY ISN'T BUT IT's TEMPORARY

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
