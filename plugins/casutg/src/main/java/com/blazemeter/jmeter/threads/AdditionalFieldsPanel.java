package com.blazemeter.jmeter.threads;

import com.blazemeter.jmeter.gui.ArrangedLabelFieldPanel;
import com.blazemeter.jmeter.threads.arrivals.ArrivalsThreadGroup;
import kg.apc.jmeter.JMeterVariableEvaluator;
import org.apache.jmeter.gui.util.HorizontalPanel;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.Enumeration;

public class AdditionalFieldsPanel extends ArrangedLabelFieldPanel implements ParamsPanel {
    protected JTextField logFile = new JTextField();
    protected JTextField iterations = new JTextField();
    private final JCheckBox sameUserBox = new JCheckBox();
    protected JTextField concurrLimit = new JTextField();
    protected ButtonGroup unitGroup = new ButtonGroup();
    protected JRadioButton unitSeconds = new JRadioButton("seconds");
    protected JRadioButton unitMinutes = new JRadioButton("minutes");

    public AdditionalFieldsPanel(boolean showConcurrencyLimit) {
        JPanel groupPanel = new HorizontalPanel();
        unitMinutes.setActionCommand(ArrivalsThreadGroup.UNIT_MINUTES);
        unitSeconds.setActionCommand(ArrivalsThreadGroup.UNIT_SECONDS);
        unitGroup.add(unitMinutes);
        unitGroup.add(unitSeconds);
        groupPanel.add(unitMinutes);
        groupPanel.add(unitSeconds);
        add("Time Unit: ", groupPanel);

        add("Thread Iterations Limit: ", iterations);
        add("Same User On Each Iteration", sameUserBox);
        add("Log Threads Status into File: ", logFile);

        if (showConcurrencyLimit) {
            add("Concurrency Limit: ", concurrLimit);
        }
    }

    public void modelToUI(AbstractDynamicThreadGroup tg) {
        logFile.setText(tg.getLogFilename());
        iterations.setText(tg.getIterationsLimit());
        sameUserBox.setSelected(tg.getSameUser());
        concurrLimit.setText("1000");
        unitMinutes.setSelected(true);
        if (tg instanceof ArrivalsThreadGroup) {
            ArrivalsThreadGroup atg = (ArrivalsThreadGroup) tg;
            concurrLimit.setText(atg.getConcurrencyLimit());
        }

        Enumeration<AbstractButton> it = unitGroup.getElements();
        while (it.hasMoreElements()) {
            AbstractButton btn = it.nextElement();
            if (btn.getActionCommand().equals(tg.getUnit())) {
                btn.setSelected(true);
            }
        }
    }

    public void UItoModel(AbstractDynamicThreadGroup tg, JMeterVariableEvaluator evaluator) {
        tg.setLogFilename(evaluator.evaluate(logFile.getText()));
        tg.setIterationsLimit(evaluator.evaluate(iterations.getText()));
        tg.setSameUser(sameUserBox.isSelected());
        if (unitGroup.getSelection() != null) {
            tg.setUnit(unitGroup.getSelection().getActionCommand());
        }

        if (tg instanceof ArrivalsThreadGroup) {
            ArrivalsThreadGroup atg = (ArrivalsThreadGroup) tg;
            atg.setConcurrencyLimit(evaluator.evaluate(concurrLimit.getText()));
        }
    }

    public void clearUI() {
        logFile.setText("");
        iterations.setText("");
        sameUserBox.setSelected(true);
        concurrLimit.setText("1000");
        unitMinutes.setSelected(true);
    }

    public void addActionListener(ActionListener listener) {
        unitMinutes.addActionListener(listener);
        unitSeconds.addActionListener(listener);
    }
}
