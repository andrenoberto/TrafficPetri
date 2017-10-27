package com.trafficlights;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.io.IOException;
import java.net.URI;

class MessageWithLink extends JEditorPane {

    MessageWithLink(String htmlBody) {
        super("text/html", "<html><body style=\"" + getStyle() + "\">" + htmlBody + "</body></html>");
        addHyperlinkListener(e -> {
            if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
                // Process the click event on the link (for example with java.awt.Desktop.getDesktop().browse())
                URI uri = URI.create(String.valueOf(e.getURL()));
                try {
                    Desktop.getDesktop().browse(uri);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        setEditable(false);
        setBorder(null);
    }

    private static StringBuffer getStyle() {
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
