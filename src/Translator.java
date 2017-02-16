
import java.util.BitSet;

public class Translator extends Thread{
	
	BitSet bitArray;
	
	public Translator(BitSet incbitArray)
	{
		bitArray = incbitArray;
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
	
	private static BitSet fromString(final String s) {
        return BitSet.valueOf(new long[] { Long.parseLong(s, 2) });
    }
	
	public static void main(String args[])
	{
		//check here if new bitset is different from old bitset?
		//not sure if faster to convert to bitset then compare or
		//just compare strings then convert if different
		
		Thread translatorThread = new Translator(fromString(args[0]));
		translatorThread.start();
	}

	
}
