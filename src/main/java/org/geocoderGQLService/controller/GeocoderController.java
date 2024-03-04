/**
 * パッケージ名：org.geocoderGQLService.controller
 * ファイル名  ：GeocoderController.java
 * 
 * @author mbasa
 * @since Mar 4, 2024
 */
package org.geocoderGQLService.controller;

import org.geocoderGQLService.bean.GeocoderResultBean;
import org.geocoderGQLService.repository.CustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

/**
 * 説明：
 *
 */
@Controller
public class GeocoderController {

    @Autowired
    CustomRepository custRepository;

    /**
     * コンストラクタ
     *
     */
    public GeocoderController() {
    }

    @QueryMapping
    public GeocoderResultBean geocoder(@Argument String addr) {
        return custRepository.geocodeAddress(addr);
    }

    @QueryMapping
    public GeocoderResultBean reverseGeocoder(@Argument double x, @Argument double y) {
        return custRepository.reverseGeocode(x, y);
    }
}
