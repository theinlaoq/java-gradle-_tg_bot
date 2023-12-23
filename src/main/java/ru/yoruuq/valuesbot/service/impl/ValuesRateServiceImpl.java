package ru.yoruuq.valuesbot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import ru.yoruuq.valuesbot.client.CrbClient;
import ru.yoruuq.valuesbot.exception.ServiceException;
import ru.yoruuq.valuesbot.service.ValuesRateService;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;

@Service
public class ValuesRateServiceImpl implements ValuesRateService {
    private static final String EURO_XPATH = "/ValCurs//Valute[@ID='R01239']/Value";
    private static final String USD_XPATH = "/ValCurs//Valute[@ID='R01235']/Value";



    @Autowired
    private CrbClient client;


    @Override
    public String getUSDRate() throws ServiceException {
        String xml = client.getRatesXML();
        return extractCurValues(xml, USD_XPATH);
    }

    @Override
    public String getEURORate() throws ServiceException {
        String xml = client.getRatesXML();
        return extractCurValues(xml, EURO_XPATH);
    }

    private static String extractCurValues(String xml, String xPathExp) throws ServiceException {
        InputSource source = new InputSource(new StringReader(xml));
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            Document document = (Document) xPath.evaluate("/", source, XPathConstants.NODE);

            return xPath.evaluate(xPathExp, document);
        }catch (XPathExpressionException e){
            throw new ServiceException("Can not parse XML", e);
        }
    }
}
