<head>
    <title>first active page</title>
    </head>
    <body>
        <%
            for (int i = 6; i > 0; i--) {
                out.println("<h" + i + ">Hello World!</h" + i + ">");
            }
        %>
    </body>