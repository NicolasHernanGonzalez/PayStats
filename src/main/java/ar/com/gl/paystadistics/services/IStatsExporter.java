package ar.com.gl.paystadistics.services;

import java.io.IOException;
import java.util.Map;

import com.google.gdata.util.ServiceException;

import ar.com.gl.paystadistics.domain.CashPayItem;
import ar.com.gl.paystadistics.domain.CreditCardEnum;
import ar.com.gl.paystadistics.domain.CreditCardBillItem;

public interface IStatsExporter {

    void exportStats(Map<CreditCardEnum,CreditCardBillItem> stats);
    
	void exportCashPayment(CashPayItem item, String spreadSheetName) throws IOException, ServiceException;
    
}
