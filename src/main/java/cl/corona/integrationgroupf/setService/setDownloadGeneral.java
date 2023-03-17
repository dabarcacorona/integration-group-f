package cl.corona.integrationgroupf.setService;

import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.Vector;

@Service
public class setDownloadGeneral {

    @Value("${sftpoinv.ip}")
    private String sftpip;

    @Value("${sftpoinv.prt}")
    private int sftpprt;

    @Value("${sftpoinv.usr}")
    private String sftpusr;

    @Value("${sftpoinv.pss}")
    private String sftppss;

    @Value("${sftpoinv.org_IPHY_g}")
    private String sftporg;

    @Value("${sftpoinv.org_PHYPG_g}")
    private String sftporg_PHY_PG;

    @Value("${sftpoinv.org_PHYPG_gr}")
    private String sftporg_PHY_PG_r;

    @Value("${sftpoinv.dstg}")
    private String sftpdst;

    @Value("${sftpoinv.dstg_p}")
    private String sftpdst_p;

    @Value("${name.file}")
    private String namefile;

    @Value("${separador.carpetas}")
    private String separador;

    @Value("${largo.archivo}")
    private int largo_archivo;

    private static final Logger LOG = LoggerFactory.getLogger(setDownloadGeneral.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    String strDir = System.getProperty("user.dir");

    public void DownloadFile() throws IOException {

        JSch jsch = new JSch();
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        config.put("PreferredAuthentications", "password");
        jsch.setConfig(config);

        try {

            Session session = jsch.getSession(sftpusr, sftpip, sftpprt);
            session.setConfig("PreferredAuthentications", "password");
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(sftppss);
            session.connect();
            Channel channel = session.openChannel("sftp");
            ChannelSftp sftp = (ChannelSftp) channel;
            sftp.connect();

            final String pathdest = strDir + separador + sftpdst + separador;
            //final String path = sftporg;
            //LOG.info(path);

            Vector<ChannelSftp.LsEntry> entries = sftp.ls(sftporg);

            //download all files (except the ., .. and folders) from given folder
            for (ChannelSftp.LsEntry en : entries) {
                if (en.getFilename().equals(".") || en.getFilename().equals("..") || en.getAttrs().isDir()) {
                    continue;
                }

                String filename = StringUtils.getFilename(en.getFilename());
                //String sSubCadena = filename.substring(0, largo_archivo).toUpperCase();
                int end = filename.indexOf("_");
                String sSubCadena = filename.substring(0, end).toUpperCase();

                //LOG.info(sSubCadena);

                if (sSubCadena.equals("PHY")) {
                    LOG.info("Downloading Congelado General " + (sftporg + en.getFilename()) + " ---> " + pathdest + en.getFilename());
                    sftp.get(sftporg + en.getFilename(), pathdest + en.getFilename());
                    sftp.rm(sftporg + en.getFilename());
                    LOG.info("{} : Download Ok", dateTimeFormatter.format(LocalDateTime.now()));
                }

            }

            //----PREGUIAS

            final String pathdest2 = strDir + separador + sftpdst_p + separador;

            Vector<ChannelSftp.LsEntry> entries2 = sftp.ls(sftporg_PHY_PG);

            //download all files (except the ., .. and folders) from given folder
            for (ChannelSftp.LsEntry en : entries2) {
                if (en.getFilename().equals(".") || en.getFilename().equals("..") || en.getAttrs().isDir()) {
                    continue;
                }

                String filename = StringUtils.getFilename(en.getFilename());
                //String sSubCadena = filename.substring(0, largo_archivo).toUpperCase();
                int end = filename.indexOf("_");
                String sSubCadena = filename.substring(0, 6).toUpperCase();

                //LOG.info(sSubCadena);

                if (sSubCadena.equals("PHY_PG")) {
                    LOG.info("Downloading Pre Guias General " + (sftporg_PHY_PG + en.getFilename()) + " ---> " + pathdest2 + en.getFilename());
                    sftp.get(sftporg_PHY_PG + en.getFilename(), pathdest2 + en.getFilename());
                    sftp.put(pathdest2 + en.getFilename(), sftporg_PHY_PG_r + en.getFilename());
                    sftp.rm(sftporg_PHY_PG + en.getFilename());
                    LOG.info("{} : Download Ok", dateTimeFormatter.format(LocalDateTime.now()));
                }

            }

            sftp.exit();
            channel.disconnect();
            session.disconnect();

        } catch (JSchException e) {
            LOG.error("No se pudo realizar la conexión ,{}",  e);
        } catch (SftpException e) {
            e.printStackTrace();
        }

    }
}
