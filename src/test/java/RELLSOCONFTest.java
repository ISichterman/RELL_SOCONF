import nl.copernicus.niklas.test.FunctionalTestCase;
import nl.copernicus.niklas.test.MockupComponentContext;
import nl.copernicus.niklas.test.MockupEngineContext;
import nl.copernicus.niklas.test.MockupHeader;
import nl.copernicus.niklas.transformer.Header;
import nl.copernicus.niklas.transformer.NiklasInterruptionException;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.util.Properties;


public class RELLSOCONFTest extends FunctionalTestCase {

    public void testProcess(String fileName) throws Exception {
        File TEST_FILE = new File(fileName);

        this.setComponentContext(new MockupComponentContext());
        this.getComponentContext().getProperties().put("connection_string", "niklas_solutions");

        Class.forName("com.mysql.cj.jdbc.Driver");
        System.setProperty("javax.net.ssl.trustStore", "I:/Afdeling/Automatisering/11. EDI/Niklas mappingen/Mariadb_Janssen_SSL/janssendev");
        System.setProperty("javax.net.ssl.trustStorePassword", "janssendev");
        this.setEngineContext(new MockupEngineContext(null){

            @Override
            public DataSource getDatasource(String name) {
                DriverManagerDataSource ds = new DriverManagerDataSource();
                ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
                ds.setUrl("jdbc:mysql://jdsftp03.coperniapps.nl:3306/"+name);
                ds.setUsername("janssen_dev");
                ds.setPassword("HqoQ#=Tu0SiCYRdqdh3R8#XI");
                Properties prop = new Properties();
                prop.setProperty("useSSL", "true");
                prop.setProperty("requireSSL", "true");
                prop.setProperty("serverTimezone", "UTC");
                ds.setConnectionProperties(prop);

                return ds;
            }
        });

        // initialise the transformer
        RELL_SOCONF transformerInstance = getTransformerInstance(RELL_SOCONF.class);

        Header hdr = new MockupHeader();
        String result = transformerInstance.process(hdr, FileUtils.readFileToString(TEST_FILE));
        System.out.println(result);
        super.destroy(transformerInstance);
    }


    @Test(expected= NiklasInterruptionException.class)
    public void testExample() throws Exception {
        testProcess("src/test/resources/SND-SHIPMENT_U2400323762_TR12031_158697958.xml");
    }

    @Test(expected= NiklasInterruptionException.class)
    public void testExample2() throws Exception {
        testProcess("src/test/resources/snd_shipment2.xml");
    }
}
