package hk.com.cts.ctcp.hotel.webservice.enquiryservice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;enqClassNationAmtReturn&quot; 
 *         type=&quot;{http://enquiry.hotel.ws.ctcp.cts.com}ClassNationAmtData&quot; 
 *         					maxOccurs=&quot;unbounded&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "enqClassNationAmtReturn" })
@XmlRootElement(name = "enqClassNationAmtResponse")
public class EnqClassNationAmtResponse {

    @XmlElement(required = true)
    protected List<ClassNationAmtData> enqClassNationAmtReturn;

    /**
     * Gets the value of the enqClassNationAmtReturn property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any
     * modification you make to the returned list will be present inside the JAXB object. This is
     * why there is not a <CODE>set</CODE> method for the enqClassNationAmtReturn property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getEnqClassNationAmtReturn().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link ClassNationAmtData }
     * 
     * 
     */
    public List<ClassNationAmtData> getEnqClassNationAmtReturn() {
        if (null == enqClassNationAmtReturn) {
            enqClassNationAmtReturn = new ArrayList<ClassNationAmtData>();
        }
        return this.enqClassNationAmtReturn;
    }

}
