package com.antonromanov.havanacrm.view.converters;

import com.antonromanov.havanacrm.mainlogic.controller.MainBean;
import com.antonromanov.havanacrm.model.Option;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter("optionsConverter")
public class OptionsConverter implements Converter {


    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                //MainBean service = (MainBean) fc.getExternalContext().getApplicationMap().get("mainBean");
                Option result = new Option();

                System.out.println("Мы получили валуе - " + value);
                MainBean service = (MainBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("mainBean");


                for (int i = 0; i < service.getSelectedPlan().getOptions().size(); i++) {
                    System.out.println((service.getSelectedPlan().getOptions()).get(i).getOptionName());
                    if (Integer.parseInt(value) == (service.getSelectedPlan().getOptions()).get(i).getId()) {
                        System.out.println(("Удаляем :" + Integer.parseInt(value) + "->" + (service.getSelectedPlan().getOptions()).get(i).getId()));
                        result = service.getSelectedPlan().getOptions().get(i);
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
            return String.valueOf(((Option) object).getId());
        } else {
            return null;
        }
    }
}
