import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import org.apache.commons.cli.*;

import javax.sound.midi.MidiUnavailableException;
import java.text.ParseException;


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
        Logger log = new Logger(MidiViaUart.class);
        Logger.setLogLevel(4);

        Options options = new Options();

        Option port = new Option("p", "port", true, "UART port name (Linux users tip: not /dev/* path, just name)");
        port.setRequired(true);
        options.addOption(port);

        Option baudArg = new Option("b", "baud", true, "Baud (115200 default)");
        baudArg.setRequired(false);
        options.addOption(baudArg);

        Option logLevel = new Option("l", "logLevel", true, "Sets log level (0 - 4)");
        logLevel.setRequired(false);
        options.addOption(logLevel);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;//not a good practice, it serves it purpose

        try {
            cmd = parser.parse(options, args);
        } catch (org.apache.commons.cli.ParseException e) {
            log.logError(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }

        String UARTportName = cmd.getOptionValue("port");
        int baud;
        try
        {
            baud = Integer.parseInt(cmd.getOptionValue("baud"));
        } catch (NumberFormatException e)
        {
            baud = 115200;
        }

        try
        {
            Logger.setLogLevel(Integer.parseInt(cmd.getOptionValue("logLevel")));
        } catch (NumberFormatException e)
        {
            Logger.setLogLevel(0);
        }

        log.logMessage("Starting with:\nBAUD: " + baud + "\n" + "Port" + ": " + UARTportName + "\nLog level: " + Logger.getLogLevel());

        MidiViaUart midiViaUart = new MidiViaUart(UARTportName, baud);

        for (; ; ) Thread.sleep(1);
    }
}
