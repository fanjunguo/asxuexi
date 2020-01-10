package cn.asxuexi.tool;

public class GeoTool {
	private static Double EARTH_RADIUS = 6371.0;// 地球半径平均值，单位千米

	/**
	 * 角度转化为弧度
	 * 
	 * @param degrees
	 *            角度
	 * @return 弧度
	 */
	public static Double degreesToRadians(Double degrees) {
		return degrees * Math.PI / 180;
	}

	/**
	 * 半正矢公式计算两个经纬度点的距离
	 * 
	 * @param geoPoint
	 *            经纬度点字符串，形式："lat,lon"
	 * @param anotherGeoPoint
	 *            经纬度点字符串，形式："lat,lon"
	 * @return 距离，单位千米
	 */
	public static Double getDistance(String geoPoint, String anotherGeoPoint) {
		String[] geoPointArray = geoPoint.split(",");
		String[] anotherGeoPointArray = anotherGeoPoint.split(",");
		Double radLat1 = degreesToRadians(Double.valueOf(geoPointArray[0]));
		Double radLon1 = degreesToRadians(Double.valueOf(geoPointArray[1]));
		Double radLat2 = degreesToRadians(Double.valueOf(anotherGeoPointArray[0]));
		Double radLon2 = degreesToRadians(Double.valueOf(anotherGeoPointArray[1]));
		Double distance = 2 * EARTH_RADIUS * Math.asin(Math.sqrt(Math.pow(Math.sin((radLat2 - radLat1) / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin((radLon2 - radLon1) / 2), 2)));
		return distance;
	}
}
