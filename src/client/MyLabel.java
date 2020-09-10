/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

/**
 *
 * @author ADMIN
 */
public final class MyLabel extends JLabel {

    public MyLabel() {
        setHorizontalAlignment(JLabel.CENTER);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        String name = this.getFont().getName();
        int style = this.getFont().getStyle();
        int size = this.getFont().getSize() + 37;
        setFont(new Font(name, style, size));
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(49, 44);
    }
}
