import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;
import java.io.IOException;

/**
 * TCP Client
 * 
 * Usage: java myFirstTCPClient.java <hostname> <port>
 * 
 * 1) Prompts the user to enter a sentence
 * 2) Sends the sentence to the server
 * 3) Receives the response from the server
 * 4) Measure the duration between time sent and received
 * 5) Displays the message received and time elapsed
 * 6) Collects the round trip time and calculates statistics
 * 
 * Note: Be sure to run this and myFirstTCPServer in seperate processes
 */
public class myFirstTCPClient {
    
    private static final int DEFAULT_PORT = 10010;

    public static void main(String[] args) throws IOException {

        if (args.length == 0 || args.length > 2) {
            throw new IllegalArgumentException("Usage: <hostname> <port>");
        }

        String hostname = args[0];
        int port = args.length == 2 ? Integer.parseInt(args[1]) : DEFAULT_PORT;

        var socket = new Socket(hostname, port);
        System.out.println(String.format("Connected to %s on port %d", hostname, port));

        var in = socket.getInputStream();
        var out = socket.getOutputStream();

        var input = new Scanner(System.in);
        int i = 0;
        double minTimeElapsed = Double.POSITIVE_INFINITY, avgTimeElapsed = 0, maxTimeElapsed = 0;

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

                double start = System.nanoTime();
                out.write(byteBuffer);

                int bytesRcvd, totalBytesRcvd = 0;

                while (totalBytesRcvd < byteBuffer.length) {
                    if ((bytesRcvd = in.read(byteBuffer, totalBytesRcvd, byteBuffer.length - totalBytesRcvd)) == -1) {
                        socket.close();
                        input.close();
                        throw new SocketException("Connection close prematurely");
                    }

                    double timeElapsed = (System.nanoTime() - start) / 1000000;
                    minTimeElapsed = Math.min(minTimeElapsed, timeElapsed);
                    maxTimeElapsed = Math.max(maxTimeElapsed, timeElapsed);
                    avgTimeElapsed += (timeElapsed - avgTimeElapsed) / (double) i;

                    totalBytesRcvd += bytesRcvd;
                    System.out.println(String.format(
                        "Received: %s - Time Elapsed: %.2fms",
                        new String(byteBuffer),
                        timeElapsed
                    ));
                }
            }
        }
        System.out.println(String.format(
            "Time Elapsed Statistics\n  Min: %.2fms\n  Max: %.2fms\n  Avg: %.2fms",
            minTimeElapsed != Double.POSITIVE_INFINITY ? minTimeElapsed : 0,
            maxTimeElapsed,
            avgTimeElapsed
        ));

        socket.close();
        input.close();
    }
}
