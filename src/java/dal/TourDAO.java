/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dal;

import models.Tour;
import models.ListBooked;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thuyk
 */
public class TourDAO extends DBContext {

    List<ListBooked> list = null;

    //Lấy danh sách tour với điều kiện availability = true và 6 tour mới nhất
//    public Map<Integer, Tour> getListRecent() throws SQLException {
//        DBContext db = new DBContext();
//        String sql = "SELECT DISTINCT trip.id, trip.name, trip.priceAdult, trip.priceChild, trip.thumbnail, trip.location FROM ( \n"
//                + "	  SELECT a.id, a.name, b.priceAdult, b.priceChild, a.thumbnail, a.location, b.depart_time \n"
//                + "	  FROM [dbo].[Tour] as a, [dbo].[Trip] AS b \n"
//                + "	  WHERE a.id = b.tour_id AND b.availability = 1 \n"
//                + "  ) as trip";
//        PreparedStatement ps = db.connection.prepareStatement(sql);
//        ResultSet rs = ps.executeQuery();
//        Map<Integer, Tour> maps = new HashMap<>();
//        while (rs.next()) {
//            int id = rs.getInt("id");
//            String name = rs.getString("name");
//            float priceAdult = rs.getFloat("priceAdult");
//            float priceChild = rs.getFloat("priceChild");
//            String thumbnail = rs.getString("thumbnail");
//            String location = rs.getString("location");
//            Tour tour = new Tour(id, name, priceAdult, priceChild, thumbnail, location);
//            maps.put(id, tour);
//            System.out.println(tour);
//        }
//        return maps;
//    }

    //Lấy danh sách tour với điều kiện availability = true
    public Map<Integer, Tour> getList() throws SQLException {
        DBContext db = new DBContext();
        String sql = "SELECT DISTINCT a.id, a.name, b.priceAdult, b.priceChild, a.thumbnail, a.location\n"
                + "  FROM [dbo].[Tour] as a, [dbo].[Trip] AS b\n"
                + "  WHERE a.id = b.tour_id AND b.availability = 1";
        PreparedStatement ps = db.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        Map<Integer, Tour> maps = new HashMap<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            float priceAdult = rs.getFloat("priceAdult");
            float priceChild = rs.getFloat("priceChild");
            String thumbnail = rs.getString("thumbnail");
            String location = rs.getString("location");
            Tour tour = new Tour(id, name, priceAdult, priceChild, thumbnail, location);
            maps.put(id, tour);
        }
        return maps;
    }
    
    //Lấy danh sách tour với điều kiện availability = true và phân trang và search
    public List<Tour> getListTourPaging(int index, String search) throws SQLException {
        DBContext db = new DBContext();
        String sql = "SELECT DISTINCT a.id, a.name, b.priceAdult, b.priceChild, a.thumbnail, a.location\n"
                + " FROM [dbo].[Tour] as a, [dbo].[Trip] AS b\n"
                + " WHERE a.id = b.tour_id AND b.availability = 1 AND a.name LIKE ? \n"
                + " ORDER BY a.id OFFSET ? ROWS FETCH NEXT 6 ROWS ONLY";
        PreparedStatement ps = db.connection.prepareStatement(sql);
        ps.setString(1, "%" + search + "%");
        ps.setInt(2, (index - 1) * 6);
        ResultSet rs = ps.executeQuery();
        List<Tour> listTour = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            float priceAdult = rs.getFloat("priceAdult");
            float priceChild = rs.getFloat("priceChild");
            String thumbnail = rs.getString("thumbnail");
            String location = rs.getString("location");
            Tour tour = new Tour(id, name, priceAdult, priceChild, thumbnail, location);
            listTour.add(tour);
        }
        return listTour;
    }

    //Lấy danh sách Booking by ID
    public List<ListBooked> select(int accountID, int index) {
        //Tạo connection để kết nối vào DBMS
        try {
            String sql = "SELECT A.depart_time ,b.name, B.thumbnail, C.totalPrice, B.id as 'Tour ID', A.id 'Trip ID', C.quantityAdult, C.quantityChild, C.status\n"
                    + "  FROM TRIP A JOIN Tour B ON A.tour_id = B.id \n"
                    + "  JOIN BOOKING C ON C.trip_id = A.id \n"
                    + "  JOIN ACCOUNT D ON D.id = C.account_id \n"
                    + "  WHERE C.account_id = ? \n"
                    + "  ORDER BY A.id OFFSET ? ROWS FETCH NEXT 3 ROWS ONLY";
            PreparedStatement ps = connection.prepareStatement(sql);
            
            ps.setInt(1, accountID);
            ps.setInt(2, (index - 1) * 3);

            ResultSet rs = ps.executeQuery();
            list = new ArrayList<>();

            while (rs.next()) {
                ListBooked product = new ListBooked();
                product.setDeparttime(rs.getDate("depart_time"));
                product.setQuantityAdult(rs.getInt("quantityAdult"));
                product.setQuantityChild(rs.getInt("quantityChild"));
                product.setName(rs.getString("name"));
                product.setTotalPrice(rs.getDouble("totalPrice"));
                product.setTour_id(rs.getInt("Tour ID"));
                product.setTrip_id(rs.getInt("Trip ID"));
                product.setThumbnail(rs.getString("thumbnail"));
                product.setStatus(rs.getBoolean("status"));
                list.add(product);
            }
            rs.close();
            ps.close();
            return list;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    public List<ListBooked> sortPriceMonth(int accountID, int index) {
        try {
            String sql = "SELECT A.depart_time,b.name, B.thumbnail, C.totalPrice, B.id as 'Tour ID', A.id 'Trip ID', C.quantityAdult, C.quantityChild, C.status FROM TRIP A JOIN Tour B ON A.tour_id = B.id JOIN BOOKING C ON C.trip_id = A.id JOIN ACCOUNT D ON D.id = C.account_id WHERE C.account_id = ? ORDER BY MONTH(A.depart_time) DESC OFFSET ? ROWS FETCH NEXT 3 ROWS ONLY";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, accountID);
            ps.setInt(2, (index - 1) * 3);

            ResultSet rs = ps.executeQuery();
            list = new ArrayList<>();

            while (rs.next()) {
                ListBooked product = new ListBooked();
                product.setDeparttime(rs.getDate("depart_time"));
                product.setQuantityAdult(rs.getInt("quantityAdult"));
                product.setQuantityChild(rs.getInt("quantityChild"));
                product.setName(rs.getString("name"));
                product.setTotalPrice(rs.getDouble("totalPrice"));
                product.setTour_id(rs.getInt("Tour ID"));
                product.setTrip_id(rs.getInt("Trip ID"));
                product.setThumbnail(rs.getString("thumbnail"));
                product.setStatus(rs.getBoolean("status"));
                list.add(product);
            }
            rs.close();
            ps.close();
            return list;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    public List<ListBooked> sortPriceDay(int accountID, int index) {
        try {
            String sql = "SELECT A.depart_time,b.name, B.thumbnail, C.totalPrice, B.id as 'Tour ID', A.id 'Trip ID', C.quantityAdult, C.quantityChild, C.status FROM TRIP A JOIN Tour B ON A.tour_id = B.id JOIN BOOKING C ON C.trip_id = A.id JOIN ACCOUNT D ON D.id = C.account_id WHERE C.account_id = ? ORDER BY DAY(A.depart_time) DESC OFFSET ? ROWS FETCH NEXT 3 ROWS ONLY";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, accountID);
            ps.setInt(2, (index - 1) * 3);

            ResultSet rs = ps.executeQuery();
            list = new ArrayList<>();

            while (rs.next()) {
                ListBooked product = new ListBooked();
                product.setDeparttime(rs.getDate("depart_time"));
                product.setQuantityAdult(rs.getInt("quantityAdult"));
                product.setQuantityChild(rs.getInt("quantityChild"));
                product.setName(rs.getString("name"));
                product.setTotalPrice(rs.getDouble("totalPrice"));
                product.setTour_id(rs.getInt("Tour ID"));
                product.setTrip_id(rs.getInt("Trip ID"));
                product.setThumbnail(rs.getString("thumbnail"));
                product.setStatus(rs.getBoolean("status"));
                list.add(product);
            }
            rs.close();
            ps.close();
            return list;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    public int count(int accountID) {
        try {
            //Tạo connection để kết nối vào DBMS

            //Tạo đối tượng statement
            PreparedStatement stm = connection.prepareStatement("select count(*) from TRIP A JOIN Tour B ON A.tour_id = B.id JOIN BOOKING C ON C.trip_id = A.id JOIN ACCOUNT D ON D.id = C.account_id WHERE C.account_id = ? ");
            stm.setInt(1, accountID);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {

        }
        return 0;
    }
    
//    public static void main(String[] args) {
//        try {
//            TourDAO dao = new TourDAO();
//            Map<Integer, Tour> Maplist = dao.getListTourPaging(1,"");
//            for (Map.Entry<Integer, Tour> x : Maplist.entrySet()) {
//                System.out.print("ID: " + x.getValue().getId() + " | ");
//                System.out.print("TOUR_ID: " + x.getValue().getName() + " | ");
//                System.out.println("");
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(TourDAO.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
}
