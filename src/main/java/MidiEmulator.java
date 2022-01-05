import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import javax.sound.midi.*;
import java.util.List;

public class MidiEmulator
{
    Logger log = LogManager.getLogger(this.getClass());

    Receiver midiReceiver = MidiSystem.getReceiver();

    public MidiEmulator() throws MidiUnavailableException
    {
        for(int i = 0; i < 16; ++i)
        {
            for (int j = 0; j < 127; ++j)
            try
            {
                this.noteOff(i, j, 0);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    void noteOn(int channel, int noteNo, int velocity)
    {
        ShortMessage myMsg = new ShortMessage();

        try
        {
            myMsg.setMessage(ShortMessage.NOTE_ON, channel, noteNo, velocity);

            long timeStamp = -1;
            midiReceiver.send(myMsg, timeStamp);
        } catch (Exception e)
        {
            log.error(e.getMessage());
        }
    }

    void noteOff(int channel, int noteNo, int velocity)
    {
        ShortMessage myMsg = new ShortMessage();

        try
        {
            myMsg.setMessage(ShortMessage.NOTE_OFF, channel, noteNo, velocity);
            long timeStamp = -1;
            midiReceiver.send(myMsg, timeStamp);
        } catch (Exception e)
        {
            log.error(e.getMessage());
        }
    }

    void sendPitch(int channel, short pitch)
    {
        ShortMessage myMsg = new ShortMessage();

        try
        {
        myMsg.setMessage(ShortMessage.PITCH_BEND, channel, (byte)( (pitch>>7)  & 0b01111111), (byte)( (pitch>>1) & 0b01111111));
        //0LLLLLLL0MMMMMMM      \
        //LSB     MSB           / How it is must be
        //00XXXXXXXXXXXXXX Pitch value in short
        //00MMMMMMMLLLLLLL
        //00XXXXXXXXXXXXXX      = 000000000LLLLLLL
        //00XXXXXXXXXXXXXX >> 7 = 000000000MMMMMMM
        //000000000LLLLLLL cuts to 0LLLLLLL \
        //                                   | 0LLLLLLL0MMMMMMM
        //000000000MMMMMMM cuts to 0MMMMMMM /

        long timeStamp = -1;
        midiReceiver.send(myMsg, timeStamp);
        } catch (Exception e)
        {
            log.error(e.getMessage());
        }
    }

    void sendPitch(int channel, byte pitchL, byte pitchM)
    {
        ShortMessage myMsg = new ShortMessage();

        try
        {
            myMsg.setMessage(ShortMessage.PITCH_BEND, channel, (byte)( pitchL  & 0b01111111), (byte)( pitchM & 0b01111111));
            //0LLLLLLL0MMMMMMM      \
            //LSB     MSB           / How it is must be
            //00XXXXXXXXXXXXXX Pitch value in short
            //00MMMMMMMLLLLLLL
            //00XXXXXXXXXXXXXX      = 000000000LLLLLLL
            //00XXXXXXXXXXXXXX >> 7 = 000000000MMMMMMM
            //000000000LLLLLLL cuts to 0LLLLLLL \
            //                                   | 0LLLLLLL0MMMMMMM
            //000000000MMMMMMM cuts to 0MMMMMMM /

            long timeStamp = -1;
            midiReceiver.send(myMsg, timeStamp);
        } catch (Exception e)
        {
            log.error(e.getMessage());
        }
    }

    void sendVolumeLevel(int channel, byte volumeLevel)
    {
        ShortMessage myMsg = new ShortMessage();

        try
        {
            myMsg.setMessage(ShortMessage.CONTROL_CHANGE, channel, 7, volumeLevel);

            long timeStamp = -1;
            midiReceiver.send(myMsg, timeStamp);
        } catch (Exception e)
        {
            log.error(e.getMessage());
        }
    }

    void sendControlsChangeCommand(int channel, byte controlNo, byte data)
    {
        ShortMessage myMsg = new ShortMessage();

        try
        {
            myMsg.setMessage(ShortMessage.CONTROL_CHANGE, channel, controlNo, data);

            long timeStamp = -1;
            midiReceiver.send(myMsg, timeStamp);
        } catch (Exception e)
        {
            log.error(e.getMessage());
        }
    };
}
