package pro.antonshu.market.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "discounts")
@NoArgsConstructor
@Getter
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "header")
    private String header;

    @Column(name = "title")
    private String title;

    @Column(name = "knob_title")
    private String knobTitle;

    @Column(name = "href")
    private String href;

    @Column(name = "image_path")
    private String imagePath;

//    private Date startDate;
//
//    private Date expiryDate;
}
