package pojavaKarabowiczCybulska.gui;
import pojavaKarabowiczCybulska.simulation.SimulationMainPanel;
import pojavaKarabowiczCybulska.universe.CelestialBodyPosition;
import pojavaKarabowiczCybulska.universe.Planet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


public class GuiPanel extends JPanel implements ActionListener //Karabowicz
{
    JButton changeObjectColorButton, createObjectButton, drawOrbitsButton;
    JToggleButton onOfButton;
    JTextField massField, radiusField;
    JComboBox objectTypeChooser, backgroundColorChooser;
    JLabel objectLocationLabel, massLabel;
    JRadioButton objectLocationRadiusChooser;
    JRadioButton objectLocationRandomChooser;


    Color choosenObjectColor;
    String choosenObject;

    CelestialBodyPosition mouseClick;

    public static Planet sun;

    public static ArrayList<Planet> planetArrayList; //lista przechowująca planety

    public static CelestialBodyPosition centerPosition; //położenie środka (tam gdzie chcemy umieścić słońce)


    private static SimulationMainPanel simulationMainPanel;

    public GuiPanel()
    {
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
                ( e -> choosenObjectColor = JColorChooser.showDialog(null, "Choose Object Color", Color.yellow) );


        backgroundColorChooser.addActionListener(backgroundColorListener);
        objectTypeChooser.addActionListener(objectTypeChooserListener);
        createObjectButton.addActionListener(createObjectButtonListener);
        //drawOrbitsButton.addActionListener(drawOrbitsButtonListener);
        onOfButton.addItemListener(onOfItemListener);
        simulationMainPanel.addMouseListener(mouseClickListener);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        simulationMainPanel.move(); //nie wiem czy to powinno być tutaj ??????????????
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
                System.out.println("Selected");
            }
            else
            {
                start();
                System.out.println("Deselected");
            }
        }
    };

    ActionListener backgroundColorListener = new ActionListener()
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
                System.out.println("----test1---");
            }
            else if (selected == 2) {
                simulationMainPanel.setBackground(Color.DARK_GRAY);
                System.out.println("---test2----");
            }
            else if (selected == 3) {
                simulationMainPanel.setBackground(Color.GRAY);
            }
            else if (selected == 4) {
                simulationMainPanel.setBackground(Color.WHITE);
            }
        }
    };

    /**
     * Ta funkcja jest jeszcze do przemyslenia
     * bo
     * 1    mozna zrobić to tak, że bedzie dodawało obiekt w momencie kliknięcia
     * i wtedy będziemy miały dwa powtarzające się fragmenty kodu
     * (lub się ten fragment po prostu do funkcji wrzuci i sie bedzie ja wywolywac)
     * i będzie trzeba pamiętać o słońcu i księżycach
     *
     * 2    można czekać na kliknięcie przycisku i wtedy bez wiekszych problemów
     * tylko będzie trzeba poinformować uzytkownika
     *
     */
    MouseListener mouseClickListener = new MouseListener()
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            if(mouseClick==null) { mouseClick = new CelestialBodyPosition( e.getX() , e.getY() ); }
            else
            {
                mouseClick.setX( e.getX() );
                mouseClick.setY( e.getY() );
            }

        }

        @Override
        public void mousePressed(MouseEvent e) { }

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
                System.out.println("----planeta---");
            }
            else if (selected == 2)
            {
                choosenObject ="Sun";
                System.out.println("---slonce----");
            }
            else if (selected == 3)
            {
                choosenObject ="Moon";
                System.out.println("---ksiezyc----");
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
                System.out.println("Masa: "+mass);

                if(objectLocationRadiusChooser.isSelected()) { radius = Double.parseDouble(radiusField.getText()); } //promien
                else { radius = 30.5; }
                System.out.println("Promien: "+ radius);

                if(objectLocationRandomChooser.isSelected()) { /* Potrzebna funkcja obliczjąca promień na podstawie punktu kliknięcia i położenia środka*/ } //Nie wiem czy tak na pewno powinno być

               /* else { center = new CelestialBodyPosition(450, 300); }
                System.out.println("Srodek: "+center.getX()+" "+center.getY() );  */


                //Tworzenie obiektu
                if(choosenObject=="!")
                {
                    System.out.println("Nie wybrano obiektu!");
                    JOptionPane.showMessageDialog (null,"Choose object type.","Not choosen type of object.",JOptionPane.ERROR_MESSAGE );
                }
                if(choosenObject=="Planet")
                {
                    planetArrayList.add(new Planet(centerPosition,(int)radius,3000,choosenObjectColor,mass,20));
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
                    }
                    else
                    {
                        centerPosition.setX(simulationMainPanel.getWidth());
                        centerPosition.setY(simulationMainPanel.getHeight());
                        sun = new Planet(centerPosition,(int)radius,3000,choosenObjectColor,mass,20); //Dodaje słońce tak jak planete
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
                       // planetArrayList.get(planetArrayList.size()-1).addMoon(      );
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


}