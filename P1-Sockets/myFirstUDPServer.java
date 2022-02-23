import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.io.IOException;

/**
 * UDP Server
 * 
 * Usage: java myFirstUDPServer.java <port>
 * 
 * Simply receives messages and returns them in all caps
 * 
 * Note: Be sure to run this and myFirstUDPClient in seperate processes
 */
public class myFirstUDPServer {

    private static final int DEFAULT_PORT = 10010;
    private static final int ECHOMAX = 255;

    public static void main(String[] args) throws IOException {

        if (args.length > 1) {
            throw new IllegalArgumentException("Usage: <Port>");
        }

        int port = args.length == 1 ? Integer.parseInt(args[0]) : DEFAULT_PORT;

        DatagramSocket socket = new DatagramSocket(port);
        DatagramPacket packet = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);

        while (true) {
            socket.receive(packet);

            System.out.println(String.format(
                "Handling client at %s on port %d",
                packet.getAddress().getHostAddress(),
                packet.getPort()
            ));

            var sentence = new String(packet.getData()).toUpperCase();

            packet.setData(sentence.getBytes());
            socket.send(packet);
            packet.setLength(ECHOMAX);
        }
    }
    
}
