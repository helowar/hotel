package com.mangocity.hotel.base.web;

import com.mangocity.hotel.base.manage.ContractManage;
import com.mangocity.hotel.base.manage.IPriceManage;
import com.mangocity.hotel.base.manage.QuotaPriceManage;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;

/**
 */
public class CurrencyPatternAction extends PersistenceAction {

    private Long contractId;

    private String currency;

    private String quotaPattern;

    private String currencyTemp;

    private String quotaPatternTemp;

    private ContractManage contractManage;

    private IPriceManage priceManage;

    private QuotaPriceManage quotaPriceManage;

    public String view() {
        currencyTemp = currency;
        quotaPatternTemp = quotaPattern;
        return VIEW;
    }

    public String edit() {
        if (!(currency.equals(currencyTemp) && quotaPattern.equals(quotaPatternTemp))) {
            // 修改合同的币种和配额模式
            HtlContract contract = contractManage.loadContract(contractId);
            contract.setCurrency(currency);
            contract.setQuotaPattern(quotaPattern);
            contractManage.modifyContract(contract);
            // 修改配额明细的配额模式 、价格明细的币种
            contractManage.updateCurrencyPattern(contract.getHotelId(), contractId, currency
                .equals(currencyTemp) ? null : currency,
                quotaPattern.equals(quotaPatternTemp) ? null : quotaPattern, contract
                    .getBeginDate(), contract.getEndDate());
        }
        return SUCCESS;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getQuotaPattern() {
        return quotaPattern;
    }

    public void setQuotaPattern(String quotaPattern) {
        this.quotaPattern = quotaPattern;
    }

    public ContractManage getContractManage() {
        return contractManage;
    }

    public void setContractManage(ContractManage contractManage) {
        this.contractManage = contractManage;
    }

    public IPriceManage getPriceManage() {
        return priceManage;
    }

    public void setPriceManage(IPriceManage priceManage) {
        this.priceManage = priceManage;
    }

    public QuotaPriceManage getQuotaPriceManage() {
        return quotaPriceManage;
    }

    public void setQuotaPriceManage(QuotaPriceManage quotaPriceManage) {
        this.quotaPriceManage = quotaPriceManage;
    }

    public String getCurrencyTemp() {
        return currencyTemp;
    }

    public void setCurrencyTemp(String currencyTemp) {
        this.currencyTemp = currencyTemp;
    }

    public String getQuotaPatternTemp() {
        return quotaPatternTemp;
    }

    public void setQuotaPatternTemp(String quotaPatternTemp) {
        this.quotaPatternTemp = quotaPatternTemp;
    }

}
