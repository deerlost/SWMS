
package com.inossem.wms.wsdl.sap.getdo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * ��ά���ִܲ�����ϵͳ��ѯ���򽻻����ӿ�
 * 
 * <p>DT_CW_WMS_GET_DO_REQ complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="DT_CW_WMS_GET_DO_REQ">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IS_MSG_HEAD" type="{urn:sinopec:ecc:iwms:cw}MSG_HEAD" minOccurs="0"/>
 *         &lt;element name="IV_TESTRUN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IV_VBELN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IV_VGBEL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DT_CW_WMS_GET_DO_REQ", propOrder = {
    "ismsghead",
    "ivtestrun",
    "ivvbeln",
    "ivvgbel"
})
public class DTCWWMSGETDOREQ {

    @XmlElement(name = "IS_MSG_HEAD")
    protected MSGHEAD ismsghead;
    @XmlElement(name = "IV_TESTRUN")
    protected String ivtestrun;
    @XmlElement(name = "IV_VBELN")
    protected String ivvbeln;
    @XmlElement(name = "IV_VGBEL")
    protected String ivvgbel;

    /**
     * ��ȡismsghead���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link MSGHEAD }
     *     
     */
    public MSGHEAD getISMSGHEAD() {
        return ismsghead;
    }

    /**
     * ����ismsghead���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link MSGHEAD }
     *     
     */
    public void setISMSGHEAD(MSGHEAD value) {
        this.ismsghead = value;
    }

    /**
     * ��ȡivtestrun���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIVTESTRUN() {
        return ivtestrun;
    }

    /**
     * ����ivtestrun���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIVTESTRUN(String value) {
        this.ivtestrun = value;
    }

    /**
     * ��ȡivvbeln���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIVVBELN() {
        return ivvbeln;
    }

    /**
     * ����ivvbeln���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIVVBELN(String value) {
        this.ivvbeln = value;
    }

    /**
     * ��ȡivvgbel���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIVVGBEL() {
        return ivvgbel;
    }

    /**
     * ����ivvgbel���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIVVGBEL(String value) {
        this.ivvgbel = value;
    }

}
