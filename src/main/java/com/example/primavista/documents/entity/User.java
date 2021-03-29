package com.example.primavista.documents.entity;


import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;

import org.hibernate.annotations.NaturalId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;
	
	private String firstName;
	private String lastName;
	@NaturalId
	@Email
	public String email;
	
	public String password;
	@OneToOne(cascade = CascadeType.ALL)
	
	@Enumerated
	private RoleName role;
	
	public User(String firstName, String lastName, String email, String password, RoleName role) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.role = role;
	}
	public User(String firstName) {
		this.firstName = firstName;
	}
	

}
