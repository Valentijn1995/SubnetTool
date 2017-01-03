package com.subnetTool;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;
import java.lang.Exception;


/**
 * Parses ipv4 and ipv6 addresses. Emits each described IP address as a
 * hexadecimal integer representing the address, the address space, and the port
 * number specified, if any.
 */
public class IPParser
{
    /*
     * Using regex to ensure that the address is a valid one. This allows for
     * separating by format and ensures that the operations done on a format
     * will be valid.
     */
    // 0.0.0.0-255.255.255.255
    private final String ipv4segment =
            "(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])";

    private final String ipv4address = "(" + ipv4segment + "\\.){3,3}"
            + ipv4segment;

    private final String ipv6segment = "[a-fA-F0-9]{1,4}";
    private final String ipv6address = "(" +
            // 1:2:3:4:5:6:7:8
            "(" + ipv6segment + ":){7,7}" + ipv6segment + "|" +
            // 1::, 1:2:3:4:5:6:7::
            "(" + ipv6segment + ":){1,7}:|" +
            // 1::8, 1:2:3:4:5:6::8, 1:2:3:4:5:6::8
            "(" + ipv6segment + ":){1,6}:" + ipv6segment + "|" +
            // 1::7:8, 1:2:3:4:5::7:8, 1:2:3:4:5::8
            "(" + ipv6segment + ":){1,5}(:" + ipv6segment + "){1,2}|" +
            // 1::6:7:8, 1:2:3:4::6:7:8, 1:2:3:4::8
            "(" + ipv6segment + ":){1,4}(:" + ipv6segment + "){1,3}|" +
            // 1::5:6:7:8, 1:2:3::5:6:7:8, 1:2:3::8
            "(" + ipv6segment + ":){1,3}(:" + ipv6segment + "){1,4}|" +
            // # 1::4:5:6:7:8, 1:2::4:5:6:7:8, 1:2::8
            "(" + ipv6segment + ":){1,2}(:" + ipv6segment + "){1,5}|" +
            // # 1::3:4:5:6:7:8, 1::3:4:5:6:7:8, 1::8
            ipv6segment + ":((:" + ipv6segment + "){1,6})|" +
            // ::2:3:4:5:6:7:8, ::2:3:4:5:6:7:8, ::8, ::
            ":((:" + ipv6segment + "){1,7}|:)|" +
            // fe80::7:8%eth0, fe80::7:8%1 (link-local IPv6 addresses with
            // zone index)
            "fe80:(:" + ipv6segment + "){0,4}%[0-9a-zA-Z]{1,}|" +
            // ::255.255.255.255, ::ffff:255.255.255.255,
            // ::ffff:0:255.255.255.255 (IPv4-mapped IPv6 addresses and
            // IPv4-translated addresses)
            "::(ffff(:0{1,4}){0,1}:){0,1}" + ipv4address + "|" +
            // 2001:db8:3:4::192.0.2.33, 64:ff9b::192.0.2.33 (IPv4-Embedded
            // IPv6 Address)
            "(" + ipv6segment + ":){1,4}:" + ipv4address + ")";


    public class IPAddress
    {
        private String address;
        private byte[] rawAddress;
        private AddressSpace space;

        public IPAddress(String address, byte[] rawAddress, AddressSpace space)
        {
            this.address = address;
            this.rawAddress = rawAddress;
            this.space = space;
        }

        public String getAddress()
        {
            return address;
        }

        public byte[] getRawAddress()
        {
            return rawAddress;
        }

        public AddressSpace getAddressSpace()
        {
            return space;
        }
    }

    public class ParseException extends Exception
    {
        public ParseException(String message)
        {
            super(message);
        }
    }

    /**
     * Parses ipv4 and ipv6 addresses. Emits each described IP address as a
     * hexadecimal integer representing the address, the address space, and the
     * port number specified, if any.
     *
     * @param address the address to analyze
     */
    public IPAddress parse(String address) throws ParseException
    {

        // Used for storing values to be printed
        AddressSpace space;// ipv4, ipv6, or unknown
        byte[] rawAddress;// hex value of the address

        // Try to match the pattern with one of the 2 types, with or without a
        // port
        if (Pattern.matches("^" + ipv4address + "$", address))
        {
            InetAddress a;
            space = AddressSpace.IPv4;
            try
            {
                a = InetAddress.getByName(address);
                rawAddress = a.getAddress();
            }
            catch (UnknownHostException e)
            {
                throw new ParseException("Could not parse given IPv4 address");
            }
        }
        else if (Pattern.matches("^" + ipv6address + "$", address))
        {
            InetAddress a;
            space = AddressSpace.IPv6;
            try
            {
                a = Inet6Address.getByName(address);
                rawAddress = a.getAddress();
            }
            catch (UnknownHostException e)
            {
               throw new ParseException("Could not parse given IPv6 address");
            }
        }
        else
        {
           throw new ParseException("Given String is not a valid IPv4 or IPv6 address");
        }

        return new IPAddress(address, rawAddress, space);

    }

    public IPAddress parse(byte[] rawAddress) throws ParseException
    {
        InetAddress a;
        String address;
        AddressSpace space;

        if (rawAddress.length == 16)
        {
            //IPv6 address
            space = AddressSpace.IPv6;
            try
            {
                a = Inet6Address.getByAddress(rawAddress);
                address = a.getHostAddress();
            }
            catch (UnknownHostException e)
            {
                throw new ParseException("Failed to parse given IPv6 address");
            }
        }
        else if(rawAddress.length == 4)
        {
            //IPv4 address
            space = AddressSpace.IPv4;
            try
            {
                a = InetAddress.getByAddress(rawAddress);
                address = a.getHostAddress();
            }
            catch (UnknownHostException e)
            {
                throw new ParseException("Failed to parse given IPv4 address");
            }
        }
        else
        {
            throw new ParseException("Given byte array is not 4 (IPv4) or 16 (IPv6) bytes long");
        }

        return new IPAddress(address, rawAddress, space);
    }
}
