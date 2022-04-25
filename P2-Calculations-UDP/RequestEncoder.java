import java.io.*;

public class RequestEncoder {
    public byte[] encode(Request request) throws Exception {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(buf);
        
        out.writeByte(request.tml);
        out.writeByte(request.id);
        out.writeByte(request.opCode);
        out.writeByte(request.numberOfOperands);
        out.writeShort(request.operandOne);
        if (request.operandTwo != null) {
            out.writeShort(request.operandTwo);
        }

        out.flush();
        return buf.toByteArray();
    }
}
