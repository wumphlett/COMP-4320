import java.io.*;
import java.net.*;

public class ResponseDecoder {
    public Response decode(InputStream wire) throws IOException {
        DataInputStream src = new DataInputStream(wire);
        byte tml = src.readByte();
        byte id = src.readByte();
        byte errorCode = src.readByte();
        int result = src.readInt();

        return new Response(id, errorCode, result);
    }

    public Response decode(DatagramPacket p) throws IOException {
        return decode(new ByteArrayInputStream(p.getData(), p.getOffset(), p.getLength()));
    }
}
