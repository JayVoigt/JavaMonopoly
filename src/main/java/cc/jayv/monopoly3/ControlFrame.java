package cc.jayv.monopoly3;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class ControlFrame {
        SwingHelper helper;
        JInternalFrame frame;

        public ControlFrame() {
                frame = new JInternalFrame();
                frame.setLayout(new MigLayout());
                frame.setSize(400, 400);
                frame.setVisible(true);

                // Current player static
                JLabel staticLabelCurrentPlayer = new JLabel();
                staticLabelCurrentPlayer.setIcon(SwingHelper.getImageIconFromResource("/player-generic.png"));
                SwingHelper.formatLabel(staticLabelCurrentPlayer, "Current Player", SwingHelper.LabelStyles.TITLE_BOLD);
                frame.add(staticLabelCurrentPlayer, "align left, cell 0 0, width 150");

                // Current player
                JLabel labelCurrentPlayer = new JLabel();
                SwingHelper.formatLabel(labelCurrentPlayer, "n/a", SwingHelper.LabelStyles.TITLE_NO_CONTENT);
                frame.add(labelCurrentPlayer, "align right, cell 2 0, width 150, wrap");

                // Current balance static
                JLabel staticLabelCurrentBalance = new JLabel();
                staticLabelCurrentBalance.setIcon(SwingHelper.getImageIconFromResource("/money.png"));
                SwingHelper.formatLabel(staticLabelCurrentBalance, "Balance", SwingHelper.LabelStyles.TITLE_BOLD);
                frame.add(staticLabelCurrentBalance, "align left, cell 0 2, width 150");

                // Current balance
                JLabel labelCurrentBalance = new JLabel();
                SwingHelper.formatLabel(labelCurrentBalance, "n/a", SwingHelper.LabelStyles.TITLE_NO_CONTENT);
                frame.add(labelCurrentBalance, "align right, cell 2 2, width 150");

                // Current position static
                JLabel staticLabelCurrentPosition = new JLabel();
                staticLabelCurrentPosition.setIcon(SwingHelper.getImageIconFromResource("/properties.png"));
                SwingHelper.formatLabel(staticLabelCurrentPosition, "Position", SwingHelper.LabelStyles.TITLE_BOLD);
                frame.add(staticLabelCurrentPosition, "align left, cell 0 4");

                // Current position
                JLabel labelCurrentPosition = new JLabel();
                SwingHelper.formatLabel(labelCurrentPosition, "n/a", SwingHelper.LabelStyles.TITLE_NO_CONTENT);
                frame.add(labelCurrentPosition);

                frame.add(new JButton(), "cell 1 0, grow");
        }

        public JInternalFrame getFrame() {
                return frame;
        }

}
