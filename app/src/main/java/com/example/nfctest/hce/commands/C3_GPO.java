package com.example.nfctest.hce.commands;

import android.util.Log;

import com.example.nfctest.hce.util.ByteUtil;
import com.payneteasy.tlv.BerTag;
import com.payneteasy.tlv.BerTlvBuilder;
import com.payneteasy.tlv.BerTlvParser;
import com.payneteasy.tlv.BerTlvs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class C3_GPO extends APDUCommandAbs {
    private static final String TAG = "C3_GPO";

    //// GPO (Get Processing Options)

    /*
     *  The data in the request can vary, but it won't affect our response. This method
     *  checks the initial 4 bytes of an APDU to see if it's a GPO command.
     */

    public static boolean isGpoCommand(byte[] apdu) {
        return (apdu.length > 4 &&
                apdu[0] == GPO_COMMAND[0] &&
                apdu[1] == GPO_COMMAND[1] &&
                apdu[2] == GPO_COMMAND[2] &&
                apdu[3] == GPO_COMMAND[3]
        );
    }

    public static final byte[] GPO_COMMAND = {
            (byte) 0x80,  // CLA
            (byte) 0xA8,  // INS
            (byte) 0x00,  // P1
            (byte) 0x00,  // P2
            (byte) 0x03,  // LC (length)
            // data
            (byte) 0x83,  // tag
            (byte) 0x01,  // length
            (byte) 0x03,  //
            (byte) 0x00   // Le
    };
//    public static final byte[] GPO_COMMAND_RESPONSE = {
//            (byte) 0x80,
//            (byte) 0x06,  // length
//            (byte) 0x00,
//            (byte) 0x80,
//            (byte) 0x08,
//            (byte) 0x01,
//            (byte) 0x01,
//            (byte) 0x00,
//            (byte) 0x90,  // SW1
//            (byte) 0x00   // SW2
//    };



    public static byte[] response(byte[] apdu,CardData cardData) {

        try {
            int lenPosition = 4;
            int len = apdu[lenPosition];
            byte[] data = new byte[len];
            for (int i = 0; i < len; i++) {
                data[i] = apdu[i + lenPosition + 1];
            }

            BerTlvParser parser = new BerTlvParser();
            BerTlvs tlvs = parser.parse(data, 0, data.length);
            byte[] tciBytes = tlvs.find(new BerTag(0x83)).getBytesValue();

            if(tciBytes[0] == 2 | tciBytes[0] == 3){
                Log.d(TAG,"TERMINAL EMV Capable");


                byte[] bytes = new BerTlvBuilder()
                        .addBytes(new BerTag(0x77),new BerTlvBuilder()
                                .addHex(new BerTag(0x82), cardData.ApplicationInterchangeProfile)
                                .addHex(new BerTag(0x94), cardData.ApplicationFileLocator)
                                .buildArray())
                        .buildArray();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                outputStream.write(bytes);
                outputStream.write(ISO7816_PROCESS_COMPLETED);
                return outputStream.toByteArray();
            }

            return ISO7816_ERROR_CONDITIONS_OF_USE_NOT_SATISFIED;


        } catch (IOException e) {
            e.printStackTrace();
            return ISO7816_UNKNOWN_ERROR_RESPONSE;
        }
    }
}
