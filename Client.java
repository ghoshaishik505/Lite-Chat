import java.net.*;
import java.io.*;
import javax.swing.*;

import java.awt.event.*;  

import java.awt.*;


public class Client extends JFrame implements KeyListener{


  Socket socket;
  BufferedReader br;
  PrintWriter out;
  
  JLabel heading=new JLabel("Client Area");
  JTextArea messageArea = new JTextArea();
  JTextField messageInput=new JTextField();
  JScrollPane scrollPane = new JScrollPane(messageArea);
  Font fonth=new Font("Roboto",Font.BOLD,20);
  Font fontb=new Font("Roboto",Font.PLAIN,20);



  public Client() 
  {
    try 
      {
        System.out.println("Sending request to server");
        socket = new Socket("127.0.0.1", 7777);
        System.out.println("Connection done.");
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream());

        createGUI();
        handleEvents();
        startReading();
        startWriting();
      } 
      catch(Exception  e) {}
  }

  public void handleEvents()
  {
      messageInput.addKeyListener(this);
  }  

  public void keyPressed(KeyEvent e)
  {  
       
  }  
  public void keyReleased(KeyEvent e)
  {  
      if(e.getKeyCode()==10)
      {
          String content=messageInput.getText();
          messageArea.append("Me :"+content+"\n");
          out.println(content);
          out.flush();
          messageInput.setText("");
          messageInput.requestFocus();

      }
         
  }  
  public void keyTyped(KeyEvent e)
  {  
        
  }  

  public void createGUI()
  {
      this.setTitle("Client Messanger");
      this.setSize(600,600);
      this.setLocationRelativeTo(null);
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      //Coding for components (heading,messageArea,messageInput)
      heading.setFont(fonth);
      messageArea.setFont(fontb);
      messageInput.setFont(fontb);

    

      //Setting the Frame Layout
      this.setLayout(new BorderLayout()); //
      this.add(heading,BorderLayout.NORTH);
      this.add(scrollPane,BorderLayout.CENTER);
      this.add(messageInput,BorderLayout.SOUTH);



      

      // Customizing the image(logo)
      ImageIcon imgicon=new ImageIcon("Logo.jpg");  // load the image to a imageIcon object
      Image img = imgicon.getImage(); // transform the imageicon to an image
      Image newimg = img.getScaledInstance(75, 48, Image.SCALE_SMOOTH); // resizing the image
      imgicon = new ImageIcon(newimg); // transforming the resizing the image into icon object
      heading.setIcon(imgicon);

      heading.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
      heading.setHorizontalTextPosition(JLabel.CENTER);
      heading.setVerticalTextPosition(JLabel.BOTTOM);
      heading.setHorizontalAlignment(JLabel.CENTER);

      // Customizing the message area
      messageArea.setEditable(false);


      // Customizing the message Input field
      messageInput.setHorizontalAlignment(JTextField.CENTER);
      messageInput.setMargin(new Insets(5,5,5,5));

      this.setVisible(true);
  }

  public void startReading() {
     Runnable r1 = () -> {
     System.out.println("Reader started..");
       try
       {
           while (true) 
           {
               String msg = br.readLine();
               if(msg.equals("exit")) {
               System.out.println("Server terminated the chat");
               JOptionPane.showMessageDialog(this,"Server terminated the chat");
               messageInput.setEnabled(false);
               socket.close();
               break;
               }
               messageArea.append("Server : " + msg +"\n");
           }
         } 
         catch(Exception e)
         {
            System.out.println("connection closed");
         }
     };
     new Thread(r1).start();
   }





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
     System.out.println("This is Client...");
     new Client();

   }

  }