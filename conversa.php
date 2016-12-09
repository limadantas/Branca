<!DOCTYPE html>
<html>
<head>
  <title>Branca - Início</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta charset="utf-8">
<link rel="stylesheet" href="https://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css">
<script src="https://code.jquery.com/jquery-1.11.3.min.js"></script>
<script src="https://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.js"></script>
<script src="./purl.js"></script>
<script src="./mqttws31.js"></script>

<script type="text/javascript">
  Array.prototype.contains = function(obj) {
    var i = this.length;
    while (i--) {
        if (this[i] === obj) {
            return true;
        }
    }
    return false;
  }

  clientId = "branca" + Math.floor((Math.random() * 1000) + 1)
  // Create a client instance
  client = new Paho.MQTT.Client("broker.mqttdashboard.com", Number(8000), clientId);
  url = $.url(document.location);
  id = url.param("id");
  topic = "/branca/grupos/"+id;

  function salvarGrupo (it) {
    var bd = localStorage.getItem("bd");
    if (bd == null)
      bd = []
    else {
      bd = JSON.parse(bd)
    }
    if (!bd.contains(it)) {
      bd.push(id)
      localStorage.setItem("bd",JSON.stringify(bd));
    }
  }

  $(function() {
  console.log("OIII")

  // set callback handlers
  client.onConnectionLost = onConnectionLost;
  client.onMessageArrived = onMessageArrived;

  // connect the client
  client.connect({onSuccess:onConnect});


  // called when the client connects
  function onConnect() {
    // Once a connection has been made, make a subscription and send a message.
    console.log("onConnect");
    document.querySelector("#enviar").disabled=false
    client.subscribe(topic);
    message = new Paho.MQTT.Message("Hello");
    message.destinationName = "/branca/debug";
    client.send(message);

    $('#enviar').on("click", function() {
      var m = {
        id: clientId,
        corpo: $("#msg").val()
      }
      message = new Paho.MQTT.Message(JSON.stringify(m));
      message.destinationName = topic;
      message.retained = true;
      client.send(message);
      $("#msg").val("")
    })
  }

  // called when the client loses its connection
  function onConnectionLost(responseObject) {
    if (responseObject.errorCode !== 0) {
      console.log("onConnectionLost:"+responseObject.errorMessage);
    }
    document.querySelector("#enviar").disabled=true
  }

  // called when a message arrives
  function onMessageArrived(message) {

    console.log("onMessageArrived:"+message.payloadString);
    try {
        var msg = JSON.parse(message.payloadString);
        $('ul').append($('\
        <li data-icon="false">\
            <a href="#">\
            <p><strong>'+msg.id+' diz:</strong></p>\
            <p>'+msg.corpo+'</p>\
            </a>\
          </li>'))
        $('ul').listview('refresh')
      }
      catch (e) {
        console.log("JSON inválido " + e)
      }
  }
})
</script>

</head>
<body>

<div data-role="page" id="conversa">
  <div data-role="header">
    <h1>Branca 1.0</h1>
    <a href="index.php" class="ui-btn-left" rel="external">Back</a>
  </div>
  <div data-role="main" class="ui-content">
    <h2 id="titulo-conversa"></h2>
    <ul class="mensagens" data-role="listview" data-inset="true">

    </ul>
  </div>
  <div data-role="footer">
    <div class="ui-field-contain">
      <input id="msg" >
      <button id="enviar" disabled>Enviar</button>
    </div>
  </div>
</div>
<script>

  var url = $.url(document.location);
  var id = url.param("id");
  $("#titulo-conversa").text(id);
  salvarGrupo(id)
</script>
</body>
</html>
