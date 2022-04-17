package cc.jayv.monopoly3;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.io.Serializable;

public class ViewFrameInformation implements ViewComponent, Serializable {

    JInternalFrame frame;
    ViewFrameInformationModule modulePlayer1;
    ViewFrameInformationModule modulePlayer2;
    ViewFrameInformationModule modulePlayer3;
    ViewFrameInformationModule modulePlayer4;

    JPanel panelPlayer1;
    JPanel panelPlayer2;
    JPanel panelPlayer3;
    JPanel panelPlayer4;

    Board board;

    public ViewFrameInformation(Board board) {
        this.board = board;

        initComponents();
        arrangeComponents();
    }

    private void initComponents() {
        frame = new JInternalFrame();
        frame.setLayout(new MigLayout("ins 0, gap 0, fill"));
        frame.setSize(308, 600);
        frame.setVisible(true);
        frame.setTitle("Information");
        frame.setFrameIcon(SwingHelper.getImageIconFromResource("/i.png"));

        modulePlayer1 = new ViewFrameInformationModule(board, board.players.get(1));
        modulePlayer2 = new ViewFrameInformationModule(board, board.players.get(2));
        modulePlayer3 = new ViewFrameInformationModule(board, board.players.get(3));
        modulePlayer4 = new ViewFrameInformationModule(board, board.players.get(4));

        panelPlayer1 = modulePlayer1.getCompositePanel();
        panelPlayer2 = modulePlayer2.getCompositePanel();
        panelPlayer3 = modulePlayer3.getCompositePanel();
        panelPlayer4 = modulePlayer4.getCompositePanel();
    }

    private void arrangeComponents() {
        String panelMigSpec = ", width 400, height 150";
        frame.add(panelPlayer1, "cell 0 0" + panelMigSpec);
        frame.add(panelPlayer2, "cell 0 1" + panelMigSpec);
        frame.add(panelPlayer3, "cell 0 2" + panelMigSpec);
        frame.add(panelPlayer4, "cell 0 3" + panelMigSpec);
    }

    @Override
    public void update(Board board) {
        modulePlayer1.update();
        modulePlayer2.update();
        modulePlayer3.update();
        modulePlayer4.update();
    }

    @Override
    public void setStateOfActionButton(ActionsGUI action, boolean isEnabled) {

    }

    @Override
    public JComponent getComponent() {
        return null;
    }

    public JInternalFrame getFrame() {
        return frame;
    }
}
