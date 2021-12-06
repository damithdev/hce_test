package com.example.nfctest.hce.commands;

public abstract class APDUCommandAbs {


    public static final byte[] ISO7816_PROCESS_COMPLETED = {
            (byte) 0x90,  // SW1
            (byte) 0x00   // SW2
    };

    // Global Error
    public static final byte[] ISO7816_UNKNOWN_ERROR_RESPONSE = {
            (byte) 0x6F,
            (byte) 0x00
    };

    // Checking Errors
    public static final byte[] ISO7816_ERROR_AUTHENTICATION_METHOD_BLOCKED = {
            (byte) 0x69,  // SW1
            (byte) 0x83   // SW2
    };

    public static final byte[] ISO7816_ERROR_REFERENCED_DATA_INVALIDATED = {
            (byte) 0x69,  // SW1
            (byte) 0x84   // SW2
    };

    public static final byte[] ISO7816_ERROR_CONDITIONS_OF_USE_NOT_SATISFIED = {
            (byte) 0x69,  // SW1
            (byte) 0x85   // SW2
    };
    public static final byte[] ISO7816_ERROR_FUNCTION_NOT_SUPPORTED = {
            (byte) 0x6A,  // SW1
            (byte) 0x81   // SW2
    };

    public static final byte[] ISO7816_ERROR_FILE_NOT_FOUND = {
            (byte) 0x6A,  // SW1
            (byte) 0x82   // SW2
    };

    public static final byte[] ISO7816_ERROR_RECORD_NOT_FOUND = {
            (byte) 0x6A,  // SW1
            (byte) 0x83   // SW2
    };

    public static final byte[] ISO7816_ERROR_REFERENCED_DATA_OBJECTS_NOT_FOUND = {
            (byte) 0x6A,  // SW1
            (byte) 0x88   // SW2
    };
}
