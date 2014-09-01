/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package markovchain.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.Border;

/**
 *
 * @author guillaumerebmann
 */
public final class GalleryComponent extends JLabel implements MouseListener{
     public String picture;
     public Icon icon;
     public String title;
     public Icon iconLarge;
     public String resource;
     private GalleryDialog gallery;
     public Border blackline = BorderFactory.createLineBorder(Color.black);
     public Border redline = BorderFactory.createLineBorder(Color.red);
     public Boolean actif = false;
     
    public GalleryComponent(String picture,String resource,String title){
        super(AbstractTaskView.getResizableIconFromGallery(picture, 200, 100));
        this.picture = picture;
        this.title = title;
        this.resource=resource;
        this.icon = AbstractTaskView.getResizableIconFromGallery(picture, 200, 100);
        this.iconLarge = AbstractTaskView.getResizableIconFromGallery(picture, 600, 500);
        this.setBorder(blackline);
        this.addMouseListener(this);
        
        
    }
    
        @Override
        protected void paintComponent(Graphics g)
    {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D)g;
            drawSt(g2);
            
        
    }
    
    public void drawSt(Graphics2D g2){
        
            g2.setColor(Color.RED);          
            g2.drawString(this.title, 20, 20);
    }
        
        
    public void connect(GalleryDialog gallery){
        this.gallery=gallery;
    }

    

    @Override
    public void mouseClicked(MouseEvent e) {
        Graphics g2d = this.getGraphics();
        paintComponent(g2d);
        gallery.selectComponent(this);
        this.actif=true;
        gallery.repaint();
        System.out.println("Select component");
    }

    @Override
    public void mousePressed(MouseEvent e) {
       
    }

    @Override
    public void mouseReleased(MouseEvent e) {
          
    }
    @Override
    public void mouseEntered(MouseEvent e) {
     
    }

    @Override
    public void mouseExited(MouseEvent e) {
    
    }
    
}
