let ddong_room_list = null

const btnMakeRoom = document.getElementById("btnMakeRoom")

const tbodyRoomListTable = document.getElementById("tbodyRoomListTable")

const userNickname = document.getElementById("userNickname")

const makeRoomCallBack = (result, roomName) => {
    console.log('makeRoomCallback 실행 : ', result)
    if(result === 'success') {
        window.location.href=`/ddong/gameroom?roomName=${roomName}&player=player0`
    } else {
        alert("방 생성 실패입니다")
    }
}


// 이름의 방을 만든다
const makeRoom = (event) => {
    event.preventDefault()
    let roomName = "" + userNickname.textContent
    if(roomName==='') {
        alert('방 이름이 없어요')
    } else {
        socket.emit('ddong_create_room', roomName, (result) => {
            makeRoomCallBack(result, roomName )
        })
    }
}

const joinRoomCallBack = (result, roomName) => {
    console.log('joinRoomCallback 실행 : ', result)
    if(result === 'success') {
        window.location.href=`/ddong/gameroom?roomName=${roomName}&player=player1`
        // Roomlist는 server에서 바꿈
    } else if(result ==='full') {
        alert("방이 full입니다")
    } else {
        alert("방 조인 실패입니다")
    }
}

// 이름의 방에 join한다
const joinRoom = (event) => {
    event.preventDefault()
    let strId = event.target.id //button의 id
    let strRoomName = strId.substring(4)
    console.log(strRoomName)
    socket.emit('ddong_join_room', strRoomName, (result) => {
        joinRoomCallBack(result, strRoomName)
    })
}


btnMakeRoom.addEventListener('click', makeRoom)

// ******* 게임 서버 접속  ******
const socket = io("http://localhost:3004/ddong", {path :"/socket.io"
});


/******* 방 list 및 table update  ***********/
const updatteList = (roomList) => {
    console.log("after get_room_list")
    ddong_room_list = [...roomList]
    //console.log("rooms in ddong namespace", ddong_room_list)
    while (tbodyRoomListTable.firstChild) {
        tbodyRoomListTable.removeChild(tbodyRoomListTable.firstChild);
    }
    for(i=0; i<ddong_room_list.length; i++){
        const row = tbodyRoomListTable.insertRow()
        row.insertCell().textContent = ddong_room_list[i].roomName
        row.insertCell().textContent = ddong_room_list[i].roomSize

        let button = document.createElement('button')
        button.innerText = 'Join';
        button.setAttribute('class', "btn btn-success btn-sm")
        button.setAttribute('id', 'btn_' + ddong_room_list[i].roomName)
        button.addEventListener('click', joinRoom)
        row.insertCell().appendChild(button)
    }
}

// 능동적으로 update List 요구 보내기
socket.emit("get_room_list", '', (roomList) => {
    console.log(roomList)
    updatteList(roomList)
} )

// ddong_room msg(방의 목록을 전송하는 msg)에 대한 callback
socket.on('ddong_rooms_info', (roomList) => {
    ddong_room_list = [...roomList]
    //console.log("rooms in ddong namespace", ddong_room_list)
    updatteList(roomList)
})