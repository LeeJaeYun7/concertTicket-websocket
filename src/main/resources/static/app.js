const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8081/gs-guide-websocket'
});

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/token', (response) => {
        console.log('Response body: ', response.body);  // 추가: 응답 내용 확인
        const token = JSON.parse(response.body).token; // 응답에서 token 값을 추출
        showToken(token); // token을 화면에 출력
    });

    stompClient.subscribe('/topic/rank', (response) => {
            console.log('Response body: ', response.body);  // 추가: 응답 내용 확인
            const rank = JSON.parse(response.body).rank; // 응답에서 rank 값을 추출
            showRank(rank); // rank를 화면에 출력
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function sendUuid() {
     const messageData = {
            uuid: $("#uuid").val(),
            concertId: 1  // concertId 값을 추가
       };
     stompClient.publish({
            destination: "/api/v1/waitingQueue/token",  // 서버에서 처리하는 엔드포인트
            body: JSON.stringify(messageData)  // JSON 형식으로 메시지 전송
     });
}

function sendToken() {
     const messageData = {
            token: $("#token").val(),
            concertId: 1  // concertId 값을 추가
     };

     console.log("Sending message:", messageData);  // 메시지 데이터 확인
     console.log("Publishing message to /api/v1/waitingQueue/rank");

     stompClient.publish({
            destination: "/api/v1/waitingQueue/rank",  // 서버에서 처리하는 엔드포인트
            body: JSON.stringify(messageData)  // JSON 형식으로 메시지 전송
     });
}

function showToken(token) {
    $("#tokens").append("<tr><td>Token: " + token + "</td></tr>");
}

function showRank(rank) {
    $("#ranks").append("<tr><td>Token: " + token + "</td></tr>");
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $("#connect").click(() => connect());
    $("#disconnect").click(() => disconnect());
    $("#send").click(() => sendUuid());
    $("#sendToken").click( () => {
        console.log("sendToken button clicked");
        sendToken()
    });
});