package com.example.njpischoolapp.bean;

public class SecondhandGoodsObject {
//                "goodsid": 1,
//                        "gname": "王怡博的手机",
//                        "price": 666,
//                        "pubuserid": 1,
//                        "pubtime": 1561104827487,
//                        "state": 1
    private String goodsid;
    private String gname;
    private String price;
    private String pubuserid;
    private String pubtime;
    private String state;
    private String pubname;
    private String buyuserid;
    private String kind;

    public String getBuyuserid() {
        return buyuserid;
    }

    public void setBuyuserid(String buyuserid) {
        this.buyuserid = buyuserid;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getPubname() {
        return pubname;
    }

    public void setPubname(String pubname) {
        this.pubname = pubname;
    }

    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPubuserid() {
        return pubuserid;
    }

    public void setPubuserid(String pubuserid) {
        this.pubuserid = pubuserid;
    }

    public String getPubtime() {
        return pubtime;
    }

    public void setPubtime(String pubtime) {
        this.pubtime = pubtime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
