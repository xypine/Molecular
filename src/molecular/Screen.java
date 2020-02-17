/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package molecular;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import javax.swing.JPanel;

/**
 *
 * @author Jonnelafin
 */
public class Screen extends JPanel{
    
    Molecular main;
    public Screen(Molecular parent) {
        main = parent;
        try {
            out = new BufferedImage(getParent().getWidth(), getParent().getHeight(), BufferedImage.TYPE_INT_RGB);
        } catch (Exception e) {
            out = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        }
    }
    BufferedImage out;
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        repaint();
        //paint(g);
        
        int w = getParent().getWidth();
        int h = getParent().getHeight();
        
        
        if(w != out.getWidth() || h != out.getHeight()){
            //System.out.println("Aspects do not match (p, i): " + w + ", " + h + " and " + out.getWidth() + ", " + out.getHeight());
            out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        }
        
        Graphics2D ga = out.createGraphics();
        ga.setColor(Color.black);
        ga.fillRect(0, 0, out.getWidth(), out.getHeight());
        
        ga.setColor(Color.blue);
        LinkedList<atom> clones = (LinkedList<atom>) main.atoms.clone();
        //System.out.println("/////////////////////////");
        clones.forEach(l -> {
            //System.out.println(l.position.represent());
            ga.setColor(l.color);
            //ga.drawOval(l.position.x*main.res, l.position.y*main.res, main.res, main.res);
            if(!l.escape_nocoll){
                ga.fillRect(l.position.x*main.res, l.position.y*main.res, main.res, main.res);
            }
            try {
                //out.setRGB(l.position.x, l.position.y, ga.getColor().getRGB());
            } catch (Exception e) {
                
            }
        });
        
        ga.setColor(Color.blue);
        ga.drawString("By Jonnelafin, under the MIT License", w/40, h/15);
        
        ga.setColor(Color.red);
        
        ga.drawString(main.atoms.size() + "", w/2, h/2);
        ga.setColor(Color.red);
        
        ga.drawString("use numbers to change the brush, t and r to change gravity, q to clear" + "", w/10, h-h/20);
        
        ga.setColor(Color.green);
        ga.drawString("space to paint" + "", w/10, h-h/35);
        g.drawImage(out, 0, 0, this);
    }
}
