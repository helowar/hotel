//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-646 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.09.13 at 04:34:51 ���� CST 
//


package zhx.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RelativePositionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RelativePositionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="Direction" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="Distance" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="PositionName" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="PositionCode" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="UnitOfMeasureCode" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="Discription" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RelativePositionType")
public class RelativePositionType {

    @XmlAttribute(name = "Direction")
    @XmlSchemaType(name = "anySimpleType")
    protected String direction;
    @XmlAttribute(name = "Distance")
    @XmlSchemaType(name = "anySimpleType")
    protected String distance;
    @XmlAttribute(name = "PositionName")
    @XmlSchemaType(name = "anySimpleType")
    protected String positionName;
    @XmlAttribute(name = "PositionCode")
    @XmlSchemaType(name = "anySimpleType")
    protected String positionCode;
    @XmlAttribute(name = "UnitOfMeasureCode")
    @XmlSchemaType(name = "anySimpleType")
    protected String unitOfMeasureCode;
    @XmlAttribute(name = "Discription")
    @XmlSchemaType(name = "anySimpleType")
    protected String discription;

    /**
     * Gets the value of the direction property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Sets the value of the direction property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDirection(String value) {
        this.direction = value;
    }

    /**
     * Gets the value of the distance property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDistance() {
        return distance;
    }

    /**
     * Sets the value of the distance property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDistance(String value) {
        this.distance = value;
    }

    /**
     * Gets the value of the positionName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPositionName() {
        return positionName;
    }

    /**
     * Sets the value of the positionName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPositionName(String value) {
        this.positionName = value;
    }

    /**
     * Gets the value of the positionCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPositionCode() {
        return positionCode;
    }

    /**
     * Sets the value of the positionCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPositionCode(String value) {
        this.positionCode = value;
    }

    /**
     * Gets the value of the unitOfMeasureCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnitOfMeasureCode() {
        return unitOfMeasureCode;
    }

    /**
     * Sets the value of the unitOfMeasureCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnitOfMeasureCode(String value) {
        this.unitOfMeasureCode = value;
    }

    /**
     * Gets the value of the discription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDiscription() {
        return discription;
    }

    /**
     * Sets the value of the discription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDiscription(String value) {
        this.discription = value;
    }

}