# MidiViaUart
This software provides data to virtual MIDI port from UART.
Currently not available to directly connect midi device to UART, so you need to use adapter, made from ArduinoMega, which repeats signal with adaptive BAUD (115200).
```C
#define MIDI_SERIAL Serial1
#define MIDI_BAUD 31250

void setup() {
  Serial.begin(115200);
  MIDI_SERIAL.begin(MIDI_BAUD);
  pinMode(13, OUTPUT);
}

void loop() {

}

void serialEvent1()
{
  digitalWrite(13, HIGH);
  Serial.write((uint8_t)MIDI_SERIAL.read());
//  delay(10);
  digitalWrite(13, LOW);
}
```
