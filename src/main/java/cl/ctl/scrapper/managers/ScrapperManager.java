package cl.ctl.scrapper.managers;

import cl.ctl.scrapper.helpers.*;
import cl.ctl.scrapper.scrappers.AbstractScrapper;

import javax.ejb.Singleton;
import javax.enterprise.context.RequestScoped;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by root on 08-03-21.
 */
@Singleton
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

        validateProcess(process);

        validateChains(chains);

        for (AbstractScrapper scrapper : ProcessHelper.getInstance().getScrappers().values()) {
            scrapper.process();
            //ProcessHelper.getInstance().getExecutor().execute(scrapper);
        }

        //ProcessHelper.getInstance().getBarrier().await();

        logger.log(Level.INFO, "Descomprimiendo y renombrando archivos");

        FilesHelper.getInstance().processFiles();

        logger.log(Level.INFO, "Subiendo archivos a servidor DivePort");

        UploadHelper.getInstance().uploadFiles();

        logger.log(Level.INFO, "Moviendo archivos en servidor DivePort");

        UploadHelper.getInstance().moveFiles();

        logger.log(Level.INFO, "Proceso finalizado con éxito. Enviando correo");

        MailHelper.getInstance().sendMail();

        LogHelper.getInstance().getFileControlList().clear();

    }

    private void validateProcess(String process) {

        if(process == null) {
            return;
        }

        DateTimeFormatter dtf;
        LocalDate localDate;

        try {
            dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            localDate = LocalDate.parse(process, dtf);
        }
        catch (Exception e) {
            try {
                dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                localDate = LocalDate.parse(process, dtf);
            }
            catch (Exception e1) {
                try {
                    dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
                    localDate = LocalDate.parse(process, dtf);
                }
                catch (Exception e2) {
                    throw e2;
                }
            }
        }

        ProcessHelper.getInstance().setProcessDate(localDate);

    }

    private void validateChains(List<String> chains) throws Exception {
        if(chains == null) {
            return;
        }

        if(chains.isEmpty()) {
            return;
        }

        for (String chain : chains) {
            if (chain.equals("")) {
                throw new Exception("Nombre de cadena '" + chain + "' no válido. Cadenas válidas: " + ProcessHelper.getInstance().getScrappers().keySet().toString());
            }
        }
        for (String chain : chains) {
            if(!ProcessHelper.getInstance().getScrappers().keySet().contains(chain)) {
                throw new Exception("Nombre de cadena '" + chain + "' no válido. Cadenas válidas: " + ProcessHelper.getInstance().getScrappers().keySet().toString());
            }
        }

        ProcessHelper.getInstance().setScrappers(chains);
    }

}
