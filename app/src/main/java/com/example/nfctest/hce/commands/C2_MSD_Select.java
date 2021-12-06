package com.example.nfctest.hce.commands;

import android.util.Log;

import com.example.nfctest.hce.util.ByteUtil;
import com.payneteasy.tlv.BerTag;
import com.payneteasy.tlv.BerTlvBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class C2_MSD_Select extends APDUCommandAbs {

    private static final String TAG = "C2_MSD_Select";
    /*
     *  MSD (Magnetic Stripe Data)
     */
    public static final byte[] MSD_SELECT = {
            (byte) 0x00,  // CLA
            (byte) 0xa4,  // INS
            (byte) 0x04,  // P1
            (byte) 0x00,  // P2
            (byte) 0x07,  // LC (data length = 7)
            // POS is selecting the AID (JCB debit or credit) that we specified in the PPSE
            // response:
            (byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x65, (byte) 0x10, (byte) 0x10,
            (byte) 0x00   // LE
    };

    public static byte[] response(byte[] apdu,CardData cardData) {
        try {
            String hex = ByteUtil.bytes2HexStr(apdu);
            Log.d(TAG, hex);

            int lenPosition = 4;
            int len = apdu[lenPosition];
            Log.d(TAG, String.valueOf(len));
            byte[] data = new byte[len];
            for (int i = 0; i < len; i++) {
                data[i] = apdu[i + lenPosition + 1];
            }

            if(Arrays.equals(data, ByteUtil.hexStr2Bytes(cardData.APPDFName))){



                byte[] bytes = new BerTlvBuilder()
                        .addBytes(new BerTag(0x6F),new BerTlvBuilder()
                                .addHex(new BerTag(0x84), cardData.APPDFName)
                                .addBytes(new BerTag(0xA5), new BerTlvBuilder()
                                        .addHex(new BerTag(0x50),cardData.ApplicationLabel)
                                        .addHex(new BerTag(0x9F,0x38),cardData.ProcessingOptionsDataObjectList)
                                        .addHex(new BerTag(0x5F,0x2D),cardData.LanguagePreference)
                                        .addHex(new BerTag(0x9F,0x11),cardData.IssuerCodeTableIndex)
                                        .addHex(new BerTag(0x9F,0x12),cardData.ApplicationPreferredName)
                                        .buildArray()
                                )
                                .buildArray()
                        )
                        .buildArray();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                outputStream.write(bytes);
                outputStream.write(ISO7816_PROCESS_COMPLETED);
                return outputStream.toByteArray();
            }else {
                return ISO7816_ERROR_FILE_NOT_FOUND;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return ISO7816_UNKNOWN_ERROR_RESPONSE;
        }
    }


//    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    public static final byte[] MSD_SELECT_RESPONSE = {
//            (byte) 0x6F,  // File Control Information (FCI) Template
//            (byte) 0x24,  // length = 30 (0x1E)
//            (byte) 0x84,  // Dedicated File (DF) Name
//            (byte) 0x07,  // DF length = 7
//
//            // A0000000651010
//            (byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x65, (byte) 0x10, (byte) 0x10,
//
//            (byte) 0xA5,  // File Control Information (FCI) Proprietary Template
//            (byte) 0x13,  // length = 19 (0x13)
//            (byte) 0x50,  // Application Label
//            (byte) 0x0A,  // length
//            'J', 'C', 'B', ' ', 'C', 'r', 'e', 'd', 'i', 't',
//            (byte) 0x9F, (byte) 0x38,  // Processing Options Data Object List (PDOL)
//            (byte) 0x03,  // length
//            (byte) 0x9F, (byte) 0x52, (byte) 0x01, // PDOL value (Does this request terminal type?)
//            (byte) 0x5F, (byte) 0x2D,  // Language Preference
//            (byte) 0x04,  // length
//            (byte) 0x6A, (byte) 0x61, (byte) 0x65, (byte) 0x6E,
//            (byte) 0x90,  // SW1
//            (byte) 0x00   // SW2
//    };

}
