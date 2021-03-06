
package com.inossem.wms.wsdl.sap.output;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;

import com.inossem.wms.util.UtilProperties;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "SI_CW_WMS_DO_POST_S_OUTService", targetNamespace = "urn:sinopec:ecc:iwms:cw", wsdlLocation = "http://10.5.91.139:50000/dir/wsdl?p=sa/9e2c405e1c8830668260be3e6b9745c4")
public class SICWWMSDOPOSTSOUTService
    extends Service
{

    private final static URL SICWWMSDOPOSTSOUTSERVICE_WSDL_LOCATION;
    private final static WebServiceException SICWWMSDOPOSTSOUTSERVICE_EXCEPTION;
    private final static QName SICWWMSDOPOSTSOUTSERVICE_QNAME = new QName("urn:sinopec:ecc:iwms:cw", "SI_CW_WMS_DO_POST_S_OUTService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL(UtilProperties.getInstance().getWmsUrl() + "/wms/web/interface/output.wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        SICWWMSDOPOSTSOUTSERVICE_WSDL_LOCATION = url;
        SICWWMSDOPOSTSOUTSERVICE_EXCEPTION = e;
    }

    public SICWWMSDOPOSTSOUTService() {
        super(__getWsdlLocation(), SICWWMSDOPOSTSOUTSERVICE_QNAME);
    }

    public SICWWMSDOPOSTSOUTService(WebServiceFeature... features) {
        super(__getWsdlLocation(), SICWWMSDOPOSTSOUTSERVICE_QNAME, features);
    }

    public SICWWMSDOPOSTSOUTService(URL wsdlLocation) {
        super(wsdlLocation, SICWWMSDOPOSTSOUTSERVICE_QNAME);
    }

    public SICWWMSDOPOSTSOUTService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, SICWWMSDOPOSTSOUTSERVICE_QNAME, features);
    }

    public SICWWMSDOPOSTSOUTService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SICWWMSDOPOSTSOUTService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns SICWWMSDOPOSTSOUT
     */
    @WebEndpoint(name = "HTTP_Port")
    public SICWWMSDOPOSTSOUT getHTTPPort() {
        return super.getPort(new QName("urn:sinopec:ecc:iwms:cw", "HTTP_Port"), SICWWMSDOPOSTSOUT.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SICWWMSDOPOSTSOUT
     */
    @WebEndpoint(name = "HTTP_Port")
    public SICWWMSDOPOSTSOUT getHTTPPort(WebServiceFeature... features) {
        return super.getPort(new QName("urn:sinopec:ecc:iwms:cw", "HTTP_Port"), SICWWMSDOPOSTSOUT.class, features);
    }

    /**
     * 
     * @return
     *     returns SICWWMSDOPOSTSOUT
     */
    @WebEndpoint(name = "HTTPS_Port")
    public SICWWMSDOPOSTSOUT getHTTPSPort() {
        return super.getPort(new QName("urn:sinopec:ecc:iwms:cw", "HTTPS_Port"), SICWWMSDOPOSTSOUT.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SICWWMSDOPOSTSOUT
     */
    @WebEndpoint(name = "HTTPS_Port")
    public SICWWMSDOPOSTSOUT getHTTPSPort(WebServiceFeature... features) {
        return super.getPort(new QName("urn:sinopec:ecc:iwms:cw", "HTTPS_Port"), SICWWMSDOPOSTSOUT.class, features);
    }

    private static URL __getWsdlLocation() {
        if (SICWWMSDOPOSTSOUTSERVICE_EXCEPTION!= null) {
            throw SICWWMSDOPOSTSOUTSERVICE_EXCEPTION;
        }
        return SICWWMSDOPOSTSOUTSERVICE_WSDL_LOCATION;
    }

}
