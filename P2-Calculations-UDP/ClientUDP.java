import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ClientUDP {
    private static final int MAXTRIES = 5;

    public static void main(String args[]) throws Exception {

        if (args.length != 2) {
            throw new IllegalArgumentException("Usage: <hostname> <port>");
        }

        Scanner input = new Scanner(System.in);
        
        InetAddress hostname = InetAddress.getByName(args[0]);
        int port = Integer.parseInt(args[1]);
        
        DatagramSocket sock = new DatagramSocket();
        
        RequestEncoder encoder = new RequestEncoder();
        ResponseDecoder decoder = new ResponseDecoder();

        int i = 0;
        int tries = 0;
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
            
            DatagramPacket message = new DatagramPacket(codedRequest, codedRequest.length, hostname, port);
            DatagramPacket response = new DatagramPacket(new byte[1024], 1024);

            responded = false;
            tries = 0;

            double start = System.nanoTime();
            sock.send(message);
            do {
                try {
                    sock.receive(response);
                    responded = true;
                } catch (InterruptedIOException e) {
                    tries++;
                }
            } while (!responded && tries < MAXTRIES);
            
            if (responded) {
                double end = System.nanoTime();
                Response receivedResponse = decoder.decode(response);

                String requestHex = "";
                byte[] buffer = response.getData();
                for (int j = 0; j < response.getLength(); j++) {
                    String datum = "0" + Integer.toHexString(buffer[j]);
                    requestHex += "0x" + datum.substring(datum.length() - 2) + " ";
                }
                System.out.println(requestHex.trim());
                System.out.println(receivedResponse + " (time elapsed: " + ((end - start) / 1000000) + "ms)");

                System.out.print("Continue? (y/n): ");
                continueCalculations = input.next().trim();
                i += 1;
            } else {
                System.out.println("No response -- giving up.");
            }
        } while (continueCalculations.equals("y"));
        sock.close();
    }
}
