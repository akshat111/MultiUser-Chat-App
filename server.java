import java.net.*;
import java.io.*;
class server {
    ServerSocket server;
    Socket socket;

    BufferedReader br;
    PrintWriter out;

    public server() {
       try {
         server = new ServerSocket(7777);
         System.out.println("server is ready to accept connection");
         System.out.println("Waiting ");
         socket = server.accept();

         br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         out = new PrintWriter(socket.getOutputStream());

         startReading();
         startWriting();
       } catch (Exception e) {
        e.printStackTrace();
        // TODO: handle exception
       }
    }

    public void startReading(){
        Runnable r1=()-> {
            System.out.println("Reader started...");

            try{
            while(true)
            {
                String msg=br.readLine();
                if(msg.equals("exit"))
                {
                    System.out.println("Client terminated the chat");
                    socket.close();

                    break;
                }
                System.out.println("Client :" +msg);
            }
        }
        catch(Exception e){
         //   e.printStackTrace();
         System.out.println("Connection is Closed");
        }
        };
        new Thread(r1).start();
    }

    public void startWriting() {
        Runnable r2 =()->{
            try{
            while(!socket.isClosed()){
                System.out.println("Writer Started....");
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));

                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                    if(content.equals("exit")){
                        socket.close();
                        break;
                    }
                }
            }
            catch(Exception e){
              //  e.printStackTrace();
              System.out.println("Connection is closed");
            } 

         //   System.out.println("Connection is Closed");
            
        };
        new Thread(r2).start();
    }
    public static void main(String[] args) {
        System.out.println(" This is server..... Going to start Server....");
        new server();
    }
}