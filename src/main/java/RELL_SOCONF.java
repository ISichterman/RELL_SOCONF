
import Create.*;
import com.logicall.FunctionLibrary.MappingFunctions;
import com.logicall.XMLobjects.Boltrics.SND_Shipment.*;
import nl.copernicus.niklas.transformer.*;
import nl.copernicus.niklas.transformer.context.ComponentContext;
import nl.copernicus.niklas.transformer.context.EngineContext;
import nl.copernicus.niklas.transformer.context.NiklasLogger;
import nl.copernicus.niklas.transformer.context.RoutingContext;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.stream.Stream;

public class RELL_SOCONF implements NiklasComponent<String, String>, NiklasLoggerAware, RoutingContextAware, ComponentContextAware, EngineContextAware, Lifecycle {

    protected NiklasLogger log;
    protected RoutingContext rc;
    protected ComponentContext cc;
    protected EngineContext ec;
    private MappingFunctions mf = new MappingFunctions();
    private Map<Carrier, String> Carriers;
    private String CustomerId = "12";



    @Override
    public String process(Header header, String payload) throws NiklasComponentException, NiklasInterruptionException {
        List<Create> messages = new ArrayList<Create>();
        Message input = mf.stringToObject_XML(payload, Message.class);

        if(input.getDocuments().getDocument().getDocumentLines() == null){
            throw new NiklasComponentException("No documentlines present!");
        }

        double TransportCost = calcTransportCosts(input.getDocuments().getDocument().getDocumentLines().getDocumentLine());

        for(Documents.Document.DocumentLines.DocumentLine line : input.getDocuments().getDocument().getDocumentLines().getDocumentLine()){
            if(line.getType() == 1) {
                messages.add(generateArticleLineConf(line, input.getDocuments().getDocument()));
            }
        }

        for(Documents.Document.DocumentPackages.DocumentPackage pac : input.getDocuments().getDocument().getDocumentPackages().getDocumentPackage()){
            messages.add(generatePackagelineConf(pac, input.getDocuments().getDocument(), TransportCost));
            TransportCost = 0.0;
        }

        for(Create create : messages){
            try {
                rc.dispatchNext(mf.objectToString_XML(create, "utf-8", false));
            } catch (RoutingException e) {
                throw new NiklasComponentException("Routing message to next step failed!", e);
            }
        }

        throw new NiklasInterruptionException("message processed");
    }

    private Create generateArticleLineConf(com.logicall.XMLobjects.Boltrics.SND_Shipment.Documents.Document.DocumentLines.DocumentLine line, com.logicall.XMLobjects.Boltrics.SND_Shipment.Documents.Document doc ){
        String artNo = line.getAttributes().getAttribute().stream()
                .filter(a -> a.getCode().equals("CUSTITEM"))
                .map(a -> a.getValue())
                .findFirst()
                .orElse(line.getExternalNo());

        Create create = new Create();
        create.setEntity(new Entity());
        Attributes att = new Attributes();
        create.getEntity().setAttributes(att);

        att.getKeyValuePairOfstringanyType().add(generateKeyPairValue("h21_dc_qtyfulfillment", line.getQtyBaseCreated() != null ? line.getQtyBaseCreated().toString() : "0", "xs:decimal"));
        att.getKeyValuePairOfstringanyType().add(generateKeyPairValue("h21_dt_ship", doc.getPostingDate().toString() + "T00:00:00", "xs:dateTime"));
        att.getKeyValuePairOfstringanyType().add(generateKeyPairValue("h21_nv_itemcode", artNo, "xs:string"));
        att.getKeyValuePairOfstringanyType().add(generateKeyPairValue("h21_nv_itemlotbincode", line.getAttribute02().getValue(), "xs:string"));
        att.getKeyValuePairOfstringanyType().add(generateKeyPairValue("h21_nv_itemlotcode", "AITLOT", "xs:string"));
        att.getKeyValuePairOfstringanyType().add(generateKeyPairValue("h21_nv_notificationtype", "Shipment", "xs:string"));
        att.getKeyValuePairOfstringanyType().add(generateKeyPairValue("h21_nv_revenueordercode", getAttribute(line.getAttributes().getAttribute(), "ORDERNO"), "xs:string"));
        att.getKeyValuePairOfstringanyType().add(generateKeyPairValue("h21_nv_shipmentlinecode", getAttribute(line.getAttributes().getAttribute(), "ORDERLINNO"), "xs:string"));
        att.getKeyValuePairOfstringanyType().add(generateKeyPairValue("h21_nv_source", "AIT", "xs:string"));
        att.getKeyValuePairOfstringanyType().add(generateKeyPairValue("h21_nv_uofm_item", "EA", "xs:string"));


        create.getEntity().setLogicalName("h21_shipmentnotification");
        create.getEntity().setId("00000000-0000-0000-0000-000000000000");

        return create;
    }

    private Create generatePackagelineConf(Documents.Document.DocumentPackages.DocumentPackage pac, Documents.Document doc, double transportCosts){
        Create create = new Create();
        create.setEntity(new Entity());
        Attributes att = new Attributes();
        create.getEntity().setAttributes(att);

        att.getKeyValuePairOfstringanyType().add(generateKeyPairValue("h21_dc_grossweight", pac.getGrossWeight().toString(), "xs:decimal"));
        att.getKeyValuePairOfstringanyType().add(generateKeyPairValue("h21_dc_netweight", pac.getNetWeight().toString(), "xs:decimal"));
        att.getKeyValuePairOfstringanyType().add(generateKeyPairValue("h21_dc_height", pac.getHeight().multiply(BigDecimal.valueOf(100)).toString(), "xs:decimal"));
        att.getKeyValuePairOfstringanyType().add(generateKeyPairValue("h21_dc_length", pac.getLength().multiply(BigDecimal.valueOf(100)).toString(), "xs:decimal"));
        att.getKeyValuePairOfstringanyType().add(generateKeyPairValue("h21_dc_width", pac.getWidth().multiply(BigDecimal.valueOf(100)).toString(), "xs:decimal"));
        att.getKeyValuePairOfstringanyType().add(generateKeyPairValue("h21_dc_trnfreightextcost", Double.toString(transportCosts), "xs:decimal"));
        att.getKeyValuePairOfstringanyType().add(generateKeyPairValue("h21_nv_freighttrackercode", doc.getAirwayBillNo(), "xs:string"));
        att.getKeyValuePairOfstringanyType().add(generateKeyPairValue("h21_nv_notificationtype", "Shipment", "xs:string"));
        att.getKeyValuePairOfstringanyType().add(generateKeyPairValue("h21_nv_packagenomsn", pac.getMarksNumbers(), "xs:string"));
        att.getKeyValuePairOfstringanyType().add(generateKeyPairValue("h21_nv_packingslipcode",doc.getExternalDocumentNo() , "xs:string"));
        att.getKeyValuePairOfstringanyType().add(generateKeyPairValue("h21_nv_shipmethod_actual", Carriers.getOrDefault(new Carrier(doc.getShippingAgentCode(), doc.getShippingAgentServices()),doc.getShippingAgentCode()) , "xs:string"));
        att.getKeyValuePairOfstringanyType().add(generateKeyPairValue("h21_nv_source", "AIT" , "xs:string"));
        att.getKeyValuePairOfstringanyType().add(generateKeyPairValue("h21_nv_trnfreightcurrencycode", "EUR", "xs:string"));
        att.getKeyValuePairOfstringanyType().add(generateKeyPairValue("h21_nv_uofm_grossweight", "KG", "xs:string"));
        att.getKeyValuePairOfstringanyType().add(generateKeyPairValue("h21_nv_uofm_netweight", "KG", "xs:string"));
        att.getKeyValuePairOfstringanyType().add(generateKeyPairValue("h21_nv_uofm_height", "CM", "xs:string"));
        att.getKeyValuePairOfstringanyType().add(generateKeyPairValue("h21_nv_uofm_length", "CM", "xs:string"));
        att.getKeyValuePairOfstringanyType().add(generateKeyPairValue("h21_nv_uofm_width", "CM", "xs:string"));


        create.getEntity().setLogicalName("h21_packagenotification");
        create.getEntity().setId("00000000-0000-0000-0000-000000000000");

        return create;
    }

    private KeyValuePairOfstringanyType generateKeyPairValue(String key, String value, String type){
        KeyValuePairOfstringanyType valuepair =  new KeyValuePairOfstringanyType();

        valuepair.setKey(key);
        valuepair.setValue(new Value());
        valuepair.getValue().setType(type);
        valuepair.getValue().setValue(value);

        return valuepair;
    }

    private double calcTransportCosts(List<Documents.Document.DocumentLines.DocumentLine> lines){
        double costs = 0;

        costs = lines.stream()
                .filter(l -> l.getType() == 2 && l.getLineAmountLCY() != null)
                .filter(l -> Stream.of("200", "201", "202", "203", "205")
                .anyMatch(s -> s.equals(l.getNo())))
                .mapToDouble(l -> TryParseDouble(l.getLineAmountLCY())).sum();

        return BigDecimal.valueOf(costs).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    private double TryParseDouble(String value){
        try{
            return Double.parseDouble(value);
        }catch(NumberFormatException e){
            return 0;
        }
    }

    private String getAttribute(List<Documents.Document.DocumentLines.DocumentLine.Attributes.Attribute> Attributes, String Code){
        return Attributes.stream()
                .filter(a -> a.getCode().equals(Code))
                .map(a -> a.getValue())
                .findFirst()
                .orElse("");
    }

    @Override
    public void setLogger(NiklasLogger nl) {
        this.log = nl;
    }

    @Override
    public void setRoutingContext(RoutingContext routingContext) {
        this.rc = routingContext;
    }

    @Override
    public void setComponentContext(ComponentContext cc) {
        this.cc = cc;
    }

    @Override
    public void initialise() throws NiklasComponentException {
        Carriers = new HashMap<Carrier, String>();
        String cs = cc.getProperty("connection_string");
        if(cs == null){
            throw new NiklasComponentException("Manditory property \"connection_string\" not set up!");
        }
        DataSource ds = ec.getDatasource(cs);
        String query = "SELECT InternalValue1, InternalValue2, ExternalValue1 from TnT_ValueTranslation where CustomerID = '"+ CustomerId + "' and purpose = 'Forwarder'";

        try( Connection cn = ds.getConnection();
             Statement st = cn.createStatement();
             ResultSet result = st.executeQuery(query)){
            while (result.next()) {
                String Carrier = result.getString("InternalValue1");
                String Service = result.getString("InternalValue2");
                String RellValue = result.getString("ExternalValue1");

                Carriers.put(new Carrier(Carrier, Service), RellValue);
            }
        }catch (SQLException e){
            throw new NiklasComponentException("Error getting Carrier mapping", e);
        }

    }

    @Override
    public void destroy() {

    }

    @Override
    public void setEngineContext(EngineContext engineContext) {
        this.ec = engineContext;
    }
}

