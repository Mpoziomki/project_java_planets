package pojavaKarabowiczCybulska.gui;
import pojavaKarabowiczCybulska.simulation.SimulationMainPanel;
import pojavaKarabowiczCybulska.universe.CelestialBodyPosition;
import pojavaKarabowiczCybulska.universe.Planet;
import pojavaKarabowiczCybulska.universe.Sun;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;

import static java.lang.Math.pow;


public class GuiPanel extends JPanel implements ActionListener //Karabowicz
{


    JButton changeObjectColorButton, createObjectButton, drawOrbitsButton;
    JToggleButton onOfButton;
    JTextField massField, radiusField;
    JComboBox objectTypeChooser, backgroundColorChooser;
    JLabel objectLocationLabel, massLabel;
    JRadioButton objectLocationRadiusChooser;
    JRadioButton objectLocationRandomChooser;


    public static Color choosenObjectColor = Color.YELLOW;
    String choosenObject;

    CelestialBodyPosition mouseClick;

    public static Sun sun;

    public static ArrayList<Planet> planetArrayList; //lista przechowująca planety

    public static CelestialBodyPosition centerPosition; //położenie środka (tam gdzie chcemy umieścić słońce)


    private static SimulationMainPanel simulationMainPanel;

    static String textToWrite = "";

    public static boolean drawOrbits;


    public GuiPanel() //Karabowicz
    {
        choosenObjectColor = Color.YELLOW;

        this.setLayout(new BorderLayout());

        planetArrayList = new ArrayList<>();
        centerPosition = new CelestialBodyPosition();

        simulationMainPanel = new SimulationMainPanel();
        simulationMainPanel.setBackground(Color.DARK_GRAY);

        JPanel changeOptionsPanel = new JPanel();
        BoxLayout changeOptionsPanelLayout = new BoxLayout(changeOptionsPanel, BoxLayout.Y_AXIS);
        changeOptionsPanel.setLayout(changeOptionsPanelLayout);

        changeObjectColorButton = new JButton("Change Color");
        createObjectButton = new JButton("Create object");
        drawOrbitsButton = new JButton("Draw orbits");
        onOfButton = new JToggleButton("ON/OFF");

        objectLocationLabel = new JLabel("Location of the object");
        massLabel = new JLabel("Mass [in units of Earth mass]");

        massField = new JTextField("2.5");
        radiusField = new JTextField("15");

        String[] objectTypeList = {"Choose type of the object", "Planet", "Sun", "Moon"};
        objectTypeChooser = new JComboBox(objectTypeList);
        objectTypeChooser.setSelectedIndex(0);
        choosenObject="!";
        String[] backgroundColorList = {"Choose background color", "Black", "Dark Gray", "Light Gray", "White"};
        backgroundColorChooser = new JComboBox(backgroundColorList);
        backgroundColorChooser.setSelectedIndex(0);

        objectLocationRadiusChooser = new JRadioButton("Orbit Radius [AU]");
        objectLocationRadiusChooser.setSelected(true);
        objectLocationRandomChooser = new JRadioButton("Place clicking the mouse");

        ButtonGroup objectLocationChooserGroup = new ButtonGroup();
        objectLocationChooserGroup.add(objectLocationRadiusChooser);
        objectLocationChooserGroup.add(objectLocationRandomChooser);

        //panel with object settings
        JPanel objectSettingsPanel = new JPanel();
        GroupLayout objectSettingsLayout = new GroupLayout(objectSettingsPanel);
        objectSettingsPanel.setLayout(objectSettingsLayout);
        objectSettingsPanel.setBorder(BorderFactory.createTitledBorder("Object settings"));

        objectSettingsLayout.setAutoCreateGaps(true);
        objectSettingsLayout.setAutoCreateContainerGaps(true);
        objectSettingsLayout.linkSize(SwingConstants.HORIZONTAL, changeObjectColorButton, massField, objectTypeChooser, createObjectButton);

        objectSettingsLayout.setHorizontalGroup(objectSettingsLayout.createParallelGroup()
                .addComponent(changeObjectColorButton)
                .addComponent(massLabel)
                .addComponent(massField)
                .addComponent(objectTypeChooser)
                .addComponent(objectLocationLabel)
                .addGroup(objectSettingsLayout.createSequentialGroup()
                        .addPreferredGap(objectLocationLabel, objectLocationRadiusChooser, LayoutStyle.ComponentPlacement.INDENT)
                        .addGroup(objectSettingsLayout.createParallelGroup()
                                .addComponent(objectLocationRadiusChooser)
                                .addGroup(objectSettingsLayout.createSequentialGroup()
                                        .addPreferredGap(objectLocationRadiusChooser, radiusField, LayoutStyle.ComponentPlacement.INDENT)
                                        .addComponent(radiusField)
                                )
                                .addComponent(objectLocationRandomChooser)
                        )
                )
                .addComponent(createObjectButton)
                );

        objectSettingsLayout.setVerticalGroup(objectSettingsLayout.createSequentialGroup()
                .addComponent(changeObjectColorButton)
                .addComponent(massLabel)
                .addComponent(massField)
                .addComponent(objectTypeChooser)
                .addComponent(objectLocationLabel)
                .addComponent(objectLocationRadiusChooser)
                .addComponent(radiusField)
                .addComponent(objectLocationRandomChooser)
                .addComponent(createObjectButton)
                );

        backgroundColorChooser.setAlignmentX(Component.CENTER_ALIGNMENT);
        drawOrbitsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        onOfButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        changeOptionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        changeOptionsPanel.add(objectSettingsPanel);
        changeOptionsPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        changeOptionsPanel.add(backgroundColorChooser);
        changeOptionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        changeOptionsPanel.add(drawOrbitsButton);
        changeOptionsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        changeOptionsPanel.add(onOfButton);
        changeOptionsPanel.add(Box.createRigidArea(new Dimension(0, 250)));

        this.add(changeOptionsPanel, BorderLayout.LINE_END);
        this.add(simulationMainPanel, BorderLayout.CENTER);

        //listeners
        changeObjectColorButton.addActionListener
                ( e -> choosenObjectColor = JColorChooser.showDialog(null, "Choose Object Color", choosenObjectColor) );


        backgroundColorChooser.addActionListener(backgroundColorListener);
        objectTypeChooser.addActionListener(objectTypeChooserListener);
        createObjectButton.addActionListener(createObjectButtonListener);
        drawOrbitsButton.addActionListener(drawOrbitsButtonListener);
        onOfButton.addItemListener(onOfItemListener);
        simulationMainPanel.addMouseListener(mouseClickListener);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        simulationMainPanel.move();
    }

    public void start() {simulationMainPanel.start();}
    public void stop() {simulationMainPanel.stop();}

    ItemListener onOfItemListener = new ItemListener()
    {
        public void itemStateChanged(ItemEvent itemEvent)
        {
            int state = itemEvent.getStateChange();
            if (state == ItemEvent.SELECTED)
            {
                stop();
                writeTerminalPlanets();
            }
            else
            {
                start();
                simulationMainPanel.move();
            }
        }
    };

    ActionListener drawOrbitsButtonListener = new ActionListener()  //Karabowicz
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            drawOrbits = true;
        }
    };

    ActionListener backgroundColorListener = new ActionListener()  //Karabowicz
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            int selected = ((JComboBox)e.getSource()).getSelectedIndex();
            if (selected == 0) {
                simulationMainPanel.setBackground(Color.DARK_GRAY);
            }
            else if (selected == 1) {
                simulationMainPanel.setBackground(Color.BLACK);
            }
            else if (selected == 2) {
                simulationMainPanel.setBackground(Color.DARK_GRAY);
            }
            else if (selected == 3) {
                simulationMainPanel.setBackground(Color.GRAY);
            }
            else if (selected == 4) {
                simulationMainPanel.setBackground(Color.WHITE);
            }
        }
    };


    MouseListener mouseClickListener = new MouseListener()
    {
        @Override
        public void mouseClicked(MouseEvent e) {  }

        @Override
        public void mousePressed(MouseEvent e)
        {
            if(mouseClick==null) { mouseClick = new CelestialBodyPosition( e.getX() , e.getY() ); }
            else
            {
                mouseClick.setX( e.getX() );
                mouseClick.setY( e.getY() );
            }
            System.out.println("Kliknięto: "+mouseClick.getX()+"  "+mouseClick.getY());
        }

        @Override
        public void mouseReleased(MouseEvent e) { }

        @Override
        public void mouseEntered(MouseEvent e) { }

        @Override
        public void mouseExited(MouseEvent e) {   }
    };

    ActionListener objectTypeChooserListener = new ActionListener() //Cybulska
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            int selected = ((JComboBox)e.getSource()).getSelectedIndex();
            if (selected == 0) { choosenObject="!"; }
            else if (selected == 1)
            {
                choosenObject = "Planet";
            }
            else if (selected == 2)
            {
                choosenObject ="Sun";
            }
            else if (selected == 3)
            {
                choosenObject ="Moon";
            }

        }
    };

    ActionListener createObjectButtonListener = new ActionListener() //Cybulska
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            double mass = 0;
            double radius = 0;

            try
            {
                mass = Double.parseDouble(massField.getText() ); //masa

                if(objectLocationRadiusChooser.isSelected()) { radius = Double.parseDouble(radiusField.getText()); } //promien
                else if (objectLocationRandomChooser.isSelected())
                {
                    if(mouseClick == null)
                    {
                        JOptionPane.showMessageDialog (null,"Click on the simulation to chose radius.","Not choosen radius.",JOptionPane.ERROR_MESSAGE );
                    }
                    else
                    {
                        int sunX = ( simulationMainPanel.getWidth()/2 );
                        int sunY = ( simulationMainPanel.getHeight()/2 );
                        int x,y;

                        if(sunX > mouseClick.getX() ) x=sunX-mouseClick.getX();
                        else x= mouseClick.getX()-sunX;

                        if(sunY > mouseClick.getY() ) y=sunY-mouseClick.getY();
                        else y= mouseClick.getY()-sunY;
                        radius = (int)Math.sqrt( pow(x,2)+pow(y,2) );
                    }
                }

                //Tworzenie obiektow
                if(choosenObject=="!")
                {
                    System.out.println("Nie wybrano obiektu!");
                    JOptionPane.showMessageDialog (null,"Choose object type.","Not choosen type of object.",JOptionPane.ERROR_MESSAGE );
                }
                if(choosenObject=="Planet")
                {
                    if(sun == null) { planetArrayList.add(new Planet(centerPosition,(int)radius,3000,choosenObjectColor,mass,1)); }
                    else { planetArrayList.add(new Planet(centerPosition,(int)radius,3000,choosenObjectColor,mass,sun.getMass())); }
                    simulationMainPanel.repaint();
                    System.out.println("Pomyślnie dodano planete!!!");
                }
                if(choosenObject=="Sun")
                {
                 /**
                 * Notes:
                 * Zapisałam słońce oddzielnie jako nowy obiekt
                 * + położenie słońca jest generowane automatycznie -> środek simulationMainPanel
                 * + można dodać tylko jedno słońce
                 * + użytkownik przy próbie ponownego dodania słońca ma możliwość zmiany masy aktualnego słońca
                 */
                    if(sun != null)
                    {
                        Double d = sun.getMass();
                        String s = (String)JOptionPane.showInputDialog (null,"Its impossible to add another sun,\n " +
                                "but you can change its mass.","Sun already exist.",JOptionPane.INFORMATION_MESSAGE,null ,null,massField.getText());
                        while ((s != null) && (s.length() > 0))
                        {
                            try { d = Double.parseDouble(s); break;}
                            catch (NumberFormatException ex) { s = (String)JOptionPane.showInputDialog (null,"Its not a mass for the sun."
                                    ,"Sun already exist.",JOptionPane.INFORMATION_MESSAGE,null ,null,massField.getText()); }
                        }
                        sun.setMass(d);
                        for(int i=0; i<planetArrayList.size()-1; i++) {planetArrayList.get(i).updateSpeed(d); }
                    }
                    else
                    {
                        centerPosition.setX(simulationMainPanel.getWidth()/2);
                        centerPosition.setY(simulationMainPanel.getHeight()/2);
                        sun = new Sun(centerPosition,choosenObjectColor,mass);
                        simulationMainPanel.repaint();
                        System.out.println("Pomyślnie dodano slonce!!!");
                    }
                }
                if(choosenObject=="Moon")
                {
                    /**
                     * Notes:
                     * Księżyc można dodoać tylko do ostatnio dodanej planety
                     * -> wtedy po dodaniu kolejnej nie ma możliwości dodawania książyców do poprzedniej
                     */
                    if(planetArrayList.isEmpty()) { JOptionPane.showMessageDialog (null,"Its impossible to add a moon to the planet that don't exist\n" +
                            "add planet and try again.","Planet don't exist.",JOptionPane.ERROR_MESSAGE ); }
                    else
                    {
                        planetArrayList.get(planetArrayList.size()-1).addMoon((int)radius,3000, choosenObjectColor, mass, 5 );
                        simulationMainPanel.repaint();
                        // planetArrayList.add(new Planet(center,(int)radius,3000,choosenObjectColor,mass,8));
                        System.out.println("Pomyślnie dodano ksiezyc!!!");
                    }
                }
            }
            catch (NumberFormatException ex)
            {
                System.out.println("Wrong options!");
                JOptionPane.showMessageDialog (null,"Choose all correct settings.","Incorrect settings of new object.",JOptionPane.ERROR_MESSAGE );
            }

        }
    };

    public static void clean() //Cybulska
    {
        for(int i=0; i<planetArrayList.size()-1; i++)
        {
            planetArrayList.get(i).moons.clear();
        }
        planetArrayList.clear();
        sun = null;
        System.out.println("Cleaning is over.");
    }

    private void writeTerminalPlanets()
    {
        for(int i=0; i<planetArrayList.size(); i++)
        {
            System.out.println("Planeta "+(i+1)+"   masa: "+planetArrayList.get(i).getMass()+" promien: "+planetArrayList.get(i).getOrbitRadius());

            if(planetArrayList.get(i).moons != null)
            {
                System.out.println("!");
                for(int j=0; j<planetArrayList.get(i).moons.size(); j++)
                {
                    System.out.println("Księżyc: "+(j+1)+"  masa: "+planetArrayList.get(i).moons.get(j).getMass()+
                            " promien: "+planetArrayList.get(i).moons.get(j).getOrbitRadius() );
                }
            }

        }

    }


    private static void setTextToWrite()   //Karabowicz
    {
        textToWrite = "";
        textToWrite = textToWrite + "Sun " + "mass: " + sun.getMass() + "\n";

        for(int i=0; i<planetArrayList.size(); i++)
        {
                textToWrite = textToWrite + "Planeta "+(i+1)+"   masa: "+planetArrayList.get(i).getMass()+" promien: "+planetArrayList.get(i).getOrbitRadius() +" kolor: "+planetArrayList.get(i).getColor().getRGB()+ "\n";
            if(planetArrayList.get(i).moons != null)
            {
                //coś nie działa z zapisywaniem tej części
                for(int j=0; j<planetArrayList.get(i).moons.size(); j++)
                {
                    textToWrite = textToWrite + "Księżyc: "+(j+1)+"  masa: "+planetArrayList.get(i).moons.get(j).getMass()+
                            " promien: "+(int)planetArrayList.get(i).moons.get(j).getOrbitRadius()  +" kolor: "+planetArrayList.get(i).getColor().getRGB()+ "\n";
                }
            }

        }
    }


    public static void saveFile()  //Karabowicz
    {
        setTextToWrite();

        String fileName;

        //wybieranie pliku do otwarcia
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Wybierz plik");
        int returnVal = fileChooser.showDialog(null, "Wybierz");

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            fileName = String.valueOf(fileChooser.getSelectedFile().toPath());
        }
        else {
            fileName = "Open command cancelled by user." ;
        }

        FileWriter fileWriter = null;

        try{
            fileWriter = new FileWriter(fileName);
            BufferedWriter bw = new BufferedWriter(fileWriter);
            bw.write(textToWrite);

            bw.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void openFile() //Cybulska
    {
        String fileName;

        //wybieranie pliku do otwarcia
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Wybierz plik");
        int returnVal = fileChooser.showDialog(null, "Wybierz");

        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            fileName = String.valueOf(fileChooser.getSelectedFile().toPath());
        }
        else
        {
            fileName = "Open command cancelled by user." ;
        }

        FileReader fileReader ;
        String wczytane="";

        try
        {
            Random random = new Random();
            clean();
            fileReader = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fileReader);
            String masa="", liczba="";
            String[] w;
            while( (wczytane = br.readLine() ) != null)
            {
                if(wczytane.contains("Sun"))
                {
                    System.out.println(wczytane.length());
                    for(int i=10; i<wczytane.length(); i++)
                    {
                        masa+=wczytane.toCharArray()[i];
                    }
                    centerPosition.setX(simulationMainPanel.getWidth()/2);
                    centerPosition.setY(simulationMainPanel.getHeight()/2);
                    sun = new Sun(centerPosition,Color.YELLOW,Double.parseDouble(masa));
                }
                if(wczytane.contains("Planeta"))
                {
                    w = wczytane.split(" ");
                    System.out.println(w[1]);
                    liczba = w[1];
                    if(sun == null) { planetArrayList.add(new Planet(centerPosition,Integer.parseInt(w[7]),3000,new Color(Integer.parseInt(w[9])),Double.parseDouble(w[5]),1)); }
                    else { planetArrayList.add(new Planet(centerPosition,Integer.parseInt(w[7]),3000,new Color(Integer.parseInt(w[9])),Double.parseDouble(w[5]),sun.getMass())); }
                }
                if(wczytane.contains("Księżyc"))
                {
                    w = wczytane.split(" ");
                    planetArrayList.get(Integer.parseInt( liczba)-1)
                            .addMoon(
                                    Integer.parseInt(w[6]),
                                    3000,
                                    new Color(Integer.parseInt(w[8])),
                                    Double.parseDouble(w[4]),
                                    5);
                }
                System.out.println( wczytane);

            }
            br.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


}
