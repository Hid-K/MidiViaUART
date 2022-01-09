# MidiViaUart
This software provides data to virtual MIDI port from UART.
## Usage:
`java -jar MidiViaUART-1.0.jar -p [Midi port name]`

Currently not available to directly connect midi device to UART (because of used libraries limitation), so you need to use adapter, made from ArduinoMega, which repeats signal with adaptive BAUD (115200).
```C
#define MIDI_SERIAL Serial1
#define MIDI_BAUD 31250

void setup() {
  Serial.begin(115200);
  MIDI_SERIAL.begin(MIDI_BAUD);
}

void loop() {}

void serialEvent1()
{
  Serial.write((uint8_t)MIDI_SERIAL.read());
}
```

If you're using an Arduino adapter - you may need to amplify signal from tour device by transistor.
<img width="441" alt="MIDI signal amplifier" src="https://user-images.githubusercontent.com/61284529/147980482-7774353a-6512-4d1f-9d9c-f9169e0924d0.png">

## Debug
U can enable debug messages with `-l`/`--logLevel` option:
* OFF (most specific, no logging)
* FATAL (most specific, little data)
* ERROR
* WARN
* INFO
* DEBUG
* TRACE (least specific, a lot of data)
* ALL (least specific, all data)

More info about debug levels on [Log4j documentation website](https://logging.apache.org/log4j/2.x/log4j-api/apidocs/org/apache/logging/log4j/Level.html)

U may get more usage info from `--help`
