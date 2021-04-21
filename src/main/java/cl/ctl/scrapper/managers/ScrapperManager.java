package cl.ctl.scrapper.managers;

import cl.ctl.scrapper.helpers.*;
import cl.ctl.scrapper.model.FileControl;
import cl.ctl.scrapper.scrappers.AbstractScrapper;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import net.lingala.zip4j.exception.ZipException;

import javax.ejb.*;
import javax.enterprise.context.RequestScoped;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by root on 08-03-21.
 */
@Singleton
@Startup
public class ScrapperManager {

    private static final Logger logger = Logger.getLogger(ScrapperManager.class.getName());

    static LogHelper fh = LogHelper.getInstance();


    public void scrap(String process, List<String> chains) throws Exception {

        try {
            logger.addHandler(fh);
        } catch (SecurityException e) {
            e.printStackTrace();
            throw e;
        }

        initProcess();

        ProcessHelper.getInstance().process(process, chains);
    }

    public void scrap(String process) throws Exception {

        try {
            logger.addHandler(fh);
        } catch (SecurityException e) {
            e.printStackTrace();
            throw e;
        }

        ProcessHelper.getInstance().getScraps(process);
    }

    private void initProcess() {
        LogHelper.getInstance().reset();
    }


    @Schedule(second = "0", minute = "0", hour = "10", persistent = false)
    @Timeout
    public void processNutrisaMorning() throws Exception {
        try {
            logger.log(Level.INFO, "processNutrisaMorning -> Ejecutando Scrap");
            ProcessHelper.getInstance().process("Nutrisa");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Schedule(second = "0", minute = "35", hour = "16", persistent = false)
    public void processNutrisaAfternoon() throws Exception {
        try {
            logger.log(Level.INFO, "processNutrisaAfternoon -> Ejecutando Scrap");
            ProcessHelper.getInstance().process("Nutrisa");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Schedule(second = "0", minute = "24", hour = "12", persistent = false)
    public void processLegrandMorning() throws Exception {
        try {
            logger.log(Level.INFO, "processLegrandMorning -> Ejecutando Scrap");
            ProcessHelper.getInstance().process("Legrand");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Schedule(second = "0", minute = "40", hour = "16", persistent = false)
    public void processLegrandAfternoon() throws Exception {
        try {
            logger.log(Level.INFO, "processLegrandAfternoon -> Ejecutando Scrap");
            ProcessHelper.getInstance().process("Legrand");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }


}
