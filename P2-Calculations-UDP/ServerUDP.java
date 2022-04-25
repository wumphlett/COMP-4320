import java.net.*;
import java.io.*;

public class ServerUDP {
    public static void main(String[] args) throws Exception {

        if (args.length > 1) {
            throw new IllegalArgumentException("Usage: java serverUDP.java <port>");
        }
        
        int port = args.length == 1 ? Integer.parseInt(args[0]) : 10011;
        
        DatagramSocket sock = new DatagramSocket(port);    
        DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);

        RequestDecoder decoder = new RequestDecoder();
        ResponseEncoder encoder = new ResponseEncoder();

        while (true) {
            sock.receive(packet);
            Request receivedRequest = decoder.decode(packet);
            String requestHex = "";

            byte[] buffer = packet.getData();
            for (int i = 0; i < packet.getLength(); i++) {
                String datum = "0" + Integer.toHexString(buffer[i]);
                requestHex += "0x" + datum.substring(datum.length() - 2) + " ";
            }
            System.out.println(requestHex.trim());
            System.out.println(receivedRequest);

            byte errorCode = receivedRequest.tml == packet.getLength() ? (byte) 0 : (byte) 127;

            Response response = new Response(receivedRequest.id, errorCode, receivedRequest.compute());
            byte[] codedResponse = encoder.encode(response);
            packet.setData(codedResponse);
            sock.send(packet);
            packet = new DatagramPacket(new byte[1024], 1024);
        }
    }
}
