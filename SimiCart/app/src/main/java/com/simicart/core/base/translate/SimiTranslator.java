package com.simicart.core.base.translate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Martial on 8/15/2016.
 */
public class SimiTranslator {

    private Map<String, String> mLanguages;

    public static SimiTranslator instance;

    public SimiTranslator() {
        mLanguages = new HashMap<String, String>();
    }

    public static SimiTranslator newInstance() {
        if(instance == null) {
            instance = new SimiTranslator();
        }
        return instance;
    }

    public void setLanguages(Map<String, String> languages) {
        try {
            mLanguages = new HashMap<String, String>();
            mLanguages = languages;
        } catch (Exception e) {

        }
    }

    public String translate(String language) {
        if (language == null) {
            return null;
        }
        String translater = language;
        if (!mLanguages.isEmpty()) {
            if (mLanguages.get(language.toLowerCase().trim()) != null) {
                translater = mLanguages.get(language.toLowerCase().trim());
                return translater;
            }
        }
        return translater;
    }

}
