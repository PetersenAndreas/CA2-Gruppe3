package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQuery(name = "Address.deleteAllRows", query = "DELETE from Address")
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String street;

    //Additional-info (add variables)!!
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private CityInfo cityInfo;

    @OneToMany(mappedBy = "address")
    private List<Person> persons = new ArrayList();

    public Address() {
    }

    public Address(String street, CityInfo cityInfo, List<Person> persons) {
        this.street = street;
        this.cityInfo = cityInfo;
        addCityToAddress(cityInfo);
        this.persons = persons;
        for (Person person : persons) {
            addPersonToAddress(person);
        }
    }

    public Address(String street, CityInfo cityInfo) {
        this.street = street;
        this.cityInfo = cityInfo;
        addCityToAddress(cityInfo);
    }

    public void addCityToAddress(CityInfo cityInfo) {
        this.cityInfo = cityInfo;
        if (!cityInfo.getAddresses().contains(this)) {
            cityInfo.getAddresses().add(this);
        }
    }

    public void addPersonToAddress(Person person) {
        if (!this.persons.contains(this)) {
            this.persons.add(person);
        }
        if (!person.getAddress().equals(this)) {
            person.setAddress(this);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public CityInfo getCityInfo() {
        return cityInfo;
    }

    public void setCityInfo(CityInfo cityInfo) {
        this.cityInfo = cityInfo;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Address)) {
            return false;
        }
        Address other = (Address) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Address[ id=" + id + " ]";

    }

}
