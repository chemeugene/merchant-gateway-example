<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
    <head>
    </head>
    <body>
        <h3>Enter your card parameters</h3>
        <form:form method="POST" action="/api/v1/doPayment" modelAttribute="Card">
             <table>
                <tr>
                    <td><form:label path="cardNumber">Card number</form:label></td>
                    <td><form:input path="cardNumber"/></td>
                </tr>
                <tr>
                    <td><form:label path="cardHolder">Card holder</form:label></td>
                    <td><form:input path="cardHolder"/></td>
                </tr>
                <tr>
                    <td><form:label path="cvc">CVV2/CVC2</form:label></td>
                    <td><form:input path="cvc"/></td>
                </tr>
                <tr>
                    <td><form:label path="validThrough">Valid through</form:label></td>
                    <td><form:input path="validThrough"/></td>
                </tr>
                <tr>
                    <td>
                    	<form:hidden path="bulkRequestId" />
                    	<input type="submit" value="Submit"/>
                    </td>
                </tr>
            </table>
        </form:form>
    </body>
</html>