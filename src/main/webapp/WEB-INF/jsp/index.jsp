<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
</head>
<body>
	<h3>Invoices</h3>
	<form:form method="POST" action="/api/v1/payInvoiceForm" modelAttribute="invoiceWrapper">
		<table>
			<tr>
				<td></td>
				<td>Invoice Id</td>
				<td>Supplier</td>
				<td>Amount</td>
			</tr>
			<c:forEach items="${invoiceWrapper.items}" varStatus="status" var="invoice">
				<tr>
					<td><form:checkbox path="items[${status.index}].enabled" value="invoice.enabled" /></td>
					<td>
						<form:hidden path="items[${status.index}].invoiceId"/>
						<form:hidden path="items[${status.index}].id"/>
						<c:out value="${invoice.invoiceId}" />
					</td>
					<td><form:hidden path="items[${status.index}].supplier"/><c:out value="${invoice.supplier}" /></td>
					<td><form:hidden path="items[${status.index}].amount"/><c:out value="${invoice.amount}" /></td>
				</tr>
			</c:forEach>
		</table>
		<input type="submit" value="Submit" />
	</form:form>
</body>
</html>