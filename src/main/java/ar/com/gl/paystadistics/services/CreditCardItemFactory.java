package ar.com.gl.paystadistics.services;

import java.util.Map;

import javax.annotation.Resource;
import javax.mail.Message;

import lombok.extern.log4j.Log4j;

import org.springframework.stereotype.Component;

import ar.com.gl.paystadistics.domain.CreditCardEnum;
import ar.com.gl.paystadistics.domain.CreditCardItem;
import ar.com.gl.paystadistics.domain.SantanderRioCreditCardItem;
import ar.com.gl.paystadistics.exceptions.BusinessException;

@Component
@Log4j
public class CreditCardItemFactory {
	
	@Resource
	private Map<CreditCardEnum,ICreditCardItemFactory> creditCardsItemFactories;

	public CreditCardItem buildCreditCardItem(CreditCardEnum creditCardKey,Message message) throws BusinessException {
		log.debug("Choosing apropiate factory to build a CreditCardItem...");
		CreditCardItem item = creditCardsItemFactories.get(creditCardKey).buildCreditCardItem(message);
		return item;
	}
	
	public CreditCardItem buildSantanderRioCreditCardItem(Message message) throws BusinessException {
		CreditCardItem item = new SantanderRioCreditCardItem(message);
		return item;
	}
}