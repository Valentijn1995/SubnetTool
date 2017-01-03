package com.subnetTool;

/**
 * Created by valentijn on 09-12-16.
 *
 * The Enum represents the different AddressSpaces supported by the program.
 */
public enum AddressSpace
{
    IPv4(4), IPv6(16);

    private final int len;

    /**
     * Creates a new AddressSpace object.
     *
     * @param len The length of the AddressSpace in bytes
     */
    AddressSpace(int len)
    {
        this.len = len;
    }

    /**
     * Returns the length of the AddressSpace.
     *
     * @return Length of the AddressSpace in bytes
     */
    public int getByteLen()
    {
        return this.len;
    }
}
