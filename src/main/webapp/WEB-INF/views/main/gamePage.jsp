<%--
  Created by IntelliJ IDEA.
  User: Eugene
  Date: 08.11.2015
  Time: 21:12
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <script src="/resources/js/jquery-2.1.3.min.js"></script>
  <script src="/resources/js/jquery.validate.min.js"></script>

  <script>
    var id = ${gameId};
    $(document).ready(
            window.setInterval(function(){
              getGame();
            }, 10000)
    );
    getGames = function (){
      var i = 0;
      $.getJSON('/game/'+id, {
        ajax: 'true'
      }, function (data) {
        var finished = data.finished;
        var whiteName = data.whiteName;
        var blackName = data.blackName;
        var winner = data.winner;
        $('#white').text(whiteName);
        $('#black').text(blackName);
        if(finished === true){
          $('#winner').text(winner);
        }else {

        }
        var html = "<table>";
        var cheks = data.field;
        for (var i = 0; i < 8; i++) {
          html += '<tr>';
          for (var j = 0; j < 8; j++) {
            html += '<td>'+image(cheks, i, j)+'</td>';
          }
          html += '</tr>';
        }
        html += '</table>';
        $('#game').html(html);
      });

      image = function(data, x, y){
        var res = '<img src="images/girl.png" width="189" height="255" alt="lorem">';
        for(var i = 0; i < data.length; i++){
          var check = data[i];
          if(check.x === x && check.y === y){
            return '<img src="'+imageUrl(check.color, check.queen)+'" width="50" height="50">';
          }
          return '<img src="'+imageUrl(50, false)+'" width="50" height="50">';
        }
      }

      image = function(x, y){
        if(x === 50){
          return "https://literaryundertakings.files.wordpress.com/2014/10/olympia_blue.jpg";
        }
        if(x === 0, y === false){
          return "http://megocomp.ru/wp-content/uploads/2014/03/vQBzi.png";
        }
        if(x === 0, y === true){
          return "http://fs153.www.ex.ua/show/55118254/55118254.png";
        }
        if(x === 1, y === false){
          return "http://b4p.at.ua/Oblogki/firefox_b4p.at.ua..png";
        }
        if(x === 1, y === true){
          return "http://rocketdock.com/images/screenshots/firefox-red-1.png";
        }
      }

    }
  </script>
</head>
<body>
${gameId}

<table>
  <tr><td>Game number</td><td>red team name</td><td>black team name</td>
  <tr><td><div id="white"></div></td><td><div id="black"></div></td><td><div id="winner"></div></td>
</table>
<div id="game">

</div>
</body>
</html>
