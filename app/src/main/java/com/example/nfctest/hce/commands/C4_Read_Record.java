package com.example.nfctest.hce.commands;

import android.util.Log;

import com.payneteasy.tlv.BerTag;
import com.payneteasy.tlv.BerTlvBuilder;
import com.payneteasy.tlv.BerTlvParser;
import com.payneteasy.tlv.BerTlvs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class C4_Read_Record extends APDUCommandAbs{
    private static final String TAG = "C4_Read_Record";

    public static boolean isReadRecCommand(byte[] apdu) {
        return (apdu.length > 4 &&
                apdu[0] == READ_REC_COMMAND[0] &&
                apdu[1] == READ_REC_COMMAND[1]
        );
    }

    private static final byte[] READ_REC_COMMAND = {
            (byte) 0x00,  // CLA
            (byte) 0xB2,  // INS
            (byte) 0x01,  // P1
            (byte) 0x0C,  // P2
            (byte) 0x00   // length
    };

    public static byte[] response(byte[] apdu,CardData cardData) {

        try {
            int recordNumber = apdu[2];
            byte[] data = null;
            switch (recordNumber){
                case 1:
                    data = getRecordOne(cardData);
                    break;
                case 2:
                    data = getRecordTwo(cardData);
                    break;
                case 3:
                    data = getRecordThree(cardData);
                    break;
                case 4:
                    data = getRecordFour(cardData);
                    break;
                case 5:
                    data = getRecordFive(cardData);
                    break;
                case 6:
                    data = getRecordSix(cardData);
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

            return ISO7816_ERROR_RECORD_NOT_FOUND;


        } catch (IOException e) {
            e.printStackTrace();
            return ISO7816_UNKNOWN_ERROR_RESPONSE;
        }
    }

    private static byte[] getRecordOne(CardData cardData){
            Log.d(TAG,"Read Record Rec No 1");
            return new BerTlvBuilder()
                            .addHex(new BerTag(0x57), cardData.Track2)
                            .addHex(new BerTag(0x5F,0x20), cardData.CardHolderName)
                            .addHex(new BerTag(0x59F,0x1F), cardData.Track1)
                            .buildArray();

    }

    private static byte[] getRecordTwo(CardData cardData) {
        Log.d(TAG,"Read Record Rec No 2");

        return new BerTlvBuilder()
                .addHex(new BerTag(0x5A), cardData.PAN)
                .addHex(new BerTag(0x5F,0x34), cardData.PANSequenceNo)
                .addHex(new BerTag(0x5F,0x24), cardData.ApplicationExpDate)
                .addHex(new BerTag(0x5F,0x28), cardData.IssuerCountryCode)
                .addHex(new BerTag(0x9F,0x07), cardData.ApplicationUsageControl)
                .addHex(new BerTag(0x8C), cardData.CardRiskManagementDataObjectList1)
                .addHex(new BerTag(0x9F,0x0D), cardData.IACDefault)
                .addHex(new BerTag(0x9F,0x0E), cardData.IACDenial)
                .addHex(new BerTag(0x9F,0x0F), cardData.IACOnline)
                .buildArray();

    }

    private static byte[] getRecordThree(CardData cardData) {
        Log.d(TAG,"Read Record Rec No 3");
        return new BerTlvBuilder()
                .addHex(new BerTag(0x90), cardData.IssuerPublicKeyCertificate)
                .buildArray();

    }

    private static byte[] getRecordFour(CardData cardData) {
        Log.d(TAG,"Read Record Rec No 4");
        return new BerTlvBuilder()
                .addHex(new BerTag(0x8F), cardData.CertificationAuthorityPublicKeyIndex)
                .addHex(new BerTag(0x9F,0x32), cardData.IssuerPublicKeyExponent)
                .addHex(new BerTag(0x9F,0x4A), cardData.StaticDataAuthenticationTagList)
                .buildArray();

    }

    private static byte[] getRecordFive(CardData cardData) {
        Log.d(TAG,"Read Record Rec No 5");
        return new BerTlvBuilder()
                .addHex(new BerTag(0x9F,0x46), cardData.ICCPublicKeyCertificate)
                .buildArray();

    }

    private static byte[] getRecordSix(CardData cardData) {
        Log.d(TAG,"Read Record Rec No 6");
        return new BerTlvBuilder()
                .addHex(new BerTag(0x9F,0x47), cardData.ICCPublicKeyExponent)
                .addHex(new BerTag(0x9F,0x48), cardData.ICCPublicKeyRemainder)
                .buildArray();

    }
}
