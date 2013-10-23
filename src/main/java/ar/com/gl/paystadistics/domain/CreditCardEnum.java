package ar.com.gl.paystadistics.domain;

public enum CreditCardEnum {
    
    VISA_SANTANDER_RIO,AMEX_SANTANDER_RIO;
    
    
    public static CreditCardEnum[] getAllCreditCardsEnumAsArray() {
        CreditCardEnum[] creditCardEnums = new CreditCardEnum[2];
        creditCardEnums[0] = VISA_SANTANDER_RIO;
        creditCardEnums[1] = AMEX_SANTANDER_RIO;
        return creditCardEnums;
    }
    
    public static CreditCardEnum[] getSantanderRioEnumAsArray(){
        CreditCardEnum[] creditCardEnums = new CreditCardEnum[2];
        creditCardEnums[0] = VISA_SANTANDER_RIO;
        creditCardEnums[1] = AMEX_SANTANDER_RIO;
        return creditCardEnums;
    }
}