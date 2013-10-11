package ar.com.gl.paystadistics.services;

import java.util.Map;

import ar.com.gl.paystadistics.domain.CreditCardEnum;
import ar.com.gl.paystadistics.domain.CreditCardItem;

public interface IStatsExporter {

    void exportStats(Map<CreditCardEnum,CreditCardItem> stats);

}
