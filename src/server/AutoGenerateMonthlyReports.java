package server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.TimerTask;

/**
 *
 *  daemon class, runs on the server and generates monthly reports if needed
 *
 * <p> Project Name: Zer-Li (Java Application Flower Store) </p>
 *
 * @author Habib Ibrahim, Vitaly Rofman, Ibrahim Daoud, Yosif Hosen
 * @version  V1.00  2022
 */

public class AutoGenerateMonthlyReports extends TimerTask {

    @Override
    public void run() {//thread start running
        Connection dbConn = SqlConnector.getConnection();
/*caculates the relevant date of the month so it will generate the relevant reports*/
        LocalDate nowDate = LocalDate.now();
        int year = nowDate.getYear();
        int month = 0;
        //check if its the beginning of the month and caculate the relevant month
        if(nowDate.getDayOfMonth()==1){//started new month!!!!!!!!!!!!!!!!!!!!!!!!1
            if(nowDate.getMonthValue()==1){
                month = 12;
            }else {
                month = nowDate.getMonthValue()-1;
            }

            try{
                /*check if reports been generated for that month*/
                String SQLmonth = "SELECT reportdetails FROM reports WHERE  month(month) ="+month+";";
                ResultSet rs4 = dbConn.createStatement().executeQuery(SQLmonth);
                rs4.beforeFirst();
                while(rs4.next()){
                    if(month==rs4.getInt("month"));
                    return;
                }
                //get the names of the shops from DB
                String SQL1 = "select shop from shopmanager;";
                ResultSet rs = dbConn.createStatement().executeQuery(SQL1);
                rs.beforeFirst();
                while(rs.next()){
                    //generate orders report from DB
                String SQL = "SELECT shop,orderNumber,price,dOrder,deliveryDate,orderDate,status,confirmed FROM orders WHERE orderDate between'"+year+"/"+month+"/1' AND '"+year+"/"+month+"/30' AND shop ='"+rs.getString("shop")+"';";
                    ResultSet rs1 = dbConn.createStatement().executeQuery(SQL);
                    rs1.beforeFirst();
                    String orderReport = "";
                    int count = 0;

                    while(rs1.next()){
                        orderReport =orderReport+ "\norder number: " + rs1.getString("orderNumber")
                                +"\norder details:"+rs1.getString("dOrder");
                        count++;
                    }
                    orderReport=orderReport+"\nNumber of orders in this month "+count+"";
                    //insert orders report to DB
                        String SQLin = "INSERT INTO reports Values (NULL,'order report','"+year+"/"+month+"/1','"+rs.getString("shop")+"','"+orderReport+"');";
                        dbConn.createStatement().execute(SQLin);
                    //generate income report
                    String SQL2 = "SELECT  orderNumber,price FROM orders o WHERE o.shop='"+rs.getString("shop")+"' AND orderDate between'"+year+"/"+month+"/1' AND '"+year+"/"+month+"/30' And o.confirmed = 'yes' and o.status != 'cancelled';";
                    ResultSet rs2 = dbConn.createStatement().executeQuery(SQL2);
                    rs2.beforeFirst();
                        double overallIncome = 0;
                        String incomeReport="";
                    while(rs2.next()){
                        incomeReport =incomeReport+ "Order number : " + rs2.getInt("orderNumber") + "\n" + "Order price : "
                                + rs2.getDouble("price") + "\n";

                        overallIncome= overallIncome + rs2.getDouble("price");
                    }
                    //caculate income and inserting the income report to DB
                    incomeReport = incomeReport + "Overall income:"+overallIncome;
                    String SQLin1 = "INSERT INTO reports Values (NULL,'income report','"+year+"/"+month+"/1','"+rs.getString("shop")+"','"+incomeReport+"');";
                    dbConn.createStatement().execute(SQLin1);
                }

            }catch(SQLException e){

            }

        }

    }
}
