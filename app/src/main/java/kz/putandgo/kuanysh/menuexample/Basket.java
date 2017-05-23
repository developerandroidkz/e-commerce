package kz.putandgo.kuanysh.menuexample;

/**
 * Created by Kuanysh on 23.04.2017.
 */

public class Basket {
    String product_id,
            barcode,
            name,
            nik_name,
            description,
            type_name,
            massa_netto,
            massa_brutto,
            manufacturer_name,
            sposobiy_hranenie,
            image_1_url,
            image_2_url,
            image_3_url,
            image_4_url,date;
    int type,market_id,price,count;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNik_name() {
        return nik_name;
    }

    public void setNik_name(String nik_name) {
        this.nik_name = nik_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getMassa_netto() {
        return massa_netto;
    }

    public void setMassa_netto(String massa_netto) {
        this.massa_netto = massa_netto;
    }

    public String getMassa_brutto() {
        return massa_brutto;
    }

    public void setMassa_brutto(String massa_brutto) {
        this.massa_brutto = massa_brutto;
    }

    public String getManufacturer_name() {
        return manufacturer_name;
    }

    public void setManufacturer_name(String manufacturer_name) {
        this.manufacturer_name = manufacturer_name;
    }

    public String getSposobiy_hranenie() {
        return sposobiy_hranenie;
    }

    public void setSposobiy_hranenie(String sposobiy_hranenie) {
        this.sposobiy_hranenie = sposobiy_hranenie;
    }

    public String getImage_1_url() {
        return image_1_url;
    }

    public void setImage_1_url(String image_1_url) {
        this.image_1_url = image_1_url;
    }

    public String getImage_2_url() {
        return image_2_url;
    }

    public void setImage_2_url(String image_2_url) {
        this.image_2_url = image_2_url;
    }

    public String getImage_3_url() {
        return image_3_url;
    }

    public void setImage_3_url(String image_3_url) {
        this.image_3_url = image_3_url;
    }

    public String getImage_4_url() {
        return image_4_url;
    }

    public void setImage_4_url(String image_4_url) {
        this.image_4_url = image_4_url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getMarket_id() {
        return market_id;
    }

    public void setMarket_id(int market_id) {
        this.market_id = market_id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
