/**
 * パッケージ名：org.geocoderGQLService.repository
 * ファイル名  ：CustomRepository.java
 * 
 * @author mbasa
 * @since Mar 1, 2024
 */
package org.geocoderGQLService.repository;

import org.geocoderGQLService.bean.GeocoderResultBean;
import org.geocoderGQLService.bean.QueryResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

/**
 * 説明：
 *
 */
@Repository
public class CustomRepository {

    /**
     * コンストラクタ
     *
     */
    public CustomRepository() {
    }

    private final Logger logger = LoggerFactory.getLogger(CustomRepository.class);

    @Autowired
    private EntityManager entityManager;

    public GeocoderResultBean geocodeAddress(String inAddress) {
        try {
            Query q = entityManager.createNativeQuery(
                    "select * from geocoder(?1) a left join data.mesh4_pop b "
                            + "on st_contains(b.geom,st_point(a.x,a.y))",
                    QueryResultBean.class);
            q.setParameter(1, inAddress);

            QueryResultBean result = (QueryResultBean) q.getSingleResult();

            return result.getGeocoderResultBean();
        } catch (Exception e) {
            ;
        }

        return new GeocoderResultBean();
    }

    public GeocoderResultBean reverseGeocode(double mLon, double mLat) {
        try {

            Query q = entityManager.createNativeQuery("select *  from "
                    + "reverse_geocoder(cast(?1 as numeric),cast(?2 as numeric)) "
                    + "a left join data.mesh4_pop b on "
                    + "st_contains(b.geom,st_point(a.x,a.y))",
                    QueryResultBean.class);

            q.setParameter(1, mLon);
            q.setParameter(2, mLat);

            QueryResultBean qrb = (QueryResultBean) q.getSingleResult();

            return qrb.getGeocoderResultBean();
        } catch (Exception e) {
            ;
        }

        return new GeocoderResultBean();
    }
}
