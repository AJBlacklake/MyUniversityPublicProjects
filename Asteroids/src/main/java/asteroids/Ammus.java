/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asteroids;

import java.util.Timer;
import java.util.TimerTask;
import javafx.scene.shape.Polygon;

/**
 *
 * @author arvim
 */
public class Ammus extends Hahmo{
    
    public boolean tuhoutuu = false;
    
    public Ammus( int x, int y) {
        super(new Polygon(2, -2, 2, 2, -2, 2, -2, -2), x, y);
    }
    
    public void poistamisAjastin() {
        
        int luku = 0;
        
        Timer ajastin = new Timer();
        
        TimerTask tehtava = new TimerTask() {
            public void run(){
                ammusTuhoutuu();
            }
        };
        
        ajastin.schedule(tehtava, 3000);
        
    }
    
    public void ammusTuhoutuu() {
        this.tuhoutuu = true;
        this.setElossa(false);
    }
    
    
}
