package com.subnetTool;

/**
 * Created by valentijn on 19-11-16.
 */
public interface MainFormListener
{
    boolean isValidIP(String IPToValidate);
    String[] calcSubnets(String IP, int netMask, int subnetMask);
    int calcSubnetMask(String IP, int netMask, int amountOfNetworks);
}
