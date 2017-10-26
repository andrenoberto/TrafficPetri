package com.trafficlights;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class MessageWithLink extends JEditorPane {

    public MessageWithLink(String htmlBody) {
        super("text/html", "<html><body style=\"" + getStyle() + "\">" + htmlBody + "</body></html>");
        addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
                    // Process the click event on the link (for example with java.awt.Desktop.getDesktop().browse())
                    URI uri = URI.create(String.valueOf(e.getURL()));
                    try {
                        java.awt.Desktop.getDesktop().browse(uri);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        setEditable(false);
        setBorder(null);
    }

    static StringBuffer getStyle() {
        // for copying style
        JLabel label = new JLabel();
        Font font = label.getFont();
        Color color = label.getBackground();

        // create some css from the label's font
        StringBuffer style = new StringBuffer("font-family:" + font.getFamily() + ";");
        style.append("font-weight:").append(font.isBold() ? "bold" : "normal").append(";");
        style.append("font-size:").append(font.getSize()).append("pt;");
        style.append("background-color: rgb(").append(color.getRed()).append(",").append(color.getGreen()).append(",").append(color.getBlue()).append(");");
        return style;
    }
}
