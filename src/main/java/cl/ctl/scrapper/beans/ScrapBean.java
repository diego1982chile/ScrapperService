package cl.ctl.scrapper.beans;

import cl.ctl.scrapper.controllers.Executor;
import cl.ctl.scrapper.helpers.LogHelper;
import cl.ctl.scrapper.helpers.ProcessHelper;
import cl.ctl.scrapper.managers.ScrapperManager;
import cl.ctl.scrapper.model.FileControl;
import cl.ctl.scrapper.model.FileControlView;
import cl.ctl.scrapper.scrappers.AbstractScrapper;
import org.omnifaces.util.Ajax;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * Created by des01c7 on 15-12-16.
 */

@ManagedBean(name = "scrapBean")
@ApplicationScoped
public class ScrapBean {

    private boolean custom = false;

    private static int timeOut;

    Date date = new Date();
    Date maxDate  = new Date();;

    boolean dateSelected = false;
    //Executor executor;
    LogHelper logHelper = LogHelper.getInstance();
    String processName;

    int logAmount = 0;

    boolean processing = false;

    ProcessHelper processHelper = ProcessHelper.getInstance();

    private List<AbstractScrapper> selectedScrappers = new ArrayList<>();

    List<FileControlView> fileControlList = new ArrayList<>();

    @Inject
    ScrapperManager scrapperManager;


    @PostConstruct
    public void init() {
        //executor = new Executor();

        LogHelper.getInstance().getLogs().clear();

        date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        setProcessName(getProcessName(localDate));

        date = Date.from(localDate.minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        maxDate = Date.from(localDate.minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

        LocalDateTime localDateTime = LocalDateTime.now();

        if(localDateTime.getHour() < 14) {
            String msg = "Los scraps para el proceso '" + getProcessName(localDate) + "' aún no están disponibles. Vuelva a intentarlo más tarde";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", msg));
            Ajax.update("scrapper-form:messages");
            Ajax.update("scrapper-form:growl");
        }

        initControlList();

    }

    public void initControlList()  {

        fileControlList.clear();
        selectedScrappers.clear();

        for (AbstractScrapper abstractScrapper : ProcessHelper.getInstance().getScrappers().values()) {
            selectedScrappers.add(abstractScrapper);
            FileControl dailyFileControl = abstractScrapper.getDailyFileControl();

            if (dailyFileControl != null) {
                FileControlView fileControlView = new FileControlView(abstractScrapper.getLogo(), dailyFileControl.getHolding(), dailyFileControl.getChain(), dailyFileControl.getFrequency(), dailyFileControl.getFileName(), dailyFileControl.getStatus());
                fileControlList.add(fileControlView);
                for (String s : dailyFileControl.getErrors()) {
                    fileControlView.setErrorMsg(s);
                }
            } else {
                fileControlList.add(new FileControlView(abstractScrapper.getLogo(), abstractScrapper.getHolding(), abstractScrapper.getCadena(), "Dia", null, null));
            }

            FileControl monthlyFileControl = abstractScrapper.getMonthlyFileControl();

            if (abstractScrapper.isOnlyDiary()) {
                continue;
            }

            if (monthlyFileControl != null) {
                FileControlView fileControlView = new FileControlView(abstractScrapper.getLogo(), monthlyFileControl.getHolding(), monthlyFileControl.getChain(), monthlyFileControl.getFrequency(), monthlyFileControl.getFileName(), monthlyFileControl.getStatus());
                fileControlList.add(fileControlView);
                for (String s : monthlyFileControl.getErrors()) {
                    fileControlView.setErrorMsg(s);
                }
            } else {
                fileControlList.add(new FileControlView(abstractScrapper.getLogo(), abstractScrapper.getHolding(), abstractScrapper.getCadena(), "Mes", null, null));
            }

            if (getProcessHelper().getProcessDate().getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                FileControl weeklyFileControl = abstractScrapper.getWeeklyFileControl();

                if (weeklyFileControl != null) {
                    FileControlView fileControlView = new FileControlView(abstractScrapper.getLogo(), weeklyFileControl.getHolding(), weeklyFileControl.getChain(), weeklyFileControl.getFrequency(), weeklyFileControl.getFileName(), weeklyFileControl.getStatus());
                    fileControlList.add(fileControlView);
                    for (String s : weeklyFileControl.getErrors()) {
                        fileControlView.setErrorMsg(s);
                    }
                } else {
                    fileControlList.add(new FileControlView(abstractScrapper.getLogo(), abstractScrapper.getHolding(), abstractScrapper.getCadena(), "Dom", null, null));
                }
            }
        }

        Ajax.update("scrapper-form");

    }

    public void updateFileControlList() {

        for (AbstractScrapper abstractScrapper : ProcessHelper.getInstance().getScrappers().values()) {
            FileControl dailyFileControl = abstractScrapper.getDailyFileControl();

            if(dailyFileControl != null) {
                FileControlView fileControlView = new FileControlView(abstractScrapper.getLogo(), dailyFileControl.getHolding(), dailyFileControl.getChain(), dailyFileControl.getFrequency(), dailyFileControl.getFileName(), dailyFileControl.getStatus());
                fileControlList.set(fileControlList.indexOf(fileControlView), fileControlView);
                for (String s : dailyFileControl.getErrors()) {
                    fileControlView.setErrorMsg(s);
                }
            }

            FileControl monthlyFileControl = abstractScrapper.getMonthlyFileControl();

            if(abstractScrapper.isOnlyDiary()) {
                continue;
            }

            if(monthlyFileControl != null) {
                FileControlView fileControlView = new FileControlView(abstractScrapper.getLogo(), monthlyFileControl.getHolding(), monthlyFileControl.getChain(), monthlyFileControl.getFrequency(), monthlyFileControl.getFileName(), monthlyFileControl.getStatus());
                fileControlList.set(fileControlList.indexOf(fileControlView), fileControlView);
                for (String s : monthlyFileControl.getErrors()) {
                    fileControlView.setErrorMsg(s);
                }
            }

            if(getProcessHelper().getProcessDate().getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                FileControl weeklyFileControl = abstractScrapper.getWeeklyFileControl();

                if(weeklyFileControl != null) {
                    FileControlView fileControlView = new FileControlView(abstractScrapper.getLogo(), weeklyFileControl.getHolding(), weeklyFileControl.getChain(), weeklyFileControl.getFrequency(), weeklyFileControl.getFileName(), weeklyFileControl.getStatus());
                    fileControlList.set(fileControlList.indexOf(fileControlView), fileControlView);
                    for (String s : weeklyFileControl.getErrors()) {
                        fileControlView.setErrorMsg(s);
                    }
                }
            }
        }

    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {

        try {
            if(!this.date.equals(date)) {

                this.date = date;

                LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                setProcessName(getProcessName(localDate));

                getProcessHelper().setProcessDate(localDate);

                initControlList();

                Ajax.update("scrapper-form:control-file");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

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

        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        setProcessName(getProcessName(localDate));

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
        logAmount = 0;

        try {
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            processName = getProcessName(localDate);

            if(!validateDate()) {
                String msg = "Los scraps para el proceso '" + getProcessName(localDate) + "' aún no están disponibles. Vuelva a intentarlo más tarde";
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", msg));
                reqCtx.execute("PF('poll').stop();");
                reqCtx.getCurrentInstance().execute("cancel();");
                return;
            }

            if(selectedScrappers.isEmpty()) {
                String msg = "Debe seleccionar al menos 1 cadena para procesar";
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", msg));
                reqCtx.execute("PF('poll').stop();");
                reqCtx.getCurrentInstance().execute("cancel();");
                return;
            }

            processing = true;
            //Ajax.update("scrapper-form");

            List<String> chains = new ArrayList<>();

            for (AbstractScrapper selectedScrapper : selectedScrappers) {
                chains.add(selectedScrapper.getCadena());
            }

            scrapperManager.scrap(localDate.toString(), chains);

            processing = false;

            Ajax.update("scrapper-form");
            reqCtx.execute("PF('poll').stop();");
            reqCtx.getCurrentInstance().execute("cancel();");

            init();

            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "El proceso '" + processName + "' se ha completado correctamente"));

        } catch (Exception e) {
            reqCtx.execute("PF('poll').stop();");
            reqCtx.getCurrentInstance().execute("cancel();");
            processing = false;
            Ajax.update("scrapper-form");

            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ocurrió el siguiente error durante el proceso '" + processName + "': " + e.getMessage()));
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
            updateFileControlList();
            //RequestContext.getCurrentInstance().scrollTo("scrapper-form:progressBarClient");
            Ajax.update("scrapper-form:control-file");
            Ajax.update("config-form:config-form-tabs:logs");
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


    public List<FileControlView> getFileControlList() {
        return fileControlList;
    }

    public String getStatus(FileControlView fileControl) {

        if(fileControl == null) {
            return "";
        }

        if(fileControl.getStatus() == null) {
            if(processing) {
                return "fa fa-cog fa-spin fa-lg";
            }
            else {
                return "fa fa-cog fa-lg";
            }
        }

        if(fileControl.getStatus().equalsIgnoreCase("OK")) {
            return "fa fa-check-circle fa-lg";
        }

        if(fileControl.getStatus().equalsIgnoreCase("ERROR")) {
            return "ui-icon ui-icon-cancel";
        }

        return "";
    }

    public String getColor(FileControlView fileControl) {

        if(fileControl == null) {
            return "";
        }

        if(fileControl.getStatus() == null) {
            return "#607D8B";
        }

        if(fileControl.getStatus().equalsIgnoreCase("OK")) {
            return "#4CAF50";
        }

        if(fileControl.getStatus().equalsIgnoreCase("ERROR")) {
            return "#F44336";
        }

        return "";
    }

}
