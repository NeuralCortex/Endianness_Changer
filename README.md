# Endianness Changer 1.0.0

![image](https://github.com/NeuralCortex/Endianness_Changer/blob/main/images/endian.png)

## Funktionsweise des Programms

Das JavaFX-Programm dient dazu Binärdateien von BigEndian zu LittleEndian oder umgekehrt zu konvertieren.</br>
Die Binärdateien müssen in folgenden Format vorliegen:

1. Kein Header
2. Kein Trailer
3. Kein Trenner

In vielen Programmiersprache spielt die Endianness keine Rolle, da schon von Hause aus Konverterfunktionen zur Verfügung gestellt werden.</br>
In Java zum Beispiel durch die Verwendung der Klasse "ByteBuffer".</br>
Hier ein paar Beispiele:

1. Für Short (16-Bit)
<pre>
    byte[] buffer = new byte[2];
    //buffer befüllen

    ByteBuffer buffer = ByteBuffer.wrap(buffer);
    short erg = buffer.order(ByteOrder.BIG_ENDIAN).getShort();
</pre>

2. Für Double (64-Bit)
<pre>
    byte[] buffer = new byte[8];
    //buffer befüllen

    ByteBuffer buffer = ByteBuffer.wrap(buffer);
    double erg = buffer.order(ByteOrder.LITTLE_ENDIAN).getDouble();
</pre>

In manchen Programmiersprachen ist das Einlesen von Dateien im Format "LittleEndian" nicht so leicht möglich.</br>
Für die Konvertierung der Daten kann dann dieses Programm genutzt werden.

## How the program works

The JavaFX program is used to convert binary files from BigEndian to LittleEndian or vice versa.</br>
The binary files must be in the following format:

1. No header
2. No trailer
3. No separator

In many programming languages, endianness does not play a role, since converter functions are already provided.</br>
In Java, for example, by using the "ByteBuffer" class.</br>
Here some examples:

1. For Short (16-bit)
<pre>
    byte[] buffer = new byte[2];
    //fill buffer

    ByteBuffer buffer = ByteBuffer.wrap(buffer);
    short erg = buffer.order(ByteOrder.BIG_ENDIAN).getShort();
</pre>

2. For Double (64-bit)
<pre>
    byte[] buffer = new byte[8];
    //fill buffer

    ByteBuffer buffer = ByteBuffer.wrap(buffer);
    double erg = buffer.order(ByteOrder.LITTLE_ENDIAN).getDouble();
</pre>

In some programming languages, reading files in the "LittleEndian" format is not so easy.</br>
This program can then be used to convert the data.

## Arbeitsschritte

1. Verzeichnis mit den Ausgangsdaten öffnen
2. Markieren der zu konvertierenden Dateien
3. Auswahl des Ergebnisses (Großbuchstaben oder Kleinbuchstaben)
4. Auswahl der Datentyp Größe (z.B.: 16-Bit oder 64-Bit)
5. Speichern der konvertierten Dateien unter ... (Es sollte nicht das selbe Verzeichnis verwendet werden)
6. Konvertierung abwarten - fertig ;-)

## Work steps

1. Open the directory with the input data
2. Mark the files to be converted
3. Selection of the result (uppercase or lowercase)
4. Selection of the data type size (e.g.: 16-bit or 64-bit)
5. Save the converted files to ... (The same directory should not be used)
6. Wait for conversion - done ;-)

## Verwendete Technologie

Dieses JavaFX-Projekt wurde erstellt mit der Apache NetBeans 17 IDE [NetBeans 17](https://netbeans.apache.org/).

Folgende Frameworks sollten installiert sein:

- JAVA-SDK [JAVA 19](https://www.oracle.com/java/technologies/javase/jdk19-archive-downloads.html)
- SceneBuilder für GUI-Entwicklung [Gluon SceneBuilder](https://gluonhq.com/products/scene-builder/)
- JAVA-FX-SDK [JavaFX](https://gluonhq.com/products/javafx/)

## Technology used

This JavaFX project was built with the Apache NetBeans 17 IDE [NetBeans 17](https://netbeans.apache.org/).

The following frameworks should be installed:

- JAVA SDK [JAVA 19](https://www.oracle.com/java/technologies/javase/jdk19-archive-downloads.html)
- SceneBuilder for GUI development [Gluon SceneBuilder](https://gluonhq.com/products/scene-builder/)
- JAVA FX SDK [JavaFX](https://gluonhq.com/products/javafx/)
