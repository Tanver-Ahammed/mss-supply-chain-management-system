function dealerFormValidator() {
    let dealerName = document.getElementById("dealerName").value;
    let dealerContact = document.getElementById("dealerContact").value;
    let dealerAge = document.getElementById("dealerAge").value;
    let password = document.getElementById("dealerPassword").value;
    let confirmPassword = document.getElementById("dealerConfirmPassword").value;

    // name validation
    if (dealerName.length < 3 && dealerName.length > 100) {
        document.getElementById("nameMessage").innerHTML = "**dealer's name must be min of 3 to 100 character...";
        return false;
    }

    // contact validation
    let reg = /^(\+)?(88)?01[0-9]{9}$/;
    if (!reg.exec(dealerContact)) {
        document.getElementById("contactMessage").innerHTML = "**invalid contact format...";
        console.log("Tanver")
        return false;
    }

    // age validation
    if (dealerAge < 18) {
        document.getElementById("ageMessage").innerHTML = "blood donors age must be minimum 18 years";
        return false;
    }

    if (dealerAge > 60) {
        document.getElementById("ageMessage").innerHTML = "blood donors age must be maximum 60 years";

        return false;
    }

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

function amountFormValidator() {
    let totalAmountOfRest = document.getElementById("totalAmountOfRest").value;
    let amount = document.getElementById("amount").value;

    console.log(totalAmountOfRest);
    console.log(amount);

    if (amount < totalAmountOfRest) {
        document.getElementById("amountMessage").innerHTML = "**your rest amount is only " + totalAmountOfRest + "...";
        console.log("Tanver");
        return false;
    }

}

function registrationDealerIsConfirmed() {
    let confirmAction = confirm("Are you want to Registration?");
    if (confirmAction) {
        alert("Registration Successfully...");
        return true;
    } else {
        alert("Registration Canceled!!");
        return false;
    }
}