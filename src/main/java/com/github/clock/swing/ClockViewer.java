package com.github.clock.swing;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JFrame;

import com.github.clock.Args;
import com.github.clock.Clock;
import com.github.clock.UpdateObserver;

public class ClockViewer extends JComponent{
    private static final long serialVersionUID = 851223340594030326L;

    private Clock clock;
    private Image background;
    private boolean debugMode = false;
    private Args args;

    public ClockViewer(Clock clock, Args args){
        this.args = args;
        clock.addUpdateObserver(new UpdateObserver(){
            @Override
            public void update(Clock clock) {
                repaint();
            }
        });
        this.clock = clock;
        clock.start();
        this.addComponentListener(new ComponentAdapter(){
            @Override
            public void componentResized(ComponentEvent e) {
                background = null;
            }
        });
    }

    public void setDebugMode(boolean debugMode){
        this.debugMode = debugMode;
    }

    public boolean isDebugMode(){
        return debugMode;
    }

    public void showClock(){
        JFrame frame = new JFrame();
        frame.add(this, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                clock.stop();
            }
        });
        frame.setVisible(true);
    }

    @Override
    public Dimension getPreferredSize(){
        return new Dimension(300, 300);
    }

    @Override
    protected void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        if(background == null){
            background = createBackgroundImage();
        }
        g2.drawImage(background, 0, 0, null);

        drawHands(g2);
    }

    private double getLength(){
        Dimension size = getSize();

        double length = size.getWidth();
        if(length > size.getHeight()){
            length = size.getHeight();
        }

        return length / 2;
    }

    private void drawHands(Graphics2D g2){
        Dimension size = getSize();

        double length = getLength();
        Point2D center = new Point2D.Double(size.getWidth() / 2, size.getHeight() / 2);

        int hour = clock.getHour();
        int minute = clock.getMinute();
        int second = clock.getSecond();
        int hourPosition = (hour % 12) * 5 + minute / 12;

        if(isDebugMode()){
            System.out.printf(
                "%02d:%02d:%02d -> (%02d, %02d, %02d)%n",
                hour, minute, second, hourPosition, minute, second
            );
        }

        g2.translate(center.getX(), center.getY());
        drawHand(g2, clock.getSecond(), length * 0.8);

        g2.setStroke(new BasicStroke(2));
        g2.setColor(Color.decode(args.getLongHandColor()));
        drawHand(g2, clock.getMinute(), length * 0.7);
        // 長針の位置は，分の位置で表そうとすると，時間×5．
        // 24時間制のため，12で割った余りを時間とする．
        g2.setStroke(new BasicStroke(4));
        g2.setColor(Color.BLUE);
        drawHand(g2, hourPosition, length * 0.6);
    }

    private void drawHand(Graphics2D g2, int dest, double length){
        // 1分あたり，60度/360度 = PI/30.
        // 12の位置が - PI / 2.
        double angle = -Math.PI / 2 + (dest * (Math.PI / 30));
        double x = length * Math.cos(angle);
        double y = length * Math.sin(angle);

        g2.draw(new Line2D.Double(0, 0, x, y));
    }

    private Image createBackgroundImage(){
        Dimension size = getSize();
        Image image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = (Graphics2D)image.getGraphics();
        double length = getLength();
        length = length * 0.9;
        Point2D center = new Point2D.Double(size.getWidth() / 2, size.getHeight() / 2);

        double angle = - Math.PI / 3;
        g.translate(center.getX(), center.getY());
        g.setColor(Color.BLACK);
        for(int i = 1; i <= 12; i++){
            double x = length * Math.cos(angle);
            double y = length * Math.sin(angle);

            angle = angle + Math.PI / 6;
            g.drawString("" + i, (int)x, (int)y);
        }
        if(isDebugMode()){
            System.out.printf("create new background image: %s%n", size);
        }

        return image;
    }
}
