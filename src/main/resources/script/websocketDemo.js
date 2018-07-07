var loc = window.location, new_uri;
new_uri = "ws:"
new_uri += "//" + loc.host;
new_uri += "/socket/coolandgood";

var socket = new WebSocket(new_uri);
socket.onmessage = function(event) {
    var data =JSON.parse(event.data)

    var table = document.getElementById("variables")
    table.innerHTML = "";

    $.each( data, function( key, val ) {
        var row = document.createElement("tr");
        var nameCell = document.createElement("td");
        var valueCell = document.createElement("td");
        nameCell.innerHTML = key;
        valueCell.innerHTML = val;
        row.appendChild(nameCell);
        row.appendChild(valueCell);
        table.appendChild(row);
      });
};