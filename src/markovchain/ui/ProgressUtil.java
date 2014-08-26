package markovchain.ui;

import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Admin
 */
public class ProgressUtil {

    static class MonitorListener implements ChangeListener, ActionListener {

        ProgressMonitor monitor;
        Window owner;
        Timer timer;

        public MonitorListener(Window owner, ProgressMonitor monitor) {
            this.owner = owner;
            this.monitor = monitor;
        }

        @Override
        public void stateChanged(ChangeEvent ce) {
            ProgressMonitor mtor = (ProgressMonitor) ce.getSource();
            if (mtor.getCurrent() != mtor.getTotal()) {
                if (timer == null) {
                    timer = new Timer(mtor.getMilliSecondsToWait(), this);
                    timer.setRepeats(false);
                    timer.start();
                }

            } else {
                if (timer != null && timer.isRunning()) {
                    timer.stop();
                }
                mtor.removeChangeListener(this);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            monitor.removeChangeListener(this);
            ProgressDialog dlg = owner instanceof MainFrame
                    ? new ProgressDialog((MainFrame) owner, monitor)
                    : new ProgressDialog((Dialog) owner, monitor);
            dlg.pack();
            dlg.setLocationRelativeTo(null);
            dlg.setVisible(true);
        }
    }

    public static ProgressMonitor createModalProgressMonitor(MainFrame owner, int total, boolean indeterminate, int milliSecondsToWait) {
        ProgressMonitor monitor = new ProgressMonitor(total, indeterminate, milliSecondsToWait);
        Window window = owner instanceof Window
                ? (Window) owner
                : SwingUtilities.getWindowAncestor(owner);
        monitor.addChangeListener(new MonitorListener(window, monitor));
        return monitor;
    }
}
