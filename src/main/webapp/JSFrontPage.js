/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
fetchAllPersons();
function fetchAllPersons() {
    let url = "api/persons/";
    let result = "<tr>" + "<th>ID</th><th>First Name</th><th>Last Name</th><th>Email</th><th>Phone</th>" + "</tr>";
    fetch(url)
            .then(res => res.json())
            .then(data => {
                console.log("data", data);
                console.log(data);
                document.getElementById("myDiv").innerHTML = "<table border='1'>" + result + insertIntoTableFooters(data) + "</table>";
            });
}

function insertIntoTableFooters(data) {
let htmlRows = "";
        data.persons.forEach(e => {
        let temp = "<tr>"
                + "<td>" + e.id+ "</td>"
                + "<td>" + e.firstName + "</td>"
                + "<td>" + e.lastName + "</td>"
                + "<td>" + e.email + "</td>"
                + "</tr>";
        htmlRows += temp;
        });
                console.log(htmlRows);
                return htmlRows;
                };







