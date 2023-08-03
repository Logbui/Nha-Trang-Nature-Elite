<%-- 
    Document   : userprofile
    Created on : Jun 14, 2023, 9:14:56 AM
    Author     : ADMIN
--%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="models.User_Account"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="shortcut icon" href="<c:url value="/assets/imgs/five-icon.png"/>" type="image/x-icon" sizes="30x30">
        <script src="https://kit.fontawesome.com/ef011c00e2.js" crossorigin="anonymous"></script>
        <link rel="stylesheet" href="https://pro.fontawesome.com/releases/v5.10.0/css/all.css">
        <title>JSP Page</title>
        
        <link href="<c:url value="/assets/css/UserProfile/styleindex.css"/>" rel="stylesheet" type="text/css">
        <link href="<c:url value="/assets/css/HomePageCSS/bootstrap1.css"/>" rel="stylesheet" type="text/css">
        
    </head>
    <body>
        <input type="hidden" id="user" value="${sessionScope.person.name}">
        <div class="container-profile">
            <div class="profile">
                <div class="row">
                    <div class="column-left">
                        <div class="wrapper-left">
                            <div class="info">
                                <div class="info-image">
                                    <a href="#">
                                        <img src="${sessionScope.person.getLinkImg()}" />
                                    </a>
                                </div>
                                <div class="info_details">
                                    <h5 class="user_name">${sessionScope.person.name}</h5>
                                    <small>${sessionScope.person.email}</small>
                                </div>
                            </div>
                            <hr>
                            <nav class="profile-link">
                                <ul>
                                    <li class="profile-link-collapse">
                                        <a href="#" class="profile-acct">
                                            <h6>Tài khoản</h6>
                                        </a>
                                        <div class="collapse-show">
                                            <ul class="profile-acct-opt">
                                                <li><a href="<c:url value="/tour/changePoint.do"/>">Thông tin cá nhân</a></li>
                                                <li><a href="<c:url value="/account/changePassword.do"/>">Đổi mật khẩu</a></li>
                                                <li><a href="#">Đăng xuất</a></li>
                                            </ul>
                                        </div>
                                    </li>
                                    <li class="profile-link-collapse2"><a href="<c:url value="/tour/bookedList.do?index=1"/>">Đơn đặt chỗ</a></li>
                                    <li class="profile-link-collapse3"><a href="#">Đánh giá của Quý khách</a></li>

                                </ul>
                            </nav>
                        </div>
                    </div>

                    <div class="column-right">
                        <div class="wrapper-right">
                            <div class="heading-right">
                                <h5>Thông tin cá nhân</h5>
                                <p>Cập nhật thông tin của Quý khách và tìm hiểu các thông tin này được sử dụng ra sao. </p>
                            </div>
                            <div class="provide-details">
                                <div class="section-details">
                                    <div class="section-item" >
                                        <div class="section-item-field">Họ và Tên</div>
                                        <div class="section-item-userinfo">${sessionScope.person.name}</div>
                                    </div>
                                    <div class="section-item">
                                        <div class="section-item-field">Điểm thưởng</div>
                                        <div class="section-item-userinfo">${sessionScope.person.accumulatedScore}</div>
                                    </div>
                                    <div class="section-item">
                                        <div class="section-item-field">Tổng số tour đã đi</div>
                                        <div class="section-item-userinfo">${sessionScope.person.totalTour}</div>
                                    </div>
                                    <div class="section-item">
                                        <div class="section-item-field">Địa chỉ email</div>
                                        <div class="section-item-userinfo">${sessionScope.person.email}</div>
                                    </div>
                                    <div class="section-item">
                                        <div class="section-item-field">Số điện thoại</div>
                                        <div class="section-item-userinfo">${sessionScope.person.phone}</div>
                                    </div>
                                    <div class="section-item">
                                        <div class="section-item-field">Địa chỉ</div>
                                        <div class="section-item-userinfo">${sessionScope.person.address}</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    <script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
    <link rel="stylesheet" href="alert/dist/sweetalert.css">
    <script type="text/javascript">    
        const user = document.getElementById("user").value;
        console.log(user);
        swal("Hello!!!", "Chào mừng "+ user  +" đến với Nha Trang Nature Elite", "success");
    </script>
    </body>
    
</html>