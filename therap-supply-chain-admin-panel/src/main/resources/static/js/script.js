function authorityFormValidator() {
    let authorityName = document.getElementById("authorityName").value;
    let authorityContact = document.getElementById("contact").value;
    let authorityAge = document.getElementById("age").value;
    let authorityAddress = document.getElementById("address").value;

    // name validation
    if (authorityName.length < 3 && authorityName.length > 100) {
        document.getElementById("nameMessage").innerHTML = "**authority's name must be min of 3 to 100 character...";
        return false;
    }

    // contact validation
    let reg = /^(\+)?(88)?01[0-9]{9}$/;
    if (!reg.exec(authorityContact)) {
        document.getElementById("contactMessage").innerHTML = "**invalid contact format...";
        return false;
    }

    // age validation
    if (authorityAge < 18) {
        document.getElementById("ageMessage").innerHTML = "blood donors age must be minimum 18 years";
        return false;
    }

    if (authorityAge > 60) {
        document.getElementById("ageMessage").innerHTML = "blood donors age must be maximum 60 years";

        return false;
    }

    // address validation
    if (authorityAddress.length < 3 && authorityAddress.length > 100) {
        document.getElementById("addressMessage").innerHTML = "**authority's address must be min of 3 to 100 character...";
        return false;
    }

}

// auth password form validation
function authorityPasswordFormValidator() {
    let password = document.getElementById("authorityPassword").value;
    let confirmPassword = document.getElementById("authorityConfirmPassword").value;


    // password validation
    if (password === "") {
        document.getElementById("passwordMessage").innerHTML = "**please fill password...";
        return false;
    }
    if (password.length < 5) {
        document.getElementById("passwordMessage").innerHTML = "**password length must be " +
            "greater then 5 character...";
        return false;
    }

    if (password.length > 20) {
        document.getElementById("passwordMessage").innerHTML = "**password length must be " +
            "smaller then 20 character...";
        return false;
    }

    if (password !== confirmPassword) {
        document.getElementById("passwordMessage").innerHTML = "**Do not match your password...";
        return false;
    }

}

function registrationAuthorityIsConfirmed() {
    let confirmAction = confirm("Are you want to Registration?");
    if (confirmAction) {
        alert("Registration Successfully...");
        return true;
    } else {
        alert("Registration Canceled!!");
        return false;
    }
}

function addProductIsConfirmed() {
    let confirmAction = confirm("Are you want to Add Product?");
    if (confirmAction) {
        alert("Product Add Successfully...");
        return true;
    } else {
        alert("Product Add Canceled!!!");
        return false;
    }
}

function approveIsConfirmed() {
    let confirmAction = confirm("Are you want to Approve Dealer?");
    if (confirmAction) {
        alert("Dealer Approve Successfully...");
        return true;
    } else {
        alert("Dealer Approve Canceled!!!");
        return false;
    }
}

function CancelIsConfirmed() {
    let confirmAction = confirm("Are you want to Cancel Dealer?");
    if (confirmAction) {
        alert("Dealer Cancelled Successfully...");
        return true;
    } else {
        alert("Dealer Cancel Not Now!!!");
        return false;
    }
}