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
                for (var i = 0; i < len; i++) {
                    console.log(data);
                    html += '<tr><td><a href="game.jsp?gameId=" ' + data.gameId + '>' + data.gameId + '</a></td><td>' + data.whiteName + '</td><td>' + data.blackName + '</td></tr>';
                }
            });
            html += '</table>';
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
