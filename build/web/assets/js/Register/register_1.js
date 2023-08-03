var changetype = document.querySelector('.changetype1');
var changetype2 = document.querySelector('.changetype2');
var changebutton = document.getElementById("password1");
var changebutton2 = document.getElementById("re_password1");
console.log(changebutton);
console.log(changebutton2);
changebutton.onclick = function () {
    if (changetype.getAttribute("type") === "text") {
        changetype.setAttribute("type", "password");
        changebutton.innerHTML = "<i class='fa-solid fa-eye'></i>";

    } else {
        changetype.setAttribute("type", "text");
        changebutton.innerHTML = "<i class='fa-solid fa-eye-slash'></i>";
    }
};
changebutton2.onclick = function () {
    if (changetype2.getAttribute("type") === "text") {
        changetype2.setAttribute("type", "password");
        changebutton2.innerHTML = "<i class='fa-solid fa-eye'></i>";

    } else {
        changetype2.setAttribute("type", "text");
        changebutton2.innerHTML = "<i class='fa-solid fa-eye-slash'></i>";
    }
};

