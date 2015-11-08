<%--
  Created by IntelliJ IDEA.
  User: KutsykV
  Date: 08.10.2015
  Time: 12:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Server</title>
    <script src="/resources/js/jquery-2.1.3.min.js"></script>
    <script src="/resources/js/jquery.validate.min.js"></script>

    <script>
        $(document).ready(
                window.setInterval(function(){
                    getGames();
                }, 5000)
        );
        getGames = function (){
            var html = '<table>';
            html += '<tr><td>Game number</td><td>red team name</td><td>black team name</td></tr>';
            var i = 0;
            $.getJSON('/games', {
                ajax: 'true'
            }, function (data) {
                var len = data.length;
                alert("len "+len);
                for (var i = 0; i < len; i++) {
                    alert("data "+data);
                    console.log(data);
                    var game = data[i];
                    alert("game "+game);
                    html += '<tr><td><a href="game.jsp?gameId=" ' + game.gameId + '>' + game.gameId + '</a></td><td>' + game.whiteName + '</td><td>' + data.blackName + '</td></tr>';
                    alert("html "+html);
                }
            });
            html += '</table>';
            alert("html "+html);
            $('#games').html(html);

            $.getJSON('/amount', {
                ajax: 'true'
            }, function (data) {
                $('#gamesAmount').html(data);
            });

        }
    </script>
</head>
<body>
Games amount:
<div id="gamesAmount">

</div>
<div id="games">

</div>
</body>
</html>
