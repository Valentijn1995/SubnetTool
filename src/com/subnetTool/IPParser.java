package com.subnetTool;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

/**
 * The IPParser is used to validate and convert IPv4 and IPv6 strings.
 */
public class IPParser
{

    /*
     * The used regular expressions where taken from -> https://rosettacode.org/wiki/Parse_an_IP_Address#Java
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

    /**
     * Each conversion function returns an IPAddress object.
     * An IPAddress contains information about the parsed IP string.
     * At first it contains the IP address in String form, but the object also
     * provides a raw version of the address. The object also contains information about
     * the address type (IPv4 or IPv6).
     */
    public class IPAddress
    {
        private final String address;
        private final byte[] rawAddress;
        private final AddressSpace space;

        /**
         * Creates a new IPAddress object.
         *
         * @param address The address in String form
         * @param rawAddress The address in raw form
         * @param space The AddressSpace where the address belongs to
         */
        public IPAddress(String address, byte[] rawAddress, AddressSpace space)
        {
            this.address = address;
            this.rawAddress = rawAddress;
            this.space = space;
        }

        /**
         * Gets the address as a String.
         */
        public String getAddress()
        {
            return address;
        }

        /**
         * Gets the address in the form of a byte array.
         */
        public byte[] getRawAddress()
        {
            return rawAddress;
        }

        /**
         * Indicates which address space the address belongs to.
         */
        public AddressSpace getAddressSpace()
        {
            return space;
        }
    }

    /**
     * Exception thrown when an address could not be parsed.
     */
    public class ParseException extends RuntimeException
    {
        public ParseException(String message)
        {
            super(message);
        }
    }

    /**
     * Parses ipv4 and ipv6 addresses. The code in this function was taken from
     * -> https://rosettacode.org/wiki/Parse_an_IP_Address#Java
     *
     * Only the parsing of the port number was removed from the code.
     *
     * @param address The address to analyze
     * @return A IPAddress object which contains the parse results
     * @throws ParseException When the given address if not a valid address
     */
    public IPAddress parse(String address) throws ParseException
    {
        AddressSpace space;
        byte[] rawAddress;

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

    /**
     * Parses a given raw address.
     *
     * @param rawAddress The address to parse in bytes
     * @return An IPAddress object with the parse results
     * @throws ParseException When the given address if not a valid address
     */

    public IPAddress parse(byte[] rawAddress) throws ParseException
    {
        InetAddress a;
        String address;
        AddressSpace space;

        if (rawAddress.length == 16) //IPv6 address
        {
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
        else if(rawAddress.length == 4) //IPv4 address
        {
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
