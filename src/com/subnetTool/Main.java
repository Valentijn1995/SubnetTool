package com.subnetTool;


import javax.swing.*;
import java.util.HashMap;

public class Main
{
    public static void main(String[] args)
    {
        IPParser parser = new IPParser();

        HashMap<AddressSpace, SubnetCalculator> calcHashMap = new HashMap<>();
        AddressSpace ipv4Space = AddressSpace.IPv4;
        calcHashMap.put(ipv4Space, new SubnetCalculator(ipv4Space.getByteLen()));
        AddressSpace ipv6Space = AddressSpace.IPv6;
        calcHashMap.put(ipv6Space, new SubnetCalculator(ipv6Space.getByteLen()));

        MainFormListener handler = new MainFormHandler(parser, calcHashMap);
        MainForm mainForm = new MainForm(handler);

        JFrame frame = new JFrame("Subnet Tool");
        frame.setContentPane(mainForm.getMainPanel());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(mainForm.getMainPanel().getMinimumSize());
        frame.pack();
        frame.setVisible(true);
    }
}
