package com.example.nfctest.hce;

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;

public class HostCardEmulatorService extends HostApduService {

    private static final String TAG = "HCE";

    private static final String STATUS_SUCCESS = "9000";
    private static final String STATUS_FAILED  = "6F00";
    private static final String CLA_NOT_SUPPORTED  = "6E00";
    private static final String INS_NOT_SUPPORTED  = "6D00";


    private static final String SELECT_INS  = "A4";
    private static final String DEFAULT_CLA  = "00";

    private static final int MIN_APDU_LEN = 12;

    private static final String AID = "F0010203040506";



//    The `processCommandApdu` method will be called every time a card reader sends
//    an APDU command that is filtered by our manifest filter.
    @Override
    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {
        Log.d(TAG,"APDU Received ");

        String inboundApduDesc;
        byte[] responseApdu;

        try{

            if (Arrays.equals(ApduMessage.PPSE_APDU_SELECT, commandApdu)) {
                inboundApduDesc = "Received PPSE select: ";
                responseApdu = ApduMessage.PPSE_APDU_SELECT_RESP;
            }else if(Arrays.equals(ApduMessage.JCB_MSD_SELECT,commandApdu)){
                inboundApduDesc = "Received JCB MSD SELECT";
                responseApdu = ApduMessage.JCB_MSD_SELECT_RESPONSE;
            }else{
                inboundApduDesc = "Received Unhandled APDU: ";
                responseApdu = ApduMessage.ISO7816_UNKNOWN_ERROR_RESPONSE;
            }




        }catch (Exception e){
            Log.e(TAG,e.getMessage(),e);
            inboundApduDesc = "Received Unhandled APDU: ";
            responseApdu = ApduMessage.ISO7816_UNKNOWN_ERROR_RESPONSE;
        }

        Log.d(TAG,inboundApduDesc + ": \n Received:"+Util.bytes2HexStr(commandApdu));
        Log.d(TAG, "Received:"+Util.bytes2HexStr(commandApdu));
        Log.d(TAG,"Response:"+Util.bytes2HexStr(responseApdu));

        return responseApdu;





//
//        if(commandApdu == null){
//            return Util.hexStr2Bytes(STATUS_FAILED);
//        }
//
//        String hexCommandApdu = Util.bytes2HexStr(commandApdu);
//        Log.d(TAG,"APDU: "+hexCommandApdu);
//        if(hexCommandApdu.length() < MIN_APDU_LEN){
//            return Util.hexStr2Bytes(STATUS_FAILED);
//        }
//
//        if(!hexCommandApdu.substring(0, 2).equals(DEFAULT_CLA)){
//            return Util.hexStr2Bytes(CLA_NOT_SUPPORTED);
//        }
//
//        if(!hexCommandApdu.substring(2,4).equals(SELECT_INS)){
//            return Util.hexStr2Bytes(INS_NOT_SUPPORTED);
//        }
//
//        String aid = hexCommandApdu.substring(10,24);
//        if(aid.equals(AID)){
//            return Util.hexStr2Bytes(STATUS_SUCCESS);
//        }else{
//            return Util.hexStr2Bytes(STATUS_FAILED);
//        }
    }

//    The `onDeactiveted` method will be called when the a different AID has been selected
//    or the NFC connection has been lost.
    @Override
    public void onDeactivated(int reason) {
        Log.d(TAG,"Deactivated: "+reason);
    }

}
