package client;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {
    public ControlPanel(JButton quit, JLabel countdown,JLabel info){
        JPanel quitPanel = new JPanel();
        quitPanel.setLayout(new FlowLayout());
        quitPanel.add(quit);
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new FlowLayout());
        infoPanel.add(info);
        JPanel timePanel = new JPanel();
        timePanel.add(countdown);
        setLayout(new GridLayout(3,1));
        add(infoPanel);
        add(timePanel);
        add(quitPanel);

    }
}
