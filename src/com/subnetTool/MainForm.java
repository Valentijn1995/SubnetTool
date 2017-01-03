package com.subnetTool;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by valentijn on 29-10-16.
 */
public class MainForm
{
    private JTextField IPAddressField;
    private JLabel AddressLabel;
    private JLabel SubnetLabel;
    private JLabel NetworkCountLabel;
    private JList SubnetList;
    private JPanel MainPanel;
    private JSpinner SubnetSpinner;
    private JSpinner NetworkSpinner;
    private JSpinner NetMaskSpiner;
    private JLabel NetLabel;

    private static final Color VALID_COLOR = Color.GREEN;
    private static final Color INVALID_COLOR = Color.RED;

    private boolean ipIsValid;

    private ChangeListener networkListener;
    private ChangeListener subnetListener;

    public MainForm(MainFormHandler handler)
    {
        ipIsValid = false;
        setUpIPField(handler);
        setUpNetmaskSpinner();
        setUpSubNetSpinner(handler);
        setUpNetworkSpinner(handler);

    }

    private void setUpIPField(MainFormHandler listener)
    {
        final Color IP_FIELD_DEFAULT_COLOR = IPAddressField.getBackground();


        IPAddressField.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                String newValue = IPAddressField.getText();

                if(newValue.equals(""))
                {
                    IPAddressField.setBackground(IP_FIELD_DEFAULT_COLOR);
                    ipIsValid = false;
                }
                else if(listener.isValidIP(newValue))
                {
                    IPAddressField.setBackground(VALID_COLOR);
                    ipIsValid = true;
                }
                else
                {
                    IPAddressField.setBackground(INVALID_COLOR);
                    ipIsValid = false;
                }
            }
        });
    }

    private void setUpNetmaskSpinner()
    {

        NetMaskSpiner.addChangeListener(e ->
        {
            int newValue = (int) NetMaskSpiner.getValue();
            if(newValue < 0)
            {
                NetMaskSpiner.setValue(0);
            }
        });
    }

    private void setUpSubNetSpinner(MainFormHandler handler)
    {
        subnetListener = e ->
        {
            int newValue = (int) SubnetSpinner.getValue();
            if(newValue < 0)
            {
                SubnetSpinner.setValue(0);
            }
            else if (ipIsValid)
            {
                String IPAddress = IPAddressField.getText();
                int netMask = (int) NetMaskSpiner.getValue();
                int subMask = newValue;

                String[] subnets = handler.calcSubnets(IPAddress, netMask, subMask);

                NetworkSpinner.removeChangeListener(networkListener);
                NetworkSpinner.setValue(subnets.length);
                NetworkSpinner.addChangeListener(networkListener);

                updateSubnetList(subnets);

            }
        };


        SubnetSpinner.addChangeListener(subnetListener);
    }

    private void setUpNetworkSpinner(MainFormHandler handler)
    {
        networkListener = e ->
        {
            int newValue = (int) NetworkSpinner.getValue();
            if(newValue < 0)
            {
                NetworkSpinner.setValue(0);
            }
            else if (ipIsValid)
            {
                String IPAddress = IPAddressField.getText();
                int netMask = (int) NetMaskSpiner.getValue();
                int networks = (int) NetworkSpinner.getValue();

                int newSubnetMask = handler.calcSubnetMask(netMask, networks);
                String[] subnets = handler.calcSubnets(IPAddress, netMask, newSubnetMask);

                SubnetSpinner.removeChangeListener(subnetListener);
                SubnetSpinner.setValue(newSubnetMask);
                SubnetSpinner.addChangeListener(subnetListener);

                updateSubnetList(subnets);

            }
        };

        NetworkSpinner.addChangeListener(networkListener);
    }

    private void updateSubnetList(String[] subnets)
    {
        SubnetList.setListData(subnets);
    }


    public JPanel getMainPanel()
    {
        return MainPanel;
    }
}
