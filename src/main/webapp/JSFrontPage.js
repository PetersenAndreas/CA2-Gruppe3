/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
fetchAllPersons();
fetchHobbies();
fetchStreet();

let newHobbies = [];
let newPhones = [];

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
document.getElementById("addHobby").addEventListener("click", addHobby);
document.getElementById("addPhone").addEventListener("click", addPhone);


function fetchAllPersons() {
    let url = "api/persons/";
    let header = "<tr>" + "<th>ID</th><th>First Name</th><th>Last Name</th><th>Email</th><th>Phone(s)</th><th>Description</th><th>Hobby</th><th>City</th><th>Street</th><th>Zip</th>" + "</tr>";
    fetch(url)
            .then(res => res.json())
            .then(data => {
                console.log("data", data);
                document.getElementById("myDiv").innerHTML = "<table border='1' style='width: 80%;'>" + header + insertIntoTableFooters(data.persons) + "</table>";
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
                + "<td>" + e.phones.phones.map(x => x.number).join("<br>") + "</td>"
                + "<td>" + e.phones.phones.map(x => x.description).join("<br>") + "</td>"
                + "<td>" + e.hobbies.join("<br>") + "</td>"
                + "<td>" + e.cityName + "</td>"
                + "<td>" + e.street + "</td>"
                + "<td>" + e.zip + "</td>"
                + "</tr>";
        htmlRows += temp;
    });
    return htmlRows;
}
;

function fetchPersonOnId() {
    let searchId = document.getElementById("searchIdPer").value;
    let url = "api/persons/" + searchId;
    let header = "<tr>" + "<th>ID</th><th>First Name</th><th>Last Name</th><th>Email</th><th>Phone(s)</th><th>Description</th><th>Hobby</th><th>City</th><th>Street</th><th>Zip</th>" + "</tr>";
    fetch(url)
            .then(res => res.json())
            .then(data => {
                console.log("data", data);
                console.log(data);

                if (data.code === 404) {
                    fetchAllPersons();
                    document.getElementById("error").innerHTML = data.message;
                } else {
                    document.getElementById("myDiv").innerHTML = "<table border='1' style='width: 80%;'>" + header + insertIntoTableFooters([data]) + "</table>";
                }
            });
}
document.getElementById("searchIdBtn").addEventListener("click", clickSearchID);


function clearError() {
    document.getElementById("error").innerHTML = "<br>";
}

function addPhone() {
    let phone = {
        number: document.getElementById("inputPhoneNrC").value,
        description: document.getElementById("inputPhoneDesC").value};
    newPhones.push(phone);
    console.log(newPhones);
    document.getElementById("addedPhones").innerHTML = newPhones.map(x => x.number + ", " + x.description).join("<br>");
    document.getElementById("inputPhoneNrC").value = "";
    document.getElementById("inputPhoneDesC").value = "";
}

function addHobby() {
    let hobby = document.getElementById("inputHobbiesC").value;
    newHobbies.push(hobby);
    console.log(newHobbies);
    document.getElementById("addedHobbies").innerHTML = newHobbies.join("<br>");


}

function createPerson() {
    let firstName = document.getElementById("inputFirstNameC").value;
    let lastName = document.getElementById("inputLastNameC").value;
    let email = document.getElementById("inputEmailC").value;


    let street = document.getElementById("inputStreetC").value;
    let options = {
        method: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            firstName: firstName,
            lastName: lastName,
            email: email,
            phones: {phones: newPhones},
            hobbies: newHobbies,
            street: street
        })
    };
    fetch("api/persons/add", options)
            .then(res => res.json())
            .then(data => {
                console.log("data", data);
            });
    newPhones = [];
    document.getElementById("addedPhones").innerHTML = newPhones.map(x => x.number + ", " + x.description).join("<br>");
    newHobbies = [];
    document.getElementById("addedHobbies").innerHTML = newHobbies.join("<br>");
}
document.getElementById("createPersonBtn").addEventListener("click", createPerson);

function fetchHobbies() {
    let url = "api/hobbies/";
    fetch(url)
            .then(res => res.json())
            .then(data => {
                console.log("data", data);
                document.getElementById("inputHobbiesC").innerHTML = "<option>" + data.hobbies.map(x => x.name).join("</option><option>") + "</option>";
                document.getElementById("inputHobbiesE").innerHTML = "<option>" + data.hobbies.map(x => x.name).join("</option><option>") + "</option>";
            });
}

function fetchStreet() {
    let url = "api/addresses/";
    fetch(url)
            .then(res => res.json())
            .then(data => {
                console.log("data", data);
                document.getElementById("inputStreetC").innerHTML = "<option>" + data.addressList.map(x => x.street).join("</option><option>") + "</option>";
                document.getElementById("inputStreetE").innerHTML = "<option>" + data.addressList.map(x => x.street).join("</option><option>") + "</option>";
            });
}

//Edit functions:

let editHobbies = [];
let editPhones = [];

document.getElementById("edit_person").addEventListener("click", editPerson);

function editPerson() {

    let id = document.getElementById("personID").value;
    let firstName = document.getElementById("firstName").value;
    let lastName = document.getElementById("lastName").value;
    let email = document.getElementById("email").value;
    let street = document.getElementById("inputStreetE").value;
    let hobbies1 = editHobbies;

    let options = {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            "id": id,
            "firstName": firstName,
            "lastName": lastName,
            "email": email,
            "street": street,
            "hobbies": hobbies1,
            phones: {phones: editPhones}})
    };

    fetch("api/persons/edit/" + id, options)
            .then(res => res.json())
            .then(data => {
                if (data.status) {
                    console.log(data.msg);
                    //document.getElementById("errorEdit").innerHTML = data.msg;
                } else {
                    //  document.getElementById("errorEdit").innerHTML = '<br>';
                    console.log("nope");
                }
            });
    editHobbies = [];
    document.getElementById("hobby_list").innerHTML = editHobbies.join("<br>");
    editPhones = [];
    document.getElementById("editPhones").innerHTML = newPhones.map(x => x.number + ", " + x.description).join("<br>");
}
;


function editHobbiesB() {

    editHobbies.push(document.getElementById("inputHobbiesE").value);
    console.log(editHobbies);
    document.getElementById("hobby_list").innerHTML = editHobbies.join("<br>");

}
document.getElementById("edit_hobby").addEventListener("click", editHobbiesB);

function editPhone() {
    let phone = {
        number: document.getElementById("inputPhoneE").value,
        description: document.getElementById("inputPhoneDesE").value};
    editPhones.push(phone);
    console.log(editPhones);
    document.getElementById("editPhones").innerHTML = editPhones.map(x => x.number + ", " + x.description).join("<br>");
    document.getElementById("inputPhoneE").value = "";
    document.getElementById("inputPhoneDesE").value = "";
}
document.getElementById("edit_phone").addEventListener("click", editPhone);



//Graveyard
//function loadeCreate() {
//    
//    let url = "api/hobbies/"; 
//    fetch(url)
//            .then(res => res.json())
//            .then(data => {
//            document.getElementById("myDiv").innerHTML = 
//            "<input type='text' name='txt' value='' id='firstName' placeholder='First Name' style='width: 150px'/>" + "  First Name"
//            + '<br>' + "<input type='text' name='txt' value=''id='lastName' placeholder='Last Name'/ style='width: 150px'>" + "  Last Name"
//            + '<br>' + "<input type='text' name='txt' value=''id='email' placeholder='Email' style='width: 150px'/>" + "  Email"
//            + '<br>' + "<input type='text' name='txt' value=''id='phone' placeholder='Phone' style='width: 150px'/>" + "  Phone"
//            + '<br>' + createPersonHobbyTable()
//            + "<p id='disAddedHobby'></p>"
//            + '<br>' + "<button id='addBtn'>ADD Hobby to Person</button>"
//            + '<br>' + "<button id='createbtn'>Create</button>";
//            
//    });
//    document.getElementById("addBtn").addEventListener("click", addHobbyToArray);
//    personHobbies = [];
//    
//    
//   
//
//}
//
//function addHobbyToArray() {
//    personHobbies[document.getElementById("hobby").text];
//}
//
//document.getElementById("createSelection").addEventListener("click", create);
//
//function createPersonHobbyTable() {
//    let headers = "<table><tr><th>Hobby Name</th><th>Description</th><th>Select</th></tr></table>";
//    
//}

