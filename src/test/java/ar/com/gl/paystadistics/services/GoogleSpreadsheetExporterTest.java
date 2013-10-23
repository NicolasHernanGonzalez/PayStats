package ar.com.gl.paystadistics.services;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.annotation.ExpectedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ar.com.gl.paystadistics.domain.CreditCardEnum;
import ar.com.gl.paystadistics.domain.CreditCardItem;

import com.google.gdata.client.spreadsheet.FeedURLFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.util.AuthenticationException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/application-contextTest.xml"})
@Configurable
public class GoogleSpreadsheetExporterTest {
    
    @Autowired
    private IStatsExporter exporter;
    
    @SuppressWarnings("unchecked")
    @Test
    public void exportStatsUsingDefaultSpreadSheet(){
        
        //Mock
        GoogleSpreadsheetStatsExporter googleExporter = mock(GoogleSpreadsheetStatsExporter.class);
        
        Map<CreditCardEnum, CreditCardItem> map = new HashMap<CreditCardEnum, CreditCardItem>();
        
        Mockito.doCallRealMethod().when(googleExporter).exportStats(map);
        Mockito.doNothing().when(googleExporter).exportStats(anyMap(), anyString());
        
        //MUD
        googleExporter.exportStats(map);
        
        verify(googleExporter).exportStats(map,"Personal Business Stuff");
    }
    

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    @ExpectedException(ar.com.gl.paystadistics.exceptions.BusinessException.class)
    public void tryToExportToInvalidGoogleSpreadSheet() throws Exception{
        
        SpreadsheetService service = mock(SpreadsheetService.class);
        GoogleSpreadsheetStatsExporter googleSpreadsheetStatsExporter = mock(GoogleSpreadsheetStatsExporter.class);
        SpreadsheetFeed spreadsheetFeed = mock(SpreadsheetFeed.class);

        Mockito.doCallRealMethod().when(googleSpreadsheetStatsExporter).exportStats(anyMap(), anyString());
        when(googleSpreadsheetStatsExporter.verifyCredentials()).thenReturn(service);
        when(googleSpreadsheetStatsExporter.getSpreadSheetFeed(any(FeedURLFactory.class), any(SpreadsheetService.class), anyString())).thenReturn(spreadsheetFeed);
        when(spreadsheetFeed.getEntries()).thenReturn(new ArrayList());

        googleSpreadsheetStatsExporter.exportStats(new HashMap<CreditCardEnum, CreditCardItem>(), "spreadSheetName");
    }
    
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    @ExpectedException(ar.com.gl.paystadistics.exceptions.BusinessException.class)
    public void loginWithinvalidCredentials() throws AuthenticationException {
        
        GoogleSpreadsheetStatsExporter googleSpreadsheetStatsExporter = mock(GoogleSpreadsheetStatsExporter.class);
        Mockito.doCallRealMethod().when(googleSpreadsheetStatsExporter).exportStats(anyMap(), anyString());
        Mockito.doThrow(new AuthenticationException("Invalid Credentials")).when(googleSpreadsheetStatsExporter).verifyCredentials();
        googleSpreadsheetStatsExporter.exportStats(new HashMap(),"Test");

    }

    
    //    @Test
    public void otherTest() {
        
        Double acumulador = new Double(36000);
        
        for (int i = 0; i < 12; i++) {
            
            if (acumulador < 40000){
                acumulador = acumulador + ((acumulador * 15.07) / 100 ) / 12;
            }
            
            else if (acumulador > 40000 && acumulador < 50000){
                acumulador = acumulador +  ((acumulador * 15.60) /100) / 12;
            }
            
            else if (acumulador > 50000 && acumulador < 75000){
                acumulador = acumulador + ((acumulador * 16.37) / 100) / 12;
            }
            
            else if (acumulador > 75000 && acumulador < 100000){
                acumulador = acumulador +  ((acumulador * 16.47) / 100 ) / 12;
            }
            
            else if (acumulador > 100000 && acumulador < 200000){
                acumulador = acumulador + ((acumulador * 16.65) / 100 ) / 12;
            }
            
            else if (acumulador > 200000){
                acumulador = acumulador +  ((acumulador * 16.99) / 100 )/ 12;
            }
            
            acumulador = acumulador + 7000;
        }
        
        System.out.println("Luego de 12 meses. Total : " + acumulador);
        
        
    }
    
//    @Test
    public void otherTest3() {
        
        Double acumulador = new Double(36000);
        
        for (int i = 0; i < 12; i++) {
            
            if (acumulador < 40000){
                acumulador = acumulador + ((acumulador * 20.07) / 100 ) / 12;
            }
            
            else if (acumulador > 40000 && acumulador < 50000){
                acumulador = acumulador +  ((acumulador * 21.60) /100) / 12;
            }
            
            else if (acumulador > 50000 && acumulador < 75000){
                acumulador = acumulador + ((acumulador * 22.37) / 100) / 12;
            }
            
            else if (acumulador > 75000 && acumulador < 100000){
                acumulador = acumulador +  ((acumulador * 23.47) / 100 ) / 12;
            }
            
            else if (acumulador > 100000 && acumulador < 200000){
                acumulador = acumulador + ((acumulador * 24.65) / 100 ) / 12;
            }
            
            else if (acumulador > 200000) {
                acumulador = acumulador +  ((acumulador * 24.99) / 100 )/ 12;
            }
            
            acumulador = acumulador + 7000;
        }
        
        System.out.println("Luego de 1 año. Total : " + acumulador);
    }
 
}