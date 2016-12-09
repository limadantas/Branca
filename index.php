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

<script type="text/javascript">
  function setGrupo(grupo) {
    localStorage.setItem("grupo",grupo)
  }

  function getGrupos() {
    var bd = localStorage.getItem("bd");
    if (bd == null)
      return [];
    return JSON.parse(bd).sort();
  }

  $(function() {
  console.log("OIII")
  var edBuscar = $("input[data-type=search]")[0];
  edBuscar.onkeydown = function() {
    console.log("digitando...");
    if ($.trim(edBuscar.value) !== "") {
      $.getJSON( "http://179.191.235.43/branca.php?q="+$.trim(edBuscar.value), function( data ) {
        $('ul.grupos').empty()
        $.each( data, function( key, val ) {
          //items.push( "<li id='" + key + "'>" + val + "</li>" );
          console.log("key="+val.id)
          console.log()
          $('ul.grupos').append($('<li><a  rel="external" href="conversa.php?id='+val.id+'" class="item-grupo" data-grupo="'+val.id+'"><h2>'+val.id+'</h2></a></li>'))
        });
        $('ul.grupos').append($('<li><a  rel="external" href="conversa.php?id='+edBuscar.value+'" class="item-grupo" href="#" data-grupo="'+edBuscar.value+' data-icon="plus"><h2>Criar "'+edBuscar.value+'"</h2></a></li>'))
        $('ul.grupos').listview('refresh')

      });
    } else { $('ul').empty() }
  }
})
</script>

</head>
<body>

<div data-role="page" id="inicio">
  <div data-role="header">
    <h1>Branca 1.0</h1>
  </div>
  <div data-role="main" class="ui-content">
    <ul class="grupos" data-role="listview" data-filter="true" data-inset="true">

    </ul>
  </div>
</div>
<script>
grupos = getGrupos();
for (var i=0; i < grupos.length; ++i) {
  $('ul.grupos').append($('<li><a  rel="external" href="conversa.php?id='+grupos[i]+'" class="item-grupo" data-grupo="'+grupos[i]+'"><h2>'+grupos[i]+'</h2></a></li>'))
}
</script>
</body>
</html>
