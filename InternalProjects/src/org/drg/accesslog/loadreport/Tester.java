/**
 * 
 */
package org.drg.accesslog.loadreport;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author deepak.gaikwad
 *
 */
public class Tester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		List<Integer> ll = new LinkedList<Integer>();

		LinkedHashMap<String, List<Integer>> lhm = new LinkedHashMap<String, List<Integer>>();

		ll.add(4);
		ll.add(2);
		ll.add(7);
		ll.add(33);
		ll.add(21);
		ll.add(65);
		ll.add(97);
		ll.add(1);

		Collections.sort(ll);
		System.out.println(lhm);

	}

}
