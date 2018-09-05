
package com.inossem.wms.wsdl.mes.update;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>WmInvQueryResult complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="WmInvQueryResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="InvId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="MvRecId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="MtrlId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Bch" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="WgtDimId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="WgtPerPack" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="WgtPreBook" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="AmntPreBook" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="WgtAftBook" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="AmntAftBook" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="UserId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="StateFlg" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="RankId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="CrtTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="BegShiftTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="EndShiftTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="Rank" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="WgtDim" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Mtrl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CurInvFlg" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="OpeTypeId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="LocationId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Location" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WmInvQueryResult", propOrder = {
    "invId",
    "mvRecId",
    "mtrlId",
    "bch",
    "wgtDimId",
    "wgtPerPack",
    "wgtPreBook",
    "amntPreBook",
    "wgtAftBook",
    "amntAftBook",
    "userId",
    "stateFlg",
    "rankId",
    "crtTime",
    "begShiftTime",
    "endShiftTime",
    "rank",
    "wgtDim",
    "mtrl",
    "curInvFlg",
    "opeTypeId",
    "locationId",
    "location"
})
public class WmInvQueryResult {

    @XmlElement(name = "InvId", required = true, type = Integer.class, nillable = true)
    protected Integer invId;
    @XmlElement(name = "MvRecId", required = true, type = Integer.class, nillable = true)
    protected Integer mvRecId;
    @XmlElement(name = "MtrlId", required = true, type = Integer.class, nillable = true)
    protected Integer mtrlId;
    @XmlElement(name = "Bch")
    protected String bch;
    @XmlElement(name = "WgtDimId", required = true, type = Integer.class, nillable = true)
    protected Integer wgtDimId;
    @XmlElement(name = "WgtPerPack", required = true, nillable = true)
    protected BigDecimal wgtPerPack;
    @XmlElement(name = "WgtPreBook", required = true, nillable = true)
    protected BigDecimal wgtPreBook;
    @XmlElement(name = "AmntPreBook", required = true, type = Integer.class, nillable = true)
    protected Integer amntPreBook;
    @XmlElement(name = "WgtAftBook", required = true, nillable = true)
    protected BigDecimal wgtAftBook;
    @XmlElement(name = "AmntAftBook", required = true, type = Integer.class, nillable = true)
    protected Integer amntAftBook;
    @XmlElement(name = "UserId")
    protected String userId;
    @XmlElement(name = "StateFlg", required = true, type = Integer.class, nillable = true)
    protected Integer stateFlg;
    @XmlElement(name = "RankId", required = true, type = Integer.class, nillable = true)
    protected Integer rankId;
    @XmlElement(name = "CrtTime", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar crtTime;
    @XmlElement(name = "BegShiftTime", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar begShiftTime;
    @XmlElement(name = "EndShiftTime", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar endShiftTime;
    @XmlElement(name = "Rank")
    protected String rank;
    @XmlElement(name = "WgtDim")
    protected String wgtDim;
    @XmlElement(name = "Mtrl")
    protected String mtrl;
    @XmlElement(name = "CurInvFlg", required = true, type = Integer.class, nillable = true)
    protected Integer curInvFlg;
    @XmlElement(name = "OpeTypeId", required = true, type = Integer.class, nillable = true)
    protected Integer opeTypeId;
    @XmlElement(name = "LocationId", required = true, type = Integer.class, nillable = true)
    protected Integer locationId;
    @XmlElement(name = "Location")
    protected String location;

    /**
     * 获取invId属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getInvId() {
        return invId;
    }

    /**
     * 设置invId属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setInvId(Integer value) {
        this.invId = value;
    }

    /**
     * 获取mvRecId属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMvRecId() {
        return mvRecId;
    }

    /**
     * 设置mvRecId属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMvRecId(Integer value) {
        this.mvRecId = value;
    }

    /**
     * 获取mtrlId属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMtrlId() {
        return mtrlId;
    }

    /**
     * 设置mtrlId属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMtrlId(Integer value) {
        this.mtrlId = value;
    }

    /**
     * 获取bch属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBch() {
        return bch;
    }

    /**
     * 设置bch属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBch(String value) {
        this.bch = value;
    }

    /**
     * 获取wgtDimId属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getWgtDimId() {
        return wgtDimId;
    }

    /**
     * 设置wgtDimId属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setWgtDimId(Integer value) {
        this.wgtDimId = value;
    }

    /**
     * 获取wgtPerPack属性的值。
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getWgtPerPack() {
        return wgtPerPack;
    }

    /**
     * 设置wgtPerPack属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setWgtPerPack(BigDecimal value) {
        this.wgtPerPack = value;
    }

    /**
     * 获取wgtPreBook属性的值。
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getWgtPreBook() {
        return wgtPreBook;
    }

    /**
     * 设置wgtPreBook属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setWgtPreBook(BigDecimal value) {
        this.wgtPreBook = value;
    }

    /**
     * 获取amntPreBook属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAmntPreBook() {
        return amntPreBook;
    }

    /**
     * 设置amntPreBook属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAmntPreBook(Integer value) {
        this.amntPreBook = value;
    }

    /**
     * 获取wgtAftBook属性的值。
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getWgtAftBook() {
        return wgtAftBook;
    }

    /**
     * 设置wgtAftBook属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setWgtAftBook(BigDecimal value) {
        this.wgtAftBook = value;
    }

    /**
     * 获取amntAftBook属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAmntAftBook() {
        return amntAftBook;
    }

    /**
     * 设置amntAftBook属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAmntAftBook(Integer value) {
        this.amntAftBook = value;
    }

    /**
     * 获取userId属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置userId属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserId(String value) {
        this.userId = value;
    }

    /**
     * 获取stateFlg属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getStateFlg() {
        return stateFlg;
    }

    /**
     * 设置stateFlg属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setStateFlg(Integer value) {
        this.stateFlg = value;
    }

    /**
     * 获取rankId属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRankId() {
        return rankId;
    }

    /**
     * 设置rankId属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRankId(Integer value) {
        this.rankId = value;
    }

    /**
     * 获取crtTime属性的值。
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCrtTime() {
        return crtTime;
    }

    /**
     * 设置crtTime属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCrtTime(XMLGregorianCalendar value) {
        this.crtTime = value;
    }

    /**
     * 获取begShiftTime属性的值。
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getBegShiftTime() {
        return begShiftTime;
    }

    /**
     * 设置begShiftTime属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setBegShiftTime(XMLGregorianCalendar value) {
        this.begShiftTime = value;
    }

    /**
     * 获取endShiftTime属性的值。
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEndShiftTime() {
        return endShiftTime;
    }

    /**
     * 设置endShiftTime属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEndShiftTime(XMLGregorianCalendar value) {
        this.endShiftTime = value;
    }

    /**
     * 获取rank属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRank() {
        return rank;
    }

    /**
     * 设置rank属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRank(String value) {
        this.rank = value;
    }

    /**
     * 获取wgtDim属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWgtDim() {
        return wgtDim;
    }

    /**
     * 设置wgtDim属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWgtDim(String value) {
        this.wgtDim = value;
    }

    /**
     * 获取mtrl属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMtrl() {
        return mtrl;
    }

    /**
     * 设置mtrl属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMtrl(String value) {
        this.mtrl = value;
    }

    /**
     * 获取curInvFlg属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCurInvFlg() {
        return curInvFlg;
    }

    /**
     * 设置curInvFlg属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCurInvFlg(Integer value) {
        this.curInvFlg = value;
    }

    /**
     * 获取opeTypeId属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOpeTypeId() {
        return opeTypeId;
    }

    /**
     * 设置opeTypeId属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOpeTypeId(Integer value) {
        this.opeTypeId = value;
    }

    /**
     * 获取locationId属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getLocationId() {
        return locationId;
    }

    /**
     * 设置locationId属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setLocationId(Integer value) {
        this.locationId = value;
    }

    /**
     * 获取location属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocation() {
        return location;
    }

    /**
     * 设置location属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocation(String value) {
        this.location = value;
    }

}
