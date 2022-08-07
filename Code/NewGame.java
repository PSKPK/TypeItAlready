import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.util.*;
import java.io.*;


public class NewGame extends JFrame{

    boolean running=true;
    JButton b=null;
    JPanel p=null;
    JTextField tf=null;
    JLabel l=null;
    JLabel l2=null;
    int time=180;
    int score=0;
    static int hscore;
    int displayed=0;

    ArrayList<String>  wordlist;
    WordRun word1=null;
    WordRun word2=null;
    WordRun word3=null;
    ImageIcon icon=new ImageIcon("Icon.png");

    public NewGame() {
        super("TypeItAlready");
        setSize(600,300);
        setLayout(new BorderLayout());
        setResizable(false);
        setIconImage(icon.getImage());
        b=new JButton("Start");
        b.addActionListener(new ButtonHandler());
        add(b,BorderLayout.NORTH);

        p=new DrawPanel();
        p.setPreferredSize(new Dimension(300,300));
        add(p,BorderLayout.CENTER);

        JPanel p2=new JPanel();
        p2.setLayout(new FlowLayout());

        tf=new JTextField(20);
        tf.getDocument().addDocumentListener(new ListenText());
        p2.add(tf);

        l2=new JLabel("Score : ");
        p2.add(l2);

        l=new JLabel("0");
        p2.add(l);

        add(p2,BorderLayout.SOUTH);

        wordlist=new ArrayList<String>();

        try {
            FileReader fr = new FileReader("demo.txt");
            BufferedReader br=new BufferedReader(fr);
            int i=0;
            String s;
            while((s=br.readLine())!=null)
            {
                wordlist.add(i,s);
                i++;
            }
            System.out.println("Words read : "+wordlist.size());
        }
        catch(IOException e){
          System.out.println("IOError in Catch-1 : ");
          System.out.println(e);
        }
        word1=new WordRun(1);
        word2=new WordRun(2);
        word3=new WordRun(3);

        word1.st=null;
        word2.st=null;
        word3.st=null;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        show();
    }
    class DrawPanel extends JPanel{
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D g2=(Graphics2D)g;
            Font f=new Font("Monospaced",Font.BOLD,20);
            FontMetrics fm=this.getFontMetrics(f);
            g2.setColor(Color.BLACK);
            g2.setFont(f);
            if(word1.st!=null&&time>0){g2.drawString(word1.st,word1.posx,word1.posy);}
            if(word2.st!=null&&time>0){g2.drawString(word2.st,word2.posx,word2.posy);}
            if(word3.st!=null&&time>0){g2.drawString(word3.st,word3.posx,word3.posy);}
            g2.drawString(Integer.toString(time/3),550,40);
            if(score>hscore) hscore=score;
            g2.drawString("High Score : "+Integer.toString(hscore),20,40);
            if(time<1)
            {g2.drawString("Your Score : "+Integer.toString(score),220,130);}
            if(score>=hscore&&time<1)
            {g2.drawString("Congratulations!! You made High Score!!",60,170);
                try{ FileOutputStream fos=new FileOutputStream("score.txt");
                    DataOutputStream dos=new DataOutputStream(fos);
                    dos.writeInt(hscore);
                    dos.flush();dos.close();
                }catch(Exception e){
                  System.out.println("Error in Catch-2 : (Error while writing to file)");
                  System.out.println(e);
                }
              }
        }
    }
    class WordRun implements Runnable{
        String st=null;
        int posx;
        int posy;
        TimerTh t;
        int k=0;

        public WordRun(int a)
        {posx=(a-1)*150+50;
            posy=120;}

        public void run(){
            try{
                t=new TimerTh();
                Random rand=null;
                while(running&&time>0){
                    if(time/3>0){if(k==0) {t.start();k=1;}
                        if(st==null){
                            rand=new Random();
                            Thread.currentThread().sleep(rand.nextInt(400));
                            st=wordlist.get(rand.nextInt(10));
                            tf.setText("");
                            displayed++;
                        }}
                    else{
                        tf.setText("");}
                    repaint();
                }
            }catch(Exception e){
              System.out.print("Error in Catch-3 : ");
              System.out.println(e);
            }
        }
      }
    class TimerTh extends Thread
    {
      public void run()
      {
        try{
          while(time>0){
            sleep(1000);
            time=time-1;
          }
        }catch(Exception e){
          System.out.print("Error in Catch-4 : ");
          System.out.println(e);
        }
      }
    }

    class ButtonHandler implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if(e.getActionCommand()=="Start"){
                Thread t1=new Thread(word1);
                Thread t2=new Thread(word2);
                Thread t3=new Thread(word3);
                t1.start();
                t2.start();
                t3.start();
            }
        }
    }
    class ListenText implements DocumentListener{
        public void changedUpdate(DocumentEvent e) {        }
        public void removeUpdate(DocumentEvent e) {        }
        public void insertUpdate(DocumentEvent e) {
            if(tf.getText().equals(word1.st)){
                word1.st=null;
                score++;
                l.setText(score+"");
            }
            if(tf.getText().equals(word2.st)){
                word2.st=null;
                score++;
                l.setText(score+"");
            }
            if(tf.getText().equals(word3.st)){
                word3.st=null;
                score++;
                l.setText(score+"");
            }
        }
    }
    public static void main(String[] args){
        try{ File file=new File("score.txt");
            if(file.createNewFile()) hscore=0;
            else
                try{ FileInputStream fis=new FileInputStream("score.txt");
                    DataInputStream dis=new DataInputStream(fis);
                    if(dis.available()>0) hscore=dis.readInt();
                }catch(Exception e){
                  System.out.print("Error in Catch-5 :");
                  System.out.println(e);
                }
        }catch(IOException e){
          System.out.print("Error in Catch-6 : ");
          System.out.println(e);
        }

        NewGame ap=new NewGame();
    }
}
