package com.subnetTool;

/**
 * Created by valentijn on 09-12-16.
 */
public enum AddressSpace
{
    IPv4(4), IPv6(16);

    private int len;

    AddressSpace(int len)
    {
        this.len = len;
    }

    public int getByteLen()
    {
        return this.len;
    }
}
