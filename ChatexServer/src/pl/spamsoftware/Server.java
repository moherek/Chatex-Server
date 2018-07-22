package pl.spamsoftware;


import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

public class Server implements Observer{

    private ArrayList clientArrayList;
    private PrintWriter printWriter;
    private static Server serverInstance;
    private static final int PORT = 5000;

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
                new Server();
        });

    }

    Server(){
        Window window = new Window(this);
        Window.setCustomerIpAddress(getCustomerIp());

    }

    public void startServer(){
        clientArrayList = new ArrayList();



        try{
            ServerSocket serverSocket = new ServerSocket(PORT);

            while (true){
                Socket socket = serverSocket.accept();
                update("Info : Listening - " + serverSocket);
                printWriter = new PrintWriter(socket.getOutputStream());
                clientArrayList.add(printWriter);
                Thread t = new Thread(new ServerClient(socket));
                t.start();
            }

        } catch (Exception e){
            update("Crash : " + e.getMessage());
            e.printStackTrace();
        }

    }


    @Override
    public void update(String consoleText) {
        Window.addServerConsoleText(consoleText);
    }

    @Override
    public void start() {
        update("Info : Server has been started.");
        startServer();
    }

    class ServerClient implements Runnable
    {
        Socket socket;
        BufferedReader bufferedReader;

        public ServerClient(Socket socketClient){
            try {
                update("Info: Client connected : " + socketClient);
                socket = socketClient;
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            } catch (Exception e){

            }

        }

        @Override
        public void run() {
            String string;
            PrintWriter pw = null;

            try {
                while ((string = bufferedReader.readLine()) != null) {
                    update("Message received : " + string);

                    Iterator it =  clientArrayList.iterator();
                    while (it.hasNext()){
                        pw = (PrintWriter) it.next();
                        pw.println(string);
                        pw.flush();
                    }
                }

            } catch (Exception e){

            }
        }
    }
    private static String getCustomerIp(){
        try {
            InetAddress ip = InetAddress.getLocalHost();
            return ip.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }

    }
}
