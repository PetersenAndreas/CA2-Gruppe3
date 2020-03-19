/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
fetchAllPersons();

let personHobbies = [];

function clickSearchID(evt) {
    evt.preventDefault();
    fetchPersonOnId();
    clearError();
}

function create(evt) {
    evt.preventDefault();
    loadeCreate();
    clearError();
}

function reloadeUsers(evt) {
    evt.preventDefault();
    fetchAllPersons();
    clearError();
}

document.getElementById("rldAllBtn").addEventListener("click", reloadeUsers);

function fetchAllPersons() {
    let url = "api/persons/";
    let header = "<tr>" + "<th>ID</th><th>First Name</th><th>Last Name</th><th>Email</th>" + "</tr>";
    fetch(url)
            .then(res => res.json())
            .then(data => {
                console.log("data", data);
                console.log(data);
                document.getElementById("myDiv").innerHTML = "<table border='1'>" + header + insertIntoTableFooters(data.persons) + "</table>";
            });
}

function insertIntoTableFooters(data) {
    let htmlRows = "";
    data.forEach(e => {
        let temp = "<tr>"
                + "<td>" + e.id + "</td>"
                + "<td>" + e.firstName + "</td>"
                + "<td>" + e.lastName + "</td>"
                + "<td>" + e.email + "</td>"
                + "</tr>";
        htmlRows += temp;
    });
    console.log(htmlRows);
    return htmlRows;
}
;

function fetchPersonOnId() {
    let searchId = document.getElementById("searchIdPer").value;
    let url = "api/persons/" + searchId;
    let header = "<tr>" + "<th>ID</th><th>First Name</th><th>Last Name</th><th>Email</th>" + "</tr>";
    fetch(url)
            .then(res => res.json())
            .then(data => {
                console.log("data", data);
                console.log(data);

                if (data.code === 404) {
                    fetchAllPersons();
                    document.getElementById("error").innerHTML = data.message;
                } else {
                    document.getElementById("myDiv").innerHTML = "<table border='1'>" + header + insertIntoTableFooters([data]) + "</table>";
                }
            });
}
document.getElementById("searchIdBtn").addEventListener("click", clickSearchID);



function loadeCreate() {
    
    let url = "api/hobbies/"; 
    fetch(url)
            .then(res => res.json())
            .then(data => {
            document.getElementById("myDiv").innerHTML = 
            "<input type='text' name='txt' value='' id='firstName' placeholder='First Name' style='width: 150px'/>" + "  First Name"
            + '<br>' + "<input type='text' name='txt' value=''id='lastName' placeholder='Last Name'/ style='width: 150px'>" + "  Last Name"
            + '<br>' + "<input type='text' name='txt' value=''id='email' placeholder='Email' style='width: 150px'/>" + "  Email"
            + '<br>' + "<input type='text' name='txt' value=''id='phone' placeholder='Phone' style='width: 150px'/>" + "  Phone"
            + '<br>' + createPersonHobbyTable()
            + "<p id='disAddedHobby'></p>"
            + '<br>' + "<button id='addBtn'>ADD Hobby to Person</button>"
            + '<br>' + "<button id='createbtn'>Create</button>";
            
    });
    document.getElementById("addBtn").addEventListener("click", addHobbyToArray);
    personHobbies = [];
    
    
   

}

function addHobbyToArray() {
    personHobbies[document.getElementById("hobby").text];
}

document.getElementById("createSelection").addEventListener("click", create);

function createPersonHobbyTable() {
    let headers = "<table><tr><th>Hobby Name</th><th>Description</th><th>Select</th></tr></table>";
    
}





function clearError() {
    document.getElementById("error").innerHTML ="<br>";
}




