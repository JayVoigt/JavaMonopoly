package cc.jayv.monopoly3;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.ArrayList;

public class InfoFrame implements ViewComponent {

    JInternalFrame frame;
    JLabel labelPlayer1;
    JLabel labelPlayer2;
    JLabel labelPlayer3;
    JLabel labelPlayer4;

    ArrayList<JLabel> playerLabels;

    public InfoFrame() {
        frame = new JInternalFrame();
        frame.setLayout(new MigLayout());
        frame.setSize(308, 600);
        frame.setTitle("Information");
        frame.setFrameIcon(SwingHelper.getImageIconFromResource("/i.png"));
        frame.setVisible(true);

        playerLabels = new ArrayList<>();
        initLabels();
    }

    private void initLabels() {
        labelPlayer1 = new JLabel();
        labelPlayer2 = new JLabel();
        labelPlayer3 = new JLabel();
        labelPlayer4 = new JLabel();

        playerLabels.add(labelPlayer1);
        playerLabels.add(labelPlayer2);
        playerLabels.add(labelPlayer3);
        playerLabels.add(labelPlayer4);

        for (JLabel l : playerLabels) {
            SwingHelper.formatLabel(l, "Player " + (1 + playerLabels.indexOf(l)), SwingHelper.LabelStyles.NO_CONTENT);
        }
    }

    private class playerOverview {
        JPanel panel;

        JLabel labelPlayerName;
        JLabel labelStatus;
        Player player;

        public playerOverview(Player player) {
            this.player = player;

        }

        private void initComponents() {
            // Init panel
            panel = new JPanel();
            panel.setLayout(new MigLayout());

            // Init labels
            labelPlayerName = new JLabel(player.getCustomName());
            labelStatus = new JLabel();

        }

        private void arrangeComponents() {
            panel.setSize(308, 200);
            panel.add(labelPlayerName, "cell 0 0");
        }

        public void update(Board board) {

        }
    }

    @Override
    public void update(Board board) {
    }

    @Override
    public void setStateOfActionButton(Actions action, boolean isEnabled) {

    }

    @Override
    public JComponent getComponent() {
        return null;
    }

    public JInternalFrame getFrame() {
        return frame;
    }
}
