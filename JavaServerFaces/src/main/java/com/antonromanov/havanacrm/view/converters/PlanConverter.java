package com.antonromanov.havanacrm.view.converters;

import com.antonromanov.havanacrm.mainlogic.controller.MainBean;
import com.antonromanov.havanacrm.model.Option;
import com.antonromanov.havanacrm.model.OrderSubType;
import com.antonromanov.havanacrm.model.OrderType;
import com.antonromanov.havanacrm.model.Plan;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter("planConverter")
public class PlanConverter implements Converter {


    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {

                Plan result = new Plan();
                System.out.println("Мы выбрали план (говорит Конвертер) - " + value);
                MainBean service = (MainBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("mainBean");

                for (int i = 0; i < service.getPlans().size(); i++) {
                    System.out.println(service.getPlans().get(i).getPlanName());
                    if (Integer.parseInt(value) == (service.getPlans().get(i).getId())) {
                        result = service.getPlans().get(i);
                    }
                }

                return result;

            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid plan."));
            }
        } else {
            return null;
        }
    }

    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            return String.valueOf(((Plan) object).getId());
        } else {
            return null;
        }
    }
}
