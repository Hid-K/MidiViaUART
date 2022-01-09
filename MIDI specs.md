# MIDI Specification

# Byte types

### Byte structure

| Bit 0 | Bit 1 | Bit 2 | Bit 3 | Bit 4 | Bit 5 | Bit 6 | Bit 7 |
| ----- | ----- | ----- | ----- | ----- | ----- | ----- | ----- |
| LSB   | X     | X     | X     | X     | X     | X     | MSB   |
|       |       |       |       |       |       |       |       |

## Status byte

Status byte are different from Data byte by LSB: 

* 1 - means Status
* 0 - means Data

### Status byte structure:

| 1    | S    | S    | S    | C    | C    | C    | C    |
| ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- |

#### S - status bit

#### C - chanel № bit

MIDI has 16 channels (4 bits of channel number gives 16 combinations): 0 - 15

## Status byte status values

| Name              | Status number (SSS value) | Data byte 0 meaning | Data byte 1 meaning                   |
| ----------------- | ------------------------- | ------------------- | ------------------------------------- |
| Note on           | 0x9n                      | Note pitch          | Velocity<br />(0 - means note off)    |
| Note off          | 0x8n                      | Note pitch          | Velocity<br />(note release velocity) |
| Pitch controll    | 0xEn                      | Pitch low 7 bits    | Pitch high 7 bits                     |
| Controll change   | 0xBn                      | Control lnumber     | Controll value                        |
| Instrument select | 0xCn                      | Instrument number   | Missed                                |

Note: n - channel number (0 - F)

## MIDI Controlls

Midi has 9 nostly used controlls:

| Controll name                 | Controll number | Description                                                  |
| ----------------------------- | --------------- | ------------------------------------------------------------ |
| Sound bank selection MSB      | 0               |                                                              |
| Sound bank selection LSB      | 32              |                                                              |
| Modulation (Vibrato, Tremolo) | 1               | Sets vibrato value                                           |
| Volume                        | 7               | Sets volume level                                            |
| Panoramic                     | 10              | Sets position of sound source to listener                    |
| Expression                    | 11              | Like high resolution volume controll. It can be 7 or 14(controll #43 used as MSB) bit resolution<br />$real \space volume = volume*(2^p)/Expression$<br />p = 14 or 7 (bits) |
| Sustain                       | 64              | Controlls sustain<br />0 - no sustain<br />> 0 - sustain on  |
| All controllers off           | 121             | Clears all controllers values (sets to default values)       |
| All notes off                 | 123             | Sets all notes to off                                        |

