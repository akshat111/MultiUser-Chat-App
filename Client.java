import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.awt.*;

public class Client extends JFrame{

    Socket socket;
    BufferedReader br;
    PrintWriter out;

    //Declare Components
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea= new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN,20);

    //Constructor

    public Client(){
        try {
            System.out.println("Sending request to Server");
            socket = new Socket("127.0.0.1",7777);
            System.out.println("Connection Done....");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

          createGUI();
          handleEvents();
          startReading();
            //  startWriting();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
              //  throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
               // System.out.println("key Released " + e.getKeyCode());
                if(e.getKeyCode()==10)
                {
                    //   System.out.println("You have pressed Entered button");
                    String contentToSend=messageInput.getText();
                    messageArea.append("Me :" +contentToSend+ "\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
                //    
                //messageInput.requestFocus();
              //}
              //throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");
            }
            
        });
    }

    private void createGUI(){
        //GUI Code
        this.setTitle("Client Messenger");
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Component of Code
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

        //frame- Layout

        heading.setIcon(new ImageIcon("cLogo.png"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
      //  heading.setVerticalTextPosition(SwingConstants.CENTER);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);


        //Setting the layout of frame
        this.setLayout(new BorderLayout());
        //adding the component to frame 
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane= new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);

        this.setVisible(true);

    }
    //start reading
    public void startReading(){
        Runnable r1=()-> {
            System.out.println("Reader started...");

            try{

            while(true)
            {

                String msg=br.readLine();
                if(msg.equals("exit"))
                {
                    System.out.println("Server terminated the chat");
                    JOptionPane.showMessageDialog(this, "Server Terminated the Chat");
                    messageInput.setEnabled(false);
                    socket.close();
                    break;
                }
                //System.out.println("Server :" +msg);
                  messageArea.append("Server :" +msg+"\n");
            }
        }catch(Exception e){
           // e.printStackTrace();
           System.out.println("Connection closed...");
        }
        };
        new Thread(r1).start();
    }
    //start writing
    public void startWriting() {
        Runnable r2 =()->{
            try{
            while(!socket.isClosed()){
                System.out.println("Writer Started....");
              
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    //InputStreamReader(System.in);
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                    if(content.equals("exit")) {
                        socket.close();
                        break;
                    }
            }
                    System.out.println("Connection is Closed");
        }catch(Exception e){
            e.printStackTrace();
        }
        };
        new Thread(r2).start();
    }
    public static void main(String[] args) {

        System.out.println("This is client");
        new Client();
    }
}
