const btnChangePassword = document.getElementById("btnChangePassword");
if (btnChangePassword == null)
    console.log("error --- btnChangePassword is null");
const divId = document.getElementById("divId")
const divToken = document.getElementById("divToken")

const changePassword = (event) => {
    const txtPassword1 = document.getElementById("txtPassword1");
    if (txtPassword1 == null)
        console.log("error --- txtPassword1 is null");
    const txtPassword2 = document.getElementById("txtPassword2");
    if (txtPassword2 == null)
        console.log("error --- txtPassword2 is null");
    const changePasswordForm = document.getElementById("changePasswordForm");
    if (changePasswordForm == null)
        alert("error --- changePasswordForm is null");
    if (txtPassword1.value.length < 8) {
        alert("패스워드1 : 8자리 이상이 필요합니다 ");
        return;
    }
    else if (txtPassword2.value.length < 8) {
        alert("패스워드2 : 8자리 이상이 필요합니다 ");
    }
    else if (txtPassword1.value != txtPassword2.value) {
        alert("두 개의 패스워드가 일치하지 않습니다");
        return;
    }

    formData = {
        "id" : divId.textContent,
        "password" : txtPassword1.value,
    }

    const JWT_TOKEN = "Bearer " + divToken.textContent;
    console.log(JWT_TOKEN)

    // Make a fetch request with custom headers
    fetch('/member/resetPassword/process', {
        method: "POST",
        credentials: "include",
        headers: {
            "Authorization": JWT_TOKEN,
            "Content-Type": "application/json"
        },
        body: JSON.stringify(formData)
    })
    .then(response => {
        // Handle the response
        console.log(response)
    })
    .catch(error => {
        // Handle errors
        console.log(error)
    });

    //changePasswordForm.submit();
    //submit 대신에 fetch
};
btnChangePassword.addEventListener("click", changePassword);
