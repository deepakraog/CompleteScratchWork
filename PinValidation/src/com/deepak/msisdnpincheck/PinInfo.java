package com.deepak.msisdnpincheck;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;

public class PinInfo extends HttpServlet
{
  private static final long serialVersionUID = 1L;
  public static Logger logger;
  public static Properties props = new Properties();
  public static BufferedWriter bs;
  public static SimpleDateFormat sdf;

  public PinInfo()
  {
    System.out.println("inside constr of CS");
  }

  public void init(ServletConfig config)
    throws ServletException
  {
    System.out.println("inside init() of CS");
    try
    {
      logger = Logger.getLogger(PinInfo.class.getName());
      props.load(PinInfo.class.getClassLoader().getResourceAsStream("config.properties"));
      Model.loadDB(props.getProperty("SELECT"));
      logger.info("Logger Name: " + logger.getName());
      System.out.println("loaded the Model.loadDB()\n");
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void destroy()
  {
    try
    {
      bs.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  protected void process(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException, SQLException
  {
    System.out.println("inside process() of CS");
    String uri = request.getRequestURI();
    Model model = new Model();
    PrintWriter out = response.getWriter();

    if (uri.contains("/openInputView.do")) {
      System.out.println("inside /openInputView of CS");
      RequestDispatcher rd = request.getRequestDispatcher("Input.jsp");
      rd.forward(request, response);
    }

    if (uri.contains("/register.do")) {
      System.out.println("Inside register.do");
      String pin = request.getParameter("pin");
      String check = props.getProperty("repeat_case_check");

      String result = model.validatefetchedData(pin, check);
      out.write(result);
      logger.info("Validation check is done");
      Date date = Calendar.getInstance().getTime();
      bs.write(sdf.format(date) + "|msisdn: " + request.getParameter("msisdn") + "|imei: " + 
        request.getParameter("pin") + "|status: " + result + "\n");
    }

    if (uri.contains("/update.do")) {
      System.out.println("Inside update.do");
      String pin = request.getParameter("pin");
      String action = request.getParameter("action");
      String update = props.getProperty("UPDATE");
      String select = props.getProperty("SELECT");
      String result = model.updatefetchedData(pin, update, select, action);
      out.write(result);
      logger.info("updation of pin is done");
      Date date = Calendar.getInstance().getTime();
      bs.write(sdf.format(date) + "|msisdn: " + request.getParameter("msisdn") + "|imei: " + 
        request.getParameter("pin") + "|dbupdate_status: " + result + "\n");
    }
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    System.out.println("inside doGet() of CS");
    try {
      sdf = new SimpleDateFormat("MMM d HH:mm:ss yyyy");
      Date dateF = new Date();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      File file = new File(props.getProperty("OUTDIR") + props.getProperty("fileName") + "_" + 
        dateFormat.format(dateF) + ".log");

      bs = new BufferedWriter(new FileWriter(file, true));
      if (FileUtils.readFileToString(file).trim().isEmpty()) {
        bs.write("Timestamp|number|pin|response(success/USED_PIN/failure)\n");
      }
      process(request, response);
    }
    catch (Exception e) {
      e.printStackTrace();
      try
      {
        if (bs != null)
          bs.close();
      }
      catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    finally
    {
      try
      {
        if (bs != null)
          bs.close();
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    System.out.println("inside doPost() of CS");
    try {
      process(request, response);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}