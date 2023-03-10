package cl.corona.integrationgroupf.scheduler;

import cl.corona.integrationgroupf.service.IntDownload;
import cl.corona.integrationgroupf.service.IntUpload;
import cl.corona.integrationgroupf.setService.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ScheduledTasks {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Autowired
    private IntDownload intownload;

    @Autowired
    private IntUpload intupload;

    @Autowired
    private setDownloadGeneral setdownloadgeneral;

    @Autowired
    private setUploadGeneral setuploadgeneral;

    @Autowired
    private setDownloadSelectivo setdownloadselectivo;

    @Autowired
    private setUploadSelectivo setuploadselectivo;

    @Autowired
    private setDownloadIVPC setdownloadivpc;

    @Autowired
    private setUpload setupload;

    @Scheduled(cron = "${cron.expression}")
    public void scheduledJob() throws InterruptedException, IOException {

        LOG.info("{} : Inicio transferencia de archivos",
                dateTimeFormatter.format(LocalDateTime.now()));

        intownload.DownloadFile();
        intupload.UploadFile();

        setdownloadgeneral.DownloadFile();
        setuploadgeneral.UploadFile();

        setdownloadselectivo.DownloadFile();
        setuploadselectivo.UploadFile();

        setdownloadivpc.DownloadFile();
        setupload.UploadFile();

        LOG.info("{} : Fin transferencia de archivos",
                dateTimeFormatter.format(LocalDateTime.now()));

    }
}
