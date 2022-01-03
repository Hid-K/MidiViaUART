import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import javax.comm.NoSuchPortException;
import javax.comm.UnsupportedCommOperationException;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.io.*;
import java.util.TooManyListenersException;


public class MidiViaUart
{
    public MidiViaUart(String UARTportName) throws InvalidMidiDataException, MidiUnavailableException
    {
        SerialPort port = initSerialPort(UARTportName);

        if(port == null)
        {
            System.err.println("Error during port opening!\nClosing...");
            System.exit(-1);
        };

        port.setComPortParameters(
                115200,
                8,
                SerialPort.ONE_STOP_BIT,
                SerialPort.NO_PARITY);

        SerialPortDataListener dataListener = new SerialPortDataListener()
        {
            MidiEmulator midiEmulator = new MidiEmulator();

            @Override
            public int getListeningEvents()
            {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            byte lastCommand = 0;

            @Override
            public void serialEvent(SerialPortEvent event)
            {
                byte[] data = new byte[event.getSerialPort().bytesAvailable()];
                int numRead = event.getSerialPort().readBytes(data, data.length);

                if(numRead > 0)
                {
                    if(data.length == 3)
                    {
                        if((data[0] & 0xF0) == 0b10010000)
                        {
                            if(data[2] != 0)
                                midiEmulator.noteOn(data[0] & 0x0F, data[1], data[2]);
                            else
                                midiEmulator.noteOff(data[0] & 0x0F, data[1], 127);

                            lastCommand = data[0];

                            System.out.println("Note " + data[1] + " on");
                        }else if((data[0] & 0xF0) == 0b10000000)
                        {
                            midiEmulator.noteOff(data[0] & 0x0F, data[1], data[0]);

                            lastCommand = data[0];

                            System.out.println("Note " + data[1] + " off");
                        }
                    } else if(data.length == 2)
                    {
                        if((lastCommand & 0xF0) == 0b10010000)
                        {
                            if(data[1] != 0)
                                midiEmulator.noteOn(lastCommand & 0x0F, data[0], data[1]);
                            else
                                midiEmulator.noteOff(lastCommand & 0x0F, data[0], 127);

                            System.out.println("Note " + data[0] + " on");
                        }else if((lastCommand & 0xF0) == 0b10000000)
                        {
                            midiEmulator.noteOff(lastCommand & 0x0F, data[0], data[1]);

                            System.out.println("Note " + data[0] + " off");
                        }
                    }
                }
                else
                    System.err.println("Error during data reading!");
            }
        };

        port.addDataListener(dataListener);

        System.out.println("Starting listening to data:");
        if(!port.openPort()) System.err.println("Error opening port!");
    }

    static SerialPort initSerialPort(String UARTportName)
    {
        SerialPort[] ports = SerialPort.getCommPorts();

        for (SerialPort port: ports)
        {
            System.out.println(port.getSystemPortName());
            if(port.getSystemPortName().equals(UARTportName))
            {
                System.out.println("Found port's system entity.");
                return port;
            };
        }

        return null;
    };

    public static void main(String[] args) throws IOException, UnsupportedCommOperationException, TooManyListenersException, NoSuchPortException, InterruptedException, MidiUnavailableException, InvalidMidiDataException
    {
        String UARTportName = "cu.usbserial-1410";

        MidiViaUart midiViaUart = new MidiViaUart(UARTportName);

        for(;;)Thread.sleep(1);
    }
}
