package org.mocraft;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;

/**
 * Created by Clode on 2017/4/5.
 */
public class GuiMain extends BahaCrawler {

    private MainListener listener = new MainListener();
    private ListClickListener listClickListener = new ListClickListener();

    public JFrame frame;
    public JLabel statuslbl = new JLabel("|");
    public JTextField urlField = new JTextField(40), filterField = new JTextField(40);
    public JButton searchBtn = new JButton("Crawl"), exitBtn = new JButton("Exit");
    public JList<String> list;
    public JTextArea log = new JTextArea();

    void fieldGenerate() {
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 0;
        g.weightx = 1;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(0, 5, 0, 5);
        frame.add(urlField, g);

        GridBagConstraints g2 = new GridBagConstraints();
        g2.gridx = 0;
        g2.gridy = 1;
        g2.weightx = 1;
        g2.fill = GridBagConstraints.HORIZONTAL;
        g2.insets = new Insets(0, 5, 0, 5);
        frame.add(filterField, g2);
    }

    void btnGenerate() {
        searchBtn.addActionListener(listener);
        searchBtn.setActionCommand("search");

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 1;
        g.gridy = 0;
        g.insets = new Insets(0, 5, 0, 5);
        g.fill = GridBagConstraints.HORIZONTAL;
        frame.add(searchBtn, g);

        exitBtn.addActionListener(listener);
        exitBtn.setActionCommand("exit");

        GridBagConstraints g2 = new GridBagConstraints();
        g2.gridx = 1;
        g2.gridy = 1;
        g2.insets = new Insets(0, 5, 0, 5);
        g2.fill = GridBagConstraints.HORIZONTAL;
        frame.add(exitBtn, g2);
    }

    void listGenerate() {
        DefaultListModel model = new DefaultListModel();
        list = new JList<>(model);
        list.addMouseListener(listClickListener);

        JScrollPane listPane = new JScrollPane(list);
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 2;
        g.gridwidth = 2;
        g.weighty = 0.3;
        g.fill = GridBagConstraints.BOTH;
        g.insets = new Insets(5, 5, 5, 5);
        frame.add(listPane, g);
    }

    void areaGenerate() {
        log.setEditable(false);
        log.setAutoscrolls(true);
        ((DefaultCaret) log.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        JScrollPane logPane = new JScrollPane(log);
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 3;
        g.gridwidth = 2;
        g.weightx = 1;
        g.weighty = 0.7;
        g.fill = GridBagConstraints.BOTH;
        g.insets = new Insets(0, 5, 0, 5);
        frame.add(logPane, g);
    }

    void lblGenerate() {
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 4;
        g.gridwidth = 2;
        g.anchor = GridBagConstraints.EAST;
        g.insets =  new Insets(0, 5, 0, 5);
        frame.add(statuslbl, g);
    }

    public GuiMain() throws Exception {
        frame = new JFrame("BahaCrawler");
        frame.setSize(new Dimension(550, 450));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());

        fieldGenerate();
        listGenerate();
        btnGenerate();
        areaGenerate();
        lblGenerate();

        frame.setVisible(true);
    }

    class MainListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String cmd = actionEvent.getActionCommand();
            if(cmd.equals("search") && !urlField.getText().equals("") && !filterField.getText().equals("")) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        searchBtn.setEnabled(false);
                        log.setText("");
                        ((DefaultListModel)list.getModel()).clear();
                        statuslbl.setText("Processing...");
                        start(urlField.getText(), filterField.getText().replace(" ", "").split(","));
                        statuslbl.setText("All crawl done.");
                        searchBtn.setEnabled(true);
                    }
                }).start();
            } else if(cmd.equals("exit")) {
                System.exit(0);
            }
        }
    }

    class ListClickListener extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            Article selected = ((JList<Article>) e.getSource()).getSelectedValue();
            if(e.getClickCount() == 2) {
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(selected.getUrl().toString()), null);
                statuslbl.setText(selected.getName().substring(0, selected.getName().length()  * 2 / 3) + "...   URL copyed!");
            }
        }

    }

}
