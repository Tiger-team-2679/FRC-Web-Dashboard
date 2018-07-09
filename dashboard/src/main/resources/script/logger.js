var loc = window.location, new_uri;
new_uri = "ws:"
new_uri += "//" + loc.host;
new_uri += "/socket/logger";

var socket = new WebSocket(new_uri);
socket.onmessage = function(event) {
    var p = document.createElement("p");
    p.innerHTML= event.data;
    var logger = document.getElementsByClassName("logger")[0]
    logger.appendChild(p)
};