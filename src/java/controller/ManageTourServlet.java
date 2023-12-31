/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import models.Destination;
import dal.DestinationDAO;
import dal.ImageDAO;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import java.util.List;

//Import thư viện DAO và DTO
import models.Tour;
import dal.TourDAO;
import models.TourItem;
import dal.TourItemDAO;
import dal.TripDAO;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpSession;
import models.Book;
import models.Image;
import models.ListBooked;
import models.Trip;
import models.User_Account;

/**
 *
 * @author thuyk
 */
@WebServlet(name = "Tour", urlPatterns = {"/tour"})
public class ManageTourServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Để có thể truyền dữ liệu lên Data để ghi dạng tiếng việt thì thêm 3 dòng 42 43 44 
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        String action = (String) request.getAttribute("action");

        switch (action) {
            //---------------XU LY VOI TOUR (CRUD)---------------
            case "homePage": {
                //Lấy danh sách tour
                getHomePage(request, response);
                break;
            }
            case "list": {
                getList(request, response);
                break;
            }
            case "detailTour": {
                // Nơi đây xử lý lấy chi tiết 1 tour
                detailTourById(request, response);
                break;
            }
            //---------------XU LY VOI DESTINATION (CRUD)---------------
            case "destinationList": {
                //Lấy danh sách tour 
                getDestinationList(request, response);
                break;
            }
            //---------------CÁC VIEW KHÁC---------------
            case "about": {
                request.getRequestDispatcher(Config.LAYOUT).forward(request, response);
                break;
            }
            case "contact": {
                request.getRequestDispatcher(Config.LAYOUT).forward(request, response);
                break;
            }
            case "sendEmail":
                sendEmail(request, response);
                break;
            case "sendEmailHandler":
                sendEmailHandler(request, response);
                break;
            case "changePoint":
                changePoint(request, response);
                break;
            //---------------XU LY VOI BOOKED LIST (CRUD)---------------
            case "booking":
                viewFormBooking(request, response);
                break;
            case "bookedList":
                bookedList(request, response);
                break;
            case "book":
                book(request, response);
                break;
            default: {
                request.getRequestDispatcher("/WEB-INF/view/error/error.jsp").forward(request, response);
                break;
            }
        }
    }

    //1.[READ] - Đọc danh sách tất cả Tour (Hash function)
    protected void getHomePage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=UTF-8");
            HttpSession session = request.getSession();
            
            //Khai báo biến
            TourDAO tourDAO = new TourDAO();
            //Thực hiện lấy danh sách
            Map<Integer, Tour> Maplist = tourDAO.getList();
            Map<Integer, Tour> MaplistRecent = tourDAO.getList();
            
            System.out.println("HOMEPAGE TEST");

            //Lưu danh sách vào Attribute
            request.setAttribute("listTour", Maplist);
            request.setAttribute("listTourRecent", MaplistRecent);
            System.out.println("Check suggestionList is valid: " + Config.isValidList);
            if (!Config.isValidList) {
                System.out.println("Create SearchList");
                String tourListJson = new Gson().toJson(Maplist);
                session.setAttribute("searchList", tourListJson);
                Config.setIsValidList(true);
            }
//
            request.getRequestDispatcher(Config.LAYOUT).forward(request, response);
        } catch (SQLException ex) {
            System.out.println("----------------EXCEPTION----------------");
            log("HomePage_SQLException: " + ex.getMessage());
        }
    }

    /*------------------------------------------------------------------------------
                            CAC FUNCTION XU LY TOUR (CRUD)
    ------------------------------------------------------------------------------*/
    //1.[READ] - Đọc danh sách tất cả Tour (Hash function)
    protected void getList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        System.out.println("----------------GETLIST----------------");
        //Tạo và lấy biến
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<Trip> tripList = null;
        TripDAO tripDAO = new TripDAO();
        String search = request.getParameter("search");
        if (search == null) {
            search = "";
        }
        String sort = (String) request.getParameter("sort_option");
        String indexPage = request.getParameter("index");
        if (sort == null) {
            sort = "normal";
        }
        if (indexPage == null) {
            indexPage = "1";
        }
        int index = Integer.parseInt(indexPage);
        String date = request.getParameter("date");
        //ĐIỀU KIỆN NGÀY KHỞI HÀNH VÀ KHÔNG CÓ NGÀY KHỞI HÀNH
        if (date == null) {
            date = "";
        }
        if (date.equals("")) {
            //Sort theo giá tiền
            if (sort.equals("asc")) {
                tripList = tripDAO.sortPriceAcending(search, index);
            } else if (sort.equals("desc")) {
                tripList = tripDAO.sortPriceDescending(search, index);
            } else if (sort.equals("normal")) {
                tripList = tripDAO.pagingStuff(search, index);
            }
            //Đếm tổng số trang cần có
            int count = tripDAO.countWithCondition(search);
            count = count / 6;
            if (count % 2 != 0) {
                count++;
            }
            request.setAttribute("date", date);
            request.setAttribute("listTrip", tripList);
            request.setAttribute("count", count);
            request.setAttribute("sort_option", sort);
            request.setAttribute("search", search);
            request.setAttribute("index", indexPage);
        } else {
            //Sort theo giá tiền có NGÀY KHỞI HÀNH
            if (sort.equals("asc")) {
                tripList = tripDAO.sortPriceAcendingDepart(date, search, index);
            } else if (sort.equals("desc")) {
                tripList = tripDAO.sortPriceDescendingDepart(date, search, index);
            } else if (sort.equals("normal")) {
                tripList = tripDAO.pagingStuffDepart(date, search, index);
            }
            //Đếm tổng số trang cần có
            int count = tripDAO.countWithConditionDepart(date, search);
            count = count / 6;
            if (count % 2 != 0) {
                count++;
            }
            request.setAttribute("date", date);
            request.setAttribute("listTrip", tripList);
            request.setAttribute("count", count);
            request.setAttribute("sort_option", sort);
            request.setAttribute("search", search);
            request.setAttribute("index", indexPage);
        }
        request.getRequestDispatcher(Config.LAYOUT).forward(request, response);
    }

    //2.[READ] - Đọc thông tin chi tiết 1 Tour by ID
    protected void detailTourById(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=UTF-8");

            System.out.println("----------------GET DETAIL TOUR----------------");
            int tourID = Integer.parseInt(request.getParameter("tourID"));
            int tripID = Integer.parseInt(request.getParameter("tripID"));
            TourItemDAO itemDAO = new TourItemDAO();
            ImageDAO imageDAO = new ImageDAO();
            TripDAO tripDAO = new TripDAO();
            if (tripID != 0) {
                Trip trip = tripDAO.getTrip_by_TripID_TourID(tourID, tripID);
                request.setAttribute("trip", trip);
            } else if (tripID == 0) {
                Trip trip = tripDAO.getTrip_by_TourID(tourID).get(0);
                request.setAttribute("trip", trip);
            }
            List<Trip> tripList = tripDAO.getTrip_by_TourID(tourID);
            Map<Integer, Image> Imagelist = imageDAO.getImage_by_TourItemID(tourID);
            Map<Integer, TourItem> Maplist = itemDAO.getListItem_by_TourItemID(tourID);

            for (Map.Entry<Integer, TourItem> x : Maplist.entrySet()) {
                System.out.print("ID: " + x.getValue().getId() + " | ");
                System.out.print("TOUR_ID: " + x.getValue().getTour_id() + " | ");
                System.out.print("DES_ID: " + x.getValue().getDestination_id() + " | ");
                System.out.print("DURATION: " + x.getValue().getDuration() + " | ");
                System.out.print("SCRIPT: " + x.getValue().getScript() + " | ");
                System.out.println("");
            }
            request.setAttribute("tripList", tripList);
            request.setAttribute("imageList", Imagelist);
            request.setAttribute("itemList", Maplist);
            request.setAttribute("tourID", tourID);
            request.getRequestDispatcher(Config.LAYOUT).forward(request, response);
        } catch (SQLException ex) {
            System.out.println("----------------EXCEPTION----------------");
            log("TourItem_SQLException: " + ex.getMessage());
        }
    }

    /*------------------------------------------------------------------------------
                        CAC FUNCTION XU LY DESTINATION (CRUD)
    ------------------------------------------------------------------------------*/
    //1.[READ] - Đọc danh sách tất cả Destination (Hash function)
    protected void getDestinationList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=UTF-8");
            DestinationDAO pf = new DestinationDAO();
            System.out.println("----------------GET DESTINATION LIST----------------");
            Map<Integer, Destination> Maplist = pf.getList();
            for (Map.Entry<Integer, Destination> x : Maplist.entrySet()) {
                System.out.println(x.getKey());
                System.out.println(x.getValue().getName());
            }
            request.setAttribute("destinationList", Maplist);
            System.out.println("----------------DESTINATION LIST----------------");
            request.getRequestDispatcher(Config.LAYOUT).forward(request, response);
        } catch (SQLException ex) {
            System.out.println("----------------EXCEPTION----------------");
            log("ListTourController_SQLException: " + ex.getMessage());
        }
    }

    /*------------------------------------------------------------------------------
                        CAC FUNCTION XU LY BOOKED LIST (CRUD)
    ------------------------------------------------------------------------------*/
    //1.[READ] - Đọc danh sách tất cả Booking
    protected void bookedList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Lấy tour từ tourDAO
        HttpSession session = request.getSession();
        User_Account user = (User_Account) session.getAttribute("person");
        
        String sort_option = request.getParameter("sort_option");
        String indexPage = request.getParameter("index");

        int index = Integer.parseInt(indexPage);
        //Lấy đối tượng tour
        TourDAO tourDAO = new TourDAO();
        List<ListBooked> list = null;

        if (sort_option == null || sort_option.isEmpty()) {
            list = tourDAO.select(user.getId(), index);
        } else if (sort_option.equalsIgnoreCase("month")) {
            list = tourDAO.sortPriceMonth(user.getId(), index);
        } else if (sort_option.equalsIgnoreCase("day")) {
            list = tourDAO.sortPriceDay(user.getId(), index);
        }
        for (ListBooked listBooked : list) {
            System.out.println("list = " + listBooked.toString());

        }
        //Đếm tổng số trang cần có
        int count = tourDAO.count(user.getId());
        count = count / 3;
        if (count % 2 != 0) {
            count++;
        }

        if (list != null) {
            request.setAttribute("count", count);
            request.setAttribute("sort_option", sort_option);
            request.setAttribute("index", index);
            request.setAttribute("list", list);
            System.out.println(list);
            request.getRequestDispatcher(Config.LAYOUT).forward(request, response);
        } else {
            request.setAttribute("error", "Something Wrong");
            request.getRequestDispatcher("/WEB-INF/view/error/error.jsp").forward(request, response);
        }
    }
    
    //2.[READ] - LẤY THÔNG TIN TRIP DISPLAY LÊN FORM BOOKING
    protected void viewFormBooking(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        List<Trip> list = new ArrayList<Trip>();

        System.out.println("----------------VIEW FORM BOOKING----------------");
        int tourID = Integer.parseInt(request.getParameter("tourID"));
        int tripID = Integer.parseInt(request.getParameter("tripID"));
        TripDAO tripDAO = new TripDAO();

        Trip trip = tripDAO.getTrip_by_TripID_TourID(tourID, tripID);
        list = tripDAO.getTrip_by_TourID(tourID);
        
        request.setAttribute("tripDate", list);
        request.setAttribute("tripInfo", trip);
        request.getRequestDispatcher(Config.LAYOUT).forward(request, response);
    }
    
    //3. [CREATE] - TẠO BOOKING
    protected void book(HttpServletRequest request, HttpServletResponse response) {
        try {
            String name = request.getParameter("Name");
            String email = request.getParameter("Email");
            String phone = request.getParameter("PhoneNumber");
            String adult = request.getParameter("AdultAmount"); //number
            String child = request.getParameter("ChildAmount"); //number
            String date = request.getParameter("StartDate"); //date
            String payment = request.getParameter("PaymentType");
            String additionfield = request.getParameter("AdditionField");
            String AdultPrice = request.getParameter("priceAdult");
            String ChildPrice = request.getParameter("priceChild");
            String requirement = request.getParameter("requirement");
            String trip = request.getParameter("tripID");
            boolean status = true;
            
            HttpSession session = request.getSession();
            User_Account user = (User_Account) session.getAttribute("person");

            int adultAmount = Integer.parseInt(adult);
            int childAmount = Integer.parseInt(child);
            int paymentID = Integer.parseInt(payment);
            int tripID = Integer.parseInt(trip);
            Book book;
            double totalPrice = Double.parseDouble(AdultPrice) * adultAmount + childAmount * Double.parseDouble(ChildPrice);
            TripDAO tripdao = new TripDAO();
            
            //Xử lí date
            Date temp = new Date();
            Date currentDay = new Date(temp.getTime()); // Lấy ra ngày hôm nay           
            Date expDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            expDate = dateFormat.parse(date); //format ngày quá hạn
            //Nếu ngày quá hạn đến trước ngày mua thì status = 0
            if(expDate.before(currentDay)) {
                status = false;
            } else {
                status = true;
            }
            System.out.println(user);
            if (user == null){
                book = new Book(totalPrice, additionfield, name, email, phone, date, status, paymentID, adultAmount, childAmount, tripID, requirement);
                System.out.println(book);
                tripdao.book_TripForGuest(book);
            } else {
                book = new Book(totalPrice, additionfield, name, email, phone, date, status, paymentID, user.getId(), adultAmount, childAmount, tripID, user.getAddress(), requirement);
                System.out.println(book);
                tripdao.book_Trip(book);
            }
//            System.out.println("Name: " + name + "email: " + email + "phone: " + phone + "adult: " + adult + "child: " + child + "date: " + date + "payment: " + payment + "addtionField: " + additionfield);
        
        } catch (ParseException ex) {
            Logger.getLogger(ManageTourServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    /*------------------------------------------------------------------------------
                        CAC FUNCTION XU LY VIEW KHÁC (CRUD)
    ------------------------------------------------------------------------------*/
    protected void sendEmail(HttpServletRequest request, HttpServletResponse response){
        try {
            request.getRequestDispatcher(Config.LAYOUT).forward(request, response);
        } catch (IOException | ServletException e) {

        }
    }
    
    protected void sendEmailHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String email = request.getParameter("email");
        String name = request.getParameter("name");
        String phone = request.getParameter("PhoneNumber");
        String subject = request.getParameter("subject");
        String content = request.getParameter("Content");
        
        
        
        final String username = "nhatrangnatureelite@gmail.com";//your email id
        final String password = "krgqhcpqhfpaspzr";// your password
        Properties props = new Properties();
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
           @Override
           protected PasswordAuthentication getPasswordAuthentication(){
               return new PasswordAuthentication(username, password);
           }
        });
        try{
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(username));
            MimeBodyPart textPart = new MimeBodyPart();
            Multipart multipart = new MimeMultipart();
            String final_Text = "Name: " + name + "    " + "Email: " + email + "    " + "Phone Number: " + phone + "    " + "Subject: " + subject + "    " + "Message: " + content;
            textPart.setText(final_Text);
            message.setSubject(subject);
            multipart.addBodyPart(textPart);
            message.setContent(multipart);
            message.setSubject("Contact Details");
            
            Transport.send(message);
        }catch(MessagingException e){
            
        }
        request.setAttribute("controller", "tour");
        request.setAttribute("action", "contact");
        request.setAttribute("status", "success");
        request.getRequestDispatcher(Config.LAYOUT).forward(request, response);
    }
    
    protected void changePoint(HttpServletRequest request, HttpServletResponse response){
        try {
            request.getRequestDispatcher(Config.LAYOUT).forward(request, response);
        } catch (IOException | ServletException e) {

        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
