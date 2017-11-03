package org.drg.workspace;

public class TestCode {

	public static void main(String[] args) {

		// String name = "deepak";
		String name = "rahulwagaji";
		System.out.println("Input String " + name);
		System.out.println("Out String " + original(name));
		System.out.println("Out String " + originalReversal(original(name)));
	}

	public static String original(String input1) {
		String temp = "";
		int x = 0;
		int len = input1.length();
		while (len > 0) {

			if (len % 2 == 0) {
				x = (len / 2) - 1;
				temp += input1.charAt(x);
				input1 = input1.substring(0, x) + input1.substring(x + 1, len);
				len = input1.length();

			} else {
				x = (len / 2);
				temp += input1.charAt(x);
				input1 = input1.substring(0, x) + input1.substring(x + 1, len);
				len = input1.length();

			}
		}

		return temp;

	}

	public static String originalReversal(String input1) {

		String s1 = "", s2 = "";
		int len = input1.length(), tempLen = input1.length();

		for (int i = 0; i < len; i++) {
			if (tempLen % 2 == 0) {
				s1 += input1.charAt(i);

				tempLen--;
			} else {
				s2 += input1.charAt(i);

				tempLen--;
			}
		}

		StringBuffer sb = new StringBuffer(s1);
		return sb.reverse() + s2;

	}

}
