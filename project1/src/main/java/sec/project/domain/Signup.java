package sec.project.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Signup extends AbstractPersistable<Long> {

    private String name;
    public String address;
    
    @Column(nullable = true)
    public Long creditcard;
    
    public Signup() {
        super();
    }

    public Signup(String name, String address, Long creditcard) {
        this();
        this.name = name;
        this.address = address;
        this.creditcard = creditcard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    @Override
    public String toString() {
        return name;
    }

}
