package cl.corona.integrationgroupf.setService;

import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

@Service
public class setUploadGeneral {

    @Value("${sftpdinvg.ip}")
    private String d_sftpip;

    @Value("${sftpdinvg.prt}")
    private int d_sftpprt;

    @Value("${sftpdinvg.usr}")
    private String d_sftpusr;

    @Value("${sftpdinvg.pss}")
    private String d_sftppss;

    @Value("${sftpdinvg.org}")
    private String d_sftporg;

    @Value("${sftpdinvg.org_p}")
    private String d_sftporg_p;

    @Value("${sftpdinvg.org_m}")
    private String d_sftporg_m;

    @Value("${sftpdinvg.dst}")
    private String d_sftpdst;

    @Value("${sftpdinvg.dst_p}")
    private String d_sftpdst_p;

    @Value("${sftpdinvg.dst_m}")
    private String d_sftpdst_m;

    @Value("${name.file}")
    private String d_namefile;

    @Value("${separador.carpetas}")
    private String separador;

    @Value("${largo.archivo}")
    private int largo_archivo;

    private static final Logger LOG = LoggerFactory.getLogger(setUpload.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    String strDir = System.getProperty("user.dir");

    public void UploadFile() throws IOException {

        JSch jsch = new JSch();
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        config.put("PreferredAuthentications", "password");
        jsch.setConfig(config);

        try {

            Session session = jsch.getSession(d_sftpusr, d_sftpip, d_sftpprt);
            session.setConfig("PreferredAuthentications", "password");
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(d_sftppss);
            session.connect();
            Channel channel = session.openChannel("sftp");
            ChannelSftp d_sftp = (ChannelSftp) channel;
            d_sftp.connect();

            final String path = strDir + separador + d_sftporg;
            //final String path = sftporg;

            File directory = new File(path);
            File[] fList = directory.listFiles();

            for (File file : fList) {

                String name = StringUtils.getFilename(file.getName());
                int end = name.indexOf("_");
                String sSubCadena = name.substring(0, end).toUpperCase();

                //if (sSubCadena.equals(d_namefile)) {

                if (file.isFile()) {
                    String filename = file.getAbsolutePath();
                    LOG.info("Uploading Files Congelado General " + filename + " ---> " + d_sftpdst);
                    d_sftp.put(filename, d_sftpdst);
                    file.delete();
                    LOG.info("{} : Upload Ok", dateTimeFormatter.format(LocalDateTime.now()));

                }
            }

            //PREGUIAS

            final String path2 = strDir + separador + d_sftporg_p;
            //final String path = sftporg;

            File directory2 = new File(path2);
            File[] fList2 = directory2.listFiles();

            for (File file2 : fList2) {

                String name = StringUtils.getFilename(file2.getName());
                int end = name.indexOf("_");
                String sSubCadena = name.substring(0, end).toUpperCase();

                //if (sSubCadena.equals(d_namefile)) {

                if (file2.isFile()) {
                    String filename = file2.getAbsolutePath();
                    LOG.info("Uploading Files Preguia General " + filename + " ---> " + d_sftpdst_p);
                    d_sftp.put(filename, d_sftpdst_p);
                    file2.delete();
                    LOG.info("{} : Upload Ok", dateTimeFormatter.format(LocalDateTime.now()));

                }
            }

            //MAESTROS

            final String path3 = strDir + separador + d_sftporg_m;
            //final String path = sftporg;

            File directory3 = new File(path3);
            File[] fList3 = directory3.listFiles();

            for (File file3 : fList3) {

                String name = StringUtils.getFilename(file3.getName());
                int end = name.indexOf("_");
                String sSubCadena = name.substring(0, end).toUpperCase();

                //if (sSubCadena.equals(d_namefile)) {

                if (file3.isFile()) {
                    String filename = file3.getAbsolutePath();
                    LOG.info("Uploading Files Maestro General " + filename + " ---> " + d_sftpdst_m);
                    d_sftp.put(filename, d_sftpdst_m);
                    file3.delete();
                    LOG.info("{} : Upload Ok", dateTimeFormatter.format(LocalDateTime.now()));

                }
            }

            d_sftp.exit();
            channel.disconnect();
            session.disconnect();

        } catch (JSchException e) {
            LOG.error("No se pudo realizar la conexión ,{}", e);
        } catch (SftpException e) {
            e.printStackTrace();
        }

    }
}
