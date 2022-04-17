package cc.jayv.monopoly3;

import javax.swing.*;

public interface ViewComponent {
    void update(Board board);

    void setStateOfActionButton(ActionsGUI action, boolean isEnabled);

    JComponent getComponent();
}
