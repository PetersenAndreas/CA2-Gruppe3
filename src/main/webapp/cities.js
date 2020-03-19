/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


getAllCitiesInfo();

function getAllCitiesInfo() {
    //evt.preventDefault();
    console.log("Hello");
    let url = "http://localhost:8080/CA2-Gruppe3/api/cities";
    fetch(url)
        .then(res => res.json())
        .then(data => {
            console.log("data", data);

            var header = "<tr><th>Zip-Code</th><th>City</th></tr>";
            
                var tableBody = data.citiesInfo.map(info => {
                return "<tr>" +
                    
                    "<td>" + info.zipCode + "</td>" +
                    "<td>" + info.cityName + "</td>" +
                    "</tr>";
            });


            document.getElementById("table_get_all").innerHTML = "<table border=1>" + header + tableBody + "</table>";

        });
        
        
} 




document.getElementById("get_persons_by_id").addEventListener("click",getPersonsByZip);

function getPersonsByZip(evt){
evt.preventDefault();

let id = document.getElementById("id").value;

 var personIdMatch = data.persons.filter(person => person.id === id);
            console.log(personIdMatch);

   let url = "http://localhost:8080/CA2-Gruppe3/api/cities"+ id;
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
                    "<td>" + info.hobbies + "</td>" +
                    "<td>" + info.phones.number + "</td>" +
                    "<td>" + info.phones.decription + "</td>" +
                    "</tr>";
            });


            document.getElementById("table_get_all").innerHTML = "<table border=1>" + header + tableBody + "</table>";

        });
        
        
}
