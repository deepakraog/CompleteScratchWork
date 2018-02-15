package com.drg.api;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.drg.Model.ApiValidator;

/**
 * Servlet implementation class redirectServlet
 */
public class redirectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static Properties props = new Properties();
	public static LinkedHashMap<String, String> lhm = new LinkedHashMap<String, String>();

	/**
	 * Default constructor.
	 */
	public redirectServlet() {
		System.out.println("Inside default constr");
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		System.out.println("inside init stub");
		try {
			props.load(redirectServlet.class.getClassLoader().getResourceAsStream("config.properties"));

			lhm.put("zone", props.getProperty("zone"));

			lhm.put("service_id", props.getProperty("service_id"));
			lhm.put("methodReference", props.getProperty("methodReference"));
			lhm.put("methodToReplace", props.getProperty("methodToReplace"));
			lhm.put("ip", props.getProperty("ip"));
			lhm.put("port", props.getProperty("port"));
			lhm.put("urlD", props.getProperty("urlD"));

			lhm.put("param_serviceid", props.getProperty("param_serviceid"));
			lhm.put("param1Reference", props.getProperty("param1Reference"));
			lhm.put("param1ToReplace", props.getProperty("param1ToReplace"));
			lhm.put("new_ip", props.getProperty("new_ip"));
			lhm.put("new_port", props.getProperty("new_port"));
			lhm.put("new_urlD", props.getProperty("new_urlD"));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		String replacedURL = null, finalURL = null;
		if (lhm.get("zone").contains("east")) {
			replacedURL = ApiValidator.methodReplace(lhm, request.getQueryString());
			finalURL = ApiValidator.urlForm(lhm, replacedURL);
		} else if (lhm.get("zone").contains("south")) {
			replacedURL = ApiValidator.param1Replace(lhm, request.getQueryString());
			finalURL = ApiValidator.paramUrlForm(lhm, replacedURL);
		}
		if (finalURL != null) {
			try {
				response.sendRedirect(finalURL);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			doProcess(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			doProcess(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}