package com.hometask.citylist.model;

import com.hometask.citylist.exception.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.validator.routines.UrlValidator;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 * @author Elnur Mammadov
 */
@Table(name = "\"city\"")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class City {
    private static final long serialVersionUID = -8896750235269402643L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    @NotNull
    private String name;
    @Column(name = "photo")
    private String photo;

    @Transient
    public void validateUrl(String photoUrl) {
        UrlValidator urlValidator = new UrlValidator();
        if (!urlValidator.isValid(photoUrl) && photoUrl != null) {
            throw new BadRequestException("Photo Url is not valid");
        }
    }
}
