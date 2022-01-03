import javax.sound.midi.*;
import java.util.List;

public class MidiEmulator
{
    //"Virtual MIDI unit",
    //"Hid-K",
    //"Provided by MidiViaUart [GitHub: ]",
    //getClass().getPackage().getImplementationVersion()

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

            System.out.println("All notes off on channel " + i);
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
        } catch (Exception ignored){};
    }

    void noteOff(int channel, int noteNo, int velocity)
    {
        ShortMessage myMsg = new ShortMessage();

        try
        {
            myMsg.setMessage(ShortMessage.NOTE_OFF, channel, noteNo, velocity);
            long timeStamp = -1;
            midiReceiver.send(myMsg, timeStamp);
        } catch (Exception ignored){};
    }

    void sendPitch(int channel, short pitch) throws InvalidMidiDataException
    {
        ShortMessage myMsg = new ShortMessage();

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
    };
}