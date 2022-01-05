import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import javax.sound.midi.MidiUnavailableException;


public class MidiViaUart
{
    Logger log = new Logger(MidiViaUart.class);

    public MidiViaUart(String UARTportName, int baud) throws MidiUnavailableException
    {
        SerialPort port = initSerialPort(UARTportName);

        if (port == null)
        {
            log.logError("Error during port opening!\nClosing...");
            System.exit(-1);
        }

        port.setComPortParameters(
                baud,
                8,
                SerialPort.ONE_STOP_BIT,
                SerialPort.NO_PARITY);

        SerialPortDataListener dataListener = new SerialPortDataListenerIMPL();

        port.addDataListener(dataListener);

        log.logMessage("Starting listening to data:");
        if (!port.openPort()) System.err.println("Error opening port!");
    }

    SerialPort initSerialPort(String UARTportName)
    {
        SerialPort[] ports = SerialPort.getCommPorts();

        for (SerialPort port : ports)
        {
            if (port.getSystemPortName().equals(UARTportName))
            {
                log.logMessage("Found port's system entity.");
                return port;
            }
        }

        return null;
    }

    public static void main(String[] args) throws InterruptedException, MidiUnavailableException
    {
        String UARTportName = "cu.usbserial-1410";
        int baud = 115200;
        Logger.setLogLevel(0);

        MidiViaUart midiViaUart = new MidiViaUart(UARTportName, baud);

        for (; ; ) Thread.sleep(1);
    }
}
