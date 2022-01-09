package com.example.nfctest.hce.commands;

import android.util.Log;

import com.example.nfctest.hce.util.StringUtil;
import com.payneteasy.tlv.BerTag;
import com.payneteasy.tlv.BerTlvBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class C5_GenAC extends APDUCommandAbs{

    private static final String TAG = "C4_Read_Record";

    public static boolean isGenACCommand(byte[] apdu) {
        return (apdu.length > 4 &&
                apdu[0] == GEN_AC_COMMAND[0] &&
                apdu[1] == GEN_AC_COMMAND[1]
        );
    }

    private static final byte[] GEN_AC_COMMAND = {
            (byte) 0x80,  // CLA
            (byte) 0xAE,  // INS
            (byte) 0x50,  // P1
            (byte) 0x00,  // P2
            (byte) 0x00   // length
    };


    public static byte[] response(byte[] apdu,CardData cardData) {

        try {
            int referenceControl = apdu[2];
            byte[] data = null;
            switch (referenceControl){
                case 00:
                    // AAC
                    break;
                case 40:
                    // TC
                    break;
                case 80:
                    // ARQC
                    break;
                case 10:
                    // AAC + CDA
                    break;
                case 50:
                    // TC + CDA
                    data = getTCwithCDA(cardData);
                    break;
                case 90:
                    // ARQC + CDA
                    break;
                default:
            }

            if(data != null && data.length > 0){
                byte[] bytes = new BerTlvBuilder()
                        .addBytes(new BerTag(0x70),data)
                        .buildArray();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                outputStream.write(bytes);
                outputStream.write(ISO7816_PROCESS_COMPLETED);
                return outputStream.toByteArray();
            }

            return ISO7816_ERROR_FUNCTION_NOT_SUPPORTED;


        } catch (IOException e) {
            e.printStackTrace();
            return ISO7816_UNKNOWN_ERROR_RESPONSE;
        }
    }

    private static byte[] getTCwithCDA(CardData cardData) {
        Log.d(TAG,"GET TC With CDA");
        // TODO Perform CDA

        int CryptogramStatus = 0x40;
        int applicationTransactionCounter = 0;
        String signedDynamicApplicationData= "";
        int offlineAccBalance = 0;
        String issuerApplicationData = "";
        String dsSlotAvailability = "";
        return new BerTlvBuilder()
                .addHex(new BerTag(0x9F,0x27), String.valueOf(CryptogramStatus))
                .addHex(new BerTag(0x9F,0x36), StringUtil.getPaddedString(applicationTransactionCounter,4))
                .addHex(new BerTag(0x9F,0x4B), signedDynamicApplicationData)
                .addHex(new BerTag(0x9F,0x50), StringUtil.getPaddedString(offlineAccBalance,6))
                .addHex(new BerTag(0x9F,0x10), issuerApplicationData)
                .addHex(new BerTag(0x9F,0x5F), dsSlotAvailability)
                .buildArray();
    }
}
