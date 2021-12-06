package com.example.nfctest.hce.commands;

import com.payneteasy.tlv.BerTag;
import com.payneteasy.tlv.BerTlvBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class C1_PPSE_Select extends APDUCommandAbs {


    /*
     *  PPSE (Proximity Payment System Environment)
     *
     *  This is the first select that a point of sale device will send to the payment device.
     */
    public static final byte[] PPSE_APDU_SELECT = {
            (byte) 0x00, // CLA (class of command)
            (byte) 0xA4, // INS (instruction); A4 = select
            (byte) 0x04, // P1  (parameter 1)  (0x04: select by name)
            (byte) 0x00, // P2  (parameter 2)
            (byte) 0x0E, // LC  (length of data)  14 (0x0E) = length("2PAY.SYS.DDF01")
            // 2PAY.SYS.DDF01 (ASCII values of characters used):
            // This value requests the card or payment device to list the application
            // identifiers (AIDs) it supports in the response:
            '2', 'P', 'A', 'Y', '.', 'S', 'Y', 'S', '.', 'D', 'D', 'F', '0', '1',
            (byte) 0x00 // LE   (max length of expected result, 0 implies 256)
    };

    public static byte[] response(byte[] apdu,CardData cardData) {
        try {

            byte[] bytes = new BerTlvBuilder()
                    .addBytes(new BerTag(0x6F),
                            new BerTlvBuilder()
                                    .addHex(new BerTag(0x84), cardData.PPSEDedicatedFileName)
                                    .addBytes(new BerTag(0xA5), new BerTlvBuilder()
                                            .addBytes(new BerTag(0xBF,0x0C), new BerTlvBuilder()
                                                    .addBytes(new BerTag(0x61), new BerTlvBuilder()
                                                            .addHex(new BerTag(0x4F), cardData.APPDFName)
                                                            .addHex(new BerTag(0x50), cardData.ApplicationLabel)
                                                            .addHex(new BerTag(0x87), cardData.ApplicationPriorityIndicator)
                                                            .buildArray()
                                                    )
                                                    .buildArray()
                                            )
                                            .buildArray()
                                    )
                                    .buildArray()
                    )
                    .buildArray();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            outputStream.write(bytes);
            outputStream.write(ISO7816_PROCESS_COMPLETED);
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return ISO7816_UNKNOWN_ERROR_RESPONSE;
        }
    }
//    public static final byte[] PPSE_APDU_SELECT_RESP = {
//            (byte) 0x6F,  // FCI Template
//            (byte) 0x2F,  // length = 35
//            (byte) 0x84,  // DF Name
//            (byte) 0x0E,  // length("2PAY.SYS.DDF01")
//            // Data (ASCII values of characters used):
//            '2', 'P', 'A', 'Y', '.', 'S', 'Y', 'S', '.', 'D', 'D', 'F', '0', '1',
//            (byte) 0xA5, // FCI Proprietary Template
//            (byte) 0x1D, // length = 17
//            (byte) 0xBF, // FCI Issuer Discretionary Data
//            (byte) 0x0C, // length = 12
//            (byte) 0x1A,
//            (byte) 0x61, // Directory Entry
//            (byte) 0x18, // Entry length = 12
//            (byte) 0x4F, // ADF Name
//            (byte) 0x07, // ADF Length = 7
//            (byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x65, (byte) 0x10, (byte) 0x10,
//            // Tell the POS (point of sale terminal) that we support the standard jcb applet A0000000651010
//
//            (byte) 0x50, // Application Label
//            (byte) 0x0A, // Application Label Length = 16
//            'J', 'C', 'B', ' ', 'C', 'r', 'e', 'd', 'i', 't',
//            (byte) 0x87,  // Application Priority Indicator
//            (byte) 0x01,  // length = 1
//            (byte) 0x01,
//            (byte) 0x90, // SW1  (90 00 = Success)
//            (byte) 0x00  // SW2
//    };

}
