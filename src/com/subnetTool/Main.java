package com.subnetTool;


import javax.swing.*;
import java.util.HashMap;

public class Main
{
    /**
     *  Initiates the program. This function creates the GUI, a handler for handling
     *  user interaction, parser for parsing IP addresses and a calculator for calculating
     *  the subnet's.
     *
     * @param args Program args
     */
    public static void main(String[] args)
    {
        // Parser for parsing IPv4 and IPv6 addresses
        IPParser parser = new IPParser();

        // Each IP type needs its own calculator. Calculators used to calculate
        // different subnet's for different addressSpaces are put into a HashMap
        HashMap<AddressSpace, SubnetCalculator> calcHashMap = new HashMap<>();
        // Add Calculator for IPv4 support
        AddressSpace ipv4Space = AddressSpace.IPv4;
        calcHashMap.put(ipv4Space, new SubnetCalculator(ipv4Space.getByteLen()));
        // Add Calculator for IPv6 support
        AddressSpace ipv6Space = AddressSpace.IPv6;
        calcHashMap.put(ipv6Space, new SubnetCalculator(ipv6Space.getByteLen()));

        // Create a new handler. The handler will handle all the user
        // interaction with the GUI form.
        MainFormHandler handler = new MainFormHandler(parser, calcHashMap);
        MainForm mainForm = new MainForm(handler);

        JFrame frame = new JFrame("Subnet Tool");
        frame.setContentPane(mainForm.getMainPanel());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(mainForm.getMainPanel().getMinimumSize());
        frame.pack();
        frame.setVisible(true);
    }
}
