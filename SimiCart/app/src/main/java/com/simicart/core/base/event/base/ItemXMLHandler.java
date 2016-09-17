package com.simicart.core.base.event.base;

/**
 * Created by frank on 8/15/16.
 */

import com.simicart.core.common.Utils;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class ItemXMLHandler extends DefaultHandler {

    ArrayList<String> mListSKU;
    private Boolean currentElement = false;
    private String currentValue = "";
    private String tags = "event";
    private String currentSKU;
    private String currentFullName;


    public ItemXMLHandler(ArrayList<String> listSKU) {
        this.mListSKU = listSKU;
    }


    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {

        currentElement = true;
        currentValue = "";

    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        currentElement = false;

        if (localName.equalsIgnoreCase("fullname")) {
            currentFullName = currentValue;
            currentValue = "";

        } else if (localName.equalsIgnoreCase("sku")) {
            currentSKU = currentValue;
            currentValue = "";
        } else if (localName.equalsIgnoreCase(this.tags)) {
            if (Utils.validateString(currentSKU) && checkSKU(currentSKU)) {
                try {
                    Class<?> change = Class.forName(currentFullName);
                    change.getConstructor().newInstance();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {

        if (currentElement) {
            currentValue = currentValue + new String(ch, start, length);
        }

    }


    private boolean checkSKU(String sku) {
        if (null != mListSKU && mListSKU.size() > 0) {
            for (int i = 0; i < mListSKU.size(); i++) {
                String currentSKU = mListSKU.get(i);
                if (sku.equals(currentSKU)) {
                    return true;
                }
            }
        }
        return false;
    }

}
