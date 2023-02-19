package com.berserk112.nowcodereditor.renderer;


import com.berserk112.nowcodereditor.model.Config;
import com.berserk112.nowcodereditor.model.Question;
import com.berserk112.nowcodereditor.setting.NowCoderPersistentConfig;
import com.intellij.ide.util.treeView.NodeRenderer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author shuzijun
 */
public class CustomTreeCellRenderer extends NodeRenderer {

    private static Color Level1 = new Color(153, 153, 153);
    private static Color Level2 = new Color(59, 151, 255);
    private static Color Level3 = new Color(50, 202, 153);
    private static Color Level4 = new Color(255, 170, 32);
    private static Color Level5 = new Color(255, 86, 28);

    public CustomTreeCellRenderer() {
        loaColor();
    }

    public static void loaColor(){
        Config config = NowCoderPersistentConfig.getInstance().getInitConfig();
        if (config != null) {
            Color[] colors = config.getFormatLevelColour();
            Level1 = colors[0];
            Level2 = colors[1];
            Level3 = colors[2];
            Level4 = colors[3];
            Level5 = colors[4];
        }
    }

    public static BufferedImage getResourceBufferedImage(String filePath) {
        if (CustomTreeCellRenderer.class.getClassLoader().getResourceAsStream(filePath) != null) {
            try {
                return ImageIO.read(CustomTreeCellRenderer.class.getClassLoader().getResourceAsStream(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return com.intellij.util.ui.UIUtil.createImage(10, 10, 1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void customizeCellRenderer(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {

        super.customizeCellRenderer(tree, value, selected, expanded, leaf, row, hasFocus);

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        Question question = (Question) node.getUserObject();

        if (question.getLevel() == null) {

        } else if (question.getLevel() == 0) {
            setForeground(Level1);
        } else if (question.getLevel() == 1) {
            setForeground(Level2);
        } else if (question.getLevel() == 2) {
            setForeground(Level3);
        } else if (question.getLevel() == 3) {
            setForeground(Level4);
        } else if (question.getLevel() == 4) {
            setForeground(Level5);
        }
    }
}