/*
 * ported to v0.36
 * using automatic conversion tool v0.08
 *
 *
 *
 */ 
package sndhrdw;

import static mame.driverH.*;
import static sound.samplesH.*;
import static sound.samples.*;

public class congo
{
	
	
	
	public static WriteHandlerPtr congo_daio = new WriteHandlerPtr() { public void handler(int offset, int data)
	{
		if (offset == 1)
		{
			if ((data & 2) != 0) sample_start(0,0,0);
		}
		else if (offset == 2)
		{
			data ^= 0xff;
	
			if ((data & 0x80) != 0)
			{
				if ((data & 8) != 0) sample_start(1,1,0);
				if ((data & 4) != 0) sample_start(2,2,0);
				if ((data & 2) != 0) sample_start(3,3,0);
				if ((data & 1) != 0) sample_start(4,4,0);
			}
		}
	} };
}
