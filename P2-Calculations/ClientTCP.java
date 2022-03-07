import java.io.*;   // for Input/OutputStream
import java.net.*;  // for Socket

public class ClientTCP {
    public static void main(String args[]) throws Exception {
            /*
        if (args.length != 2)  // Test for correct # of args
        throw new IllegalArgumentException("Parameter(s): <Destination> <Port>");

        InetAddress destAddr = InetAddress.getByName(args[0]);  // Destination address
        int destPort = Integer.parseInt(args[1]);               // Destination port

        Socket sock = new Socket(destAddr, destPort);

        Request friend = new Request(1234567890987654L, "John Smith", 
                    (short) 2360, 36830, false, true, false);
        
        System.out.println("Display friend"); 
        System.out.println(friend); // Display friend just to check what we send


        RequestEncoder encoder = new RequestEncoderBin();

        System.out.println("Sending Friend (Binary)");
        OutputStream out = sock.getOutputStream(); // Get a handle onto Output Stream
        out.write(encoder.encode(friend)); // Encode and send

        sock.close();
        */
    }
}
