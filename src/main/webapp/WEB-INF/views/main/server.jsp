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
                function () {
                    var html = '<table>';
                    html += '<tr><td>Game number</td><td>Result</td></tr>';
                    var i=0;
                    $.getJSON('${games}', {
                        ajax: 'true'
                    }, function (data) {
                        var len = data.length;
                        for (var i = 0; i < len; i++) {
                            var gameResult = '';
                            if(data[i].winner() != null){
                                console.log('has winner');
                            }else console.log('stil playing');
                            html += '<tr><td>' + i + '</td><td>' + gameResult + '</td></tr>';
                        }
                    });
                    html += '</table>';
                    $('#games').html(html);
                }
        );
    </script>
</head>
<body>
Games amount:
<h3>${gamesAmount}</h3>
<div id="games">

</div>
</body>
</html>
