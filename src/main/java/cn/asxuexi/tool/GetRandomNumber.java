package cn.asxuexi.tool;

/**
 * @author John
 * @version v1
 * this class is used to get random number 
 * */
public class GetRandomNumber {
	
	/**
	 * @param int,if you want get a random five digits，
	 *        you can write 10000
	 * @return the return is the random number
	 * */
	public static int getRandomNumber(int range) {
		return (int)((Math.random()*9+1)*range);
	}
}
