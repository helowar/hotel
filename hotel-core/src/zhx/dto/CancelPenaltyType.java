//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-646 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.09.13 at 04:34:51 ���� CST 
//


package zhx.dto;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * ȡ����������
 * 
 * <p>Java class for CancelPenaltyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CancelPenaltyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PenaltyDescription" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DeadLines">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence maxOccurs="3">
 *                   &lt;element name="DeadLine" type="{}DeadLineType"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="ShortDescription" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CancelPenaltyType", propOrder = {
    "penaltyDescription",
    "deadLines",
    "shortDescription",
    "description"
})
public class CancelPenaltyType {

    @XmlElement(name = "PenaltyDescription", required = true)
    protected String penaltyDescription;
    @XmlElement(name = "DeadLines", required = true)
    protected CancelPenaltyType.DeadLines deadLines;
    @XmlElement(name = "ShortDescription", required = true)
    protected Object shortDescription;
    @XmlElement(name = "Description", required = true)
    protected Object description;

    /**
     * Gets the value of the penaltyDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPenaltyDescription() {
        return penaltyDescription;
    }

    /**
     * Sets the value of the penaltyDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPenaltyDescription(String value) {
        this.penaltyDescription = value;
    }

    /**
     * Gets the value of the deadLines property.
     * 
     * @return
     *     possible object is
     *     {@link CancelPenaltyType.DeadLines }
     *     
     */
    public CancelPenaltyType.DeadLines getDeadLines() {
        return deadLines;
    }

    /**
     * Sets the value of the deadLines property.
     * 
     * @param value
     *     allowed object is
     *     {@link CancelPenaltyType.DeadLines }
     *     
     */
    public void setDeadLines(CancelPenaltyType.DeadLines value) {
        this.deadLines = value;
    }

    /**
     * Gets the value of the shortDescription property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getShortDescription() {
        return shortDescription;
    }

    /**
     * Sets the value of the shortDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setShortDescription(Object value) {
        this.shortDescription = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setDescription(Object value) {
        this.description = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence maxOccurs="3">
     *         &lt;element name="DeadLine" type="{}DeadLineType"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "deadLine"
    })
    public static class DeadLines {

        @XmlElement(name = "DeadLine", required = true)
        protected List<DeadLineType> deadLine;

        /**
         * Gets the value of the deadLine property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the deadLine property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getDeadLine().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link DeadLineType }
         * 
         * 
         */
        public List<DeadLineType> getDeadLine() {
            if (deadLine == null) {
                deadLine = new ArrayList<DeadLineType>();
            }
            return this.deadLine;
        }

    }

}
