package hk.com.cts.ctcp.hotel.webservice.commonservice;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each Java content interface and Java element interface
 * generated in the hk.com.cts.ctcp.hotel.webservice.commonService package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the Java representation
 * for XML content. The Java representation of XML content can consist of schema derived interfaces
 * and classes representing the binding of schema type definitions, element declarations and model
 * groups. Factory methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes
     * for package: hk.com.cts.ctcp.hotel.webservice.commonService
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BasicData }
     * 
     */
    public BasicData createBasicData() {
        return new BasicData();
    }

    /**
     * Create an instance of {@link AppRegData }
     * 
     */
    public AppRegData createAppRegData() {
        return new AppRegData();
    }

    /**
     * Create an instance of {@link ComAppRegResponse }
     * 
     */
    public ComAppRegResponse createComAppRegResponse() {
        return new ComAppRegResponse();
    }

    /**
     * Create an instance of {@link ComAppReg }
     * 
     */
    public ComAppReg createComAppReg() {
        return new ComAppReg();
    }

}
