/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

let editHobbies = [];
let editPhones = [];

getAllPersons();

function getAllPersons() {
    //evt.preventDefault();
    console.log("Hello");
    let url = "http://localhost:8080/CA2-Gruppe3/api/persons";
    fetch(url)
            .then(res => res.json())
            .then(data => {
                console.log("data", data);

                var header = "<tr><th>ID</th><th>FirstName</th><th>LastName</th><th>Email</th><th>Street</th><th>City</th><th>ZipCode</th><th>Hobbies</th><th>PhoneNumbers</th><th>Phone-Description</th></tr>";

                var tableBody = data.persons.map(info => {
                    return "<tr>" +
                            "<td>" + info.id + "</td>" +
                            "<td>" + info.firstName + "</td>" +
                            "<td>" + info.lastName + "</td>" +
                            "<td>" + info.email + "</td>" +
                            "<td>" + info.street + "</td>" +
                            "<td>" + info.cityName + "</td>" +
                            "<td>" + info.zip + "</td>" +
                            "<td>" + info.hobbies.map(x => x).join("<br>") + "</td>" +
                            "<td>" + info.phones.phones.map(x => x.number).join("<br>") + "</td>" +
                            "<td>" + info.phones.phones.map(x => x.description).join("<br>") + "</td>" +
                            "</tr>";
                });

                document.getElementById("table_get_all").innerHTML = "<table border=1>" + header + tableBody + "</table>";

            });
}




document.getElementById("edit_person").addEventListener("click",editPerson);

function editPerson(evt){
    evt.preventDefault();
    
    let id = document.getElementById("personID").value;
    let firstName = document.getElementById("firstName").value;
    let lastName = document.getElementById("lastName").value;
    let email = document.getElementById("email").value;
    let street = document.getElementById("inputStreetE").value;
    let hobbies1 = editHobbies;
    let phoneNumber1 = document.getElementById("phone").value;
    let description = document.getElementById("description").value;

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
            "phones": {"phones":[
                    {"number":phoneNumber1,
                     "description": description}
            ]}
            
        })
    };

    fetch("http://localhost:8080/CA2-Gruppe3/api/persons/edit/" + id, options)
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
    getAllPersons();

};
    
  
 getHobbyList();
function getHobbyList(){
    
     let url = "api/hobbies/";
    fetch(url)
            .then(res => res.json())
            .then(data => {
                console.log("data", data);
                document.getElementById("inputHobbiesE").innerHTML = "<option>" + data.hobbies.map(x=>x.name).join("</option><option>") + "</option>";
            });
    
}

getAddressList();
function getAddressList(){
    
   let url = "api/addresses/";
    fetch(url)
            .then(res => res.json())
            .then(data => {
                console.log("data", data);
                document.getElementById("inputStreetE").innerHTML = "<option>" + data.addressList.map(x=>x.street).join("</option><option>") + "</option>";
            });
}


document.getElementById("edit_hobby").addEventListener("click",editHobbiesB);

function editHobbiesB(){
   
 editHobbies.push(document.getElementById("inputHobbiesE").value);
 console.log(editHobbies);
 document.getElementById("hobby_list").innerHTML = editHobbies.join("<br>");
    
}