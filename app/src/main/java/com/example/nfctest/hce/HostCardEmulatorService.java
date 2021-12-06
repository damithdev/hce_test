package com.example.nfctest.hce;

import android.content.Intent;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.nfctest.hce.commands.APDUCommandAbs;
import com.example.nfctest.hce.commands.C1_PPSE_Select;
import com.example.nfctest.hce.commands.C2_MSD_Select;
import com.example.nfctest.hce.commands.C3_GPO;
import com.example.nfctest.hce.commands.C4_Read_Record;
import com.example.nfctest.hce.commands.CardData;
import com.example.nfctest.hce.util.ByteUtil;
import com.example.nfctest.hce.util.CommandRunnable;
import com.example.nfctest.hce.util.StaticEntry;

import java.util.Arrays;

public class HostCardEmulatorService extends HostApduService {

    private static final String TAG = "HCE";
    private static CardData cardData = null;
    public static Handler handler = new Handler();
    private static String seperator = "________________________________ \n";

//    The `processCommandApdu` method will be called every time a card reader sends
//    an APDU command that is filtered by our manifest filter.
    @Override
    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {
        Log.d(TAG,"APDU Received ");
        if(cardData == null)initCardData();

        String inboundApduDesc;
        byte[] responseApdu;

        try{
            if (Arrays.equals(C1_PPSE_Select.PPSE_APDU_SELECT, commandApdu)) {
                inboundApduDesc = "Received PPSE select: ";
                responseApdu = C1_PPSE_Select.response(commandApdu,cardData);
            }else if(Arrays.equals(C2_MSD_Select.MSD_SELECT,commandApdu)){
                inboundApduDesc = "Received MSD SELECT";
                responseApdu = C2_MSD_Select.response(commandApdu,cardData);
            }else if(C3_GPO.isGpoCommand(commandApdu)){
                inboundApduDesc = "Received GPO";
                responseApdu =  C3_GPO.response(commandApdu,cardData);
            }else if(C4_Read_Record.isReadRecCommand(commandApdu)) {
                inboundApduDesc = "Received Read Record";
                responseApdu = C4_Read_Record.response(commandApdu,cardData);
            }else{
                inboundApduDesc = "Received Unhandled APDU: \n" + seperator;
                responseApdu = APDUCommandAbs.ISO7816_UNKNOWN_ERROR_RESPONSE;
            }

        }catch (Exception e){
            Log.e(TAG,e.getMessage(),e);
            inboundApduDesc = "Received Unhandled APDU: \n" + seperator;
            responseApdu = APDUCommandAbs.ISO7816_UNKNOWN_ERROR_RESPONSE;
        }


        handler.post(new CommandRunnable(this,inboundApduDesc,commandApdu,responseApdu));
        return responseApdu;



    }

    @Override
    public void onDeactivated(int reason) {
        Log.d(TAG,"Deactivated: "+reason);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.initCardData();
    }





    private void initCardData(){
        cardData = new CardData(
                "325041592E5359532E4444463031",
                "A0000000651010",
                "4A43422050726570616964",
                "01",
                "A0000000651010",
                "9F5201",
                "656E",
                "01",
                "4A43422050726570616964",
                "1980",
                "1001010010020601",
                "3571114100152638D24012211900262300000F",
                "202F",
                "313930303230303030303030303030363233303030303030",
                "3571114100152638",
                "02",
                "240131",
                "0144",
                "2900",
                "9F02069F03069F1A0295055F2A029A039C019F37049F35019F5303",
                "0000000000",
                "B450000000",
                "0000000000",
                "4705476E184A6E5D250F6E4BCF93D812AAD0E36A7C19A45124CAB0EA507F52FABD869EA9F527214A9BC5876466C2BEC3BDEF3A0864F0AFEB4CDE5CC640972DE24FD4150656B6C495B0D32773DE7E08E23DB41EE7B6AFA16C6179825FFE19F214CE8C7F8AB48EADA08038D5087E6205B8F5BD4FDEA6568F68B9EAD46BC4B79FAC56841B0716FA6D75FAD3E9AED4151D5781D6E4E0695DABBF692F73137E76168D69A761A65EEC105F035B4B337B452307C1784A3AE37FA1C6044338E67C0B3D8C876A33544E089EBF978154B1099B87895DD431A025BE51F3A9B09EB184A90633D7E6E56BB7959FC4CB51087B3CB2575B266C01F82B5D0246",
                "13",
                "03",
                "82",
                "863C8631CA5FA16EDDDB29F769AEB2812251BCE2CE9B4E9244FEA638A3B5E160F0B089A4FB772126BC2B8D1D1D32A85A0E1F98387B518DED0A38D3460690F00448E74E4D5C727DCB805022C24FD3D01EB60DA5D695817758410066502ABAB943707584E7AF0629BE91973C397CA230D0B6112C4301CD24E44DBFAAE8C8A55E0752A5E34B6C57B2AD799854535D474BBA60B09F486F1B5070B7AA6CFE43154B9EB4DB7A5E7B072CCEE8E57B5AA13D2723",
                "03",
                "22576A59047E93D5FB1D613D927B98CA29E65C65832BA725BC0054302A558074223FF6999620806876B3"

        );
    }

}
