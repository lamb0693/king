const txtId = document.getElementById("id")

const divExistID = document.getElementById("divExistId")

if (divExistID == null)
    console.log("****** Error : divExistID : null ******");

if (txtId == null)
    console.log("****** Error : txtId : null ******");

const checkExistId = (event) => {
    event.preventDefault();
    fetch("http://10.100.203.29:8080/member/exist/id/aaa@bbb.ccc")
        .then((response) => {
            return response.text()
        })
        .then( (data) => {
            console.log(data);
            if(data==='true'){
                divExistID.innerHTML = "사용할 수 없는 Id 입니다"
            } else{
                divExistID.innerHTML = "사용할 수 있는 Id 입니다"

            }
        })
        .catch((error) => {
            console.log(error);
        });
    };

txtId.addEventListener('change', checkExistId);
