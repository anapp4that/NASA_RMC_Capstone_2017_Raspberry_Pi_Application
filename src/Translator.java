import java.util.BitSet;
import java.io.IOException;
import java.util.Date;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinDirection;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.trigger.GpioCallbackTrigger;
import com.pi4j.io.gpio.trigger.GpioPulseStateTrigger;
import com.pi4j.io.gpio.trigger.GpioSetStateTrigger;
import com.pi4j.io.gpio.trigger.GpioSyncStateTrigger;
import com.pi4j.io.gpio.event.GpioPinListener;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.gpio.event.PinEventType;

import com.pi4j.io.serial.*;
import com.pi4j.util.CommandArgumentParser;
import com.pi4j.util.Console;

public class Translator{
	

	private BitSet bitArray;
	final Serial serial;
	
	public Translator()
	{

		bitArray = new BitSet(9);
		serial = SerialFactory.createInstance();
		

	}

	private static BitSet fromString(final String s) {
		return BitSet.valueOf(new long[]{Long.parseLong(s, 2)});
	}
	
	public void setArray(BitSet incbitArray)
	{
		//compare bitArrays here?
		
		bitArray = incbitArray;
		translateArray();
	}
	
	private void translateArray()
	{
		BitSet armArray = bitArray.get(0, 1);
		BitSet leftArray = bitArray.get(1, 5);
		BitSet rightArray = bitArray.get(5, 9);


		//translate here
		
		
		
		//end translation
		
		//pins 15 and 16 are the UART pins (GPIO 15 and 16)
		
		
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
