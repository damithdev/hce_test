package com.example.nfctest.hce.commands;

public class CardData {

    public CardData(String PPSEDedicatedFileName, String applicationIdentifier, String applicationLabel, String applicationPriorityIndicator, String APPDFName, String processingOptionsDataObjectList, String languagePreference, String issuerCodeTableIndex, String applicationPreferredName, String applicationInterchangeProfile, String applicationFileLocator, String track2, String cardHolderName, String track1, String PAN, String PANSequenceNo, String applicationExpDate, String issuerCountryCode, String applicationUsageControl, String cardRiskManagementDataObjectList1, String IACDefault, String IACDenial, String IACOnline, String issuerPublicKeyCertificate, String certificationAuthorityPublicKeyIndex, String issuerPublicKeyExponent, String staticDataAuthenticationTagList, String ICCPublicKeyCertificate, String ICCPublicKeyExponent, String ICCPublicKeyRemainder) {
        this.PPSEDedicatedFileName = PPSEDedicatedFileName;
        ApplicationIdentifier = applicationIdentifier;
        ApplicationLabel = applicationLabel;
        ApplicationPriorityIndicator = applicationPriorityIndicator;
        this.APPDFName = APPDFName;
        ProcessingOptionsDataObjectList = processingOptionsDataObjectList;
        LanguagePreference = languagePreference;
        IssuerCodeTableIndex = issuerCodeTableIndex;
        ApplicationPreferredName = applicationPreferredName;
        ApplicationInterchangeProfile = applicationInterchangeProfile;
        ApplicationFileLocator = applicationFileLocator;
        Track2 = track2;
        CardHolderName = cardHolderName;
        Track1 = track1;
        this.PAN = PAN;
        this.PANSequenceNo = PANSequenceNo;
        ApplicationExpDate = applicationExpDate;
        IssuerCountryCode = issuerCountryCode;
        ApplicationUsageControl = applicationUsageControl;
        CardRiskManagementDataObjectList1 = cardRiskManagementDataObjectList1;
        this.IACDefault = IACDefault;
        this.IACDenial = IACDenial;
        this.IACOnline = IACOnline;
        IssuerPublicKeyCertificate = issuerPublicKeyCertificate;
        CertificationAuthorityPublicKeyIndex = certificationAuthorityPublicKeyIndex;
        IssuerPublicKeyExponent = issuerPublicKeyExponent;
        StaticDataAuthenticationTagList = staticDataAuthenticationTagList;
        this.ICCPublicKeyCertificate = ICCPublicKeyCertificate;
        this.ICCPublicKeyExponent = ICCPublicKeyExponent;
        this.ICCPublicKeyRemainder = ICCPublicKeyRemainder;
    }

    // PPSE SELECT
    public final String PPSEDedicatedFileName; //84
    public final String ApplicationIdentifier; //4F
    public final String ApplicationLabel; //50
    public final String ApplicationPriorityIndicator; //87

    // APP SELECT

    public final String APPDFName; //84
    public final String ProcessingOptionsDataObjectList; //9F38
    public final String LanguagePreference; //5F2D
    public final String IssuerCodeTableIndex; //9F11
    public final String ApplicationPreferredName; //9F12

    // GPO

    public final String ApplicationInterchangeProfile; //82
    public final String ApplicationFileLocator; //94

    // Read Record

    public final String Track2; //57
    public final String CardHolderName; //5F20
    public final String Track1; //9F1F

    public final String PAN; //
    public final String PANSequenceNo; //5F34
    public final String ApplicationExpDate; //5F24
    public final String IssuerCountryCode; //5F28
    public final String ApplicationUsageControl; //9F07
    public final String CardRiskManagementDataObjectList1; //8C
    public final String IACDefault; //9F0D
    public final String IACDenial; //9F0E
    public final String IACOnline; //9F0F

    public final String IssuerPublicKeyCertificate; //90

    public final String CertificationAuthorityPublicKeyIndex; //8f
    public final String IssuerPublicKeyExponent; //9F32
    public final String StaticDataAuthenticationTagList; //9F4A

    public final String ICCPublicKeyCertificate; //9F46

    public final String ICCPublicKeyExponent; //9F47
    public final String ICCPublicKeyRemainder; //9F48


}
