import java.io.*;   // for Input/OutputStream
import java.net.*;  // for Socket and ServerSocket

public class ServerTCP {
    public static void main(String args[]) throws Exception {
        
        if (args.length > 1) {
            throw new IllegalArgumentException("Usage: java serverUDP.java <port>");
        }

        int port = args.length == 1 ? Integer.parseInt(args[0]) : 10011;
        int numberRead = 0;

        ServerSocket serverSocket = new ServerSocket(port);

        RequestDecoder decoder = new RequestDecoder();
        ResponseEncoder encoder = new ResponseEncoder();

        byte[] buffer = new byte[8];

        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();

                InputStream in = clientSocket.getInputStream();
                OutputStream out = clientSocket.getOutputStream();

                while (true) {
                    numberRead = in.read(buffer);

                    if (numberRead == -1) {
                        break;
                    }

                    Request receivedRequest = decoder.decode(buffer);
                    String requestHex = "";

                    for (int i = 0; i < receivedRequest.tml; i++) {
                        String datum = "0" + Integer.toHexString(buffer[i]);
                        requestHex += "0x" + datum.substring(datum.length() - 2) + " ";
                    }
                    System.out.println(requestHex.trim());
                    System.out.println(receivedRequest);

                    byte errorCode = receivedRequest.tml == numberRead ? (byte) 0 : (byte) 127;

                    Response response = new Response(receivedRequest.id, errorCode, receivedRequest.compute());
                    byte[] codedResponse = encoder.encode(response);

                    out.write(codedResponse);
                }
            } catch (SocketException e) {
                System.out.println("Connection is closed");
                break;
            }
        }
    }
}
