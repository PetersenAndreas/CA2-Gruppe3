/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
    let street = document.getElementById("street").value;
    let cityName = document.getElementById("cityName").value;
    let zip = document.getElementById("zip").value;
//    let hobbies1 = document.getElementById("hobbies1").value;
//    let hobbies2 = document.getElementById("hobbies2").value;
//    let phoneNumber1 = document.getElementById("phoneNumber1").value;
//    let phoneNumber2 = document.getElementById("phoneNumber2").value;
//    let description = document.getElementById("description").value;

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
            "cityName": cityName,
            "zip": zip
            
        })
    };

    fetch("http://localhost:8080/CA2-Gruppe3/api/persons/edit/" + id, options)
        .then(res => res.json())
        .then(data => {
            if (data.status) {
                console.log(data.msg);
                document.getElementById("errorEdit").innerHTML = data.msg;
            } else {
              //  document.getElementById("errorEdit").innerHTML = '<br>';
              console.log("nope");
            }
        });

    getAllPersons();

};
    
  
