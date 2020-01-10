package cn.asxuexi.tool;

import java.util.Date;
import java.util.Random;

public class RandomTool {
	/**
	 * 生成随机ID:毫秒+3位随机数
	 * 
	 * @author 张顺
	 * @param param 前缀
	 */

	public static String randomId(String param) {
		long time = new Date().getTime();
		int nextInt = new Random().nextInt(900) + 100;
		String Id = param + "_" + time + nextInt;
		return Id;
	}

	/**
	 * 生成随机码值，包含数字、大小写字母
	 * 
	 * @param number 位数
	 */
	public static String getRandomCode(int number) {
		String codeNum = "";
		int[] code = new int[3];
		Random random = new Random();
		for (int i = 0; i < number; i++) {
			int num = random.nextInt(10) + 48;
			int uppercase = random.nextInt(26) + 65;
			int lowercase = random.nextInt(26) + 97;
			code[0] = num;
			code[1] = uppercase;
			code[2] = lowercase;
			codeNum += (char) code[random.nextInt(3)];
		}
		return codeNum;
	}
	
	/**
	 * 返回一组int类型的随机数,随机数的长度由传入的参数决定.最长不能超过9位
	 * 
	 * @author fanjunguo
	 * @param n 随机数的长度,1表示1位,9表示9位.n的取值范围为1~9,如果大于9,按9计算.如果小于1,按1计算
	 * @return 得到的指定位数的随机数
	 */
	public static int getRandomInt(int n) {
		if (n>9) {
			n=9;
		}
		if (n<1) {
			n=1;
		}
		Random ran=new Random();
		int pow=(int)Math.pow(10, (n-1));
		int result=ran.nextInt(pow*9)+pow;
		return result;
	}
}
