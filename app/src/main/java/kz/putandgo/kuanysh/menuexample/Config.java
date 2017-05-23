package kz.putandgo.kuanysh.menuexample;

/**
 * Created by Kuanysh on 22.02.2017.
 */

public class Config {
    public static final String DATA_URL_GET_PRODUCT = "http://www.putandgo.ru/api/public/index.php/api/mobile/android/getProducts/";
    public static final String DATA_URL_LOGIN = "http://www.putandgo.ru/api/public/index.php/api/mobile/android/login";
    public static final String DATA_URL_REGISTER = "http://www.putandgo.ru/api/public/index.php/api/mobile/android/register";
    public static final String DATA_URL_GET_MARKET_LIST = "http://www.putandgo.ru/api/public/index.php/api/mobile/android/marketList";
    public static final String DATA_URL_SEND_ORDER = "http://www.putandgo.ru/api/public/index.php/api/mobile/android/order";
    public static final String DATA_URL_GET_ORDER = "http://www.putandgo.ru/api/public/index.php/api/mobile/android/getOrder/";
    public static final String DATA_URL_SCAN="http://www.putandgo.ru/api/public/index.php/api/mobile/android/getProducts/byScan";

 public static final String DATA_URL_USER_IMAGE="http://www.putandgo.ru/image/users/user_";
 // public static final String DATA_URL_GET_PRODUCT = "https://achtung-95.000webhostapp.com/api/public/index.php/api/mobile/android/getProducts/";
    //public static final String DATA_URL_LOGIN = "https://achtung-95.000webhostapp.com/api/public/index.php/api/mobile/android/login";
    //public static final String DATA_URL_REGISTER = "https://achtung-95.000webhostapp.com/api/public/index.php/api/mobile/android/register";
    //public static final String DATA_URL_GET_MARKET_LIST = "https://achtung-95.000webhostapp.com/api/public/index.php/api/mobile/android/marketList";
    //public static final String DATA_URL_SEND_ORDER = "https://achtung-95.000webhostapp.com/api/public/index.php/api/mobile/android/order";
    //public static final String DATA_URL_GET_ORDER = "https://achtung-95.000webhostapp.com/api/public/index.php/api/mobile/android/getOrder/";
    //public static final String DATA_URL_SCAN="https://achtung-95.000webhostapp.com/api/public/index.php/api/mobile/android/getProducts/byScan";

   // public static final String DATA_URL_GET_PRODUCT = "http://putandgo.000webhostapp.com/api/public/index.php/api/mobile/android/getProducts/";
   // public static final String DATA_URL_LOGIN = "http://putandgo.000webhostapp.com/api/public/index.php/api/mobile/android/login";
   // public static final String DATA_URL_REGISTER = "http://putandgo.000webhostapp.com/api/public/index.php/api/mobile/android/register";
   // public static final String DATA_URL_GET_MARKET_LIST = "http://putandgo.000webhostapp.com/api/public/index.php/api/mobile/android/marketList";
   // public static final String DATA_URL_SEND_ORDER = "http://putandgo.000webhostapp.com/api/public/index.php/api/mobile/android/order";
   // public static final String DATA_URL_GET_ORDER = "http://putandgo.000webhostapp.com/api/public/index.php/api/mobile/android/getOrder/";
   // public static final String DATA_URL_SCAN="http://putandgo.000webhostapp.com/api/public/index.php/api/mobile/android/getProducts/byScan";
    //user data tags
    public static final String TAG_ID = "id";
    public static final String TAG_TELL_NUMBER = "tellNumber";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_IS_ACTIVE = "is_active";
    public static final String TAG_DATE_REGISTRATED = "date_registrated";

    //choose market tags
    public static final String TAG_MARKET_NAME= "name_market";
    public static final String TAG_MARKET_ID= "id_market";
    public static final String TAG_MARKET_latitude= "latitude";
    public static final String TAG_MARKET_longitude= "longitude";
    public static final String TAG_MARKET_MODE= "mode";
    public static final String TAG_MARKET_ADDRES="address_market";
    public static final String TAG_MARKET_URL="image_url";

    /**
     * tags for products
     */
    public static final String TAG_PRODUCT_ID = "product_id";
    public static final String TAG_PRODUCT_BARCODE = "barcode";
    public static final String TAG_PRODUCT_NAME = "name";
    public static final String TAG_PRODUCT_NIK_NAME = "nik_name";
    public static final String TAG_PRODUCT_DESCRIPTION = "description";
    public static final String TAG_PRODUCT_TYPE = "type";
    public static final String TAG_PRODUCT_TYPE_NAME = "type_name";
    public static final String TAG_PRODUCT_MASSA_NETTO = "massa_netto";
    public static final String TAG_PRODUCT_MASSA_BRUTTO = "massa_brutto";
    public static final String TAG_PRODUCT_RAZDEL_ID = "razdel_id";
    public static final String TAG_PRODUCT_MANUFACTERER_NAME = "manufacturer_name";
    public static final String TAG_PRODUCT_SPOSOBIY_HRANENIE = "sposobiy_hranenie";
    public static final String TAG_PRODUCT_IMAGE_URL_1 = "image_1_url";
    public static final String TAG_PRODUCT_IMAGE_URL_2= "image_2_url";
    public static final String TAG_PRODUCT_IMAGE_URL3_ = "image_3_url";
    public static final String TAG_PRODUCT_IMAGE_URL_4 = "image_4_url";
    public static final String TAG_PRODUCT_MARKET_ID = "market_id";
    public static final String TAG_PRODUCT_PRICE = "price";
    public static final String TAG_STATUS = "status";

    /**
     * tags for orders
     */
    public static final String TAG_ORDER_ID = "id";
    public static final String TAG_ORDER_DATE_DELIVERY = "date_delivery";
    public static final String TAG_ORDER_TIME = "time_delivery";
    public static final String TAG_ORDER_MARKET_ID = "market_id";
    public static final String TAG_ORDER_DATE_INSERTED = "date_inserted";
    public static final String TAG_ORDER_DATA = "data";
    public static final String TAG_ORDER_STATUS = "status";
    public static final String TAG_ORDER_MERCHENDAISER = "merchandiser";
    public static final String TAG_ORDER_COURIER = "courier";

    public static final String TAG_ORDER_TYPE_NAME = "typename";
    public static final String TAG_ORDER_QUANTITY = "quantity";
    public static final String TAG_ORDER_SPOSOBIY_HRANENIE = "sposobiy_hranenie";
    public static final String TAG_ORDER_IMAGE_URL_1 = "image_1_url";
    public static final String TAG_ORDER_IMAGE_URL_2= "image_2_url";
    public static final String TAG_ORDER_IMAGE_URL3_ = "image_3_url";
    public static final String TAG_ORDER_IMAGE_URL_4 = "image_4_url";

    public static final String TAG_ORDER_PRICE = "price";
    public static final String TAG_ORDER = "status";

}
