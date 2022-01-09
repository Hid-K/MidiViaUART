import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import javax.sound.midi.MidiUnavailableException;
import java.util.Arrays;

public class SerialPortDataListenerIMPL implements SerialPortDataListener
{
    Logger log = LogManager.getLogger(this.getClass());

    MidiEmulator midiEmulator = new MidiEmulator();

    byte lastCommand = 0;

    public static class MIDIStatusByteUtils
    {
        public static final byte DATA_MASK = (byte) 0b01111111;

        public static final byte STATUS_MASK = (byte) 0xF0;
        public static final byte CHANNEL_MASK = 0x0F;

        public static final byte STATUS_NOTE_ON = (byte) 0b10010000;
        public static final byte STATUS_NOTE_OFF = (byte) 0b10000000;

        public static final byte STATUS_PITCH_SET = (byte) 0b11100000;
        public static final byte STATUS_CONTROL_CHANGE = (byte) 0b10110000;
    }

    public SerialPortDataListenerIMPL() throws MidiUnavailableException
    {

    }

    @Override
    public int getListeningEvents()
    {
        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
    }

    @Override
    public void serialEvent(SerialPortEvent event)
    {
        byte[] data = new byte[event.getSerialPort().bytesAvailable()];
        int numRead = event.getSerialPort().readBytes(data, data.length);

        if (numRead > 0)
        {
            processMidiInputData(data);

            if (log.getEffectiveLevel().isGreaterOrEqual(Level.DEBUG))
            {
                StringBuilder dataInBinary = new StringBuilder();

                for (byte b : data)
                {
                    dataInBinary.append(Integer.toBinaryString(b & 255 | 256).substring(1) + "\t");
                }

                log.debug(dataInBinary.toString());
            }
        } else
            log.error("Error during data reading!");
    }

    private int onMIDIDataWithStatusByteLead(byte[] data)
    {
        if(data.length >= 3){
            if ((data[0] & MIDIStatusByteUtils.STATUS_MASK) == MIDIStatusByteUtils.STATUS_NOTE_ON)
            {
                midiEmulator.noteOn(data[0] & MIDIStatusByteUtils.CHANNEL_MASK, data[1], data[2]);

                lastCommand = data[0];

                if (data[2] > 0)
                    log.log(Level.toLevel(Priority.INFO_INT), "Note " + data[1] + " on");
                else log.log(Level.toLevel(Priority.INFO_INT), "Note " + data[1] + " off");

                return 3;
            } else if ((data[0] & MIDIStatusByteUtils.STATUS_MASK) == MIDIStatusByteUtils.STATUS_NOTE_OFF)
            {
                midiEmulator.noteOff(data[0] & MIDIStatusByteUtils.CHANNEL_MASK, data[1], data[0]);

                lastCommand = data[0];

                log.log(Level.toLevel(Priority.INFO_INT), "Note " + data[1] + " off");

                return 3;
            } else if ((data[0] & MIDIStatusByteUtils.STATUS_MASK) == MIDIStatusByteUtils.STATUS_PITCH_SET)
            {
                midiEmulator.sendPitch(data[0] & MIDIStatusByteUtils.CHANNEL_MASK, data[1], data[2]);

                lastCommand = data[0];

                log.log(Level.toLevel(Priority.INFO_INT), "Pitch set to: " + data[1]);

                return 3;
            } else if ((data[0] & MIDIStatusByteUtils.STATUS_MASK) == MIDIStatusByteUtils.STATUS_CONTROL_CHANGE)
            {
                midiEmulator.sendControlsChangeCommand(data[0] & MIDIStatusByteUtils.CHANNEL_MASK, data[1], data[2]);

                lastCommand = data[0];

                log.log(Level.toLevel(Priority.INFO_INT), "Control #" + data[1] + " level set to: " + data[2]);

                return 3;
            }
        }
        return 100;
    }

    private int onMIDIDataWithDataByteLead(byte[] data)
    {
        if ((lastCommand & MIDIStatusByteUtils.STATUS_MASK) == MIDIStatusByteUtils.STATUS_NOTE_ON)
        {
            midiEmulator.noteOn(lastCommand & MIDIStatusByteUtils.CHANNEL_MASK, data[0], data[1]);

            if(data[1] > 0)
                log.log(Level.toLevel(Priority.INFO_INT),"Note " + data[0] + " on");
            else log.log(Level.toLevel(Priority.INFO_INT),"Note " + data[0] + " off");

            return 2;
        } else if ((lastCommand & MIDIStatusByteUtils.STATUS_MASK) == MIDIStatusByteUtils.STATUS_NOTE_OFF)
        {
            midiEmulator.noteOff(lastCommand & MIDIStatusByteUtils.CHANNEL_MASK, data[0], data[1]);

            log.log(Level.toLevel(Priority.INFO_INT),"Note " + data[0] + " off");

            return 2;
        } else if ((lastCommand & MIDIStatusByteUtils.STATUS_MASK) == MIDIStatusByteUtils.STATUS_PITCH_SET)
        {
            midiEmulator.sendPitch(lastCommand & MIDIStatusByteUtils.CHANNEL_MASK, data[0], data[1]);

            log.log(Level.toLevel(Priority.INFO_INT),"Pitch set to: " + (long) (data[0] | (data[1] << 7)));

            return 2;
        } else if ((lastCommand & MIDIStatusByteUtils.STATUS_MASK) == MIDIStatusByteUtils.STATUS_CONTROL_CHANGE)
        {
            midiEmulator.sendControlsChangeCommand(lastCommand & MIDIStatusByteUtils.CHANNEL_MASK, data[0], data[1]);

            log.log(Level.toLevel(Priority.INFO_INT),"Control #" + data[0] + " level set to: " + data[1]);

            return 2;
        }

        return 100;
    };

    public void processMidiInputData(byte[] data)
    {
        int bytesProceed;
        if ((data[0] & 0b10000000) != 0)
        {
            bytesProceed = onMIDIDataWithStatusByteLead(data);
        } else
        {
            bytesProceed = onMIDIDataWithDataByteLead(data);
        }
        if (bytesProceed < data.length)
        {
            processMidiInputData(Arrays.copyOfRange(data, bytesProceed, data.length));
        }
    };
}
