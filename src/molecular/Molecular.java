/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package molecular;

import JFUtils.Input;
import JFUtils.point.Point2D;
import JFUtils.point.Point2Int;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author jonnelafin
 */
public class Molecular extends JFrame{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("Using JFUtils v." + JFUtils.versionCheck.version);
        new Molecular();
    }
    
    LinkedList<atom> atoms = new LinkedList<>();
    //public LinkedList<String> locations = new LinkedList<>();
    
    
    //GLOBALS
    boolean assimilation = false;
    
    
    JPanel screen = new Screen(this);
    Input input;
    public Molecular() throws HeadlessException {
        setSize(500, 500);
        setTitle("Molecular by Jonnelafin");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        input = new Input(new active());
        
        //screen.setBackground(Color.BLACK);
        this.add(screen);
        
        this.addKeyListener(input);
        this.addMouseListener(input);
        this.addMouseWheelListener(input);
        screen.addMouseMotionListener(input);
        this.requestFocusInWindow();
        //input.verbodose = true;
        setVisible(true);
        startLogic();
    }
    
    long sleep = (long) 0;
    
    int ticks = 0;
    
    int res = 4;
    
    void startLogic(){
        //atoms.add(new atom(new Point2Int(10, 10), this));
        while(true){
            tick();
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException ex) {
                Logger.getLogger(Molecular.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    boolean spawn = true;
    
    public int getW(){
        return this.getWidth()/res;
    }
    public int getH(){
        return this.getHeight()/res;
    }
    int brush = 0;
    void tick(){
        spawn = true;
        atoms.forEach(l -> {
            l.update();
            if(l.position.x == 10 && l.position.y == 10){
                spawn = false;
            }
        });
        if(ticks < 1200 && spawn){
            //atoms.add(new atom(new Point2Int(10, 10), this));
        }
        
        if(input.keys[32]){
            atom atomz = new atom(new Point2Int(input.mouseX()/res, input.mouseY()/res), this);
            atomz.state = brush;
            atomz.update();
            if(!atomz.colliding(atomz.position)){
                atoms.add(atomz);
            }
        }
        if (input.keys[82]) {
            atoms.forEach(l->l.up = -1);
        }
        if (input.keys[84]) {
            atoms.forEach(l->l.up = 1);
        }
        //f
        if (input.keys[70]) {
            atoms.forEach(l->l.state = 0);
        }
        //d
        if (input.keys[68]) {
            atoms.forEach(l->l.state = 1);
        }
        //q
        if (input.keys[81]) {
            atoms.removeAll(atoms);
        }
        //1
        if (input.keys[49]) {
            brush = 0;
        }
        //2
        if (input.keys[49+1]) {
            brush = 1;
        }
        //3
        if (input.keys[49+2]) {
            brush = 2;
        }
        //4
        if (input.keys[49+3]) {
            brush = 3;
        }
        //5
        if (input.keys[49+4]) {
            brush = 4;
        }
        
        ticks++;
    }
    
    
}
class active extends JFUtils.InputActivated{

    public active() {
    }
    
}