package com.subnetTool;

import java.lang.IllegalArgumentException;
import java.math.BigInteger;

/**
 * Created by valentijn on 01-11-16.
 */
public class SubnetCalculator
{
    private static final int BYTE_LEN = 8;
    private final int ADDRESS_LEN;
    private final int ADDRESS_LEN_IN_BITS;

    public SubnetCalculator(int addressLengthInBytes)
    {
        ADDRESS_LEN = addressLengthInBytes;
        ADDRESS_LEN_IN_BITS = ADDRESS_LEN * BYTE_LEN;
    }


    public byte[][] calcSubnetsForNetwork(byte[] networkIP, int netMask, int subnetMask)
    {
        if(networkIP.length != this.ADDRESS_LEN)
        {
            throw new IllegalArgumentException("Network byte array has to be " + Integer.toString(this.ADDRESS_LEN)
                    + " bytes long");
        }
        else if(subnetMask <= netMask)
        {
            throw new IllegalArgumentException("SubnetMask has to be higher than the NetMask");
        }
        else if(netMask > this.ADDRESS_LEN_IN_BITS || subnetMask > this.ADDRESS_LEN_IN_BITS)
        {
            throw new IllegalArgumentException("Netmask and/or SubnetMask has to be lower than the amount of" +
                    " bits in the IP address");
        }

        final int borrowedBits = subnetMask - netMask;
        final int amountOfSubnets = (int) Math.pow(2, borrowedBits);
        final int hostPortion = (this.ADDRESS_LEN * BYTE_LEN) - subnetMask;
        final BigInteger baseAddress = new BigInteger(networkIP);
        final BigInteger subnetBase = new BigInteger(new byte[this.ADDRESS_LEN]).setBit(hostPortion);
        final byte[][] subnetArray = new byte[amountOfSubnets][this.ADDRESS_LEN];


        for(int subnetCounter = 0; subnetCounter < amountOfSubnets; subnetCounter++)
        {
            BigInteger subnetPart = subnetBase.multiply(BigInteger.valueOf(subnetCounter));
            BigInteger subnetAddress = baseAddress.add(subnetPart);
            subnetArray[subnetCounter] = subnetAddress.toByteArray();
        }

        return subnetArray;

    }

    public static int calcSubnetMaskForNetwork(int netMask, int minimumSubnets)
    {
        if(minimumSubnets < 1)
        {
            throw new IllegalArgumentException("Amount of minimum subnet's must be one or higher");
        }
        else if(netMask < 1)
        {
            throw new IllegalArgumentException("NetMask must be one or higher");
        }

        int exponentCounter = 1;
        while(true)
        {
            int subnets = ((int)Math.pow(2, exponentCounter) - 2);
            if (subnets >= minimumSubnets)
            {
                return netMask + exponentCounter;
            }
            else
            {
                exponentCounter++;
            }
        }
    }

}
