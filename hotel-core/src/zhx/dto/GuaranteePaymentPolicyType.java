//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-646 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.09.13 at 04:34:51 ���� CST 
//


package zhx.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * ������������
 * 
 * <p>Java class for GuaranteePaymentPolicyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GuaranteePaymentPolicyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Type" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *         &lt;element name="PolicyBased" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *         &lt;element name="DeadLines">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
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
@XmlType(name = "GuaranteePaymentPolicyType", propOrder = {
    "type",
    "policyBased",
    "deadLines",
    "shortDescription",
    "description"
})
public class GuaranteePaymentPolicyType {

    @XmlElement(name = "Type", required = true)
    protected Object type;
    @XmlElement(name = "PolicyBased", required = true)
    protected Object policyBased;
    @XmlElement(name = "DeadLines", required = true)
    protected GuaranteePaymentPolicyType.DeadLines deadLines;
    @XmlElement(name = "ShortDescription", required = true)
    protected Object shortDescription;
    @XmlElement(name = "Description", required = true)
    protected Object description;

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setType(Object value) {
        this.type = value;
    }

    /**
     * Gets the value of the policyBased property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getPolicyBased() {
        return policyBased;
    }

    /**
     * Sets the value of the policyBased property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setPolicyBased(Object value) {
        this.policyBased = value;
    }

    /**
     * Gets the value of the deadLines property.
     * 
     * @return
     *     possible object is
     *     {@link GuaranteePaymentPolicyType.DeadLines }
     *     
     */
    public GuaranteePaymentPolicyType.DeadLines getDeadLines() {
        return deadLines;
    }

    /**
     * Sets the value of the deadLines property.
     * 
     * @param value
     *     allowed object is
     *     {@link GuaranteePaymentPolicyType.DeadLines }
     *     
     */
    public void setDeadLines(GuaranteePaymentPolicyType.DeadLines value) {
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
     *       &lt;sequence>
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
        protected DeadLineType deadLine;

        /**
         * Gets the value of the deadLine property.
         * 
         * @return
         *     possible object is
         *     {@link DeadLineType }
         *     
         */
        public DeadLineType getDeadLine() {
            return deadLine;
        }

        /**
         * Sets the value of the deadLine property.
         * 
         * @param value
         *     allowed object is
         *     {@link DeadLineType }
         *     
         */
        public void setDeadLine(DeadLineType value) {
            this.deadLine = value;
        }

    }

}
