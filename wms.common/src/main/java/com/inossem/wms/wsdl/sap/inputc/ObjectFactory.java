
package com.inossem.wms.wsdl.sap.inputc;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.inossem.wms.wsdl.sap.inputc package. 
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

    private final static QName _MTCWWMSCANCELMATDOCREQ_QNAME = new QName("urn:sinopec:ecc:iwms:cw", "MT_CW_WMS_CANCEL_MATDOC_REQ");
    private final static QName _MTCWWMSCANCELMATDOCRESP_QNAME = new QName("urn:sinopec:ecc:iwms:cw", "MT_CW_WMS_CANCEL_MATDOC_RESP");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.inossem.wms.wsdl.sap.inputc
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DTCWWMSCANCELMATDOCREQ }
     * 
     */
    public DTCWWMSCANCELMATDOCREQ createDTCWWMSCANCELMATDOCREQ() {
        return new DTCWWMSCANCELMATDOCREQ();
    }

    /**
     * Create an instance of {@link DTCWWMSCANCELMATDOCRESP }
     * 
     */
    public DTCWWMSCANCELMATDOCRESP createDTCWWMSCANCELMATDOCRESP() {
        return new DTCWWMSCANCELMATDOCRESP();
    }

    /**
     * Create an instance of {@link DTZRETURN }
     * 
     */
    public DTZRETURN createDTZRETURN() {
        return new DTZRETURN();
    }

    /**
     * Create an instance of {@link MSGHEAD }
     * 
     */
    public MSGHEAD createMSGHEAD() {
        return new MSGHEAD();
    }

    /**
     * Create an instance of {@link DTCWWMSCANCELMATDOCREQ.ITMATDOC }
     * 
     */
    public DTCWWMSCANCELMATDOCREQ.ITMATDOC createDTCWWMSCANCELMATDOCREQITMATDOC() {
        return new DTCWWMSCANCELMATDOCREQ.ITMATDOC();
    }

    /**
     * Create an instance of {@link DTCWWMSCANCELMATDOCRESP.ETLOG }
     * 
     */
    public DTCWWMSCANCELMATDOCRESP.ETLOG createDTCWWMSCANCELMATDOCRESPETLOG() {
        return new DTCWWMSCANCELMATDOCRESP.ETLOG();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DTCWWMSCANCELMATDOCREQ }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sinopec:ecc:iwms:cw", name = "MT_CW_WMS_CANCEL_MATDOC_REQ")
    public JAXBElement<DTCWWMSCANCELMATDOCREQ> createMTCWWMSCANCELMATDOCREQ(DTCWWMSCANCELMATDOCREQ value) {
        return new JAXBElement<DTCWWMSCANCELMATDOCREQ>(_MTCWWMSCANCELMATDOCREQ_QNAME, DTCWWMSCANCELMATDOCREQ.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DTCWWMSCANCELMATDOCRESP }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sinopec:ecc:iwms:cw", name = "MT_CW_WMS_CANCEL_MATDOC_RESP")
    public JAXBElement<DTCWWMSCANCELMATDOCRESP> createMTCWWMSCANCELMATDOCRESP(DTCWWMSCANCELMATDOCRESP value) {
        return new JAXBElement<DTCWWMSCANCELMATDOCRESP>(_MTCWWMSCANCELMATDOCRESP_QNAME, DTCWWMSCANCELMATDOCRESP.class, null, value);
    }

}
