const webSocketUrl = "ws://localhost:443/WebSocket/";

const webSocket = {};
webSocket.initWebSocket = function (pathVar, webSocketOnOpen, webSocketOnMessage, webSocketOnClose, webSocketOnError) {
    if (pathVar && webSocketOnOpen && webSocketOnMessage) {
        let webSocket = new WebSocket(webSocketUrl + pathVar);
        webSocket.onopen = webSocketOnOpen;
        webSocket.onmessage = webSocketOnMessage;
        webSocket.onclose = webSocketOnClose ? webSocketOnClose : this.webSocketOnClose;
        webSocket.onerror = webSocketOnError ? webSocketOnError : this.webSocketOnError;
        return webSocket;
    } else {
        return null;
    }
}

function webSocketOnClose(event) {

}

function webSocketOnError(event) {

}


export default webSocket;
