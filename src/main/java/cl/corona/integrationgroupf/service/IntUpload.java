package cl.corona.integrationgroupf.service;

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
public class IntUpload {
    @Value("${sftpd.ip}")
    private String d_sftpip;

    @Value("${sftpd.prt}")
    private int d_sftpprt;

    @Value("${sftpd.usr}")
    private String d_sftpusr;

    @Value("${sftpd.pss}")
    private String d_sftppss;

    @Value("${sftpd.org}")
    private String d_sftporg;

    @Value("${sftpd.dst_RSL}")
    private String d_sftpdtn_RSL;

    @Value("${sftpd.dst_PHY}")
    private String d_sftpdtn_PHY;

    @Value("${sftpd.dst_ORG}")
    private String d_sftpdtn_ORG;

    @Value("${sftpd.dst_RBM}")
    private String d_sftpdtn_RBM;

    @Value("${sftpd.dst_TRD}")
    private String d_sftpdtn_TRD;

    @Value("${sftpd.dst_VPC}")
    private String d_sftpdtn_VPC;

    @Value("${name.file}")
    private String d_namefile;

    @Value("${separador.carpetas}")
    private String separador;

    @Value("${largo.archivo}")
    private int largo_archivo;

    private static final Logger LOG = LoggerFactory.getLogger(IntUpload.class);
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

                switch (sSubCadena) {
                    //FLUJO RSL
                    case "SDIFINGLH":
                        if (file.isFile()) {
                            String filename = file.getAbsolutePath();
                            //System.out.println(filename + " transfered to --> " + sftpdtn);
                            LOG.info("Uploading RSL " + filename + " ---> " + d_sftpdtn_RSL);
                            d_sftp.put(filename, d_sftpdtn_RSL);
                            file.delete();
                            LOG.info("{} : Upload Ok", dateTimeFormatter.format(LocalDateTime.now()));
                        }
                        break;
                    //FLUJO PHY
                    case "SDIPHYPIH":
                        if (file.isFile()) {
                            String filename = file.getAbsolutePath();
                            //System.out.println(filename + " transfered to --> " + sftpdtn);
                            LOG.info("Uploading PHY " + filename + " ---> " + d_sftpdtn_PHY);
                            d_sftp.put(filename, d_sftpdtn_PHY);
                            file.delete();
                            LOG.info("{} : Upload Ok", dateTimeFormatter.format(LocalDateTime.now()));
                        }
                        break;
                    //FLUJO ORG
                    case "SDIORGMST":
                        if (file.isFile()) {
                            String filename = file.getAbsolutePath();
                            //System.out.println(filename + " transfered to --> " + sftpdtn);
                            LOG.info("Uploading ORG " + filename + " ---> " + d_sftpdtn_ORG);
                            d_sftp.put(filename, d_sftpdtn_ORG);
                            file.delete();
                            LOG.info("{} : Upload Ok", dateTimeFormatter.format(LocalDateTime.now()));
                        }
                        break;
                    //FLUJO RBM
                    case "SDIFINAPH":
                        if (file.isFile()) {
                            String filename = file.getAbsolutePath();
                            //System.out.println(filename + " transfered to --> " + sftpdtn);
                            LOG.info("Uploading RBM " + filename + " ---> " + d_sftpdtn_RBM);
                            d_sftp.put(filename, d_sftpdtn_RBM);
                            file.delete();
                            LOG.info("{} : Upload Ok", dateTimeFormatter.format(LocalDateTime.now()));
                        }
                        break;
                    //FLUJO TRD
                    case "MTCSDIINVMRD":
                        if (file.isFile()) {
                            String filename = file.getAbsolutePath();
                            //System.out.println(filename + " transfered to --> " + sftpdtn);
                            LOG.info("Uploading TRD " + filename + " ---> " + d_sftpdtn_TRD);
                            d_sftp.put(filename, d_sftpdtn_TRD);
                            file.delete();
                            LOG.info("{} : Upload Ok", dateTimeFormatter.format(LocalDateTime.now()));
                        }
                        break;
                    //FLUJO VPC
                    case "SDIVPCMST":
                        if (file.isFile()) {
                            String filename = file.getAbsolutePath();
                            //System.out.println(filename + " transfered to --> " + sftpdtn);
                            LOG.info("Uploading VPC " + filename + " ---> " + d_sftpdtn_VPC);
                            d_sftp.put(filename, d_sftpdtn_VPC);
                            file.delete();
                            LOG.info("{} : Upload Ok", dateTimeFormatter.format(LocalDateTime.now()));
                        }
                        break;

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
