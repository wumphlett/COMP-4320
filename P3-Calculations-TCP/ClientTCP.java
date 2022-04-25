import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientTCP {
    public static void main(String args[]) throws Exception {

        if (args.length != 2) {
            throw new IllegalArgumentException("Usage: <hostname> <port>");
        }

        Scanner input = new Scanner(System.in);

        InetAddress hostname = InetAddress.getByName(args[0]);
        int port = Integer.parseInt(args[1]);

        Socket sock = new Socket(hostname, port);

        InputStream in = sock.getInputStream();
        OutputStream out = sock.getOutputStream();

        RequestEncoder encoder = new RequestEncoder();
        ResponseDecoder decoder = new ResponseDecoder();
        
        int i = 0;
        String continueCalculations = "";
        boolean responded;

        do {
            System.out.print("Please enter an operator code [0-6]: ");
            int opCode = Integer.parseInt(input.next().trim());
            System.out.print("Please enter the first operand: ");
            int operandOne = Integer.parseInt(input.next().trim());

            Request request;

            if (opCode != 6) {
                System.out.print("Please enter the second operand: ");
                int operandTwo = Integer.parseInt(input.next().trim());
                request = new Request(i, opCode, operandOne, operandTwo);
            } else {
                request = new Request(i, opCode, operandOne);
            }

            byte[] codedRequest = encoder.encode(request);
            byte[] codedResponse = new byte[7];

            responded = false;

            double start = System.nanoTime();
            out.write(codedRequest);
            
            int bytesRcvd, totalBytesRcvd = 0;

            while (totalBytesRcvd < codedResponse.length) {
                if ((bytesRcvd = in.read(codedResponse, totalBytesRcvd, codedResponse.length - totalBytesRcvd)) == -1) {
                    sock.close();
                    input.close();
                    throw new SocketException("No response -- giving up.");
                }

                totalBytesRcvd += bytesRcvd;
            }

            double end = System.nanoTime();
            Response receivedResponse = decoder.decode(codedResponse);

            String requestHex = "";
            for (int j = 0; j < codedResponse.length; j++) {
                String datum = "0" + Integer.toHexString(codedResponse[j]);
                requestHex += "0x" + datum.substring(datum.length() - 2) + " ";
            }
            System.out.println(requestHex.trim());
            System.out.println(receivedResponse + " (time elapsed: " + ((end - start) / 1000000) + "ms)");

            System.out.print("Continue? (y/n): ");
            continueCalculations = input.next().trim();
            i += 1;

        } while (continueCalculations.equals("y"));
        sock.close();
    }
}
