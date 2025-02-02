package com.agrotis.backendtest.utils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import java.beans.PropertyDescriptor;

public class ComparatorUtils {
    public static void compararECopiarValores(Object velho, Object novo) {
        if (velho == null || novo == null) {
            return;
        }

        BeanWrapper velWrap = new BeanWrapperImpl(velho);
        BeanWrapper novWrap = new BeanWrapperImpl(novo);
        PropertyDescriptor[] pds = velWrap.getPropertyDescriptors();

        for (PropertyDescriptor pd : pds) {
            String prpNome = pd.getName();
            if ("class".equals(prpNome)) {
                continue;
            }

            Object propertyValue = velWrap.getPropertyValue(prpNome);
            if (propertyValue != null) {
                novWrap.setPropertyValue(prpNome, propertyValue);
            }
        }
    }
}