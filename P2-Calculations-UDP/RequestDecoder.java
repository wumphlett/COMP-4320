import java.io.*;
import java.net.*;

public class RequestDecoder {
    public Request decode(InputStream wire) throws IOException {
        DataInputStream src = new DataInputStream(wire);
        byte tml = src.readByte();
        byte id = src.readByte();
        byte opCode = src.readByte();
        byte numberOfOperands = src.readByte();
        short operandOne = src.readShort();

        if (tml == 6) {
            return new Request(id, opCode, operandOne);
        } else {
            return new Request(id, opCode, operandOne, src.readShort());
        }
    }

    public Request decode(DatagramPacket p) throws IOException {
        return decode(new ByteArrayInputStream(p.getData(), p.getOffset(), p.getLength()));
    }
}
