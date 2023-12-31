<%-- 
    Document   : detailTour
    Created on : Jun 5, 2023, 1:23:32 AM
    Author     : thuyk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<fmt:setLocale value="vi_VN"/>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <!--Import FONT-->
        <link rel="shortcut icon" href="./assets/imgs/five-icon.png" type="image/x-icon" sizes="30x30">
        <link href="https://pro.fontawesome.com/releases/v5.10.0/css/all.css" rel="stylesheet" type="text/css">
        <link href="<c:url value="/assets/css/HomePageCSS/bootstrap1.css"/>" rel="stylesheet" type="text/css">
        <!--Import CSS-->
        <link href="<c:url value="/assets/css/DetailTour/styles.css"/>" rel="stylesheet" type="text/css">
        
        <!--Import JS Slider -->
        
        <title>Detail Tour</title>
    </head>

    <body>
        <div class="warpped">
            <div class="container">
                <div class="header"></div>
                <div class="content">
                    <h1 class="header-title" style="font-size: 40px; color: red">${trip.getName()}</h1>
                    <span
                        >"Đặt ngay tour du lịch để khám phá những điểm đến tuyệt vời và tạo
                        ra những kỷ niệm không thể quên."</span
                    >
                    <div class="layout">
                        <!-- Layout 1 -->
                        <div class="layout-item">
                            <video
                                class="layout-video"
                                src="https://firebasestorage.googleapis.com/v0/b/nha-trang-nature-elite.appspot.com/o/VINPEARL%20NHA%20TRANG%2FClip%20su%20kien%20Vinpeal%20Nha%20Trang%20-%20final%2045s.mp4?alt=media&token=b8c753a5-4640-4e0c-8b3e-0052798049c4&_gl=1*14gbuo5*_ga*MTU1OTk0NzM3OC4xNjgzNTA1MDE2*_ga_CW55HF8NVT*MTY4NjE4NTY5Ni40NS4xLjE2ODYxODU3MTUuMC4wLjA."
                                controls
                                ></video>
                        </div>
                        <c:if test="${imageList != null}">
                            <c:forEach var="image" items="${imageList}">
                                <div class="layout-item">
                                    <img class="layout-image" src="${image.getValue().getImgUrl()}">
                                </div>
                            </c:forEach>
                        </c:if>
                    </div>
<!--                    <div class="departTime">
                        <h2>Ngày bắt đầu đi</h2>
                    </div>
                    
                    CHỌN TRIP THEO NGÀY
                    <div class="selectOption">
                        <select>
                            <c:forEach var="tripItem" items="${tripList}">
                                <option value="<c:url value="/tour/detailTour.do?tourID=${tripItem.getTour_id()}&tripID=${tripItem.getId()}"/>">${tripItem.getDepart_time()}</option>
                            </c:forEach>
                        </select>
                    </div>-->
                    <div class="time-line">
                        <h2>Lịch trình di chuyển</h2>
                    </div>
                    
                    <!--HIỂN THỊ TOURITEM || TIMELINE-->
                    <ul class="destination">
                        <c:if test="${itemList != null}">
                            <c:forEach var="item" items="${itemList}" varStatus="counter">
                                <li class="destination-item">
                                    <p class="duration">
                                        <strong>${item.getValue().getDuration()}</strong>
                                    </p>
                                    <p class="detail">${item.getValue().getScript()}</p>
                                </li>
                            </c:forEach>
                        </c:if>
                    </ul>

                    <!-- Start Box Booking  -->
                    <div class="booking-tour">
                        <div class="container">
                            <h1>Đặt ngay <b class="title">${trip.getName()}</b></h1>
                            <div class="tourpackage-body">
                                <div class="tourpackage-body-detail">
                                    
                                    <!--HIỂN THỊ GIÁ CƠ BẢN CỦA TRIP-->
                                    <h3 class="tourpackage-body-detail-title">Giá áp dụng cho người lớn <fmt:formatNumber value="${trip.getPriceAdult()}" pattern="###,### VNĐ" /></h3>
                                    <form action="<c:url value="/tour/booking.do"/>">
                                        <input type="hidden" name="tourID" value="${tourID}"/>
                                        <input type="hidden" name="tripID" value="${trip.getId()}"/>
                                        <button type="submit" class="btn-booking-tour"">
                                            <i class="fa-solid fa-o"></i> CHỌN
                                        </button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!--End Box Booking-->

                    <!-- START MAP BOX -->
                    <div class="map-tour">
                        <div class="map-header">
                            <h1 class="map-title">Thông tin địa điểm</h1>
                        </div>
                        <div class="map-content">
                            <div class="map-location">
                                <iframe
                                    src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d8942.069828394564!2d109.23393489419314!3d12.219054958045032!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x3170677575316031%3A0xd2b5f697e03e7b37!2sVinpearl%20nha%20trang!5e0!3m2!1svi!2s!4v1685178779540!5m2!1svi!2s"
                                ></iframe>
                            </div>
                        </div>
                    </div>
                    <!-- END MAP BOX -->
                </div>
                <!-- Start Booking Tour -->
                <!--                <div class="box-hotline">
                                    <div class="box-content">
                                        <span class="price-origin">800.000đ</span>
                                        <h1 class="price-discount">700.000đ /người lớn</h1>
                                        <h1 class="price">500.000đ /trẻ em</h1>
                                        <span class="note">
                                            Giá trên là giá Tour trọn gói, không bao gồm chi phí cá nhân phát
                                            sinh như (ăn, chơi riêng).
                                        </span>
                                    </div>
                                    <div class="btn">
                                        <button class="contact_btn">Liên hệ tư vấn</button>
                                        <button class="book_btn">
                                            <a style="text-decoration: none; color: black" href="<c:url value="/view/booking.jsp"/>" alt="Booking Tour">Đặt Tour</a>
                                        </button>
                                    </div>
                                </div>-->
                <!-- End Booking Tour -->
            </div>
        </div>
        <script src="<c:url value="/assets/js/DetailTour/detailTour.js"/>" type="text/javascript"></script>
    </body>
</html>
