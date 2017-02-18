
import java.util.BitSet;

public class Translator extends Thread{
	
	BitSet bitArray;
	
	public Translator(BitSet incbitArray)
	{
		bitArray = incbitArray;
	}

	private static BitSet fromString(final String s) {
		return BitSet.valueOf(new long[]{Long.parseLong(s, 2)});
	}
	
	public void run()
	{
		BitSet armArray = bitArray.get(0, 1);
		BitSet leftArray = bitArray.get(1, 5);
		BitSet rightArray = bitArray.get(5, 9);

		//lock rs485

		//transmit data

		//unlock rs485
	}

	
}
