
package com.inossem.wms.wsdl.sap.output;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.inossem.wms.wsdl.sap.output package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _MTCWWMSDOPOSTREQ_QNAME = new QName("urn:sinopec:ecc:iwms:cw", "MT_CW_WMS_DO_POST_REQ");
    private final static QName _MTCWWMSDOPOSTRESP_QNAME = new QName("urn:sinopec:ecc:iwms:cw", "MT_CW_WMS_DO_POST_RESP");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.inossem.wms.wsdl.sap.output
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DTCWWMSDOPOSTREQ }
     * 
     */
    public DTCWWMSDOPOSTREQ createDTCWWMSDOPOSTREQ() {
        return new DTCWWMSDOPOSTREQ();
    }

    /**
     * Create an instance of {@link DTCWWMSDOPOSTREQ.ITPARTNER }
     * 
     */
    public DTCWWMSDOPOSTREQ.ITPARTNER createDTCWWMSDOPOSTREQITPARTNER() {
        return new DTCWWMSDOPOSTREQ.ITPARTNER();
    }

    /**
     * Create an instance of {@link DTCWWMSDOPOSTREQ.ISSPLITBATCH }
     * 
     */
    public DTCWWMSDOPOSTREQ.ISSPLITBATCH createDTCWWMSDOPOSTREQISSPLITBATCH() {
        return new DTCWWMSDOPOSTREQ.ISSPLITBATCH();
    }

    /**
     * Create an instance of {@link DTCWWMSDOPOSTRESP }
     * 
     */
    public DTCWWMSDOPOSTRESP createDTCWWMSDOPOSTRESP() {
        return new DTCWWMSDOPOSTRESP();
    }

    /**
     * Create an instance of {@link MSGHEAD }
     * 
     */
    public MSGHEAD createMSGHEAD() {
        return new MSGHEAD();
    }

    /**
     * Create an instance of {@link DTCWWMSDOPOSTREQ.ISMSGHEAD }
     * 
     */
    public DTCWWMSDOPOSTREQ.ISMSGHEAD createDTCWWMSDOPOSTREQISMSGHEAD() {
        return new DTCWWMSDOPOSTREQ.ISMSGHEAD();
    }

    /**
     * Create an instance of {@link DTCWWMSDOPOSTREQ.ITLIKP }
     * 
     */
    public DTCWWMSDOPOSTREQ.ITLIKP createDTCWWMSDOPOSTREQITLIKP() {
        return new DTCWWMSDOPOSTREQ.ITLIKP();
    }

    /**
     * Create an instance of {@link DTCWWMSDOPOSTREQ.ITLIPS }
     * 
     */
    public DTCWWMSDOPOSTREQ.ITLIPS createDTCWWMSDOPOSTREQITLIPS() {
        return new DTCWWMSDOPOSTREQ.ITLIPS();
    }

    /**
     * Create an instance of {@link DTCWWMSDOPOSTREQ.ITPARTNER.ZLTSDPARTNERCONTENT }
     * 
     */
    public DTCWWMSDOPOSTREQ.ITPARTNER.ZLTSDPARTNERCONTENT createDTCWWMSDOPOSTREQITPARTNERZLTSDPARTNERCONTENT() {
        return new DTCWWMSDOPOSTREQ.ITPARTNER.ZLTSDPARTNERCONTENT();
    }

    /**
     * Create an instance of {@link DTCWWMSDOPOSTREQ.ISSPLITBATCH.ZLTSDBATCHCONTENT }
     * 
     */
    public DTCWWMSDOPOSTREQ.ISSPLITBATCH.ZLTSDBATCHCONTENT createDTCWWMSDOPOSTREQISSPLITBATCHZLTSDBATCHCONTENT() {
        return new DTCWWMSDOPOSTREQ.ISSPLITBATCH.ZLTSDBATCHCONTENT();
    }

    /**
     * Create an instance of {@link DTCWWMSDOPOSTRESP.ETBAPIRET2 }
     * 
     */
    public DTCWWMSDOPOSTRESP.ETBAPIRET2 createDTCWWMSDOPOSTRESPETBAPIRET2() {
        return new DTCWWMSDOPOSTRESP.ETBAPIRET2();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DTCWWMSDOPOSTREQ }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sinopec:ecc:iwms:cw", name = "MT_CW_WMS_DO_POST_REQ")
    public JAXBElement<DTCWWMSDOPOSTREQ> createMTCWWMSDOPOSTREQ(DTCWWMSDOPOSTREQ value) {
        return new JAXBElement<DTCWWMSDOPOSTREQ>(_MTCWWMSDOPOSTREQ_QNAME, DTCWWMSDOPOSTREQ.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DTCWWMSDOPOSTRESP }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sinopec:ecc:iwms:cw", name = "MT_CW_WMS_DO_POST_RESP")
    public JAXBElement<DTCWWMSDOPOSTRESP> createMTCWWMSDOPOSTRESP(DTCWWMSDOPOSTRESP value) {
        return new JAXBElement<DTCWWMSDOPOSTRESP>(_MTCWWMSDOPOSTRESP_QNAME, DTCWWMSDOPOSTRESP.class, null, value);
    }

}
