import java.io.*;

public class ResponseDecoder {
    public Response decode(InputStream wire) throws IOException {
        DataInputStream src = new DataInputStream(wire);
        byte tml = src.readByte();
        byte id = src.readByte();
        byte errorCode = src.readByte();
        int result = src.readInt();

        return new Response(id, errorCode, result);
    }

    public Response decode(byte[] b) throws IOException {
        return decode(new ByteArrayInputStream(b, 0, b.length));
    }
}
