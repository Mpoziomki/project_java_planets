package pojavaKarabowiczCybulska.universe;

import pojavaKarabowiczCybulska.universe.CelestialBodyOrbit;
import pojavaKarabowiczCybulska.universe.CelestialBodyPosition;

import java.awt.*;
import java.util.ArrayList;

import static java.lang.Math.pow;

public class Planet extends CelestialBodyOrbit
{
    private int numOrbits = 0;
    private final int g = 1;

    public ArrayList<Moon> moons;

    public void addMoon(int orbitRadius, double orbitTime, Color color, double mass, int size )
    {
        Moon m = new Moon( this.orbitCentre, orbitRadius, orbitTime, color, mass, this.mass, size) ;
        System.out.println("Moons"+size );
        moons.add(m);

    }

    public Planet(CelestialBodyPosition orbitCentre, int orbitRadius, double orbitTime, Color colour, double mass, double sunMass, int size)
    {
        super();
        moons = new ArrayList<>();
        this.colour = colour;
        this.size = size;
        this.mass = mass;
        this.orbitCentre = orbitCentre; //położenie srodka
        this.orbitRadius = orbitRadius; //promien
        this.orbitPeriod = orbitTime;//
        this.position = new CelestialBodyPosition(orbitCentre.getX(),orbitCentre.getY()+this.orbitRadius);
        this.angularSpeed = Math.sqrt( (6.67*pow(10,-14)*sunMass) / pow(this.orbitRadius,3) ) ;
        //this.angularSpeed = Math.sqrt(g * mass / orbitRadius / orbitRadius / orbitRadius);
    }



    /**
     * Napisałam aktualizowanie położenia planet i księżyców
     * @return
     */
    @Override
    public CelestialBodyPosition getPosition() { return this.position; }
    //Nowa pozycja -> obliczenie
    public void updatePosition()
    {

        this.orbitAngle += this.angularSpeed;

        if(this.orbitAngle > Math.PI * 2)
        {
            this.orbitAngle %= Math.PI * 2;
            updateOrbits();
        }
        // Nowe zmienne x, y
        this.position.setX((int)((Math.cos(this.orbitAngle) * this.orbitRadius) + this.orbitCentre.getX()));
        this.position.setY((int)((Math.sin(this.orbitAngle) * this.orbitRadius) + this.orbitCentre.getY()));

        for(int i=0;i<moons.size()-1;i++) { moons.get(i).updatePosition(this.position); }
    }

    public void updateSpeed(double sunMass){ this.angularSpeed = Math.sqrt( (6.67*pow(10,-14)*sunMass) / pow(this.orbitRadius,3) ) ; }

    public int getOrbitRadius(){
        return this.orbitRadius;
    }

    public int getOrbits() {
        return this.numOrbits;
    }
    private void updateOrbits() {
        numOrbits++;
    }
    public void setMass(double mass) { this.mass =mass; }
    public double getMass() { return this.mass; }

    public void paint(Graphics g)
    {
        g.setColor(this.colour);
        g.fillOval(position.getX(), position.getY(), size(), size());
    }

}
