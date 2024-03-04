/**
 * パッケージ名：org.geocoderGQLService.repository
 * ファイル名  ：CustomRepository.java
 * 
 * @author mbasa
 * @since Mar 1, 2024
 */
package org.geocoderGQLService.repository;

import java.util.ArrayList;
import java.util.List;

import org.geocoderGQLService.bean.GeocoderResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

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
            Object[] result = (Object[]) entityManager.createNativeQuery("select * from geocoder(?1)")
                    .setParameter(1, inAddress)
                    .getSingleResult();

            logger.debug("Result Code:" + result[0]);

            return objToGrb(result);
        } catch (Exception e) {
            ;
        }

        return new GeocoderResultBean();
    }

    public GeocoderResultBean reverseGeocode(double mLon, double mLat) {
        String sql = "select * from reverse_geocoder("
                + "cast(?1 as numeric),cast(?2 as numeric) )";

        try {

            Object[] result = (Object[]) entityManager.createNativeQuery(sql)
                    .setParameter(1, mLon)
                    .setParameter(2, mLat)
                    .getSingleResult();

            logger.debug("Result Code:" + result[0]);

            return objToGrb(result);
        } catch (Exception e) {
            ;
        }

        return new GeocoderResultBean();
    }

    public GeocoderResultBean reverseGeocode(double mLon,
            double mLat, double mDist, boolean useAddr, boolean details,
            String category, String owner) {

        String sql = "select * from reverse_geocoder("
                + "cast(?1 as numeric),cast(?2 as numeric),cast(?3 as numeric),"
                + "cast(?4 as bool),cast(?5 as text),cast(?6 as text) )";

        if (details) {
            sql = "select a.*,cast(details as text) details "
                    + "from reverse_geocoder("
                    + "cast(?1 as numeric),cast(?2 as numeric),cast(?3 as numeric),"
                    + "cast(?4 as bool),cast(?5 as text),cast(?6 as text)"
                    + ") a, places b "
                    + "where a.address = b.name";
        }

        try {

            Object[] result = (Object[]) entityManager.createNativeQuery(sql)
                    .setParameter(1, mLon)
                    .setParameter(2, mLat)
                    .setParameter(3, mDist)
                    .setParameter(4, useAddr)
                    .setParameter(5, category)
                    .setParameter(6, owner)
                    .getSingleResult();

            logger.debug("Result Code:" + result[0]);

            return objToGrb(result);
        } catch (Exception e) {
            ;
        }

        return new GeocoderResultBean();
    }

    public List<GeocoderResultBean> reverseGeocodeArr(double mLon,
            double mLat, double mDist, boolean useAddr, boolean details,
            String category, String owner, int limit) {

        List<GeocoderResultBean> retVal = new ArrayList<GeocoderResultBean>();

        String sql = "select * from reverse_geocoder("
                + "cast(?1 as numeric),cast(?2 as numeric),cast(?3 as numeric),"
                + "cast(?4 as bool),cast(?5 as text),cast(?6 as text),"
                + "cast(?7 as numeric) )";

        if (details) {
            sql = "select a.*,cast(details as text) details "
                    + "from reverse_geocoder("
                    + "cast(?1 as numeric),cast(?2 as numeric),cast(?3 as numeric),"
                    + "cast(?4 as bool),cast(?5 as text),cast(?6 as text),"
                    + "cast(?7 as numeric) ) a, places b "
                    + "where a.address = b.name";
        }

        List<?> result = (List<?>) entityManager.createNativeQuery(sql)
                .setParameter(1, mLon)
                .setParameter(2, mLat)
                .setParameter(3, mDist)
                .setParameter(4, useAddr)
                .setParameter(5, category)
                .setParameter(6, owner)
                .setParameter(7, limit)
                .getResultList();

        for (int i = 0; i < result.size(); i++) {
            GeocoderResultBean gcb = objToGrb((Object[]) result.get(i));
            retVal.add(gcb);
        }

        return retVal;
    }

    private GeocoderResultBean objToGrb(Object[] result) {

        GeocoderResultBean gcb = new GeocoderResultBean();

        if (result[0] != null)
            gcb.setCode((int) result[0]);
        if (result[1] != null)
            gcb.setX((double) result[1]);
        if (result[2] != null)
            gcb.setY((double) result[2]);
        if (result[3] != null)
            gcb.setAddress((String) result[3]);
        if (result[4] != null)
            gcb.setTodofuken((String) result[4]);
        if (result[5] != null)
            gcb.setShikuchoson((String) result[5]);
        if (result[6] != null)
            gcb.setOoaza((String) result[6]);
        if (result[7] != null)
            gcb.setChiban((String) result[7]);
        if (result[8] != null)
            gcb.setGo((String) result[8]);

        if (result.length > 9 && result[9] != null) {
            gcb.setDetails((String) result[9]);
        }

        return gcb;
    }

    public Object listTodofuken() {

        String sql = "with tt as (select todofuken,lat,lon from "
                + "address_t order by id) "
                + "select cast(array_to_json(array_agg(tt)) as text) from tt";

        Object retVal = entityManager.createNativeQuery(sql).getSingleResult();

        return retVal;
    }

    public Object listShikuchoson(String todofuken) {
        String sql = "select cast(array_to_json("
                + "array_agg(address_s order by shikuchoson)) as text) "
                + "from address_s where todofuken = ?";

        Object retVal = entityManager.createNativeQuery(sql)
                .setParameter(1, todofuken)
                .getSingleResult();

        return retVal;
    }

    public Object listOoaza(String todofuken, String shikuchoson) {
        String sql = "with tt as (select todofuken,shikuchoson,ooaza,lat,lon "
                + "from address_o where todofuken = ? and shikuchoson = ? "
                + "order by ooaza) select cast(array_to_json(array_agg(tt)) "
                + "as text) from tt;";

        Object retVal = entityManager.createNativeQuery(sql)
                .setParameter(1, todofuken)
                .setParameter(2, shikuchoson)
                .getSingleResult();

        return retVal;
    }

    public Object listBanchi(String todofuken,
            String shikuchoson, String ooaza) {

        String sql = "with tt as (select todofuken,shikuchoson,"
                + "ooaza,chiban as banchi, lat,lon "
                + "from address where todofuken = ? and shikuchoson = ? "
                + "and ooaza = ? order by cast(chiban as numeric)) "
                + "select cast(array_to_json(array_agg(tt)) "
                + "as text) from tt;";

        Object retVal = entityManager.createNativeQuery(sql)
                .setParameter(1, todofuken)
                .setParameter(2, shikuchoson)
                .setParameter(3, ooaza)
                .getSingleResult();

        return retVal;
    }
}
