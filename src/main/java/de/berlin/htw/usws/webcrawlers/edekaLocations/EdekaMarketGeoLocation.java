package de.berlin.htw.usws.webcrawlers.edekaLocations;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.berlin.htw.usws.model.SupermarketGEO;
import de.berlin.htw.usws.model.enums.Supermarket;
import lombok.Data;

@Data
public class EdekaMarketGeoLocation {

    @JsonProperty("geoLng_doubleField_d")
    private float geoLng_doubleField_d;

    @JsonProperty("urlExtern_tlc")
    private String urlExtern_tlc;

    @JsonProperty("telefon_tlc")
    private String telefon_tlc;

    @JsonProperty("geoLat_doubleField_d")
    private float geoLat_doubleField_d;

    @JsonProperty("marktID_tlc")
    private String marktID_tlc;

    @JsonProperty("strasse_tlc")
    private String strasse_tlc;

    @JsonProperty("ort_tlc")
    private String ort_tlc;

    @JsonProperty("name_tlc")
    private String name_tlc;

    @JsonProperty("plz_tlc")
    private String plz_tlc;

    //TODO find a way to separate house number from street in strasse_tlc
    public SupermarketGEO convertEdekaMarketGeoLocationToSupermarketGeo(){
        return new SupermarketGEO(Supermarket.EDEKA,
                this.name_tlc,
                this.geoLat_doubleField_d,
                this.geoLng_doubleField_d,
                this.strasse_tlc,
                "",
                this.ort_tlc,
                Integer.valueOf(this.plz_tlc) );
    }
}
