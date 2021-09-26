<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Asteroids</title>
</head>
<body>

<h3>Largest</h3>
<p>${largest.name}</p>
<p>Diameter: ${largest.diameterInKilometers}</p>
<p>Distance: ${largest.distanceInKilometers}</p>


<h3>Nearest (${startDate} - ${endDate})</h3>
<p>${nearest.name}</p>
<p>Diameter: ${nearest.diameterInKilometers}</p>
<p>Distance: ${nearest.distanceInKilometers}</p>

</body>
</html>