package dk.bankdata.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExhangeRateApiResponse {
    private String result;
    private String documentation;
    private String termsOfUse;
    private long timeLastUpdateUnix;
    private String timeLastUpdateUtc;
    private long timeNextUpdateUnix;
    private String timeNextUpdateUtc;
    private String baseCode;
    private String targetCode;
    private double conversionRate;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    @JsonProperty("terms_of_use")
    public String getTermsOfUse() {
        return termsOfUse;
    }

    public void setTermsOfUse(String termsOfUse) {
        this.termsOfUse = termsOfUse;
    }

    @JsonProperty("time_last_update_unix")
    public long getTimeLastUpdateUnix() {
        return timeLastUpdateUnix;
    }

    public void setTimeLastUpdateUnix(long timeLastUpdateUnix) {
        this.timeLastUpdateUnix = timeLastUpdateUnix;
    }

    @JsonProperty("time_last_update_utc")
    public String getTimeLastUpdateUtc() {
        return timeLastUpdateUtc;
    }

    public void setTimeLastUpdateUtc(String timeLastUpdateUtc) {
        this.timeLastUpdateUtc = timeLastUpdateUtc;
    }

    @JsonProperty("time_next_update_unix")
    public long getTimeNextUpdateUnix() {
        return timeNextUpdateUnix;
    }

    public void setTimeNextUpdateUnix(long timeNextUpdateUnix) {
        this.timeNextUpdateUnix = timeNextUpdateUnix;
    }

    @JsonProperty("time_next_update_utc")
    public String getTimeNextUpdateUtc() {
        return timeNextUpdateUtc;
    }

    public void setTimeNextUpdateUtc(String timeNextUpdateUtc) {
        this.timeNextUpdateUtc = timeNextUpdateUtc;
    }

    @JsonProperty("base_code")
    public String getBaseCode() {
        return baseCode;
    }

    public void setBaseCode(String baseCode) {
        this.baseCode = baseCode;
    }

    @JsonProperty("target_code")
    public String getTargetCode() {
        return targetCode;
    }

    public void setTargetCode(String targetCode) {
        this.targetCode = targetCode;
    }

    @JsonProperty("conversion_rate")
    public double getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(double conversionRate) {
        this.conversionRate = conversionRate;
    }
}
