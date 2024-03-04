/**
 * パッケージ名：org.geocoderGQLService.bean
 * ファイル名  ：CoordinatesBean.java
 * 
 * @author mbasa
 * @since Mar 1, 2024
 */
package org.geocoderGQLService.bean;

/**
 * 説明：
 *
 */
public class CoordinatesBean {

    /**
     * コンストラクタ
     *
     */
    public CoordinatesBean() {
    }

    private double x;
    private double y;

    /**
     * @return x を取得する
     */
    public double getX() {
        return x;
    }

    /**
     * @param x x を設定する
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return y を取得する
     */
    public double getY() {
        return y;
    }

    /**
     * @param y y を設定する
     */
    public void setY(double y) {
        this.y = y;
    }

}
