package pl.spamsoftware;



import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Window extends JFrame implements ActionListener, Observer {
    private static Observer communicationInterface;
    private static TextField customerIpDisplayField;
    private static JButton buttonStart;
    private static JButton buttonStop;
    private static JTextArea serverConsole;
    private static final String buttonStartText = "Start Server";
    private static final String buttonStopText = "Stop Server";


    Window(Observer communiationInterface){
        super("Chatex");
        this.communicationInterface = communiationInterface;
        setMinimumSize(new Dimension(820, 640));
        setContentPane(createContentPane());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }



    private Container createContentPane(){
        JPanel newContentPane = new JPanel(new GridLayout(1,1));
        createGUI(newContentPane);
        return newContentPane;
    }
    private void createGUI(Container pane){
        JPanel guiPane = new JPanel();
        guiPane.setLayout(new BorderLayout());

        JPanel customerIpDisplay = new JPanel();
        Border etchedBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        customerIpDisplay.setBorder(etchedBorder);
        customerIpDisplay.setLayout(new FlowLayout());

        customerIpDisplayField = new TextField();
        customerIpDisplayField.setEditable(false);
        customerIpDisplayField.setText("IP : ");
        customerIpDisplayField.setPreferredSize(new Dimension(120,20));
        customerIpDisplay.add(customerIpDisplayField);

        JPanel serverControlPage = new JPanel();
        serverControlPage.setBorder(etchedBorder);
        serverControlPage.setLayout(new BoxLayout(serverControlPage, BoxLayout.Y_AXIS));

        addJButton(buttonStartText, customerIpDisplay, buttonStart);
        addJButton(buttonStopText, customerIpDisplay, buttonStop);

        serverConsole = new JTextArea(5,20);
        addTextArea(serverControlPage, serverConsole);

        guiPane.add(customerIpDisplay, BorderLayout.NORTH);
        guiPane.add(serverControlPage, BorderLayout.CENTER);

        pane.add(guiPane);
    }

    private void addJButton(String description, Container container, JButton jButton){
        Border border = BorderFactory.createEmptyBorder(10,10,10,10);
        JPanel jPanel = new JPanel();
        jPanel.setBorder(border);
        jPanel.setLayout(new GridLayout(1,1));
        jButton = new JButton(description);
        jButton.setActionCommand(description);
        jButton.addActionListener(this::actionPerformed);
        jPanel.add(jButton);


        container.add(jPanel);
    }

    private void addTextArea(Container container, JTextArea jTextArea){
        Border border = BorderFactory.createEmptyBorder(50,50,10,50);
        JPanel jPanel = new JPanel();
        jPanel.setBorder(border);
        jPanel.setLayout(new GridLayout(1, 1));
        jTextArea.setEditable(false);



        jPanel.add(jTextArea);
        container.add(jPanel);
    }

    public static void setCustomerIpAddress(String adress){
        customerIpDisplayField.setText("IP : " + adress);
    }

    public static void addServerConsoleText(String text){
        serverConsole.append(text + "\n");
        serverConsole.setCaretPosition(serverConsole.getDocument().getLength());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case buttonStartText:
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        communicationInterface.start();
                        return null;
                    }
                };
                worker.execute();
                break;
            case buttonStopText:
                System.out.println("stop");
                break;
        }
    }

    @Override
    public void update(String consoleText) {
    }

    @Override
    public void start() {

    }
}
