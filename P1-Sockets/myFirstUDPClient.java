import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Scanner;
import java.io.IOException;
import java.io.InterruptedIOException;

/**
 * UDP Client
 * 
 * Usage: java myFirstUDPClient.java <hostname> <port>
 * 
 * 1) Prompts the user to enter a sentence
 * 2) Sends the sentence to the server
 * 3) Receives the response from the server
 * 4) Measure the duration between time sent and received
 * 5) Displays the message received and time elapsed
 * 6) Collects the round trip time and calculates statistics
 * 
 * Note: Be sure to run this and myFirstUDPServer in seperate processes
 */
public class myFirstUDPClient {

    private static final int DEFAULT_PORT = 10010;
    private static final int TIMEOUT = 3000;
    private static final int MAXTRIES = 5;

    public static void main(String[] args) throws IOException {

        if (args.length == 0 || args.length > 2) {
            throw new IllegalArgumentException("Usage: <hostname> <port>");
        }

        var hostname = InetAddress.getByName(args[0]);
        int port = args.length == 2 ? Integer.parseInt(args[1]) : DEFAULT_PORT;

        var socket = new DatagramSocket();
        socket.setSoTimeout(TIMEOUT);

        var input = new Scanner(System.in);
        int i = 0;
        double minTimeElapsed = Double.POSITIVE_INFINITY, avgTimeElapsed = 0, maxTimeElapsed = 0, timeElapsed = 0;

        while (true) {
            System.out.print("Enter a sentence or Q to exit: ");
            String sentence = input.next().trim();

            if (sentence.toLowerCase().equals("q")) {
                break;
            } else if (sentence.length() == 0) {
                System.out.println("Please try again");
            } else {
                i++;

                byte[] byteBuffer = sentence.getBytes();
                var sendPacket = new DatagramPacket(byteBuffer, byteBuffer.length, hostname, port);
                var receivePacket = new DatagramPacket(new byte[byteBuffer.length], byteBuffer.length);

                int tries = 0;
                boolean receivedResponse = false;
                do {
                    double start = System.nanoTime();
                    socket.send(sendPacket);

                    try {
                        socket.receive(receivePacket);

                        if (!receivePacket.getAddress().equals(hostname)) {
                            throw new IOException("Received packet from an unknown source");
                        }

                        timeElapsed = (System.nanoTime() - start) / 1000000;
                        minTimeElapsed = Math.min(minTimeElapsed, timeElapsed);
                        maxTimeElapsed = Math.max(maxTimeElapsed, timeElapsed);
                        avgTimeElapsed += (timeElapsed - avgTimeElapsed) / (double) i;

                        receivedResponse = true;
                    } catch (InterruptedIOException e) {
                        tries++;
                        System.out.println(String.format(
                                "Timed out, %d more tries...",
                                MAXTRIES - tries));
                    }
                } while (!receivedResponse && tries < MAXTRIES);

                if (receivedResponse) {
                    System.out.println(String.format(
                            "Received: %s - Time Elapsed: %.2fms",
                            new String(byteBuffer),
                            timeElapsed));
                } else {
                    System.out.println("No response -- giving up.");
                }
            }
        }
        System.out.println(String.format(
                "Time Elapsed Statistics\n  Min: %.2fms\n  Max: %.2fms\n  Avg: %.2fms",
                minTimeElapsed != Double.POSITIVE_INFINITY ? minTimeElapsed : 0,
                maxTimeElapsed,
                avgTimeElapsed));

        socket.close();
        input.close();
    }
}
