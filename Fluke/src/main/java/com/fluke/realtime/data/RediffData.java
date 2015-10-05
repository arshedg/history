package com.fluke.realtime.data;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class RediffData {

    @SerializedName("LastTradedPrice")
    @Expose
    private String LastTradedPrice;
    @SerializedName("Volume")
    @Expose
    private String Volume;
    @SerializedName("PercentageDiff")
    @Expose
    private String PercentageDiff;
    @SerializedName("FiftyTwoWeekHigh")
    @Expose
    private String FiftyTwoWeekHigh;
    @SerializedName("FiftyTwoWeekLow")
    @Expose
    private String FiftyTwoWeekLow;
    @SerializedName("LastTradedTime")
    @Expose
    private String LastTradedTime;
    @SerializedName("ChangePercent")
    @Expose
    private String ChangePercent;
    @SerializedName("Change")
    @Expose
    private String Change;
    @SerializedName("MarketCap")
    @Expose
    private String MarketCap;
    @SerializedName("High")
    @Expose
    private String High;
    @SerializedName("Low")
    @Expose
    private String Low;
    @SerializedName("PrevClose")
    @Expose
    private String PrevClose;
    @SerializedName("BonusSplitStatus")
    @Expose
    private String BonusSplitStatus;
    @SerializedName("BonusSplitRatio")
    @Expose
    private String BonusSplitRatio;

    /**
     *
     * @return The LastTradedPrice
     */
    public String getLastTradedPrice() {
        return LastTradedPrice;
    }

    /**
     *
     * @param LastTradedPrice The LastTradedPrice
     */
    public void setLastTradedPrice(String LastTradedPrice) {
        this.LastTradedPrice = LastTradedPrice;
    }

    /**
     *
     * @return The Volume
     */
    public String getVolume() {
        return Volume;
    }

    /**
     *
     * @param Volume The Volume
     */
    public void setVolume(String Volume) {
        this.Volume = Volume;
    }

    /**
     *
     * @return The PercentageDiff
     */
    public String getPercentageDiff() {
        return PercentageDiff;
    }

    /**
     *
     * @param PercentageDiff The PercentageDiff
     */
    public void setPercentageDiff(String PercentageDiff) {
        this.PercentageDiff = PercentageDiff;
    }

    /**
     *
     * @return The FiftyTwoWeekHigh
     */
    public String getFiftyTwoWeekHigh() {
        return FiftyTwoWeekHigh;
    }

    /**
     *
     * @param FiftyTwoWeekHigh The FiftyTwoWeekHigh
     */
    public void setFiftyTwoWeekHigh(String FiftyTwoWeekHigh) {
        this.FiftyTwoWeekHigh = FiftyTwoWeekHigh;
    }

    /**
     *
     * @return The FiftyTwoWeekLow
     */
    public String getFiftyTwoWeekLow() {
        return FiftyTwoWeekLow;
    }

    /**
     *
     * @param FiftyTwoWeekLow The FiftyTwoWeekLow
     */
    public void setFiftyTwoWeekLow(String FiftyTwoWeekLow) {
        this.FiftyTwoWeekLow = FiftyTwoWeekLow;
    }

    /**
     *
     * @return The LastTradedTime
     */
    public String getLastTradedTime() {
        return LastTradedTime;
    }

    /**
     *
     * @param LastTradedTime The LastTradedTime
     */
    public void setLastTradedTime(String LastTradedTime) {
        this.LastTradedTime = LastTradedTime;
    }

    /**
     *
     * @return The ChangePercent
     */
    public String getChangePercent() {
        return ChangePercent;
    }

    /**
     *
     * @param ChangePercent The ChangePercent
     */
    public void setChangePercent(String ChangePercent) {
        this.ChangePercent = ChangePercent;
    }

    /**
     *
     * @return The Change
     */
    public String getChange() {
        return Change;
    }

    /**
     *
     * @param Change The Change
     */
    public void setChange(String Change) {
        this.Change = Change;
    }

    /**
     *
     * @return The MarketCap
     */
    public String getMarketCap() {
        return MarketCap;
    }

    /**
     *
     * @param MarketCap The MarketCap
     */
    public void setMarketCap(String MarketCap) {
        this.MarketCap = MarketCap;
    }

    /**
     *
     * @return The High
     */
    public String getHigh() {
        return High;
    }

    /**
     *
     * @param High The High
     */
    public void setHigh(String High) {
        this.High = High;
    }

    /**
     *
     * @return The Low
     */
    public String getLow() {
        return Low;
    }

    /**
     *
     * @param Low The Low
     */
    public void setLow(String Low) {
        this.Low = Low;
    }

    /**
     *
     * @return The PrevClose
     */
    public String getPrevClose() {
        return PrevClose;
    }

    /**
     *
     * @param PrevClose The PrevClose
     */
    public void setPrevClose(String PrevClose) {
        this.PrevClose = PrevClose;
    }

    /**
     *
     * @return The BonusSplitStatus
     */
    public String getBonusSplitStatus() {
        return BonusSplitStatus;
    }

    /**
     *
     * @param BonusSplitStatus The BonusSplitStatus
     */
    public void setBonusSplitStatus(String BonusSplitStatus) {
        this.BonusSplitStatus = BonusSplitStatus;
    }

    /**
     *
     * @return The BonusSplitRatio
     */
    public String getBonusSplitRatio() {
        return BonusSplitRatio;
    }

    /**
     *
     * @param BonusSplitRatio The BonusSplitRatio
     */
    public void setBonusSplitRatio(String BonusSplitRatio) {
        this.BonusSplitRatio = BonusSplitRatio;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
