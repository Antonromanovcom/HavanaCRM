package com.antonromanov.havanacrm.view.converters;

import com.antonromanov.havanacrm.mainlogic.controller.MainBean;
import com.antonromanov.havanacrm.model.Option;
import com.antonromanov.havanacrm.model.OrderSubType;
import com.antonromanov.havanacrm.model.OrderType;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter("orderSubTypeConverter")
public class OrderSubTypeConverter implements Converter {


    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                //MainBean service = (MainBean) fc.getExternalContext().getApplicationMap().get("mainBean");
                OrderSubType result = new OrderSubType();

                System.out.println("Мы получили валуе (orderSubType) - " + value);
                MainBean service = (MainBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("mainBean");


                for (int i = 0; i < service.getOrderSubTypes().size(); i++) {
                    System.out.println(service.getOrderSubTypes().get(i).getSubtype());
                    if (Integer.parseInt(value) == (service.getOrderSubTypes().get(i).getId())) {
//                        System.out.println(("Хотим добавить Ордер-САБ-Тайп :" + Integer.parseInt(value) + "->" + (service.getSelectedPlan().getOptions()).get(i).getId()));
                        result = service.getOrderSubTypes().get(i);
                    }
                }


                //return service.getSelectedPlan().getOptions().get(Integer.parseInt(value)-2);
                return result;


            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid theme."));
            }
        } else {
            return null;
        }
    }

    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            return String.valueOf(((OrderSubType) object).getId());
        } else {
            return null;
        }
    }
}
