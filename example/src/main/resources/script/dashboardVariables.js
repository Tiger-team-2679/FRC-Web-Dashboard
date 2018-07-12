var loc = window.location, new_uri;
new_uri = "ws:"
new_uri += "//" + loc.host;
new_uri += "/socket/coolandgood";

var socket = new WebSocket(new_uri);
socket.onmessage = function(event) {
    var data =JSON.parse(event.data)

    var table = document.getElementsByClassName("data-entry")
    table.innerHTML = "";

    var count = 0;
    $.each( data, function( key, val ) {
        if(typeof table[count] !== 'undefined'){
            table[count].innerHTML = ""
            var nameCell = document.createElement("td");
            var valueCell = document.createElement("td");
            nameCell.innerHTML = key;
            valueCell.innerHTML = val;
            table[count].appendChild(nameCell);
            table[count].appendChild(valueCell);
            count  = count + 1;
        }
      });
};