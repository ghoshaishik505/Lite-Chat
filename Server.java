import java.net.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;


import java.io.*;
class Server  extends JFrame
{
  ServerSocket server;
  Socket socket;
  BufferedReader br;
  PrintWriter out;

  //Declare Components
  private JLabel heading =new JLabel("Server Area");
  private JTextArea messageArea=new JTextArea();
  private JTextField messageInput=new JTextField();
  private  Font fontb=new Font("Roboto",Font.PLAIN,20);
  private  Font fonth=new Font("Roboto",Font.BOLD,20);
  //constructor
  public Server()
  {
    try{
      server = new ServerSocket(7777);
      System.out.println("Server is ready to accept connection request");
      System.out.println("waiting..");
      socket=server.accept();

      br=new BufferedReader(new InputStreamReader(socket.getInputStream()));

      out= new PrintWriter(socket.getOutputStream());

      createGUI();
      handleEvents();

      startReading();
      // startWriting();
    }catch(Exception e){
      e.printStackTrace();
    }

  }

   private void handleEvents() {

      messageInput.addKeyListener(new KeyListener(){

        @Override
        public void keyTyped(KeyEvent e) {
          // TODO Auto-generated method stub
          
        }

        @Override
        public void keyPressed(KeyEvent e) {
          // TODO Auto-generated method stub
          
        }

        @Override
        public void keyReleased(KeyEvent e) {
          // TODO Auto-generated method stub
          //System.out.println("Key released"+e.getKeyCode());
          if(e.getKeyCode()==10){
            //System.out.println("You have pressed enter button");
            String contentToSend=messageInput.getText();
            messageArea.append("Me :"+contentToSend+"\n");
            out.println(contentToSend);
            out.flush();
            messageInput.setText("");
            messageInput.requestFocus();

          }
        }
        
      });
    }


  private void createGUI()
    {
      //gui code..
      this.setTitle("Server Messenger[END]");
      this.setSize(600,600);
      this.setLocationRelativeTo(null);
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      //coding for component
      heading.setFont(fonth);
      messageArea.setFont(fontb);
      messageInput.setFont(fontb);

      // Customizing the image(logo)
      ImageIcon imgicon=new ImageIcon("Logo.jpg");  // load the image to a imageIcon object
      Image img = imgicon.getImage(); // transform the imageicon to an image
      Image newimg = img.getScaledInstance(75, 48, Image.SCALE_SMOOTH); // resizing the image
      imgicon = new ImageIcon(newimg); // transforming the resizing the image into icon object
      heading.setIcon(imgicon);
      heading.setHorizontalTextPosition(SwingConstants.CENTER);
      heading.setVerticalTextPosition(SwingConstants.BOTTOM);
      heading.setHorizontalAlignment(SwingConstants.CENTER);
      heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

      messageArea.setEditable(false);
      messageInput.setHorizontalAlignment(SwingConstants.CENTER);

      //frame layout set
      this.setLayout(new BorderLayout());

      //adding the components to frame
      this.add(heading,BorderLayout.NORTH);
      JScrollPane jScrollPane=new JScrollPane(messageArea);
      this.add(jScrollPane,BorderLayout.CENTER);
      this.add(messageInput,BorderLayout.SOUTH);
      this.setVisible(true);
    }


  //start reading [Method]
  public void startReading() {
    Runnable r1 = () -> {
      System.out.println("Reader started..");
      try{
        while (true) {
          String msg = br.readLine();
          if(msg.equals("exit")) {
            System.out.println("Client terminated the chat");
            JOptionPane.showMessageDialog(this,"Client Terminated the chat");
            messageInput.setEnabled(false);
            socket.close();
            break;
          }
          //System.out.println("Client : " + msg);
          messageArea.append("Client : " + msg+"\n");

        }

      } catch(Exception e){
        System.out.println("connection closed");
      }

    };
    new Thread(r1).start();
  }


  //start writing send [Method]
  public void startWriting() {
    Runnable r2 = () -> {
      System.out.println("Writer Started..");

      try{
        while (!socket.isClosed()) {
          BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
          String content=br1.readLine();
          out.println(content);
          out.flush();

          if(content.equals("exit")){
            socket.close();
            break;
          }
         
        }

        System.out.println("Connection is closed");
      } catch(Exception e) {
        e.printStackTrace();
      }
    };
    new Thread(r2).start();
  }

  public static void main(String args[]){
    System.out.println("This is Server..going to start");
    new Server();
  }
}