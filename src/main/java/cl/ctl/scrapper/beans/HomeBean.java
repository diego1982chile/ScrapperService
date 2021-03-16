package cl.ctl.scrapper.beans;

import cl.ctl.scrapper.controllers.Executor;
import cl.ctl.scrapper.helpers.LogHelper;
import cl.ctl.scrapper.helpers.ProcessHelper;
import cl.ctl.scrapper.managers.ScrapperManager;
import cl.ctl.scrapper.scrappers.AbstractScrapper;
import org.omnifaces.util.Ajax;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by des01c7 on 15-12-16.
 */

@ManagedBean(name = "homeBean")
@ViewScoped
public class HomeBean {

    private boolean custom = false;

    private static int timeOut;

    Date date = new Date();
    Date maxDate  = new Date();;

    boolean dateSelected = false;
    Executor executor;
    LogHelper logHelper = LogHelper.getInstance();
    String processName;

    int logAmount = 0;

    boolean processing = false;

    ProcessHelper processHelper = ProcessHelper.getInstance();

    private List<AbstractScrapper> selectedScrappers = new ArrayList<>();

    @Inject
    ScrapperManager scrapperManager;

    @PostConstruct
    public void init() {
        executor = new Executor();

        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        setProcessName(getProcessName(localDate));

        date = Date.from(localDate.minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        maxDate = Date.from(localDate.minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

        LocalDateTime localDateTime = LocalDateTime.now();

        if(localDateTime.getHour() < 14) {
            String msg = "Los archivos para el proceso actual aún no están disponibles. Vuelva a intentarlo más tarde";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", msg));
            Ajax.update("scrapper-form:messages");
        }


        for (AbstractScrapper abstractScrapper : ProcessHelper.getInstance().getScrappers().values()) {
            selectedScrappers.add(abstractScrapper);
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;

        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        setProcessName(getProcessName(localDate));
    }

    public boolean isCustom() {
        return custom;
    }

    public void setCustom(boolean custom) {
        this.custom = custom;
    }

    public String inputSwitchClass() {
        if(!custom) {
            return "ui-inputswitch ui-widget ui-widget-content ui-corner-all";
        }
        else {
            return "ui-inputswitch ui-widget ui-widget-content ui-corner-all ui-inputswitch-checked";
        }
    }

    public void selectDate() {
        dateSelected = true;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public List<AbstractScrapper> getSelectedScrappers() {
        return selectedScrappers;
    }

    public void setSelectedScrappers(List<AbstractScrapper> selectedScrappers) {
        this.selectedScrappers = selectedScrappers;
    }

    public boolean isProcessing() {
        return processing;
    }

    public void setProcessing(boolean processing) {
        this.processing = processing;
    }

    public void process() {
        RequestContext reqCtx = RequestContext.getCurrentInstance();
        FacesContext context = FacesContext.getCurrentInstance();
        LogHelper.getInstance().getLogs().clear();

        try {
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            processName = getProcessName(localDate);

            if(!validateDate()) {
                String msg = "Los archivos para el proceso actual aún no están disponibles. Vuelva a intentarlo más tarde";
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", msg));
                reqCtx.execute("PF('poll').stop();");
                reqCtx.getCurrentInstance().execute("cancel();");
                return;
            }

            processing = true;

            List<String> chains = new ArrayList<>();

            for (AbstractScrapper selectedScrapper : selectedScrappers) {
                chains.add(selectedScrapper.getCadena());
            }

            scrapperManager.scrap(localDate.toString(), chains);

            /*
            if(!dateSelected) {
                executor.process();
            }
            else {
                executor.process(localDate);
            }
            */

            reqCtx.execute("PF('poll').stop();");
            reqCtx.getCurrentInstance().execute("cancel();");

            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "El proceso se ha completado correctamente"));

        } catch (Exception e) {
            reqCtx.execute("PF('poll').stop();");
            reqCtx.getCurrentInstance().execute("cancel();");

            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ocurrió un error durante el proceso"));
            e.printStackTrace();
        }

    }

    public ProcessHelper getProcessHelper() {
        return processHelper;
    }

    public LogHelper getLogHelper() {
        return logHelper;
    }

    public void setLogHelper(LogHelper logHelper) {
        this.logHelper = logHelper;
    }

    public void scroll() {
        if(LogHelper.getInstance().getLogs().size() > logAmount) {
            logAmount = LogHelper.getInstance().getLogs().size();
            //RequestContext.getCurrentInstance().execute("scroll();");
            RequestContext.getCurrentInstance().scrollTo("scrapper-form:progressBarClient");
            Ajax.update("scrapper-form:logs");
        }
    }

    public String getProcessName(LocalDate localDate) {
        if(localDate.equals(LocalDate.now())) {
            localDate = localDate.minusDays(1);
        }

        String year = String.valueOf(localDate.getYear());
        String month = String.valueOf(localDate.getMonthValue());
        String day = String.valueOf(localDate.getDayOfMonth());

        if(localDate.getMonthValue() < 10) {
            month = "0" + month;
        }

        if(localDate.getDayOfMonth() < 10) {
            day = "0" + day;
        }

        return year + month + day;
    }

    public Date getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }

    public boolean validateDate() {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if(localDate.equals(LocalDate.now().minusDays(1))) {
            return LocalDateTime.now().getHour() >= 14;
        }
        else {
            return true;
        }
    }



}
