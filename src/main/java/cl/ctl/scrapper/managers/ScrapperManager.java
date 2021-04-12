package cl.ctl.scrapper.managers;

import cl.ctl.scrapper.helpers.*;
import cl.ctl.scrapper.model.FileControl;
import cl.ctl.scrapper.scrappers.AbstractScrapper;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import net.lingala.zip4j.exception.ZipException;

import javax.ejb.Asynchronous;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
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
public class ScrapperManager {

    private static final Logger logger = Logger.getLogger(ScrapperManager.class.getName());

    static LogHelper fh = LogHelper.getInstance();

    Semaphore semaphore = new Semaphore(1);

    public void scrap(String process, List<String> chains) throws Exception {

        semaphore.tryAcquire();

        try {
            logger.addHandler(fh);
        } catch (SecurityException e) {
            e.printStackTrace();
            throw e;
        }

        initProcess();

        validateProcess(process);

        validateChains(chains);

        scrap(true);

        upload();

        semaphore.release();
    }

    public void scrap(String process) throws Exception {

        semaphore.tryAcquire();

        try {
            logger.addHandler(fh);
        } catch (SecurityException e) {
            e.printStackTrace();
            throw e;
        }

        initProcess();

        validateProcess(process);

        scrap(false);

        semaphore.release();
    }

    private void initProcess() {
        LogHelper.getInstance().reset();
    }

    private void validateProcess(String process) throws IOException {

        if(process == null) {
            return;
        }

        DateTimeFormatter dtf;
        LocalDate localDate;

        try {
            dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            localDate = LocalDate.parse(process, dtf);
        }
        catch (Exception e) {
            try {
                dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                localDate = LocalDate.parse(process, dtf);
            }
            catch (Exception e1) {
                try {
                    dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    localDate = LocalDate.parse(process, dtf);
                }
                catch (Exception e2) {
                    try {
                        dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
                        localDate = LocalDate.parse(process, dtf);
                    }
                    catch (Exception e3) {
                        throw e3;
                    }
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

        List<String> validChains = new ArrayList<>();

        for (AbstractScrapper abstractScrapper : ProcessHelper.getInstance().getScrappers().values()) {
            validChains.add(abstractScrapper.getCadena());
        }


        for (String chain : chains) {
            if(!validChains.contains(chain) && !validChains.contains(chain.toUpperCase())) {
                throw new Exception("Nombre de cadena '" + chain + "' no válido. Cadenas válidas: " + ProcessHelper.getInstance().getScrappers().keySet().toString());
            }
        }

        ProcessHelper.getInstance().setScrappers(chains);
    }

    @Schedule(second = "0", minute = "0", hour = "14", persistent = false)
    public void processMorning() throws Exception {

        semaphore.tryAcquire();

        // Setear los scrappers correspondientes a las cadenas de Nutrisa
        ProcessHelper.getInstance().setScrappers(Arrays.asList("Construmart", "Easy", "Sodimac", "Cencosud", "Smu", "Tottus", "WalMart"));

        // Repasar toda la última semana por si hay scraps pendientes
        LocalDate today = LocalDate.now();

        LocalDate date = today.minusDays(7);

        while(date.isBefore(today)) {

            ProcessHelper.getInstance().setProcessDate(date);

            logger.log(Level.INFO, "Proceso Mañana -> " + ProcessHelper.getInstance().getProcessDate());

            scrap(true);

            date = date.plusDays(1);

        }

        upload();

        semaphore.release();
    }

    @Schedule(second = "0", minute = "0", hour = "20", persistent = false)
    public void processAfternoon() throws Exception {

        semaphore.tryAcquire();

        // Setear los scrappers correspondientes a las cadenas de Nutrisa
        ProcessHelper.getInstance().setScrappers(Arrays.asList("Construmart", "Easy", "Sodimac", "Cencosud", "Smu", "Tottus", "WalMart"));

        // Repasar toda la última semana por si hay scraps pendientes
        LocalDate today = LocalDate.now();

        LocalDate date = today.minusDays(7);

        while(date.isBefore(today)) {

            ProcessHelper.getInstance().setProcessDate(date);

            logger.log(Level.INFO, "Proceso Tarde -> " + ProcessHelper.getInstance().getProcessDate());

            scrap(true);

            date = date.plusDays(1);

        }

        upload();

        UploadHelper.getInstance().sendSignal();

        semaphore.release();
    }


    private void scrap(boolean flag) throws Exception {

        int max = 3;

        for (int i = 0; i < max; i++) {

            int cont = i + 1;

            logger.log(Level.INFO, "Descargando scraps -> Intento " + cont + " de " + max);

            for (AbstractScrapper scrapper : ProcessHelper.getInstance().getScrappers().values()) {
                if(scrapper != null) {
                    scrapper.process(flag);
                }
                //ProcessHelper.getInstance().getExecutor().execute(scrapper);
            }

            //ProcessHelper.getInstance().getBarrier().await();

            int errors = 0;

            for (AbstractScrapper scrapper : ProcessHelper.getInstance().getScrappers().values()) {
                for (FileControl fileControl : scrapper.getFileControlList()) {
                    if(!fileControl.getErrors().isEmpty()) {
                        errors++;
                    }
                }
            }

            if(errors == 0) {
                break;
            }
        }

    }

    private void upload() throws Exception {

        logger.log(Level.INFO, "Descomprimiendo y renombrando archivos");

        FilesHelper.getInstance().processFiles();

        logger.log(Level.INFO, "Subiendo archivos a servidor DivePort");

        UploadHelper.getInstance().uploadFiles();

        logger.log(Level.INFO, "Moviendo archivos en servidor DivePort");

        UploadHelper.getInstance().moveFiles();

        logger.log(Level.INFO, "Proceso finalizado con éxito. Enviando correo");

        MailHelper.getInstance().sendMail();
    }

}
