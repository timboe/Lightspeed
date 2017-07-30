package com.timboe.lightspeed;

import javax.swing.*;
import java.applet.Applet;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Tim on 30/07/2017.
 */
public class main {
    public static void main(String[] args){
        JFrame frame=new JFrame();
        frame.setSize(1090,735);

        final Applet applet=new LIGHTSPEED();

        frame.getContentPane().add(applet);
        frame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                applet.stop();
                applet.destroy();
                System.exit(0);
            }
        });

        frame.setVisible(true);
        applet.init();
        applet.start();
    }
}
