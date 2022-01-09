import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;

import javax.sound.midi.MidiUnavailableException;

public class SerialPortDataListenerIMPL_TEST
{
    Logger log = LogManager.getLogger(this.getClass());
    @Test
    void processMidiInputDataTEST()
    {
        org.apache.log4j.BasicConfigurator.configure();
        SerialPortDataListenerIMPL spdlTestObject = null;
        try{
            spdlTestObject = new SerialPortDataListenerIMPL();
        } catch (MidiUnavailableException e)
        {
            log.error("Unavailable to test, because MIDI unavailable");
        }
        byte[] testDataBank =
                {
                        (byte)0b10010000, (byte)0b01000000, (byte)0b00111001, //Note 64 on
                        (byte)0b01000001, (byte)0b01000101,                   //Note 65 on
                        (byte)0b01000000, (byte)0b00000000,                   //Note 64 off
                        (byte)0b01000001, (byte)0b00000000,                    //Note 65 off
                        (byte)0b10010000, (byte)0b01000000, (byte)0b00111001, //Note 64 on
                        (byte)0b01000001, (byte)0b01000101,                   //Note 65 on
                        (byte)0b01000000, (byte)0b00000000,                   //Note 64 off
                        (byte)0b01000001, (byte)0b00000000,                    //Note 65 off
                        (byte)0b10010000, (byte)0b01000000, (byte)0b00111001, //Note 64 on
                        (byte)0b01000001, (byte)0b01000101,                   //Note 65 on
                        (byte)0b01000000, (byte)0b00000000,                   //Note 64 off
                        (byte)0b01000001, (byte)0b00000000,                    //Note 65 off
                        (byte)0b10010000, (byte)0b01000000, (byte)0b00111001, //Note 64 on
                        (byte)0b01000001, (byte)0b01000101,                   //Note 65 on
                        (byte)0b01000000, (byte)0b00000000,                   //Note 64 off
                        (byte)0b01000001, (byte)0b00000000                    //Note 65 off
                };

        assert spdlTestObject != null;
        spdlTestObject.processMidiInputData(testDataBank);
    }
}
