<%-- 
    Document   : internal_product_page
    Created on : 28 Dec, 2015, 8:25:58 PM
    Author     : arsh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jsgrid/1.3.1/jsgrid.min.css" />
        <link type="text/css" rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jsgrid/1.3.1/jsgrid-theme.min.css" />
        <script src="//code.jquery.com/jquery-1.10.2.min.js"></script>

        <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jsgrid/1.3.1/jsgrid.min.js"></script>

        <title>JSP Page</title>
    </head>
    <body>
        <h1>OData Service</h1>
        <div id="jsGrid"></div>

        <script>
            $(function () {

                $("#jsGrid").jsGrid({
                    height: "auto",
                    width: "100%",
                    sorting: true,
                    paging: true,
                    autoload: true,
                    editing: true,
                    inserting:true,
                    onItemInserting: handleInsert,
                    onItemUpdating: handleEdit,
                    controller: {
                        loadData: function () {
                            var d = $.Deferred();

                            $.ajax({
                                url: "api/product/listall",
                                dataType: "json"
                            }).done(function (response) {
                                d.resolve(response);
                            });

                            return d.promise();
                        }
                    },
                    fields: [
                        {name: "name", type: "text"},
                        {name: "displayName", type: "textarea", width: 150},
                        {name: "sizeSpecification", type: "text"},
                        {name: "marketPrice", type: "text"},
                        {name: "sellingPrice", type: "text"},
                        {name: "bookingOnly", type: "checkbox"},
                        {name: "visible", type: "checkbox"},
                        {name: "displayPosition", type: "text"},
                        {name: "type", type: "select", items: JSON.parse('[{"name":"FISH"},{"name":"MEAT"}]'), valueField: "name", textField: "name"},
                        {type: "control", deleteButton: false}

                    ]
                });

            });
            function handleInsert(event){
                $.ajax
                        ({
                            type: "POST",
                            //the url where you want to sent the userName and password to
                            url: 'api/product/insert',
                            dataType: 'json',
                            contentType: 'application/json',
                            async: false,
                            //json object to sent to the authentication url
                            data: JSON.stringify(event.item),
                            success: function () {

                                alert("Thanks!");
                            }
                        });
            }
            function handleEdit(event) {
                $.ajax
                        ({
                            type: "POST",
                            //the url where you want to sent the userName and password to
                            url: 'api/product/update',
                            dataType: 'json',
                            contentType: 'application/json',
                            async: false,
                            //json object to sent to the authentication url
                            data: JSON.stringify(event.item),
                            success: function () {

                                alert("Thanks!");
                            }
                        })
            }
        </script>
    </body>
</html>
