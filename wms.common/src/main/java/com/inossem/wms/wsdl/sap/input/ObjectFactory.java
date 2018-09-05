
package com.inossem.wms.wsdl.sap.input;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.inossem.wms.wsdl.sap.input package. 
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

    private final static QName _MTCWWMSMATDOCPOSTRESP_QNAME = new QName("urn:sinopec:ecc:iwms:cw", "MT_CW_WMS_MATDOC_POST_RESP");
    private final static QName _MTCWWMSMATDOCPOSTREQ_QNAME = new QName("urn:sinopec:ecc:iwms:cw", "MT_CW_WMS_MATDOC_POST_REQ");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.inossem.wms.wsdl.sap.input
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DTCWWMSMATDOCPOSTREQ }
     * 
     */
    public DTCWWMSMATDOCPOSTREQ createDTCWWMSMATDOCPOSTREQ() {
        return new DTCWWMSMATDOCPOSTREQ();
    }

    /**
     * Create an instance of {@link DTCWWMSMATDOCPOSTRESP }
     * 
     */
    public DTCWWMSMATDOCPOSTRESP createDTCWWMSMATDOCPOSTRESP() {
        return new DTCWWMSMATDOCPOSTRESP();
    }

    /**
     * Create an instance of {@link MSGHEAD }
     * 
     */
    public MSGHEAD createMSGHEAD() {
        return new MSGHEAD();
    }

    /**
     * Create an instance of {@link DTRETURN }
     * 
     */
    public DTRETURN createDTRETURN() {
        return new DTRETURN();
    }

    /**
     * Create an instance of {@link DTCWWMSMATDOCPOSTREQ.ITDOCHEAD }
     * 
     */
    public DTCWWMSMATDOCPOSTREQ.ITDOCHEAD createDTCWWMSMATDOCPOSTREQITDOCHEAD() {
        return new DTCWWMSMATDOCPOSTREQ.ITDOCHEAD();
    }

    /**
     * Create an instance of {@link DTCWWMSMATDOCPOSTREQ.ITDOCITEM }
     * 
     */
    public DTCWWMSMATDOCPOSTREQ.ITDOCITEM createDTCWWMSMATDOCPOSTREQITDOCITEM() {
        return new DTCWWMSMATDOCPOSTREQ.ITDOCITEM();
    }

    /**
     * Create an instance of {@link DTCWWMSMATDOCPOSTRESP.ETLOG }
     * 
     */
    public DTCWWMSMATDOCPOSTRESP.ETLOG createDTCWWMSMATDOCPOSTRESPETLOG() {
        return new DTCWWMSMATDOCPOSTRESP.ETLOG();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DTCWWMSMATDOCPOSTRESP }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sinopec:ecc:iwms:cw", name = "MT_CW_WMS_MATDOC_POST_RESP")
    public JAXBElement<DTCWWMSMATDOCPOSTRESP> createMTCWWMSMATDOCPOSTRESP(DTCWWMSMATDOCPOSTRESP value) {
        return new JAXBElement<DTCWWMSMATDOCPOSTRESP>(_MTCWWMSMATDOCPOSTRESP_QNAME, DTCWWMSMATDOCPOSTRESP.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DTCWWMSMATDOCPOSTREQ }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sinopec:ecc:iwms:cw", name = "MT_CW_WMS_MATDOC_POST_REQ")
    public JAXBElement<DTCWWMSMATDOCPOSTREQ> createMTCWWMSMATDOCPOSTREQ(DTCWWMSMATDOCPOSTREQ value) {
        return new JAXBElement<DTCWWMSMATDOCPOSTREQ>(_MTCWWMSMATDOCPOSTREQ_QNAME, DTCWWMSMATDOCPOSTREQ.class, null, value);
    }

}
