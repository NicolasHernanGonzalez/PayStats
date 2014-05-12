package ar.com.gl.paystadistics.services;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.annotation.PostConstruct;

import lombok.extern.java.Log;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ar.com.gl.paystadistics.domain.CashPayItem;
import ar.com.gl.paystadistics.domain.CreditCardBillItem;
import ar.com.gl.paystadistics.domain.CreditCardEnum;
import ar.com.gl.paystadistics.exceptions.BusinessException;

import com.google.gdata.client.spreadsheet.CellQuery;
import com.google.gdata.client.spreadsheet.FeedURLFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetQuery;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

@Component
@Log
public class GoogleSpreadsheetStatsExporter implements IStatsExporter {

    @Value("${google.drive.user}")
    private String googleUser;

    @Value("${google.drive.password}")
    private String googlePass;
    
    private SpreadsheetService service;
    
    @PostConstruct
    public void postConstructor() throws AuthenticationException {
    	
    	log.info("Verifying credentials...");
    	
    	service = verifyCredentials();
    }
    
    @Override
    public void exportStats(Map<CreditCardEnum, CreditCardBillItem> stats) {
        
    	log.info("Export stats using google spreasheet implementation");
        exportStats(stats,"Personal Business Stuff");
    }
    
    @Override
	public void exportCashPayment(CashPayItem item,String spreadSheetName) throws IOException, ServiceException {
    	
    	SpreadsheetEntry entry = retrieveSpreedSheet(spreadSheetName);
        
        URL listFeedUrl = getListFeedUrl(entry);
        
        ListEntry row = buildSpreadSheetRows(item);
		
		log.log(Level.INFO,"adding spreadsheet rows");
		row = service.insert(listFeedUrl, row);
	}
    

	protected void exportStats(Map<CreditCardEnum,CreditCardBillItem> dtos,String spreadSheetName) {
        
        FeedURLFactory factory = FeedURLFactory.getDefault();

        try {
                
                SpreadsheetFeed spreadsheetFeed = getSpreadSheetFeed(factory,spreadSheetName);
            
                List<SpreadsheetEntry> spreadsheets = spreadsheetFeed.getEntries();
            
                if (spreadsheets.isEmpty()) {
                    log.log(Level.SEVERE,"No spreadsheets with the name : '" + spreadSheetName + "'");
                    throw new BusinessException("Error : No spreadsheets with the name : '" + spreadSheetName + "'");
                }
            
                SpreadsheetEntry entry = spreadsheets.get(0);
                
                log.info("Using '" + entry.getTitle().getPlainText() + "' spreadsheet");
                
                URL cellFeedUrl = getCellFeedUrl(entry);
                
                updateCells(dtos, cellFeedUrl);
        
            } 
            
            catch (AuthenticationException e) {
                log.log(Level.SEVERE,"Invalid User credentials)");
                throw new BusinessException("Invalid User credentials",e);
            }
            
            catch (Exception e) {
                log.log(Level.SEVERE,"Error trying to export metrics to google spreadsheet (" + spreadSheetName + ")");
                throw new BusinessException("Error trying to export metrics to google spreadsheet",e);
            }
        }

        private URL getCellFeedUrl(SpreadsheetEntry entry) throws IOException, ServiceException {
            WorksheetFeed worksheetFeed = service.getFeed(entry.getWorksheetFeedUrl(), WorksheetFeed.class);
            List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
            WorksheetEntry worksheet = worksheets.get(0);
            return worksheet.getCellFeedUrl();
        }
        
        protected SpreadsheetService verifyCredentials() throws AuthenticationException {
            SpreadsheetService service = new SpreadsheetService("metricSpreadSheet");
            service.setUserCredentials(googleUser,googlePass);
            return service;
        }
        
        protected SpreadsheetFeed getSpreadSheetFeed(FeedURLFactory factory,String spreadSheetName) throws IOException, ServiceException {
            SpreadsheetQuery spreadsheetQuery = new SpreadsheetQuery(factory.getSpreadsheetsFeedUrl());
            spreadsheetQuery.setTitleQuery(spreadSheetName);
            spreadsheetQuery.setTitleExact(true);
            return service.query(spreadsheetQuery, SpreadsheetFeed.class);
        }
        
            
        private void updateCells(Map<CreditCardEnum,CreditCardBillItem> dtos,URL cellFeedUrl) throws IOException, ServiceException {
            
            for (Map.Entry<CreditCardEnum,CreditCardBillItem> entry : dtos.entrySet()) {
                
                //TODO DESACOPLAR ESTO!
                String creditCardName = entry.getValue().getCreditCardName();
                String creditCardAmount = entry.getValue().getAmount();
                String creditCardExpirationDate = entry.getValue().getExpirationDate();
                
                CellEntry creditCardNameEntry = searchCellEntryByName(creditCardName,cellFeedUrl,service);
                
                CellEntry amountValueCellEntry = getRightCellEntryOf(creditCardNameEntry,cellFeedUrl, service);
                
                amountValueCellEntry.changeInputValueLocal(creditCardAmount);
                amountValueCellEntry.update();
                
                CellEntry expirationDateCellFeed = getRightCellEntryOf(amountValueCellEntry,cellFeedUrl, service);
                
                expirationDateCellFeed.changeInputValueLocal(creditCardExpirationDate);
                expirationDateCellFeed.update();
            }
            
        }

        private CellEntry searchCellEntryByName(String cellKey,URL cellFeedUrl,SpreadsheetService service) throws IOException, ServiceException {
            CellQuery cellQuery = new CellQuery(cellFeedUrl);
            cellQuery.setFullTextQuery(cellKey);
            CellFeed cellFeed = service.query(cellQuery,CellFeed.class);
            return cellFeed.getEntries().get(0);
        }
        
        private CellEntry getRightCellEntryOf(CellEntry cellEntry,URL cellFeedUrl,SpreadsheetService service) throws IOException, ServiceException {
            
            int  cellTitleRow = cellEntry.getCell().getRow();
            char cellTitleCol = cellEntry.getTitle().getPlainText().toString().charAt(0);
            char cellValueCol = ++cellTitleCol;  
            
            CellQuery cellQuery = new CellQuery(cellFeedUrl);
            cellQuery.setRange(String.valueOf(cellValueCol) + String.valueOf(cellTitleRow));
            CellFeed cellFeed = service.query(cellQuery,CellFeed.class);
            
            return cellFeed.getEntries().get(0);
        }

        private ListEntry buildSpreadSheetRows(CashPayItem item) {
			
			ListEntry row = new ListEntry();
            
            log.log(Level.INFO,"Building spreadsheet rows");
            row.getCustomElements().setValueLocal("Concept",item.getConceptEnum().name());
			row.getCustomElements().setValueLocal("Amount", item.getAmount().toString());
			row.getCustomElements().setValueLocal("Date", item.getDate().toString());
			
			return row;
		}


		/**
		 * Get the Google spreed sheet according with the key name parameter,throwing an exception if not exists
		 * @param spreadSheetName
		 * @return
		 * @throws IOException
		 * @throws ServiceException
		 */
		private SpreadsheetEntry retrieveSpreedSheet(String spreadSheetName) throws IOException, ServiceException {
			
			FeedURLFactory factory = FeedURLFactory.getDefault();

        	SpreadsheetFeed spreadsheetFeed = getSpreadSheetFeed(factory,spreadSheetName);
            
            List<SpreadsheetEntry> spreadsheets = spreadsheetFeed.getEntries();
        
            if (spreadsheets.isEmpty()) {
                log.log(Level.SEVERE,"No spreadsheets with the name : '" + spreadSheetName + "'");
                throw new BusinessException("Error : No spreadsheets with the name : '" + spreadSheetName + "'");
            }
        
            SpreadsheetEntry entry = spreadsheets.get(0);
			return entry;
		}
        
        private URL getListFeedUrl(SpreadsheetEntry entry) throws IOException, ServiceException {
    		
        	WorksheetFeed worksheetFeed = service.getFeed(entry.getWorksheetFeedUrl(), WorksheetFeed.class);
    		List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
    		WorksheetEntry worksheet = worksheets.get(1);
    		
    		return worksheet.getListFeedUrl();	
    	}


		
}
