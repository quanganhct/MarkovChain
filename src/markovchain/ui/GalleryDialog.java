/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package markovchain.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

/**
 *
 * @author guillaumerebmann
 */
public class GalleryDialog extends JDialog implements ActionListener {

    private JPanel jpanelTopGallery;
    private JPanel jpanelBotGallery;
    private JPanel jpanelCenterGallery;
    public JButton close;
    public JButton open;
    public JButton delete;
    private JFrame _mainFrame;
    private List<GalleryComponent> listJlabelGallery;
    private JLabel full;
    private JScrollPane scrollPane;
    List<String> result = new ArrayList<String>();
    
  public GalleryDialog(MainFrame _mainFrame) throws InterruptedException {
         super(_mainFrame,"Gallery",true);
            this._mainFrame=_mainFrame;
            this.listJlabelGallery=_mainFrame.listJlabelGallery;
            jpanelTopGallery = new JPanel();
            jpanelCenterGallery =new JPanel();
            jpanelBotGallery = new JPanel();
            full = new JLabel();
            result.add("");
            result.add("");
            open = new JButton("Open");
            delete = new JButton("Delete");
            close = new JButton("Close");
            
            open.addActionListener(this);
            delete.addActionListener(this);
            close.addActionListener(this);
        // Init list of JLabel (For the picture)
        
        // Essai
            for (GalleryComponent e : listJlabelGallery) {
               e.connect(this);
               jpanelTopGallery.add(e);
                     
            }
           
        // Fin essai    
        
            // Ajout des Images;
            
            
            scrollPane = new JScrollPane(jpanelTopGallery);
            scrollPane.setMaximumSize(new Dimension(10000, 150));
            scrollPane.setPreferredSize(new Dimension(600, 150));
            scrollPane.setMinimumSize(new Dimension(1, 150));
            
            jpanelCenterGallery.setPreferredSize(new Dimension(600, 500));
            jpanelCenterGallery.add(full);
            
            jpanelBotGallery.setPreferredSize(new Dimension(600, 50));
            jpanelBotGallery.add(close);
            jpanelBotGallery.add(delete);
            jpanelBotGallery.add(open);
      
     add(scrollPane,BorderLayout.NORTH);
     add(jpanelCenterGallery,BorderLayout.CENTER);
     add(jpanelBotGallery, BorderLayout.SOUTH);
     
     // Select the first component
     if(!listJlabelGallery.isEmpty()){
        
        selectComponent(listJlabelGallery.get(0));
     }
   
     //Affichage
     this.
     setSize(new Dimension(600, 500));
     setMinimumSize(new Dimension(600, 500));
    
     pack();
     _mainFrame.validate();
     
     // Begining of Harry Potter Code
     /*new Thread(new Runnable() {
        @Override public void run() {
            try {
                Thread.sleep(500);
                repaint();
            } catch (InterruptedException ex) {
                Logger.getLogger(GalleryDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }).start();
     */
     // End of Harry potter Code
     
     setVisible(true);
     //select first
     
  }
  
    
    @Override
  public void actionPerformed(ActionEvent e) {
      result.set(0, "");
      if(listJlabelGallery.size() != 0){
          GalleryComponent gc = getSelectComponent();
            result.set(1, gc.resource);
      }
      
      
      if(e.getSource() == close){
          result.set(0, "close");
         dispose(); 
      }else if(e.getSource() == delete){
          result.set(0, "delete");
          dispose(); 
      }else{
          result.set(0, "open");
          dispose();
      }
      
  }
  
  public List<String> getResult(){
      return result;
  }
  
  public void selectComponent(GalleryComponent g){
      for (GalleryComponent e : listJlabelGallery) {
                e.actif=false;
                e.setBorder(e.blackline);
                if(e == g){
                    System.out.println("We are equal");
                     e.setBorder(e.redline);
                     e.actif=true;
                     full.setIcon(e.iconLarge);
                     
                }
                e.updateUI();
                     
            }
    
      
  }
  
  public GalleryComponent getSelectComponent(){
      GalleryComponent comp = null;
       for (GalleryComponent e : listJlabelGallery) {
                if(e.actif == true){
                    comp = e;
                }
                e.updateUI();
                     
            }
       return comp;
  }
  
}