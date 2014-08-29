package com.jetbrains.isaev.notifications;

import com.intellij.codeInsight.hint.HintManager;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.*;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.ui.components.JBList;
import com.jetbrains.isaev.issues.*;
import com.jetbrains.isaev.issues.StackTraceElement;
import com.jetbrains.isaev.state.BTIssue;
import com.jetbrains.isaev.ui.BTIssueShowDialog;
import com.jetbrains.isaev.ui.CopyMarkerToLineBalloon;
import com.jetbrains.isaev.ui.ParsedException;

import javax.management.Notification;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ilya.Isaev on 05.08.2014.
 */
public class IssuesPopupList extends JBList {
    private JBPopup containingPopup;
    private boolean isPopupShown = false;
    private boolean numbersShown = false;
    private boolean firstTimeClick;
    private ReportedExceptionLineMarkerInfo anchor;

    public IssuesPopupList(final ReportedExceptionLineMarkerInfo anchor) {
        super(new ArrayList<BTIssue>(anchor.getIssues().values()));
        this.anchor = anchor;
        final ListModel model = getModel();

        addMouseListener(new MouseAdapter() {
            public void leftClick(MouseEvent e, int pos) {
                BTIssue issue = (BTIssue) model.getElementAt(pos);
                if (getModel().getSize() > 0) {
                    int index = locationToIndex(e.getPoint());
                    BTIssue item = (BTIssue) getModel().getElementAt(index);
                    if (IssuesPopupList.this.getContainingPopup() != null)
                        IssuesPopupList.this.getContainingPopup().cancel();
                    new BTIssueShowDialog(item).show();
                }
            }

            public void rightClick(MouseEvent e, int pos) {
                final BTIssue issue = (BTIssue) model.getElementAt(pos);
                final EditorSettings settings = anchor.getEditor().getSettings();
                if (firstTimeClick) {
                    firstTimeClick = false;
                    settings.setLineNumbersShown(true);
                }
                Point p = IssuesPopupList.this.getLocationOnScreen();
                p.y = e.getYOnScreen();
                p.x += IssuesPopupList.this.getContainingPopup().getContent().getWidth();
                final CopyMarkerToLineBalloon content = new CopyMarkerToLineBalloon(issue);
                final Balloon balloon = JBPopupFactory.getInstance().createBalloonBuilder(content.panel1)
                        .setHideOnClickOutside(true)
                        .setHideOnKeyOutside(true).setShadow(false)
                        .setBlockClicksThroughBalloon(true)
                        .createBalloon();
                balloon.show(new RelativePoint(p), Balloon.Position.atRight);
                content.okButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        String text = content.textField1.getText();
                        int currentRow = anchor.getLine();
                        try {
                            int row = Integer.parseInt(text) - 1;
                            if (row != currentRow) {
                                HashMap<Long, StackTraceElement> elMap = anchor.stElements;
                                StackTraceElement element = null;
                                for (StackTraceElement el : issue.getAllSTElements().values()) {
                                    if (elMap.containsKey(el.getID())) element = el;
                                }
                                elMap = new HashMap<Long, StackTraceElement>();
                                elMap.put(element.getID(), element);
                                element.getPlacementInfo().absolute.add(row);
                                HashMap<Integer, BTIssue> isMap = new HashMap<Integer, BTIssue>();
                                BTIssue is = element.getIssue();
                                isMap.put(is.getIssueID(), is);
                                ReportedExceptionLineMarkerInfo info = new ReportedExceptionLineMarkerInfo(anchor.getElement(), 0, elMap, isMap, anchor.file, anchor.getEditor(), ReportedExceptionLineMarkerInfo.Type.CLASS);
                                info.updateUI(row);
                                balloon.hide();
                            } else {
                                Notifications.Bus.notify(new com.intellij.notification.Notification("", "Cannot perform copying", "This is current row", NotificationType.WARNING));
                            }
                        } catch (NumberFormatException ex) {
                            Notifications.Bus.notify(new com.intellij.notification.Notification("", "Cannot perform copying", "Not a number: " + text, NotificationType.WARNING));
                        }
                    }
                });
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                int pos = IssuesPopupList.this.locationToIndex(e.getPoint());
                if (pos != -1 && pos < model.getSize()) {
                    BTIssue issue = (BTIssue) model.getElementAt(pos);
                    if (SwingUtilities.isRightMouseButton(e)) {
                        rightClick(e, pos);
                    } else {
                        leftClick(e, pos);
                    }
                }
            }


        });
    }

    public JBPopup getContainingPopup() {
        return containingPopup;
    }

    public void setContainingPopup(JBPopup containingPopup) {
        this.containingPopup = containingPopup;
    }

    public void addListener() {
        containingPopup.addListener(new JBPopupAdapter() {
            @Override
            public void beforeShown(LightweightWindowEvent event) {
                super.beforeShown(event);
                isPopupShown = true;
                numbersShown = anchor.getEditor().getSettings().isLineNumbersShown();
                firstTimeClick = true;
            }

            @Override
            public void onClosed(LightweightWindowEvent event) {
                super.onClosed(event);
                isPopupShown = false;
                anchor.getEditor().getSettings().setLineNumbersShown(numbersShown);
            }
        });
    }
}
