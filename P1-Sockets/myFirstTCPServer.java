import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.io.IOException;

/**
 * TCP Server
 * 
 * Usage: java myFirstTCPServer.java <port>
 * 
 * Simply receives messages and returns them in all caps
 * 
 * Note: Be sure to run this and myFirstTCPClient in seperate processes
 */
public class myFirstTCPServer {
    
    private static final int DEFAULT_PORT = 10010;
    private static final int BUFSIZE = 32; // Size of receive buffer

    public static void main(String[] args) throws IOException {

        if (args.length > 1) {
            throw new IllegalArgumentException("Usage: <Port>");
        }

        int port = args.length == 1 ? Integer.parseInt(args[1]) : DEFAULT_PORT;

        var serverSocket = new ServerSocket(port);

        int recvMsgSize;
        byte[] byteBuffer = new byte[BUFSIZE];

        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();

                System.out.println(String.format(
                    "Handling client at %s on port %d",
                    clientSocket.getInetAddress().getHostAddress(),
                    clientSocket.getPort()
                ));

                var in = clientSocket.getInputStream();
                var out = clientSocket.getOutputStream();

                String sentence = null;

                while ((recvMsgSize = in.read(byteBuffer)) != -1) {
                    sentence = new String(byteBuffer).toUpperCase();
                    byteBuffer = sentence.getBytes();
                    out.write(byteBuffer, 0, recvMsgSize);
                }
            } catch (SocketException e) {
                System.out.println("Connection is closed");
                break;
            }
        }
    }
}
