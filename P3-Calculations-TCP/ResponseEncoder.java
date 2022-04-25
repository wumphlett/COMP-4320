import java.io.*;

public class ResponseEncoder {
    public byte[] encode(Response response) throws Exception {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(buf);

        out.writeByte(response.tml);
        out.writeByte(response.id);
        out.writeByte(response.errorCode);
        out.writeInt(response.result);

        out.flush();
        return buf.toByteArray();
    }
}
