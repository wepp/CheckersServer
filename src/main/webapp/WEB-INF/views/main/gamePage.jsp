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
    var currentStep = 0;
    $(document).ready(
            function(){
              getGame();
              window.setInterval(function(){
                getGame();
              }, 5000);
            }
    );
    getGame = function () {
      var i = 0;
      $.getJSON('/'+id+'/field/' + currentStep, {
        ajax: 'true'
      }, function (data) {
        var cheks = data.field;
        var finished = data.finished;
        var whiteName = data.whiteName;
        var blackName = data.blackName;
        var winner = data.winner;
        $('#white').text(whiteName);
        $('#black').text(blackName);
        $('#currentStep').text(currentStep);
        if (finished === true) {
          $('#winner').text(winner);
        }
        if(cheks !== undefined && cheks != null && cheks.length > 0){
          var whiteAmount = getAmount(cheks, 0);
          var blackAmount = getAmount(cheks, 1);
          $('#whiteAmount').text(whiteAmount);
          $('#blackAmount').text(blackAmount);

          var html = "<table>";
          html += '<tr><td></td>';
          for (var j = 0; j < 8; j++) {
            html += '<td>'+(7 - j)+'</td>';
          }
          html += '<td></td>';
          for (var i = 7; i >= 0; i--) {
            html += '</tr>';
            html += '<tr><td>'+i+'</td>';
            for (var j = 0; j < 8; j++) {
              html += '<td>'+image(cheks, j, i)+'</td>';
            }
            html += '<td>'+(7 - i)+'</td>';
          }
          html += '<tr><td></td>';
          for (var j = 0; j < 8; j++) {
            html += '<td>'+j+'</td>';
          }
          html += '<td></td>';
          html += '</tr>';
          html += '</table>';
          $('#game').html(html);
          currentStep = currentStep + 1;
        }
      });
    };

    image = function(data, x, y){
      var res = '<img src="images/girl.png" width="189" height="255" alt="lorem">';
      var x_cord = x;
      var y_cord = y;
      for(var i = 0; i < data.length; i++){
        var check = data[i];
        if(check.position.x === x_cord && check.position.y === y_cord){
          return '<img src="'+imageUrl(check.color, check.queen)+'" width="50" height="50">';
        }
      }
      return '<img src="'+imageUrl(50, false)+'" width="50" height="50">';
    };

    imageUrl = function(x, y){
      if(x === 50){
        return "https://literaryundertakings.files.wordpress.com/2014/10/olympia_blue.jpg";
      }
      if(x === 0 && y === false){
        return "http://megocomp.ru/wp-content/uploads/2014/03/vQBzi.png";
      }
      if(x === 0 && y === true){
        return "http://fs153.www.ex.ua/show/55118254/55118254.png";
      }
      if(x === 1 && y === false){
        return "http://b4p.at.ua/Oblogki/firefox_b4p.at.ua..png";
      }
      if(x === 1 && y === true){
        return "http://rocketdock.com/images/screenshots/firefox-red-1.png";
      }
    };

    getAmount = function(data, x){
      var res = 0;
      for(var i = 0; i < data.length; i++){
        if(data[i].color === x)
          res = res + 1;
      }
      return res;
    };
  </script>
</head>
<body>

<table>
  <tr><td>red team name</td><td>left</td><td>middle</td><td>right</td><td>black team name</td></tr>
  <tr><td id="white"></td><td id="whiteAmount"></td><td> - </td><td id="blackAmount"></td><td id="black"></td></tr>
</table>

<div id="currentStep"></div>

<div id="game"></div>
</body>
</html>
