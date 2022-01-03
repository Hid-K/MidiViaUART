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

If you're using an Arduino adapter - you may need to aplify signal from tour device by transistor.
<img width="441" alt="Снимок экрана 2022-01-04 в 00 04 43" src="https://user-images.githubusercontent.com/61284529/147980482-7774353a-6512-4d1f-9d9c-f9169e0924d0.png">
