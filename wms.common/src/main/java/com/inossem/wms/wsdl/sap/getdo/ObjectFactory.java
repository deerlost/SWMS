package com.inossem.wms.wsdl.sap.getdo;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.inossem.wms.wsdl.sap package. 
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

    private final static QName _MTCWWMSGETDOREQ_QNAME = new QName("urn:sinopec:ecc:iwms:cw", "MT_CW_WMS_GET_DO_REQ");
    private final static QName _MTCWWMSGETDORESP_QNAME = new QName("urn:sinopec:ecc:iwms:cw", "MT_CW_WMS_GET_DO_RESP");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.inossem.wms.wsdl.sap
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DTCWWMSGETDORESP }
     * 
     */
    public DTCWWMSGETDORESP createDTCWWMSGETDORESP() {
        return new DTCWWMSGETDORESP();
    }

    /**
     * Create an instance of {@link DTCWWMSGETDOREQ }
     * 
     */
    public DTCWWMSGETDOREQ createDTCWWMSGETDOREQ() {
        return new DTCWWMSGETDOREQ();
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
     * Create an instance of {@link DTCWWMSGETDORESP.ETLIKP }
     * 
     */
    public DTCWWMSGETDORESP.ETLIKP createDTCWWMSGETDORESPETLIKP() {
        return new DTCWWMSGETDORESP.ETLIKP();
    }

    /**
     * Create an instance of {@link DTCWWMSGETDORESP.ETLIPS }
     * 
     */
    public DTCWWMSGETDORESP.ETLIPS createDTCWWMSGETDORESPETLIPS() {
        return new DTCWWMSGETDORESP.ETLIPS();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DTCWWMSGETDOREQ }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sinopec:ecc:iwms:cw", name = "MT_CW_WMS_GET_DO_REQ")
    public JAXBElement<DTCWWMSGETDOREQ> createMTCWWMSGETDOREQ(DTCWWMSGETDOREQ value) {
        return new JAXBElement<DTCWWMSGETDOREQ>(_MTCWWMSGETDOREQ_QNAME, DTCWWMSGETDOREQ.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DTCWWMSGETDORESP }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sinopec:ecc:iwms:cw", name = "MT_CW_WMS_GET_DO_RESP")
    public JAXBElement<DTCWWMSGETDORESP> createMTCWWMSGETDORESP(DTCWWMSGETDORESP value) {
        return new JAXBElement<DTCWWMSGETDORESP>(_MTCWWMSGETDORESP_QNAME, DTCWWMSGETDORESP.class, null, value);
    }

}
