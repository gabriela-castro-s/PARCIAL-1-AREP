package edu.eci.arep;
import java.net.*;
import java.io.*;
import java.util.HashMap;


public class HttpServer {



    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(36000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        Socket clientSocket = null;
        boolean running = true;
        while (running) {
            try {
                System.out.println("Ready to Receive ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            String inputLine;
            String outputLine = " ";
            boolean firstLine = true;
            String chatAns = " ";
            while ((inputLine = in.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                }
                System.out.println("Received: " + inputLine);
                String ans;
                if (inputLine.startsWith("GET /get/?t=")) {
                    ans = inputLine.split("=")[1];
                    chatAns = ans.split(" ")[0];
                }
                if (!in.ready()) {
                    break;
                }
            }

            outputLine = ("HTTP/1.1 200 OK\r\n" +
                    "        Content-Type: text/html\r\n" +
                    "\r\n" +
                    "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "<title>Form Example</title>\n" +
                    "<meta charset=\"UTF-8\">\n" +
                    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<h1>Form with GET</h1>\n" +
                    "<form action=\"/hello\">\n" +
                    "<label for=\"name\">Name:</label><br>\n" +
                    "<input type=\"text\" id=\"name\" name=\"name\" value=\"John\"><br><br>\n" +
                    "<input type=\"button\" value=\"Submit\" onclick=\"loadGetMsg()\">\n" +
                    "<h1>" + chatAns + "</h1>\n " +
                    "</form>\n" +
                    "<div id=\"getrespmsg\"></div>\n" +
                    "\n" +
                    "<script>\n" +
                    "function loadGetMsg(){\n" +
                    "let name = document.getElementById(\"name\");\n" +
                    "let url = \"get/?t=\" + name.value;\n" +
                    "fetch (url, {method: 'GET'})\n" +
                    ".then(x => x.text())\n" +
                    ".then(y => document.getElementById(\"getrespmsg\").innerHTML = y);\n" +
                    "}\n " +
                    "</script>\n" +
                    "        </body>\n" +
                    "        </html>");

            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }


}



