package com.antonromanov.havanacrm.view.converters;

import com.antonromanov.havanacrm.mainlogic.controller.MainBean;
import com.antonromanov.havanacrm.model.Client;
import com.antonromanov.havanacrm.model.Option;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter("clientConverter")
public class ClientConverter implements Converter {


    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {

                Client result = new Client();
                MainBean service = (MainBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("mainBean");


                for (int i = 0; i < service.getClients().size(); i++) {

                    if (Integer.parseInt(value) == service.getClients().get(i).getId()) {
                        result = service.getClients().get(i);
                    }
                }

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
            return String.valueOf(((Client) object).getId());
        } else {
            return null;
        }
    }
}
