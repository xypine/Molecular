/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package molecular;

import JFUtils.point.Point2D;
import JFUtils.point.Point2Int;
import java.awt.Color;
import java.util.Random;

/**
 *
 * @author Jonnelafin
 */
public class atom {
    public Point2Int position;
    public Point2Int velocity = new Point2Int(0, 0);
    public float mass = 1F;
    public float id;
    
    private Molecular parent;
    
    public atom(Point2Int position, Molecular parent) {
        this.position = position;
        this.parent = parent;
        this.id = new Random().nextFloat();
    }
    
    public int up = -1;
    
    public Color color = Color.white;
    
    /**
     * 0: solid, like rock
     * 1: liquid, like water
     * 2: anti-solid, like rock but with inverse gravity
     * 3: gas, like liquid but with inverse gravity
     * 4: no gravity
     */
    public int state = 0;
    
    Point2Int[] moves = new Point2Int[]{};
    
    int side = 1;
    
    public void updateMoves(){
        int gm = up;
        int sm = 1;
        //side = -side;
        if(state == 2 || state == 3){
            gm = -gm; //Invert gravity
        }
        if (state == 4){
            gm = 0;
        }
        if(state == 0 || state == 2){
            sm = 0; //Do not use sidesteps
        }
        if(escape_nocoll){
            gm = -gm;
            //sm = 0;
        }
        moves = new Point2Int[]{
            new Point2D(0, -gm),
            new Point2D(-side, -gm),
            new Point2D(side, -gm),
            //new Point2D(-1, 0),
            //new Point2D(1, 0),
        };
        if(sm == 0){
            return;
        }
        moves = new Point2Int[]{
            new Point2D(0, -gm),
            new Point2D(-side, -gm),
            new Point2D(side, -gm),
            new Point2D(-side, 0),
            new Point2D(side, 0),
        };
    }
    public void uColor(){
        switch(state){
            case 0:
                color = Color.white;
                break;
            case 1:
                color = Color.BLUE;
                break;
            case 2:
                color = Color.gray;
                break;
            case 3:
                color = Color.red;
                break;
            case 4:
                color = Color.green;
                break;
        }
        if(escape_nocoll){
            color = Color.LIGHT_GRAY;
        }
    }
    
    public void update(){
        updateMoves();
        uColor();
        if(state == 5){
            return;
        }
        //velocity.y = (int) (velocity.y + mass * 10);
        
        
        if(!inside(position)){
            position = Point2Int.subtract(position, velocity);
        }
        if((state == 1 || state == 3) && colliding(position)){
            escape_nocoll = true;
        }
        
        for (Point2Int move : moves) {
            Point2Int vel2 = Point2Int.add(velocity, move);
            vel2 = move;
            Point2Int future = Point2Int.add(position, vel2);
            //System.out.println(colliding());
            colliding(future);
            if (inside(future) && !soft_coll) {
                velocity = vel2;
                position = future;
                break;
            }
        }
    }

    public boolean inside(Point2Int position) {
        boolean out = true;
        if(!(position.x > 0 && position.y > 0)){
            out = false;
        }
        if(!(position.x < parent.getW()-(parent.getW()/10)*1 && position.y < parent.getH()-(parent.getH()/10)*1.5)){
            out = false;
        }
        return out;
    }
    
    boolean escape_nocoll = false;
    private atom collider = null;
    
    boolean soft_coll = false;
    public boolean colliding(Point2Int position) {
        
        boolean out = false;
        collider = null;
        boolean Tout = false;
        atom Tcoll = null;
        int c = 0;
        int c2 = 0;
        for(atom i : parent.atoms){
            if(i.id != id){
                if(i.position.x == position.x && i.position.y == position.y){
                    if( !((i.state == 1 || i.state == 3) && (state == 0 || state == 2)) ){
                        out = true;
                        collider = i;
                    }
                    if (!i.escape_nocoll) {
                        if (((state == 1 || state == 3) && (i.state == 0 || i.state == 2))) {
                            //escape_nocoll = true;
                            c++;
                        } else {
                        }
                        Tout = true;
                        Tcoll = i;
                    }
                    if( (state == 1 || state == 3) && i.escape_nocoll){
                        //c2++;
                    }
                    
                    
                }
            }
        }
       // escape_nocoll = false;
        if(c == 0){
            escape_nocoll = false;
        }
        if(Tout && parent.assimilation){
            if(state == 3 && Tcoll.state != 0 && Tcoll.state != 2){
                Tcoll.state = 3;
            }
            if(state == 0 && Tcoll.state == 3){
                Tcoll.state = 0;
                try {
                    Tcoll.collider.state = 0;
                } catch (Exception e) {
                }
            }
        }
        if(escape_nocoll && c2 == 0){
            //return false;
        }
        soft_coll = out;
        return Tout;
    }
    
}
