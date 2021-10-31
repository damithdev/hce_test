package com.example.nfctest.hce;

public class ApduMessage {
    public static final byte[] ISO7816_UNKNOWN_ERROR_RESPONSE = {
            (byte)0x6F, (byte)0x00
    };

    /*
     *  PPSE (Proximity Payment System Environment)
     *
     *  This is the first select that a point of sale device will send to the payment device.
     */
    public static final byte[] PPSE_APDU_SELECT = {
            (byte)0x00, // CLA (class of command)
            (byte)0xA4, // INS (instruction); A4 = select
            (byte)0x04, // P1  (parameter 1)  (0x04: select by name)
            (byte)0x00, // P2  (parameter 2)
            (byte)0x0E, // LC  (length of data)  14 (0x0E) = length("2PAY.SYS.DDF01")
            // 2PAY.SYS.DDF01 (ASCII values of characters used):
            // This value requests the card or payment device to list the application
            // identifiers (AIDs) it supports in the response:
            '2', 'P', 'A', 'Y', '.', 'S', 'Y', 'S', '.', 'D', 'D', 'F', '0', '1',
            (byte)0x00 // LE   (max length of expected result, 0 implies 256)
    };


    public static final byte[] PPSE_APDU_SELECT_RESP = {
            (byte)0x6F,  // FCI Template
            (byte)0x2F,  // length = 35
            (byte)0x84,  // DF Name
            (byte)0x0E,  // length("2PAY.SYS.DDF01")
            // Data (ASCII values of characters used):
            '2', 'P', 'A', 'Y', '.', 'S', 'Y', 'S', '.', 'D', 'D', 'F', '0', '1',
            (byte)0xA5, // FCI Proprietary Template
            (byte)0x1D, // length = 17
            (byte)0xBF, // FCI Issuer Discretionary Data
            (byte)0x0C, // length = 12
            (byte)0x1A,
            (byte)0x61, // Directory Entry
            (byte)0x18, // Entry length = 12
            (byte)0x4F, // ADF Name
            (byte)0x07, // ADF Length = 7
            (byte)0xA0, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x65, (byte)0x10, (byte)0x10,
            // Tell the POS (point of sale terminal) that we support the standard jcb applet A0000000651010

            (byte)0x50, // Application Label
            (byte)0x0A, // Application Label Length = 16
            'J','C','B',' ','C','r','e','d','i','t',
            (byte)0x87,  // Application Priority Indicator
            (byte)0x01,  // length = 1
            (byte)0x01,
            (byte) 0x90, // SW1  (90 00 = Success)
            (byte) 0x00  // SW2
    };


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*
     *  MSD (Magnetic Stripe Data)
     */
    public static final byte[] JCB_MSD_SELECT = {
            (byte)0x00,  // CLA
            (byte)0xa4,  // INS
            (byte)0x04,  // P1
            (byte)0x00,  // P2
            (byte)0x07,  // LC (data length = 7)
            // POS is selecting the AID (JCB debit or credit) that we specified in the PPSE
            // response:
            (byte)0xA0, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x65, (byte)0x10, (byte)0x10,
            (byte)0x00   // LE
    };

    public static final byte[] JCB_MSD_SELECT_RESPONSE = {
            (byte) 0x6F,  // File Control Information (FCI) Template
            (byte) 0x24,  // length = 30 (0x1E)
            (byte) 0x84,  // Dedicated File (DF) Name
            (byte) 0x07,  // DF length = 7

            // A0000000651010
            (byte)0xA0, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x65, (byte)0x10, (byte)0x10,

            (byte) 0xA5,  // File Control Information (FCI) Proprietary Template
            (byte) 0x13,  // length = 19 (0x13)
            (byte) 0x50,  // Application Label
            (byte) 0x0A,  // length
            'J', 'C', 'B', ' ', 'C', 'r', 'e', 'd', 'i', 't',
            (byte) 0x9F, (byte) 0x38,  // Processing Options Data Object List (PDOL)
            (byte) 0x03,  // length
            (byte) 0x9F, (byte) 0x52, (byte) 0x01, // PDOL value (Does this request terminal type?)
            (byte) 0x5F, (byte) 0x2D,  // Language Preference
            (byte) 0x04,  // length
            (byte) 0x6A, (byte) 0x61, (byte) 0x65, (byte) 0x6E,
            (byte) 0x90,  // SW1
            (byte) 0x00   // SW2
    };
}
