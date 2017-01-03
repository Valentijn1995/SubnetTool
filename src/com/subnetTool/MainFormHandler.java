package com.subnetTool;

import java.util.HashMap;

/**
 * Created by valentijn on 09-12-16.
 */
public class MainFormHandler
{
    private final IPParser parser;
    private final HashMap<AddressSpace, SubnetCalculator> calculators;

    public MainFormHandler(IPParser parser, HashMap<AddressSpace, SubnetCalculator> calculators)
    {
        this.parser = parser;
        this.calculators = calculators;
    }

    public boolean isValidIP(String IPToValidate)
    {
        try
        {
            parser.parse(IPToValidate);
            return true;
        }
        catch(IPParser.ParseException ex)
        {
            return false;
        }

    }

    public String[] calcSubnets(String IP, int netMask, int subnetMask)
    {
        try
        {
            IPParser.IPAddress address = parser.parse(IP);
            SubnetCalculator calculator = calculators.get(address.getAddressSpace());
            byte[][] subnets = calculator.calcSubnetsForNetwork(address.getRawAddress(), netMask, subnetMask);
            String[] subnetsAsStrings = new String[subnets.length];
            for (int i = 0; i < subnets.length; i++)
            {
                subnetsAsStrings[i] = parser.parse(subnets[i]).getAddress();
            }
            return subnetsAsStrings;
        }
        catch(IPParser.ParseException | IllegalArgumentException ex)
        {
            return new String[0];
        }
    }

    public int calcSubnetMask(int netMask, int amountOfNetworks)
    {
        int subnetMask = SubnetCalculator.calcSubnetMaskForNetwork(netMask, amountOfNetworks);
        return subnetMask;
    }

}
