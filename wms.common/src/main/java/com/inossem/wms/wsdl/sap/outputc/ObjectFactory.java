
package com.inossem.wms.wsdl.sap.outputc;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.inossem.wms.wsdl.sap.outputc package. 
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

    private final static QName _MTCWWMSDOREVERSEREQ_QNAME = new QName("urn:sinopec:ecc:iwms:cw", "MT_CW_WMS_DO_REVERSE_REQ");
    private final static QName _MTCWWMSDOREVERSERESP_QNAME = new QName("urn:sinopec:ecc:iwms:cw", "MT_CW_WMS_DO_REVERSE_RESP");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.inossem.wms.wsdl.sap.outputc
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DTCWWMSDOREVERSEREQ }
     * 
     */
    public DTCWWMSDOREVERSEREQ createDTCWWMSDOREVERSEREQ() {
        return new DTCWWMSDOREVERSEREQ();
    }

    /**
     * Create an instance of {@link DTCWWMSDOREVERSERESP }
     * 
     */
    public DTCWWMSDOREVERSERESP createDTCWWMSDOREVERSERESP() {
        return new DTCWWMSDOREVERSERESP();
    }

    /**
     * Create an instance of {@link MSGHEAD }
     * 
     */
    public MSGHEAD createMSGHEAD() {
        return new MSGHEAD();
    }

    /**
     * Create an instance of {@link DTCWWMSDOREVERSEREQ.ITDATA }
     * 
     */
    public DTCWWMSDOREVERSEREQ.ITDATA createDTCWWMSDOREVERSEREQITDATA() {
        return new DTCWWMSDOREVERSEREQ.ITDATA();
    }

    /**
     * Create an instance of {@link DTCWWMSDOREVERSERESP.ETDATA }
     * 
     */
    public DTCWWMSDOREVERSERESP.ETDATA createDTCWWMSDOREVERSERESPETDATA() {
        return new DTCWWMSDOREVERSERESP.ETDATA();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DTCWWMSDOREVERSEREQ }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sinopec:ecc:iwms:cw", name = "MT_CW_WMS_DO_REVERSE_REQ")
    public JAXBElement<DTCWWMSDOREVERSEREQ> createMTCWWMSDOREVERSEREQ(DTCWWMSDOREVERSEREQ value) {
        return new JAXBElement<DTCWWMSDOREVERSEREQ>(_MTCWWMSDOREVERSEREQ_QNAME, DTCWWMSDOREVERSEREQ.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DTCWWMSDOREVERSERESP }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sinopec:ecc:iwms:cw", name = "MT_CW_WMS_DO_REVERSE_RESP")
    public JAXBElement<DTCWWMSDOREVERSERESP> createMTCWWMSDOREVERSERESP(DTCWWMSDOREVERSERESP value) {
        return new JAXBElement<DTCWWMSDOREVERSERESP>(_MTCWWMSDOREVERSERESP_QNAME, DTCWWMSDOREVERSERESP.class, null, value);
    }

}
