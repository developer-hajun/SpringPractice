package jpabook.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;


@Entity
@DiscriminatorValue("M")
@Getter
@Setter
public class Movie extends Item {

    private String director;
    private String actor;
}
