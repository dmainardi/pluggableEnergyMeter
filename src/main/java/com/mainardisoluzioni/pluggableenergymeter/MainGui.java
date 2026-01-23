/*
 * Copyright (C) 2026 Davide Mainardi <davide at mainardisoluzioni.com>.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mainardisoluzioni.pluggableenergymeter;

import com.mainardisoluzioni.pluggableenergymeter.communication.ModbusController;
import com.mainardisoluzioni.pluggableenergymeter.logging.JTextAreaHandler;
import com.mainardisoluzioni.pluggableenergymeter.logging.LevaGuiFormatter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.swing.SwingWorker;

/**
 *
 * @author Davide Mainardi <davide at mainardisoluzioni.com>
 */
public class MainGui extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MainGui.class.getName());

    /**
     * Creates new form NewJFrame
     *
     * @param properties
     */
    public MainGui(Properties properties) {
        initComponents();
        
        testLinkButton.addActionListener(e -> testLink());
        startDataHoardingButton.addActionListener(e -> startDataHoarding());
        stopDataHoardingButton.addActionListener(e -> stopDataHoarding());

        this.properties = properties;

        // Setting up the logger
        logger.addHandler(new JTextAreaHandler(logs, new LevaGuiFormatter()));
        logger.setUseParentHandlers(false);

        energyMeterIpAddressTextField.setText(this.properties.getProperty(ENERGY_METER_IP_ADDRESS_KEY, "Not found"));
        logger.fine("added ENERGY_METER_IP_ADDRESS_KEY");
        energyMeterModbusIdTextField.setText(this.properties.getProperty(ENERGY_METER_MODBUS_ID_KEY, "Not found"));
        logger.fine("added ENERGY_METER_MODBUS_ID_KEY");
        
        controller = new ModbusController();
        
        actualPowers = new ArrayList<>();
        
        dataHoardingWorker = new SwingWorker<>() {
            @Override
            protected List<Integer> doInBackground() throws Exception {
                List<Integer> result = new ArrayList<>();
                while(!isCancelled()) {
                    Integer actualPower = controller.readActualPower(
                            properties.getProperty(
                                    ENERGY_METER_IP_ADDRESS_KEY,
                                    "Not found"
                            ),
                            502,
                            Integer.parseInt(
                                    properties.getProperty(
                                            ENERGY_METER_MODBUS_ID_KEY,
                                            "0"
                                    )
                            )
                    );
                    if (actualPower != null && actualPower.compareTo(0) > 0) {
                        publish(actualPower);
                        result.add(actualPower);
                    }
                    Thread.sleep(1000);
                }
                
                return result;
            }

            @Override
            protected void process(List<Integer> chunks) {
                for (Integer actualPower : chunks)
                    logger.log(Level.INFO, "Actual power: {0} W", actualPower);
                readCounter += chunks.size();
                readCounterLabel.setText("Read counter: " + readCounter);
                actualPowers.addAll(chunks);
            }

            @Override
            protected void done() {
                controller.disconnect();
                logger.info("Modbus disconnected correclty");
                try {
                    String csvFilePath = properties.getProperty(CSV_FILE_PATH_KEY);
                    if (csvFilePath != null && !csvFilePath.isBlank()) {
                        FileWriter csvFileWriter = null;
                        BufferedWriter csvBufferedWriter = null;
                        try {
                            csvFileWriter = new FileWriter(csvFilePath);
                            csvBufferedWriter = new BufferedWriter(csvFileWriter);
                            for (Integer actualPower : actualPowers) {
                                csvBufferedWriter.write(actualPower.toString());
                                csvBufferedWriter.newLine();
                            }
                        } catch (IOException ex) {
                            logger.warning(ex.getLocalizedMessage());
                        } finally {
                            if (csvBufferedWriter != null)
                                csvBufferedWriter.close();
                            if (csvFileWriter != null)
                                csvFileWriter.close();
                        }
                    }
                } catch (IOException ex) {
                    logger.warning(ex.getLocalizedMessage());
                }
            }
            
        };
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        energyMeterIpAddressTextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        energyMeterModbusIdTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        testLinkButton = new javax.swing.JButton();
        testLinkResultLabel = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        startDataHoardingButton = new javax.swing.JButton();
        stopDataHoardingButton = new javax.swing.JButton();
        readCounterLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        logs = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pluggable Energy Meter");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Energy Meter"));

        energyMeterIpAddressTextField.setEditable(false);
        energyMeterIpAddressTextField.setText("jTextField1");

        jLabel1.setText("IP address");

        energyMeterModbusIdTextField.setEditable(false);
        energyMeterModbusIdTextField.setText("jTextField2");

        jLabel2.setText("ModBus unit ID");

        testLinkButton.setText("Test link");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(testLinkButton, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(testLinkResultLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(testLinkButton, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(testLinkResultLabel))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(energyMeterIpAddressTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(energyMeterModbusIdTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(energyMeterIpAddressTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(energyMeterModbusIdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Data hoarding"));

        jPanel5.setLayout(new java.awt.GridLayout(1, 3, 25, 0));

        startDataHoardingButton.setBackground(new java.awt.Color(102, 204, 0));
        startDataHoardingButton.setText("Start");
        jPanel5.add(startDataHoardingButton);

        stopDataHoardingButton.setBackground(new java.awt.Color(255, 0, 0));
        stopDataHoardingButton.setText("Stop");
        stopDataHoardingButton.setEnabled(false);
        jPanel5.add(stopDataHoardingButton);
        jPanel5.add(readCounterLabel);

        logs.setEditable(false);
        logs.setColumns(20);
        logs.setRows(5);
        jScrollPane1.setViewportView(logs);

        jLabel4.setText("Logs");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(0, 578, Short.MAX_VALUE))
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void testLink() {
        // Disable button so the user can't start the task twice
        testLinkButton.setEnabled(false);
        startDataHoardingButton.setEnabled(false);
        testLinkResultLabel.setText("Link testing in progress...");

        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return ModbusController.testConnection(
                        properties.getProperty(
                                ENERGY_METER_IP_ADDRESS_KEY,
                                "Not found"
                        ),
                        502,
                        Integer.parseInt(
                                properties.getProperty(
                                        ENERGY_METER_MODBUS_ID_KEY,
                                        "0"
                                )
                        )
                );
            }

            @Override
            protected void done() {
                // This runs on the EDT – safe to touch UI components
                try {
                    if (get()) {    // retrieve the Boolean returned above
                        testLinkResultLabel.setText("Connection OK");
                        logger.fine("Test link result is OK");
                    } else {
                        testLinkResultLabel.setText("Connection ERROR");
                        logger.warning("Test link result does not complete correctly");
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    // get() can throw ExecutionException / InterruptedException
                    testLinkResultLabel.setText("Connection ERROR");
                    logger.warning(ex.getLocalizedMessage());
                } finally {
                    testLinkButton.setEnabled(true);
                    startDataHoardingButton.setEnabled(true);
                }
            }
        };

        worker.execute();   // schedules doInBackground() on a worker thread
    }
    
    private void startDataHoarding() {
        // Disable button so the user can't test the link during dataHoarding
        testLinkButton.setEnabled(false);
        // Disable button so the user can't start the task twice
        startDataHoardingButton.setEnabled(false);
        // Enable button so the user can cancel the data hoarding execution
        stopDataHoardingButton.setEnabled(true);
        
        dataHoardingWorker.execute();
    }
    
    private void stopDataHoarding() {
        // Enable button so the user can test the link
        testLinkButton.setEnabled(true);
        // Enable button so the user can start the data hoarding execution
        startDataHoardingButton.setEnabled(true);
        // Disable button
        stopDataHoardingButton.setEnabled(false);
        
        dataHoardingWorker.cancel(false);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        final String configFilePath;
        if (args.length > 0) {
            configFilePath = args[0];
        } else {
            configFilePath = "app.config";
        }

        /* Set the Metal look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Metal".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new MainGui(ConfigFileLoader.loadConfigFile(configFilePath)).setVisible(true));
    }

    private final Properties properties;
    private final String ENERGY_METER_IP_ADDRESS_KEY = "energymeter.ip";
    private final String ENERGY_METER_MODBUS_ID_KEY = "energymeter.modbus.id";
    private final String CSV_FILE_PATH_KEY = "csv.file.path";
    private final ModbusController controller;
    private int readCounter = 0;
    private final List<Integer> actualPowers;
    private final SwingWorker<List<Integer>, Integer> dataHoardingWorker;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField energyMeterIpAddressTextField;
    private javax.swing.JTextField energyMeterModbusIdTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea logs;
    private javax.swing.JLabel readCounterLabel;
    private javax.swing.JButton startDataHoardingButton;
    private javax.swing.JButton stopDataHoardingButton;
    private javax.swing.JButton testLinkButton;
    private javax.swing.JLabel testLinkResultLabel;
    // End of variables declaration//GEN-END:variables
}
