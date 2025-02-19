
const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8081/gs-guide-websocket'
});

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);

    const userName = frame.headers["user-name"]; // user-name 추출
    const heartBeat = frame.headers["heart-beat"];
    const [clientHeartbeat, serverHeartbeat] = heartBeat.split(",").map(Number);

    console.log(`Client heartbeat interval: ${clientHeartbeat}s, Server heartbeat interval: ${serverHeartbeat}s`);

    stompClient.subscribe('/topic/token', (response) => {
        console.log('Response body: ', response.body);  // 추가: 응답 내용 확인
        const token = JSON.parse(response.body).token;
        showToken(token); // token을 화면에 출력
        window.token = token
    });

    stompClient.subscribe('/topic/waitingQueue/status', (response) => {
        console.log('Response body: ', response.body);  // 추가: 응답 내용 확인
        const status = JSON.parse(response.body).status; // 응답에서 status 값을 추출
    });

    stompClient.subscribe('/user/topic/token', (response) => {
        console.log('Response body: ', response.body);  // 추가: 응답 내용 확인
        const token = JSON.parse(response.body).token; // 응답에서 token 값을 추출
        showToken(token); // token을 화면에 출력
    });

    stompClient.subscribe('/user/topic/reconnect', (response) => {
        console.log('Response body: ', response.body);  // 추가: 응답 내용 확인
        const result = JSON.parse(response.body).result; // 응답에서 result 값을 추출
    });

    stompClient.subscribe('/topic/rank', (response) => {
        console.log('Response body: ', response.body);  // 추가: 응답 내용 확인
        const rank = JSON.parse(response.body).rank; // 응답에서 rank 값을 추출
        showRank(rank); // rank를 화면에 출력
    });

    stompClient.subscribe('/user/topic/rank', (response) => {
        console.log('Response body: ', response.body);  // 추가: 응답 내용 확인
        const rank = JSON.parse(response.body).rank; // 응답에서 rank 값을 추출
        showRank(rank); // rank를 화면에 출력
    });

    sendUuid();

     if (clientHeartbeat) {
           setInterval(() => {
           if(window.token){
                sendHeartbeat(window.token);
           }
       }, clientHeartbeat * 1000);  // 클라이언트의 heartbeat 간격에 맞춰 실행
     }
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

function sendHeartbeat(token) {
    const currentTime = new Date().toISOString();  // 현재 시간을 ISO 8601 포맷으로 가져오기
    console.log("Sending heartbeat at: " + currentTime);
    const timestampInMillis = new Date(currentTime).getTime();

    const heartbeatData = {
       token: token,
       timestamp: timestampInMillis
    };

    stompClient.publish({
        destination: '/api/v1/heartbeat',  // 서버에서 Heartbeat을 처리할 경로 (예시)
        body: JSON.stringify(heartbeatData)
    });
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function sendUuid() {
     const messageData = {
            uuid: $("#uuid").val()
       };
     stompClient.publish({
            destination: "/api/v1/waitingQueue/token",  // 서버에서 처리하는 엔드포인트
            body: JSON.stringify(messageData)  // JSON 형식으로 메시지 전송
     });
}

function sendToken() {
     const messageData = {
            token: $("#token").val()
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
    $("#ranks").append("<tr><td>waitingRank: " + rank + "</td></tr>");
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